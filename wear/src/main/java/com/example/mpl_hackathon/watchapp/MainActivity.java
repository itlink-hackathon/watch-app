package com.example.mpl_hackathon.watchapp;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import com.google.android.gms.wearable.MessageApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    private boolean mAlertDetected = false;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        initTopButton();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mLocationManager = new LocationManager(this);

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
        //MainActivity.Se
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    // Created code under hackathon
    private void initTopButton() {
        final ImageView btnTop = (ImageView) findViewById(R.id.btn_top);
        if (btnTop != null) {
            Log.i("Watch:", "initTopButton 2");

            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAlertDetected();
                }
            });
        }
    }

    private void onAlertDetected() {
        changeLedColor();
        sendAlertData();
    }

    private void changeLedColor() {
        //final TextView textBottom = (TextView) findViewById(R.id.text_bottom);
//        if (textBottom != null && !mAlertDetected) {
//            mAlertDetected = true;
//            Handler handler = new Handler();
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    textBottom.setText("OK");
//                    textBottom.setTextColor(Color.parseColor("#99CC00"));
//                    mAlertDetected = false;
//                }
//            }, 2000);
//
//            textBottom.setText("ALERTE !");
//            textBottom.setTextColor(Color.parseColor("#FF4444"));
//        }
        if (!mAlertDetected) {
            mAlertDetected = true;
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAlertDetected = false;
                }
            }, 2000);

        }
    }

    private void sendAlertData() {
        try {
            Log.i("Watch:", "Enter in sendAlertData");

            final JSONObject jsonObject = getCurrentInformation();

            if (jsonObject != null) {
                // création de la requête
                JsonObjectRequest testRequestPost =
                        new JsonObjectRequest(Request.Method.POST,
                                "http://" + NetworkManager.HOSTNAME + "app-urgence/web/app.php/api/new-alerte",
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(MainActivity.this, "Response received : " + response, Toast
                                                .LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, "Response Error", Toast.LENGTH_LONG)
                                                .show();
                                        error.printStackTrace();
                                    }
                                });

                // envoi de requête
                Log.i("Watch:", "Enter in testRequestPost");

                NetworkManager.getInstance(getApplicationContext()).addToRequestQueue(testRequestPost);
            }
        } catch (JSONException e) {
            Log.i("Watch:", "error : " + e.getMessage());
        }
    }

    private JSONObject getCurrentInformation() throws JSONException {
        JSONObject jsonBody = null;
        Location location = mLocationManager.getCurrentLocation();

        if (location != null) {
            jsonBody = new JSONObject();
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            jsonBody.put("lastname", "Paul");//PersonalInformationActivity.LASTNAME, "N/A"));
            jsonBody.put("firstname", "Jean");//PersonalInformationActivity.FIRSTNAME, "N/A"));
            jsonBody.put("phone_number", "0645751254");//settings.getString(PersonalInformationActivity.PHONE_NUMBER, "N/A"));
            jsonBody.put("timestamp_current", new Date().getTime());
            jsonBody.put("latitude", location.getLatitude());
            jsonBody.put("longitude", location.getLongitude());
            jsonBody.put("timestamp_position", location.getTime());
            jsonBody.put("drive_link", "");
        } else {
            Log.w("Watch:", "no location !");
        }

        return jsonBody;
    }



}
