package com.activities.theatreticketsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.classfiles.theatreticketsapp.Basket;
import com.classfiles.theatreticketsapp.User;
import com.configfiles.theatreticketsapp.DatabaseAPI;
import com.configfiles.theatreticketsapp.HashPassword;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private Basket basket = new Basket();
    private RequestQueue requestQueue;
    private TextView emailTxt, passwordTxt;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private boolean stayLoggedOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("loggedon", false)){
            String firstName = sharedPreferences.getString("firstName", "");
            String lastName = sharedPreferences.getString("lastName", "");
            String email = sharedPreferences.getString("email", "");
            int id = sharedPreferences.getInt("id", -1);
            displayHome(new User(id, email, firstName, lastName));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        emailTxt = findViewById(R.id.username);
        passwordTxt = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stayLoggedOn = isChecked;
            }
        });

    }


    public void checkDetails(View view) {
        login();
    }

    public void register(View view) {
        Intent displayRegister = new Intent(this, Register.class);
        startActivity(displayRegister);
    }

    public void resetPassword(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter your email");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                sendMail(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void sendMail(String email) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                DatabaseAPI.URL_SEND_RESET_PASSWORD_EMAIL + email, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        requestQueue.add(jsonObjectRequest);

    }


    private void displayHome(User user) {
        Intent displayHome = new Intent(this, Homepage.class);
        displayHome.putExtra("basket", basket);
        displayHome.putExtra("user", user);
        startActivity(displayHome);
        finish();
    }

    private boolean validate(String username, String password) {
        return !username.equals("") && !password.equals("");
    }


    private void login() {
        final String email = emailTxt.getText().toString().toLowerCase().trim();
        String password = passwordTxt.getText().toString().trim();
        if (validate(email, password)) {
            progressBar.setVisibility(View.VISIBLE);
            String hashedPassword = HashPassword.getSHA256SecurePassword(password);
            JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, DatabaseAPI.URL_GET_USER + email + "&password=" + hashedPassword, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                if (response.getString("error").equals("false")) {
                                    JSONObject jsonUser = response.getJSONObject("user");
                                    int id = jsonUser.getInt("userID");
                                    String firstName = jsonUser.getString("firstName");
                                    String lastName = jsonUser.getString("lastName");
                                    User user = new User(id, email, firstName, lastName);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("firstName", firstName);
                                    editor.putString("lastName", lastName);
                                    editor.putString("email", email);
                                    editor.putInt("id", id);
                                    editor.putBoolean("loggedon", stayLoggedOn);
                                    editor.apply();
                                    displayHome(user);


                                } else
                                    Toast.makeText(getApplicationContext(),
                                            "Username or password not recognised", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

            requestQueue.add(jsObjectRequest);
        } else {
            Toast.makeText(this, "You must enter your username and password", Toast.LENGTH_LONG).show();
        }
    }

}
