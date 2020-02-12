package com.example.theatreticketsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theatreticketsapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

//TODO: Improve layout and design
public class MainActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "error";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ID = "userid";

    private Basket basket = new Basket();
    private int userID = -1;

    public static ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }


    public void checkDetails(View view){
        login();
    }

    public void register(View view){
        Intent displayRegister = new Intent(this, Register.class);
        startActivity(displayRegister);
    }


    public void guestLogin(View view){
        displayHome();
    }

    private void displayHome() {
        Intent displayHome = new Intent(this, Homepage.class);
        displayHome.putExtra("basket", basket);
        System.out.println("MAIN ACTIVITY: " + userID);

        if (userID != -1) //determines if user is a guest or not
            displayHome.putExtra("userid", userID);

        startActivity(displayHome);
    }

    private boolean validate(String username, String password){
        if (username.equals("") || password.equals("")) return false;
        else return true;
    }



    private void login() {
        String email = mainBinding.usernameEditText.getText().toString().toLowerCase().trim();
        String password = mainBinding.passwordEditText.getText().toString().trim();
        if (validate(email, password)) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
            final JSONObject request = new JSONObject();

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                    (Request.Method.GET, DatabaseAPI.URL_GET_USER + email + "&password=" + password, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //pDialog.dismiss();
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                            try {
                                //Check if user entered correct details

                                if (response.getString(KEY_STATUS).equals("false")) {
                                    userID = response.getInt(KEY_ID);
                                    displayHome();
                                    Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Username or password not recognised", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //pDialog.dismiss();

                            //Display error message whenever an error occurs
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

            // Access the RequestQueue through your singleton class.
            Volley.newRequestQueue(this).add(jsArrayRequest);
        } else{
            Toast.makeText(this, "You must enter your username and password", Toast.LENGTH_LONG).show();
        }
    }
}
