package com.example.theatreticketsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theatreticketsapp.databinding.ActivityRegisterBinding;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {



    private ActivityRegisterBinding registerBinding;
    private RequestQueue requestQueue;
    private ArrayList<String> userLocationPreferences;
    private HashMap<String, String> fireBaseTopicName;
    private FirebaseMessaging firebaseMessaging;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        userLocationPreferences = new ArrayList<>();


        firebaseMessaging = FirebaseMessaging.getInstance();

        fireBaseTopicName = new HashMap<>();
        fireBaseTopicName.put("Scotland", "newShowScotland");
        fireBaseTopicName.put("Northern Ireland", "newShowNorthernIreland");
        fireBaseTopicName.put("Wales", "newShowWales");
        fireBaseTopicName.put("North East", "newShowNorthEast");
        fireBaseTopicName.put("North West", "newShowNorthWest");
        fireBaseTopicName.put("Yorkshire", "newShowYorkshire");
        fireBaseTopicName.put("West Midlands", "newShowWestMidlands");
        fireBaseTopicName.put("East Midlands", "newShowEastMidlands");
        fireBaseTopicName.put("South West", "newShowSouthWest");
        fireBaseTopicName.put("South East", "newShowSouthEast");
        fireBaseTopicName.put("East of England", "newShowEastOfEngland");
        fireBaseTopicName.put("Greater London", "newShowLondon");


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void register(View view){

        String username = registerBinding.username.getText().toString().trim();
        String password = registerBinding.password.getText().toString().trim();
        String password2 = registerBinding.confirmPassword.getText().toString().trim();
        String firstName = registerBinding.firstname.getText().toString().trim();
        String lastName = registerBinding.lastname.getText().toString().trim();

        if (validate(username, firstName, lastName, password, password2))
            createAccount();

    }


    //TODO: add more validation
    private Boolean validate(String email, String firstName, String lastName,
                            String password, String password2){

        Boolean valid = true;

        //check email is formatted correctly

        if (!email.contains("@") || !email.contains(".")) {
            registerBinding.username.setError("Enter a valid email");
            valid= false;
        }
        else{
            //further validation to be done
        }

        if (firstName.length()<3){
            valid = false;
            registerBinding.firstname.setError("First name must be 3 characters");
        }

        if (lastName.length()<3){
            valid = false;
            registerBinding.lastname.setError("Surname must be 3 characters");

        }
        if (!password.equals(password2)){
            registerBinding.password.setError(("The passwords do not match"));

            valid = false;
        }
        if (password.length() < 5){
            registerBinding.password.setError("Password must be at least 5 chars");
            valid = false;
        }




        return valid;
    }



    private void createAccount(){

        StringRequest stringRequest = new StringRequest( Request.Method.POST, DatabaseAPI.URL_CREATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")){
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        openDialog();
                    }
                    else{
                        Toast.makeText(Register.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams(){

                String username = registerBinding.username.getText().toString().trim();
                String password = registerBinding.password.getText().toString().trim();
                String encryptedPassword = HashPassword.getSHA256SecurePassword(password);
                String firstName = registerBinding.firstname.getText().toString().trim();
                String lastName = registerBinding.lastname.getText().toString().trim();



                HashMap<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("password", encryptedPassword);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void openDialog() {

        final String[] items = getResources().getStringArray(R.array.locations);
        boolean[] checkedItems = new boolean[items.length];



        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
        dialog.setTitle("Select the region(s) you would like to hear about new shows in");
        dialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked)
                    userLocationPreferences.add(items[which]);
                else
                    userLocationPreferences.remove(items[which]);
            }
        });
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processLocationChoices();
            }
        });
        dialog.show();
    }

    private void processLocationChoices() {

        for (String s : userLocationPreferences)
            firebaseMessaging.subscribeToTopic(fireBaseTopicName.get(s));


        final View dialogView = View.inflate(Register.this, R.layout.dialog_musical_play, null);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
        dialog.setTitle("Notification options");
        dialog.setView(dialogView);
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id !=1) {
                    RadioButton checkedBtn = dialogView.findViewById(id);
                    String text = checkedBtn.getText().toString();
                    switch (text){
                        case("Both"):
                            firebaseMessaging.subscribeToTopic("plays");
                            firebaseMessaging.subscribeToTopic("musicals");
                            break;
                        case("Plays only"):
                            firebaseMessaging.subscribeToTopic("plays");
                            break;
                        case("Musicals only"):
                            firebaseMessaging.subscribeToTopic("musicals");
                            break;
                    }
                }

                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        dialog.show();

    }





}
