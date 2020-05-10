package com.example.scannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        try {
            JSONObject ticket = new JSONObject(rawResult.getText());
            scanTicket(ticket);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scanTicket(final JSONObject ticket) throws JSONException {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseAPI.URL_SCAN_TICKET +ticket.getString("ticketID"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    System.out.println("On response");
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("error").equals("true"))
                        Toast.makeText(MainActivity.this, "ERROR: Ticket has already been scanned ID:" + ticket.getString("ticketID"), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Ticket successfully scanned. ID: " + ticket.getString("ticketID"), Toast.LENGTH_LONG).show();

                    mScannerView.resumeCameraPreview( MainActivity.this);
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
