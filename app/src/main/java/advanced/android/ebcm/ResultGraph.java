package advanced.android.ebcm;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.*;

import java.security.KeyStore;

public class ResultGraph extends AppCompatActivity {

GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_graph);
        GraphView graphView = (GraphView) findViewById(R.id.graphView);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(8);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(400);
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




         BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 700),
                new DataPoint(2, 3000),
                new DataPoint(3, 2500),
                new DataPoint(4, 60),
                new DataPoint(5, 17060),
                new DataPoint(6, 700),
                new DataPoint(7, 0),
                new DataPoint(8, 700),
                new DataPoint(9, 3000),
                new DataPoint(10, 2500),
                new DataPoint(11, 60),
                new DataPoint(12, 17060),
                new DataPoint(13, 700),
               
        });
        graphView.addSeries(series);
series.setOnDataPointTapListener(new OnDataPointTapListener() {
    @Override
    public void onTap(Series series, DataPointInterface dataPoint) {
        String masg = "X: "+dataPoint.getX()+"\nY: "+dataPoint.getY();
        Toast.makeText(ResultGraph.this,masg,Toast.LENGTH_SHORT).show();

    }

});


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255/4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);




    }
}