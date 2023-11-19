package com.example.lab05;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity  extends android.app.Activity {

    private static final int REQUEST_CODE = 1;
    private static final int RESPONSE_CODE = 2;
    private static final String FILENAME = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save data to a file
        saveDataToFile();

        // Read data from a file
        String dataFromFile = readDataFromFile();
        TextView textView = findViewById(R.id.textView);
        textView.setText(dataFromFile);

        // Send data to other apps
        sendDataToOtherApps();
    }

    private void saveDataToFile() {
        String dataToSave = "Hello, this is my data.";

        try {
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            fos.write(dataToSave.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDataFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void sendDataToOtherApps() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Data to send to other app");
        sendIntent.setType("text/plain");

        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(sendIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(Intent.EXTRA_TEXT)) {
                String receivedData = data.getStringExtra(Intent.EXTRA_TEXT);
                // Handle the received data from other apps
                processReceivedData(receivedData);
            }
        } else if (requestCode == RESPONSE_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(Intent.EXTRA_TEXT)) {
                String response = data.getStringExtra(Intent.EXTRA_TEXT);
                // Handle the response from other apps if needed
            }
        }
    }

    private void processReceivedData(String receivedData) {
        // Process the received data
        String processedData = "Processed: " + receivedData;

        // Send back a response to the other app
        Intent responseIntent = new Intent();
        responseIntent.setAction(Intent.ACTION_SEND);
        responseIntent.putExtra(Intent.EXTRA_TEXT, processedData);
        responseIntent.setType("text/plain");

        if (responseIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(responseIntent, RESPONSE_CODE);
        }
    }

}

