package advanced.android.ebcm.Graph;

import advanced.android.ebcm.R;
import advanced.android.ebcm.ResultGraph;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;

public class ResultDetails extends AppCompatActivity {

    public TextView name, power, powerTotal, timeTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_device_info);


        name = findViewById(R.id.result_device_name);
        power = findViewById(R.id.result_device_power);
        powerTotal = findViewById(R.id.result_device_consumption);
        timeTotal = findViewById(R.id.result_device_time);

        name.setText(getIntent().getStringExtra("NAME"));
        power.setText(getIntent().getStringExtra("POWER"));
        powerTotal.setText(getIntent().getStringExtra("CONSUMPTION"));

        setTime();

        findViewById(R.id.backViewResultDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }


    private void setTime() {

        int hours =(int) (Float.valueOf(getIntent().getStringExtra("TIME")) / 60);
        float minutes =  (Float.valueOf(getIntent().getStringExtra("TIME")) /60 %1);
        minutes = minutes * 100;
        BigDecimal usageTotal = ResultGraph.round( minutes, 2);
        String properMinutes = String.valueOf(Integer.valueOf(usageTotal.intValue()));
        properMinutes = properMinutes.contains(".") ? properMinutes.replaceAll("0*$","").replaceAll("\\.$","") : properMinutes;

        String correctTime = String.valueOf(hours) + ":" + properMinutes + " h";

        timeTotal.setText(correctTime);
    }

}
