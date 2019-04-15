package advanced.android.ebcm;

import advanced.android.ebcm.Device.Device;
import advanced.android.ebcm.Graph.CalculationResult;
import advanced.android.ebcm.Graph.ItemDetailsAdapter;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.Toast;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;


public class ResultGraph extends AppCompatActivity implements GestureDetector.OnGestureListener {

    ArrayList<CalculationResult> results;
    ArrayList<CalculationResult> resultList;
    ArrayList<Device> devicesList;
    GraphView graphView;
    double maxResult;
    double kWhPrice;
    int profileId;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileId = getIntent().getIntExtra("PROFILE_ID", -1);

        if (profileId == 0 || profileId == -1) {
            finish();
        }

        //used to fill the graph
        results = loadData();

        drawGraph();

    } // end of onCreate

    private DataPoint[] getDataPoints() {
        Collections.sort(results);
        DataPoint[] dataPoints = new DataPoint[results.size()];

        for (int i = 0; i < dataPoints.length; i++) {
            double result = results.get(i).getResults();
            if (result > maxResult) {
                maxResult = result;
            }
            dataPoints[i] = new DataPoint(i+1, result);
        }

        return dataPoints;
    }

    private ArrayList<CalculationResult> loadData() {
        resultList = new ArrayList<>();

        getProfileCost();
        getDevices();

        for (Device device : devicesList ) {
            resultList.add(new CalculationResult(device.getName(), device.getPower(),device.getQuantity(),
                    device.getHours(),device.getMinutes(), device.getDays()));
        }

        return resultList;
    }


    private void getDevices() {
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor devices = mDatabaseHelper.getDeviceData(profileId);
        devicesList = new ArrayList<>();
        graphView = findViewById(R.id.graphView);

        if (devices != null) {
            if (devices.moveToFirst() && devices.getCount() >= 1) {
                do {

                    devicesList.add(new Device(devices.getInt(0),devices.getString(1),devices.getInt(2),
                                                devices.getInt(3),devices.getInt(4),devices.getInt(5),
                            devices.getInt(7), devices.getInt(8)));

                } while (devices.moveToNext());
            }
            devices.close();
        }
        mDatabaseHelper.close();
    }

    private void getProfileCost() {
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor profile = mDatabaseHelper.getProfileItemByID(profileId);

        if (profile != null) {
            if (profile.moveToFirst() && profile.getCount() >= 1) {
                do {

                    kWhPrice = profile.getFloat(3);

                } while (profile.moveToNext());
            }
            profile.close();
        }
        mDatabaseHelper.close();

    }

    void drawGraph() {
        setContentView(R.layout.activity_result_graph);
        graphView = findViewById(R.id.graphView);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        if (results.size() != 0){
            graphView.getViewport().setMaxX(results.size()+1);
        } else {
            graphView.getViewport().setMaxX(1);
        }

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
//        graphView.getViewport().setMaxY(400);
//        graphView.setHorizontalScrollBarEnabled(true);

        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(true);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);



        graphView.getGridLabelRenderer(). setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + "W";
                }
            }

        });

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(getDataPoints());

        graphView.getViewport().setMaxY(maxResult * 1.2);

        graphView.addSeries(series);
        series.setAnimated(true);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int x = (int) dataPoint.getX();
                CalculationResult result = results.get(x-1);

                BigDecimal expenditure = round(result.getResults(), 2);
                String message = "Device name: " + result.getItemName() + "\n\t" + "Power: " + result.getPower() +
                        " W\n\t"+ "Consumption total: " + expenditure + " kWh";

                Toast toast = Toast.makeText(ResultGraph.this, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.START, 120, 40);
                toast.show();
            }
        });

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return getColor(data.getX());
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        ListView itemDetailsView = findViewById(R.id.item_details);

//        ArrayList<String> itemDetailsList = new ArrayList<>();

//        for (CalculationResult result : results) {
//
//            BigDecimal timeResult = round(result.getUsageTimeTotal(), 2);
//
//            String itemDetail = result.getItemName() + " " + result.getResults() + "Units\n" +
//                    timeResult + " hours/minutes, " + result.getPower() + " W";
////            itemDetailsList.add(itemDetail);
//        }
        final ItemDetailsAdapter ida = new ItemDetailsAdapter(this, results);
        itemDetailsView.setAdapter(ida);


    }

    private int getColor(double x) {
//        double r = c.getResults() / maxResult;
//        int rr =(int) Math.round(x/maxResult) * 255;
        int color = Color.rgb((int)Math.round(255.0/x), (int) Math.round(x*40), (int) Math.round(x*40));
       //Log.i("COLOR", Integer.toString(color));
        return color;
    }
    //nothing implemented --down

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("###RESULTGRAPH", "outTouchEVent X: " + event.getX() + ", Y: " + event.getY());


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("RESULTGRAPH", "onDown X: " + e.getX() + ", Y: " + e.getY());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("RESULTGRAPH", "onShowPress X: " + e.getX() + ", Y: " + e.getY());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("RESULTGRAPH", "onSingleTapUp X: " + e.getX() + ", Y: " + e.getY());
return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("RESULTGRAPH", "onScroll X: " + e1.getX() + ", Y: " + e2.getY());
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("RESULTGRAPH", "onLongPress X: " + e.getX() + ", Y: " + e.getY());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("RESULTGRAPH", "onFling X: " + e1.getX() + ", Y: " + e2.getY());
        return false;
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}