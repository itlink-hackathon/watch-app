package com.example.mpl_hackathon.watchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

public class MainActivity extends AppCompatActivity implements MessageApi.MessageListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
/*
    protected void initMessageListener() {
        // Create MessageListener
        MessageApi.MessageListener messageListener = new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                // To execute when a message is received

            }
        };

        // Register MessageListener
        Wearable.MessageApi.addListener(googleApiClient, messageListener);
    }*/
}
