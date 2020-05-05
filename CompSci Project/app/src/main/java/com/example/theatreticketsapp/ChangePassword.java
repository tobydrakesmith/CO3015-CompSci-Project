package com.example.theatreticketsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword extends AppCompatActivity {

    TextView currentPassword, newPassword1, newPassword2;
    Button btnConfirm;
    RequestQueue requestQueue;
    User user;
    boolean valid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        requestQueue = Volley.newRequestQueue(this);

        currentPassword = findViewById(R.id.currentPassword);
        newPassword1 = findViewById(R.id.newPassword1);
        newPassword2 = findViewById(R.id.newPassword2);

        btnConfirm = findViewById(R.id.buttonConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    correctPassword();
                }else {
                    //passwords do not match
                }
            }
        });
    }


    //TODO: add more validation
    private boolean validate(){
        if (!(newPassword1.getText().toString().equals(newPassword2.getText().toString()))){
            newPassword1.setError("Passwords do not match");
            return false;
        }
        return true;
    }
    
    private void correctPassword() {

        String password = "&password=" + getSHA256SecurePassword(currentPassword.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_CHECK_PASSWORD + user.getId() + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            valid = jsonObject.getString("error").equals("false");

                            if (!valid)
                                Toast.makeText(ChangePassword.this,
                                        "Provided current password is incorrect", Toast.LENGTH_SHORT).show();
                            else
                                updatePassword();

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

        requestQueue.add(stringRequest);
    }

    private void updatePassword(){

        btnConfirm.setClickable(false);

        String hashPassword = "&password=" + getSHA256SecurePassword(newPassword1.getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                DatabaseAPI.URL_RESET_PASSWORD_LOGGED_ON+user.getId()+hashPassword,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("true"))
                        Toast.makeText(ChangePassword.this, "Error", Toast.LENGTH_SHORT).show();
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                        alertDialog.setTitle("Password changed");
                        alertDialog.setMessage("Your password has been successfully updated");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.show();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });



                    }

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

        requestQueue.add(request);
        
    }

    private static String getSHA256SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));

            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
