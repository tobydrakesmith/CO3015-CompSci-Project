package com.example.theatreticketsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
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


public class Login extends AppCompatActivity {

    private Basket basket = new Basket();
    public ActivityMainBinding mainBinding;
    private RequestQueue requestQueue;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

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
                dialog.dismiss();
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


    public void guestLogin(View view) {
        displayHome(new User());
    }


    private void displayHome(User user) {
        Intent displayHome = new Intent(this, Homepage.class);
        displayHome.putExtra("basket", basket);
        displayHome.putExtra("user", user);
        startActivity(displayHome);
    }

    private boolean validate(String username, String password) {
        return !username.equals("") && !password.equals("");
    }


    private void login() {
        final String email = mainBinding.usernameEditText.getText().toString().toLowerCase().trim();
        String password = mainBinding.passwordEditText.getText().toString().trim();
        if (validate(email, password)) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
            String hashedPassword = HashPassword.getSHA256SecurePassword(password);
            JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, DatabaseAPI.URL_GET_USER + email + "&password=" + hashedPassword, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                            try {
                                if (response.getString("error").equals("false")) {
                                    JSONObject jsonUser = response.getJSONObject("user");
                                    int id = jsonUser.getInt("userID");
                                    String firstName = jsonUser.getString("firstName");
                                    String lastName = jsonUser.getString("lastName");
                                    User user = new User(id, email, firstName, lastName);
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
                            mainBinding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

            requestQueue.add(jsObjectRequest);
        } else {
            Toast.makeText(this, "You must enter your username and password", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Toast.makeText(Login.this, "fling", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
