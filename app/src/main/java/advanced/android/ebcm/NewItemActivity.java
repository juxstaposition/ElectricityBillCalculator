package advanced.android.ebcm;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout name = null, consumption = null,
        quantity = null, usageHours = null, usageDays = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        System.out.println("height = "+ height + " width = "+width);

        getWindow().setLayout( width*8/10,height*8/10 );

        name = findViewById(R.id.newItemName);
        consumption = findViewById(R.id.newItemConsumption);
        quantity = findViewById(R.id.newItemQuantity);
        usageHours = findViewById(R.id.newItemUsageHours);
        usageDays = findViewById(R.id.newItemUsageDays);

        findViewById(R.id.buttonItemCancel).setOnClickListener(this);
        findViewById(R.id.buttonItemAdd).setOnClickListener(this);
        findViewById(R.id.buttonPickItem).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if (view.getId() == R.id.buttonItemCancel){
            super.onBackPressed();
        }
        else if (view.getId() == R.id.buttonPickItem){

        }
        else if (view.getId() == R.id.buttonItemAdd){
            System.out.println("Item Name "        + name.getEditText().getText().toString());
            System.out.println("Item Consumption " + consumption.getEditText().getText().toString());
            System.out.println("Item Quantity "    + quantity.getEditText().getText().toString());
            System.out.println("Item Usage hours "  + usageHours.getEditText().getText().toString());
            System.out.println("Item Usage days "   + usageHours.getEditText().getText().toString());
        }

    }
}
