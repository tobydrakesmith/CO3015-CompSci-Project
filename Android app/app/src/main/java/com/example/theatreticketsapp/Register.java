package com.example.theatreticketsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theatreticketsapp.databinding.ActivityRegisterBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//TODO: Improve design
public class Register extends AppCompatActivity {



    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Register.this, MainActivity.class);
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

        if (validate(username, firstName, lastName, password, password2)) createAccount();

    }


    //TODO: add more validation
    public Boolean validate(String email, String firstName, String lastName,
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

    //TODO: Move the code across into this file, and provide feedback to user


    private void createAccount(){

        StringRequest stringRequest = new StringRequest( Request.Method.POST, DatabaseAPI.URL_CREATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")){
                        Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();
                        try {
                            GMailSender sender = new GMailSender("theatreticketsapp@gmail.com", "Stadium0fL1ght?");
                            sender.sendMail("Account registration",
                                    "This is Body",
                                    "theatreticketsapp@gmail.com",
                                    "toby_drakesmith95@hotmail.com");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    else{
                        Toast.makeText(Register.this, "Whoops! An account is already registered with this email", Toast.LENGTH_SHORT).show();
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
                String firstName = registerBinding.firstname.getText().toString().trim();
                String lastName = registerBinding.lastname.getText().toString().trim();

                HashMap<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("password", password);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }



}
