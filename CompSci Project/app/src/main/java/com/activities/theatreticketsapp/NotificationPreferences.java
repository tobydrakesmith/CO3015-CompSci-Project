package com.activities.theatreticketsapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationPreferences extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String tokenID;
    private CheckBox london, scotland, ni, wales, northEast, northWest, wMidlands, eMidlands, southEast, southWest, eastEngland, yorkshire;
    private HashMap<String, String> topicNames;
    private ArrayList<String> originalPreferences, newPreferences;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        requestQueue = Volley.newRequestQueue(this);
        topicNames = new HashMap<>();
        originalPreferences = new ArrayList<>();
        newPreferences = new ArrayList<>();

        topicNames.put("Scotland", "newShowScotland");
        topicNames.put("Northern Ireland", "newShowNorthernIreland");
        topicNames.put("Wales", "newShowWales");
        topicNames.put("North East", "newShowNorthEast");
        topicNames.put("North West", "newShowNorthWest");
        topicNames.put("Yorkshire", "newShowYorkshire");
        topicNames.put("West Midlands", "newShowWestMidlands");
        topicNames.put("East Midlands", "newShowEastMidlands");
        topicNames.put("South West", "newShowSouthWest");
        topicNames.put("South East", "newShowSouthEast");
        topicNames.put("East of England", "newShowEastOfEngland");
        topicNames.put("Greater London", "newShowLondon");

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String name = buttonView.getText().toString();
                if (buttonView.isChecked())
                    newPreferences.add(topicNames.get(name));
                else {
                    newPreferences.remove(topicNames.get(name));
                }

            }
        };

        london = findViewById(R.id.londonBox);
        london.setOnCheckedChangeListener(listener);

        scotland = findViewById(R.id.scotlandBox);
        scotland.setOnCheckedChangeListener(listener);

        ni = findViewById(R.id.niBox);
        ni.setOnCheckedChangeListener(listener);

        wales = findViewById(R.id.walesBox);
        wales.setOnCheckedChangeListener(listener);

        northEast = findViewById(R.id.neBox);
        northEast.setOnCheckedChangeListener(listener);

        northWest = findViewById(R.id.nwBox);
        northWest.setOnCheckedChangeListener(listener);

        wMidlands = findViewById(R.id.wmBox);
        wMidlands.setOnCheckedChangeListener(listener);

        eMidlands = findViewById(R.id.emBox);
        eMidlands.setOnCheckedChangeListener(listener);

        southEast = findViewById(R.id.seBox);
        southEast.setOnCheckedChangeListener(listener);

        southWest = findViewById(R.id.swBox);
        southWest.setOnCheckedChangeListener(listener);

        eastEngland = findViewById(R.id.eeBox);
        eastEngland.setOnCheckedChangeListener(listener);

        yorkshire = findViewById(R.id.yorkshireBox);
        yorkshire.setOnCheckedChangeListener(listener);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                String text = rb.getText().toString();
                Toast.makeText(NotificationPreferences.this, text, Toast.LENGTH_SHORT).show();
                switch (text) {
                    case "Plays only":
                        if (!newPreferences.contains("plays")) newPreferences.add("plays");
                        newPreferences.remove("musicals");
                        break;
                    case "Musicals only":
                        if (!newPreferences.contains("musicals")) newPreferences.add("musicals");
                        newPreferences.remove("plays");
                        break;

                    case "Both":
                        if (!newPreferences.contains("musicals")) newPreferences.add("musicals");
                        if (!newPreferences.contains("plays")) newPreferences.add("plays");
                        break;

                }

            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        tokenID = task.getResult().getToken();

                        getSubscriptions();
                    }
                });

        Button submit = findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String s : newPreferences) {
                    firebaseMessaging.subscribeToTopic(s);
                }

                for (String s : originalPreferences) {
                    if (!newPreferences.contains(s)) {
                        System.out.println(s);
                        firebaseMessaging.unsubscribeFromTopic(s);
                    }
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(NotificationPreferences.this);
                dialog.setTitle("Success");
                dialog.setMessage("Your preferences have successfully been updated");
                dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void getSubscriptions() {


        String url = "https://iid.googleapis.com/iid/info/" + tokenID + "?details=true";

        StringRequest topics = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsObj = new JSONObject(response);
                    jsObj = jsObj.getJSONObject("rel").getJSONObject("topics");
                    JSONArray names = jsObj.names();

                    assert names != null;
                    for (int i = 0; i < names.length(); i++) {
                        originalPreferences.add(names.get(i).toString());
                        checkBox(names.get(i).toString());
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                String key = "key=AAAAQy0X4fw:APA91bELtTlZGqwPnkCtyE4ENWLw0FNruvuw7eowC" +
                        "_4Ge4pPiaQgPE1MhB3y_yQqxoIUS6UFqStlhF4uDZmuGvrAIF8Z7FMY6-0Ns9tmt3G" +
                        "Qe7OoTHEuibjW585KWju-2gKemQiQoh7P";
                headers.put("Authorization", key);
                return headers;
            }
        };

        requestQueue.add(topics);

    }


    private void checkBox(String name) {

        switch (name) {

            case "newShowLondon":
                london.setChecked(true);
                break;
            case "newShowScotland":
                scotland.setChecked(true);
                break;
            case "newShowNorthernIreland":
                ni.setChecked(true);
                break;
            case "newShowNorthEast":
                northEast.setChecked(true);
                break;
            case "newShowWales":
                wales.setChecked(true);
                break;
            case "newShowNorthWest":
                northWest.setChecked(true);
                break;
            case "newShowYorkshire":
                yorkshire.setChecked(true);
                break;
            case "newShowWestMidlands":
                wMidlands.setChecked(true);
                break;
            case "newShowEastMidlands":
                eMidlands.setChecked(true);
                break;
            case "newShowSouthWest":
                southWest.setChecked(true);
                break;
            case "newShowSouthEast":
                southEast.setChecked(true);
                break;
            case "newShowEastOfEngland":
                eastEngland.setChecked(true);
                break;

            case "plays":
                if (originalPreferences.contains("musicals")) {
                    radioGroup.check(R.id.radioButtonBoth);
                } else {
                    radioGroup.check(R.id.radioButtonPlays);
                }
                break;

            case "musicals":
                if (originalPreferences.contains("plays"))
                    radioGroup.check(R.id.radioButtonBoth);
                else
                    radioGroup.check(R.id.radioButtonMusicals);


                break;

        }

    }
}
