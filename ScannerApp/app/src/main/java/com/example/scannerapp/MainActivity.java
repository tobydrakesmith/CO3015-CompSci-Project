package com.example.scannerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.configfiles.scannerapp.DatabaseAPI;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private ZXingScannerView mScannerView;
    private String showName, date, startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        showName = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        startTime = intent.getStringExtra("time");

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);

    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCameraPreview();

        try {
            JSONObject ticket = new JSONObject(rawResult.getText());
            if (ticket.getString("date").equals(date) && ticket.getString("time").equals(startTime)
            && ticket.getString("showName").equals(showName))
                scanTicket(ticket);
            else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Incorrect details");
                dialog.setMessage("The show, date or time of the ticket(s) do not match the provided settings");
                dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mScannerView.resumeCameraPreview(MainActivity.this);
                    }
                }).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scanTicket(final JSONObject ticket) throws JSONException {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_SCAN_TICKET +ticket.getString("ticketID"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                try {
                    System.out.println("On response");
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("error").equals("true")) {
                        dialog.setTitle("ERROR");
                        dialog.setMessage("Ticket has already been scanned");
                    }else {
                        dialog.setTitle("Success");
                        dialog.setMessage("Ticket successfully scanned");
                    }
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                            mScannerView.resumeCameraPreview( MainActivity.this);
                        }
                    });
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSON ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
