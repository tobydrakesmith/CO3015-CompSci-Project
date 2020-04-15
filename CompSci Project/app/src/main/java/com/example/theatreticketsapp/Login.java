package com.example.theatreticketsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theatreticketsapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

//TODO: Improve layout and design
public class Login extends AppCompatActivity {

    private Basket basket = new Basket();
    private int userID = -1;
    private String randomPassword;
    
    public static ActivityMainBinding mainBinding;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
    }


    public void checkDetails(View view){
        login();
    }

    public void register(View view){
        Intent displayRegister = new Intent(this, Register.class);
        startActivity(displayRegister);
    }

    public void resetPassword(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset password");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter your email");
        builder.setView(input);

        // Set up the buttons
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

    private void sendMail(final String email){
        
       randomPassword = getRandomPassword();
       String hashPW = getSHA256SecurePassword(randomPassword);

        JsonObjectRequest resetPasswordRequest = new JsonObjectRequest(Request.Method.POST,
                DatabaseAPI.URL_UPDATE_PASSWORD + email +"&password="+hashPW, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equals("false")) {

                                final String body = "Your new password is: " +
                                        randomPassword + "\n" +
                                        "Please use this to log in to your account " +
                                        "and change the password again to a memorable one";

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender(EmailConfig.email,
                                                    EmailConfig.password);
                                            sender.sendMail("Account creation confirmation", body,
                                                    EmailConfig.email, email);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }

                                }).start();

                                Toast.makeText(Login.this,
                                        "An email with your new password has been sent",
                                        Toast.LENGTH_LONG).show();
                            }else
                                Toast.makeText(Login.this,
                                        "The provided email was not recognised",
                                        Toast.LENGTH_SHORT).show();
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

        requestQueue.add(resetPasswordRequest);

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


    private void login() {
        String email = mainBinding.usernameEditText.getText().toString().toLowerCase().trim();
        String password = mainBinding.passwordEditText.getText().toString().trim();
        if (validate(email, password)) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
            String hashedPassword = getSHA256SecurePassword(password);
            JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, DatabaseAPI.URL_GET_USER + email + "&password=" + hashedPassword, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

            requestQueue.add(jsObjectRequest);
        } else{
            Toast.makeText(this, "You must enter your username and password", Toast.LENGTH_LONG).show();
        }
    }

    private String getRandomPassword(){

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(8);
        ArrayList<Integer> generatedIndex = new ArrayList();
        int index = 0;


        Random random = new Random();
        for (int i = 0; i < 8; i++) {

            // generate a random number between
            // 0 to String variable length
            //int index = (int) (AlphaNumericString.length() * Math.random());


            if (i==0) index = random.nextInt(AlphaNumericString.length());
            else {
                do {
                    index = random.nextInt(AlphaNumericString.length());
                } while (generatedIndex.contains(index));
            }
            generatedIndex.add(index);
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
