package com.Group9_KNN;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
//import android.widget.Toolbar;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Weka Model

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Smart Phone Sensing LOCATE BAYES. APP2: GROUP 9
 */
public class MainActivity extends AppCompatActivity {
    Instances train_data;
    Instance test_data;
    List<ScanResult> scanResults;

    private String filename = "combineall_merged";
    private Integer NUM_ATTRIBUTE = 18;
    private TextView textRssi;
    private TextView textACC;
    private Button buttonLOCATE;
    private Button buttonSCAN;
    private ImageView ImageCell1, ImageCell2, ImageCell3, ImageCell4, ImageCell5;;
    private ImageView ImageCell6, ImageCell7, ImageCell8, ImageCell9, ImageCell10;
    private ImageView ImageCell11, ImageCell12, ImageCell13, ImageCell14, ImageCell15;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private static final int REQUEST_PERMISSION_FINE_LOCATION = 443;
    private boolean isLocationRetrieved;
    private float altitude;
    private double locate_results_bayes_double;
    private WifiManager wifiManager;
    private MediaPlayer mediaPlayer;
    HashMap<String, Integer> current_rss = new HashMap<>();

    @SuppressLint("SdCardPath")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        locationRequest = locationRequest.create();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // TODO toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        textRssi = (TextView) findViewById(R.id.textRSSI);
        textACC = (TextView) findViewById(R.id.textACC);
        buttonSCAN = (Button) findViewById(R.id.buttonScan);
        buttonLOCATE = (Button) findViewById(R.id.buttonLOCATE);
        ImageCell1 = (ImageView) findViewById(R.id.Cell1);
        ImageCell2 = (ImageView) findViewById(R.id.Cell2);
        ImageCell3 = (ImageView) findViewById(R.id.Cell3);
        ImageCell4 = (ImageView) findViewById(R.id.Cell4);
        ImageCell5 = (ImageView) findViewById(R.id.Cell5);
        ImageCell6 = (ImageView) findViewById(R.id.Cell6);
        ImageCell7 = (ImageView) findViewById(R.id.Cell7);
        ImageCell8 = (ImageView) findViewById(R.id.Cell8);
        ImageCell9 = (ImageView) findViewById(R.id.Cell9);
        ImageCell10 = (ImageView) findViewById(R.id.Cell10);
        ImageCell11 = (ImageView) findViewById(R.id.Cell11);
        ImageCell12 = (ImageView) findViewById(R.id.Cell12);
        ImageCell13 = (ImageView) findViewById(R.id.Cell13);
        ImageCell14 = (ImageView) findViewById(R.id.Cell14);
        ImageCell15 = (ImageView) findViewById(R.id.Cell15);

        buttonSCAN.setOnClickListener(scan_rss);
        buttonLOCATE.setOnClickListener(locate_me);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            //if permission is not granted, request location permissions from user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_FINE_LOCATION);
        }

        try {
            Csv2Arff.transfer("/data/data/com.Group9_KNN/cache/"+ filename + ".csv", "/data/data/com.Group9_KNN/cache/" + filename +".arff");
            DataSource source = new DataSource("/data/data/com.Group9_KNN/cache/" + filename + ".arff");
            train_data = source.getDataSet();
            train_data.setClassIndex(train_data.numAttributes() - 1);
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            System.out.println(train_data);
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

            BAYES.process(train_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // altitude
//        LocationCallback locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult != null) {
//                    if (locationResult == null) {
//                        return;
//                    }
//                    //Showing the latitude, longitude and accuracy on the home screen.
//                    for (Location location : locationResult.getLocations()) {
////                        textRssi.setText("altitude=" + location.getAltitude());
//
//                    }
//                }
//            }
//        };
//        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        new CountDownTimer(5001000, 10000) {
            public void onTick(long millisUntilFinished) {
                textRssi.setText("Seconds remaining: " + millisUntilFinished / 1000 +"\n" +"\n" +
                                "Please locate yourself after 20 Seconds");
                wifiManager.startScan();
                wifiManager.getScanResults();
            }
            public void onFinish() {
                textRssi.setText("done!");
            }
        }.start();
    }
    // onResume() registers the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
    }

    // onPause() unregisters the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
    }

    public static class DataHolder {
        private static float data;
        public static float getData() {return data;}
        public static void setData(float data) {DataHolder.data = data;}
    }

    private Button.OnClickListener locate_me = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
//                Bundle bundle = getIntent().getExtras();
//                float value = bundle.getInt("temp 11");
                mediaPlayer.start();
                WifiManager wifiManager;
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.startScan();

                scanResults = wifiManager.getScanResults();

                for (ScanResult scanResult : scanResults) {
                    current_rss.put(scanResult.BSSID, scanResult.level);

                }
                System.out.println("HashMap------------------------------HashMap");
                System.out.println(current_rss);
                System.out.println(current_rss.size());

                // test_data instance
                test_data = new DenseInstance(NUM_ATTRIBUTE) {
                    {
                        for (int i=0; i < NUM_ATTRIBUTE - 1; i++) {
                            if (current_rss.containsKey(train_data.attribute(i).name()) == false)
                            {
                                setValue(train_data.attribute(i), -100);
                            }else {
//                                System.out.println(train_data.attribute(i).name());
                                setValue(train_data.attribute(i),current_rss.get(train_data.attribute(i).name()));
                            }
                        }

                    }
                };
                System.out.println("Test data------------------------------Test Data");
                System.out.println(test_data);
                test_data.setDataset(train_data);
//                train_data.add(test_data);
                current_rss.clear();
                try {
                    String prediction_cel = BAYES.locate_bayes_String(train_data,test_data);
                    locate_results_bayes_double = BAYES.locate_bayes_double(test_data);
                    textRssi.setText("LOCATE ME:" +"\n" +
                            "Number of Samples: 1200" +"\n" +"prediction_cell = "
                            + prediction_cel + "\n" +"\n"+ "Welcome to Cell " + prediction_cel
                            +"\n" +"\n" +"Designed By 2022_Group 9");
                    System.out.println(locate_results_bayes_double);
                    // LIGHT THE CELL
                    ImageCell1.setVisibility(View.INVISIBLE);
                    ImageCell2.setVisibility(View.INVISIBLE);
                    ImageCell3.setVisibility(View.INVISIBLE);
                    ImageCell4.setVisibility(View.INVISIBLE);
                    ImageCell5.setVisibility(View.INVISIBLE);
                    ImageCell6.setVisibility(View.INVISIBLE);
                    ImageCell7.setVisibility(View.INVISIBLE);
                    ImageCell8.setVisibility(View.INVISIBLE);
                    ImageCell9.setVisibility(View.INVISIBLE);
                    ImageCell10.setVisibility(View.INVISIBLE);
                    ImageCell11.setVisibility(View.INVISIBLE);
                    ImageCell12.setVisibility(View.INVISIBLE);
                    ImageCell13.setVisibility(View.INVISIBLE);
                    ImageCell14.setVisibility(View.INVISIBLE);
                    ImageCell15.setVisibility(View.INVISIBLE);

                    if (prediction_cel.compareTo("C1") == 0){
                        ImageCell1.setVisibility(View.VISIBLE);
                        ImageCell1.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C2") == 0){
                        ImageCell2.setVisibility(View.VISIBLE);
                        ImageCell2.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C3") == 0){
                        ImageCell3.setVisibility(View.VISIBLE);
                        ImageCell3.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C4") == 0){
                        ImageCell4.setVisibility(View.VISIBLE);
                        ImageCell4.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C5") == 0){
                        ImageCell5.setVisibility(View.VISIBLE);
                        ImageCell5.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C6") == 0){
                        ImageCell6.setVisibility(View.VISIBLE);
                        ImageCell6.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C7") == 0){
                        ImageCell7.setVisibility(View.VISIBLE);
                        ImageCell7.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C8") == 0){
                        ImageCell8.setVisibility(View.VISIBLE);
                        ImageCell8.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C9") == 0){
                        ImageCell9.setVisibility(View.VISIBLE);
                        ImageCell9.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C10") == 0){
                        ImageCell10.setVisibility(View.VISIBLE);
                        ImageCell10.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C11") == 0){
                        ImageCell11.setVisibility(View.VISIBLE);
                        ImageCell11.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C12") == 0){
                        ImageCell12.setVisibility(View.VISIBLE);
                        ImageCell12.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C13") == 0){
                        ImageCell13.setVisibility(View.VISIBLE);
                        ImageCell13.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C14") == 0){
                        ImageCell14.setVisibility(View.VISIBLE);
                        ImageCell14.setAlpha(0.50f);
                    }
                    if (prediction_cel.compareTo("C15") == 0){
                        ImageCell15.setVisibility(View.VISIBLE);
                        ImageCell15.setAlpha(0.50f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            textRssi.setText("altitude = " + altitude);



            }
    };


    private Button.OnClickListener scan_rss = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(MainActivity.this, MotionModel.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Current Cell", (int)locate_results_bayes_double);
            startActivity(intent);

        }
    };
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o2,
                               Map.Entry<String, Integer> o1)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    // altitude
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        super.onRequestPermissionsResult(1000, null, grantResults);
//        if (requestCode == REQUEST_PERMISSION_FINE_LOCATION) {
//            if (grantResults.length > 0 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation();
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getLocation() {
//        fusedLocationClient.getCurrentLocation(100, null)
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            altitude = (float) location.getAltitude();
//                        }
//                    }
//                });
//    }

}