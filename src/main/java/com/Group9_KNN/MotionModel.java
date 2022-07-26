package com.Group9_KNN;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Smart Phone Sensing Particle Filter.
 */
public class MotionModel extends AppCompatActivity implements SensorEventListener{

    /**
     * The shape.
     */
    private ShapeDrawable drawable;
    /**
     * The canvas.
     */
    private Canvas canvas;
    /**
     * The walls.
     */
    private List<ShapeDrawable> walls;

    private LinkedList<Particle> particles;

    private Layout layout;

    private ArrayList<Cell> cells;

    private int width;

    private int height;

    private int direction;

    private int results;

    // motion
    // Gravity for accelerometer data
    private float[] gravity = new float[3];
    // smoothed values
    private float[] smoothed = new float[3];
    // sensor manager
    private SensorManager sensorManager;
    // sensor gravity
    private Sensor sensorGravity;
    private Sensor sensorDirection;
    private double bearing = 0;

    private TextView acc;
    private TextView stepView;
    private TextView thresholdView;
    private TextView tempView;
    private SeekBar seek;
    private ToggleButton countToggle;
    private int flag = 0;
    private float angle_cali = 0;
    private int stepCount;
    private boolean toggle;
    private double prevY;
    private double threshold;
    private boolean ignore;
    private int countdown;
    private long currentTime;
    private long lastTime;
    private long time_difference_ms;
    private float temp;
    private float angle;
    HashMap<String, Integer> Current_prob = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_motion_model);

//        direction = 0;

        // get the screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        // create a canvas
        Bundle bundle = getIntent().getExtras();
        results = bundle.getInt("Current Cell");

        ImageView canvasView = (ImageView) findViewById(R.id.imageview2);
        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);
        layout = new Layout();
        cells = layout.cells;

        layout.draw_layout(canvas, layout, (int)((cells.get(results).right_wall - cells.get(results).left_wall) * 70), (int)((cells.get(results).bottom_wall - cells.get(results).top_wall) * 70), 0, (int)(cells.get(results).top_wall * 70), (int)(cells.get(results).left_wall * 70));
        //System.out.println("=============================================================");
        //Log.d("1111", String.valueOf(particles.size()));

        // move the particles
//        create_canvas();
//
//        layout.draw_layout(canvas, layout, width, height, 1);

        int counter = layout.count_cell();
        Log.d("2222", String.valueOf(counter));

        // get step_count
        acc = (TextView) findViewById((R.id.accelerometer));
        thresholdView = (TextView) findViewById((R.id.thresholdView));
        stepView = (TextView) findViewById((R.id.stepView));
        tempView = (TextView) findViewById(R.id.temp);
        countToggle = (ToggleButton) findViewById(R.id.countToggle);
        seek = (SeekBar) findViewById(R.id.seekbar);

//        seek.setProgress(10000);
        seek.incrementProgressBy(1);

        implementListeners();

    }

    //motion model
    protected float[] lowPassFilter( float[] input, float[] output ) {
        float alpha = 0.8f;
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = alpha * output[i] + (1.0f - alpha) * input[i];
        }
        return output;
    }

    @Override
    protected void onStart() {
        Log.d("3333", "here");
        super.onStart();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorDirection = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // listen to these sensors
        sensorManager.registerListener(this, sensorGravity,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorDirection,
                SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove listeners
        sensorManager.unregisterListener(this, sensorGravity);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            flag ++;
            float[] rotationMatrix = new float[16];
            float[] orientationVals = new float[3];

            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
//            System.out.print(rotationMatrix);
            SensorManager.getOrientation(rotationMatrix, orientationVals);
//            System.out.print(orientationVals);
            angle = (float) Math.toDegrees(orientationVals[0]);
            System.out.println("angle" + angle);
            if(angle < 0)
            {
                angle = angle + 360;
            }

            if(flag == 50)
            {
                angle_cali = 90 - angle;
            }

//            System.out.println("angle" + angle_cali);

            angle = angle + angle_cali ;
            if(angle >= 360)
            {
                angle = angle - 360;
            }

            if(angle < 0)
            {
                angle = angle + 360;
            }
            if((angle >= 0 && angle < 45) | (angle >= 315 && angle <= 359))
            {
                //Log.d("2222", String.valueOf(angle));
                direction = 3;
            }

            if(angle >= 45 && angle < 135)
            {
                //Log.d("23333", String.valueOf(angle));
                direction = 0; //right
            }

            if(angle >= 135 && angle < 225)
            {
                direction = 2;
            }
            if(angle >= 225 && angle < 315)
            {
                direction = 1;
            }
        }


        // get accelerometer data
        else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // we need to use a low pass filter to make data smoothed
            smoothed = lowPassFilter(event.values, gravity);
            temp = event.values[0] * event.values[0] +
                    event.values[1] * event.values[1] +
                    event.values[2] * event.values[2];
//            temp = gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2];

            gravity[0] = smoothed[0];
            gravity[1] = smoothed[1];
            gravity[2] = smoothed[2];

            tempView.setText("Current angle = " + angle + "\n" + "angle_offset = " + angle_cali + "\n" + "temp = " + temp);
            MainActivity.DataHolder.setData(temp);
//            acc.setText("x: "+gravity[0] + " y: " + gravity[1] + " z: " + gravity[2]+ "ignore: "+ ignore + "countdown: "+ countdown);
            if(ignore) {
                countdown--;
                ignore = (countdown < 0)? false : ignore;
            }
            else
                countdown = 12;

            if (toggle && (temp > threshold) && !ignore) {
                currentTime = Calendar.getInstance().getTimeInMillis();
                time_difference_ms = currentTime - lastTime;
                if (time_difference_ms > 700) {
                    stepCount++;
                    stepView.setText("Step Count: " + stepCount);
//                    acc.setText("Current Cell:" + "C" + (results + 1) + Arrays.toString(layout.counter));
                    create_canvas();
                    System.out.println("direction" + direction);
                    layout.draw_layout(canvas, layout, width, height, direction,0, 0);
                    layout.count_cell();

                    for (int i = 0; i < 14; i ++) {
                        Current_prob.put("Cell" + (int)(i+1), layout.counter[i]);
                    }
                    HashMap<String, Integer> Current_prob_sorted = MainActivity.sortByValue(Current_prob);
                    System.out.println(Current_prob_sorted);
//                    Arrays.sort(layout.counter);
                    String Top_1, Top_2, Top_3;
                    Top_1 = Top_2 = Top_3 = "";
                    int Top_1_value, Top_2_value, Top_3_value;
                    Top_1_value = Top_2_value = Top_3_value = 0;
                    int counter = 0;
                    for (HashMap.Entry entry: Current_prob_sorted.entrySet())
                    {
                        switch (counter){
                            case 0:
                                Top_1 = entry.getKey().toString();
                                Top_1_value = entry.getValue().hashCode() / 3;
                                break;
                            case 1:
                                Top_2 = entry.getKey().toString();
                                Top_2_value = entry.getValue().hashCode() / 3;
                                break;
                            case 2:
                                Top_3 = entry.getKey().toString();
                                Top_3_value = entry.getValue().hashCode() / 3;
                                break;
                            default:
                                break;
                        }
                        counter++;

                    }
                    acc.setText("Current Cell:" + "C" + (results + 1) + "\n" + "\n" +  "The Top 3 Possible Cell:" +"\n"
                                + Top_1 + ": " + Top_1_value+ "% \n" + Top_2 + ": " + Top_2_value + "% \n" +Top_3 + ": " + Top_3_value
                                + "% \n" + "\n" +"Period(ms)" +  "\n" + "= " + time_difference_ms) ;
//                    System.out.println(Arrays.toString(layout.counter));
                    ignore = true;
                    lastTime = currentTime;
                }
            }
            prevY = gravity[1];
        }
    }

    public void implementListeners() {
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = ((double) seek.getProgress()) * 0.01;
                thresholdView.setText("Threshold: " + threshold);
            }
        });

        countToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle = !toggle;
                if (toggle) {
                    stepCount = 0;
                    countdown = 5;
                    ignore = true;
                    stepView.setText("Step Count: " + stepCount);
                }
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private boolean isCollision() {
        for(ShapeDrawable wall : walls) {
            if(isCollision(wall,drawable))
                return true;
        }
        return false;
    }

    private boolean isCollision(ShapeDrawable first, ShapeDrawable second) {
        Rect firstRect = new Rect(first.getBounds());
        return firstRect.intersect(second.getBounds());
    }

    /**
     * create canvas
     */
    public void create_canvas()
    {
        // get the screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // create a canvas
        ImageView canvasView = (ImageView) findViewById(R.id.imageview2);
        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);
    }
}