package advanced.android.ebcm;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.*;

import java.util.ArrayList;
import java.util.Collections;


public class ResultGraph extends AppCompatActivity {

    GraphView graphView;
    ArrayList<CalculationResult> results;
    double maxResult = -1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        results = loadData();
        setContentView(R.layout.activity_result_graph);
        GraphView graphView = (GraphView) findViewById(R.id.graphView);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(8);

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
//        new DataPoint(0, 0),
//                new DataPoint(1, 700),
//                new DataPoint(2, 100),
//                new DataPoint(3, 200),
//                new DataPoint(4, 90),
//                new DataPoint(5, 170),
//                new DataPoint(6, 7000),
//                new DataPoint(7, 0),
//                new DataPoint(8, 700),
//                new DataPoint(9, 300),
//                new DataPoint(10, 2500),
//                new DataPoint(11, 25),
//                new DataPoint(12, 17060),
//                new DataPoint(13, 700),
        graphView.getViewport().setMaxY(maxResult * 1.2);

        graphView.addSeries(series);
        series.setAnimated(true);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                String masg = +dataPoint.getY() + "W";
                Toast toast = Toast.makeText(ResultGraph.this, masg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
//                return getColor(data.getY());
                return getColor(data.getX());
//                if (0 <= data.getY() && data.getY() < 1500)  {
//
//                    return Color.GREEN;
//
//                    } else if(1500<= data.getY() && data.getY()<8000){
//
//                    return Color.BLUE;
//                } else {
//                    return Color.RED;
//                }
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        ListView itemDetailsView = findViewById(R.id.item_details);
        ListView totalDetailsView = findViewById(R.id.total_details);

        ArrayList<String> itemDetailsList = new ArrayList<>();
        ArrayList<String> totalDetailsList = new ArrayList<>();

        for (CalculationResult result : results) {
            String itemDetail = result.getItemName() + " " + result.getResults() + "Units\n" +
                    result.getUsageTimeTotal() + " hours, " + result.getPower() + " W";
            itemDetailsList.add(itemDetail);
        }
        final ItemDetailsAdapter ida = new ItemDetailsAdapter(this, results);
//        final ArrayAdapter<String> aa =
//                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemDetailsList);
        itemDetailsView.setAdapter(ida);


    }

    private DataPoint[] getDataPoints() {
        Collections.sort(results);
        DataPoint[] dataPoints = new DataPoint[results.size()];
        for (int i = 0; i < dataPoints.length; i++) {
            double result = results.get(i).getResults();
            if (result > maxResult) {
                maxResult = result;
            }
            dataPoints[i] = new DataPoint(i, result);
        }
        return dataPoints;
    }

    private ArrayList<CalculationResult> loadData() {
        ArrayList<CalculationResult> resultList = new ArrayList<>();
        resultList.add(new CalculationResult("Lamps", 60.0, 4, 4, 16));
        resultList.add(new CalculationResult("Coffee Machine", 1000.0, 1, .5, 30));
        resultList.add(new CalculationResult("TV", 500.0, 1, 6, 20));
        resultList.add(new CalculationResult("Vacuum Cleaner", 900.0, 1, 2, 6));
        return resultList;
    }

    private int getColor(double x) {
//        double r = c.getResults() / maxResult;
//        int rr =(int) Math.round(x/maxResult) * 255;
        int color = Color.rgb((int)Math.round(255.0/x), (int) Math.round(x*40), (int) Math.round(x*40));
        Log.i("COLOR", Integer.toString(color));
        return color;
    }
}