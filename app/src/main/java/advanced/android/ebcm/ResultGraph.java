package advanced.android.ebcm;

import advanced.android.ebcm.Device.Device;
import advanced.android.ebcm.Essentials.DatabaseHelper;
import advanced.android.ebcm.Graph.CalculationResult;
import advanced.android.ebcm.Graph.ItemDetailsAdapter;
import advanced.android.ebcm.Graph.ResultDetails;
import advanced.android.ebcm.Graph.SortCalculationResult;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;


public class ResultGraph extends AppCompatActivity {

    ArrayList<CalculationResult> results;
    ArrayList<CalculationResult> resultList;
    ArrayList<Device> devicesList;
    GraphView graphView;
    double maxResult;
    float kWhPrice, totalUnits = 0, totalTime = 0;
        BigDecimal totalPrice = BigDecimal.valueOf(0);
    int profileId;
    String correctTime;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileId = getIntent().getIntExtra("PROFILE_ID", -1);

        setTitle("Result for:       " + getIntent().getStringExtra("PROFILE_NAME") );

        if (profileId == 0 || profileId == -1) {
            finish();
        }

        //used to fill the graph
        results = loadData();

        drawGraph();
    } // end of onCreate

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("PROFILE_POWER",totalUnits);
        returnIntent.putExtra("PROFILE_COST", totalPrice.floatValue());
        returnIntent.putExtra("PROFILE_TIME", correctTime);

        setResult(RESULT_OK);
        super.onBackPressed();
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

        // setting size of a graph
        DisplayMetrics dm = getApplication().getResources().getDisplayMetrics();
        LinearLayout graphLayout = findViewById(R.id.graph_layout);
        ViewGroup.LayoutParams params = graphLayout.getLayoutParams();
        params.height = dm.heightPixels / 2;
        graphLayout.setLayoutParams(params);

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

        graphView.getViewport().setScrollable(false);
        graphView.getViewport().setScrollableY(false);

        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScalableY(false);


        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
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

                Intent intent = new Intent(ResultGraph.this, ResultDetails.class);
                intent.putExtra("NAME", result.getItemName());
                intent.putExtra("POWER", result.getPower() + " W");
                intent.putExtra("CONSUMPTION", expenditure + " kWh");
                intent.putExtra("TIME", String.valueOf(result.getUsageTimeTotal()));
                startActivity(intent);
                overridePendingTransition( R.anim.blink, 0);
                
            }
        });

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return getColor(data.getX(),devicesList.size());
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        ListView itemDetailsView = findViewById(R.id.item_details);

        priceList();

        ArrayList<CalculationResult> mostHungry = new ArrayList<>();

        for (int i = 0; i < 5; i ++){
            if (i < results.size()) {
                mostHungry.add(results.get(i));
            }
        }

        final ItemDetailsAdapter ida = new ItemDetailsAdapter(this, mostHungry);


        itemDetailsView.setAdapter(ida);
    }


    private void priceList() {

        TextView resultConsumption = findViewById(R.id.total_power);

        for ( CalculationResult result : results ) {

            totalUnits = totalUnits + result.getResults();
            totalTime = totalTime + result.getUsageTimeTotal();
        }

        totalPrice = round( totalUnits * kWhPrice,2) ;
        BigDecimal allUnits = round( totalUnits, 2);


        TextView costSum = findViewById(R.id.result_price_view);
        String costString = String.valueOf(totalPrice) + "€";
        costSum.setText(costString);

        resultConsumption.setText(String.valueOf((round(totalUnits,2).floatValue())) + " kWh");


        databasePowerCostTimeUpdate(totalPrice, allUnits, totalTime);
    }

    private void databasePowerCostTimeUpdate(BigDecimal totalCost, BigDecimal totalUnits, float totalTime) {

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        int hours = (int)(totalTime / 60);
        float minutes = (totalTime / 60)%1;
        minutes = minutes * 100;
        BigDecimal usageTotal = round( minutes, 2);
        String properMinutes = String.valueOf(Integer.valueOf(usageTotal.intValue()));
        properMinutes = properMinutes.contains(".") ? properMinutes.replaceAll("0*$","").replaceAll("\\.$","") : properMinutes;

        correctTime = String.valueOf(hours) + ":" + properMinutes;
        TextView resultTime = findViewById(R.id.total_time);
        resultTime.setText(String.valueOf(correctTime) + " h");


        mDatabaseHelper.updateProfilePowerCost(totalCost, totalUnits, profileId);
        mDatabaseHelper.updateProfileTime(String.valueOf(correctTime), profileId);

        mDatabaseHelper.close();
    }

    private DataPoint[] getDataPoints() {

        Collections.sort(results, new SortCalculationResult());
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

    private int getColor(double x,int devicesTot) {
        double R = (255 * (devicesTot-x)) / devicesTot;
        double G = (255 * (devicesTot - (devicesTot-x))) / devicesTot;
        double B = 0;
        return Color.rgb((int)R, (int)G, (int)B);

    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}