package advanced.android.ebcm;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

import java.security.KeyStore;

public class ResultGraph extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_graph);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        graph.getGridLabelRenderer(). setLabelFormatter(new DefaultLabelFormatter() {
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
               
        });
        graph.addSeries(series);


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);




    }
}