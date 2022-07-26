package com.Group9_KNN;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;



public class DisplayMessageActivity extends AppCompatActivity {

    private Button buttonRSSI;

    private TextView textView;

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_display_message);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        buttonRSSI = (Button) findViewById(R.id.buttonRSSI);
        buttonRSSI.setOnClickListener(scan_rss);
        textView = (TextView)findViewById(R.id.textView);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


    }

    private Button.OnClickListener scan_rss = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            textView.setText("\n\tScan all access points:");
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
            List<ScanResult> Results = wifiManager.getScanResults();
            for (ScanResult scanResult : Results) {
                textView.setText(textView.getText() + "\n\t "
                        + (float)(Math.round(scanResult.frequency* 100 ))/ 100 + "| "
                        + scanResult.BSSID + "  | ip="
                        + scanResult.SSID + " | "
                        + scanResult.level );
            }
        }
    };
}