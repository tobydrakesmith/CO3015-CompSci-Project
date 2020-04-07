package com.example.theatreticketsapp;

import android.content.Intent;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//TODO: Improve layout and design
public class Login extends AppCompatActivity {

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

        if (userID != -1) //determines if user is a guest or not
            displayHome.putExtra("userid", userID);

        startActivity(displayHome);
    }

    private boolean validate(String username, String password){
        if (username.equals("") || password.equals("")) return false;
        else return true;
    }

    private static String getSHA256SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }





 //TODO:
    //change the volley request below to fit in with rest of project

    private void login() {
        String email = mainBinding.usernameEditText.getText().toString().toLowerCase().trim();
        String password = mainBinding.passwordEditText.getText().toString().trim();
        if (validate(email, password)) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
            final JSONObject request = new JSONObject();

            String encryptedPassword = getSHA256SecurePassword(password);

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                    (Request.Method.GET, DatabaseAPI.URL_GET_USER + email + "&password=" + encryptedPassword, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //pDialog.dismiss();
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                            try {


                                if (response.getString("error").equals("false")) {
                                    userID = response.getInt("userid");
                                    displayHome();
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

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
