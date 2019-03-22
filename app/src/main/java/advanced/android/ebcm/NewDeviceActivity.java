package advanced.android.ebcm;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class NewDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout nameInput = null, consumptionInput = null,
        quantityInput = null, usageHoursInput = null, usageDaysInput = null;
    boolean validation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        System.out.println("height = "+ height + " width = "+width);

        getWindow().setLayout( width*8/10,height*8/10 );                */



        nameInput = findViewById(R.id.newItemName);
        consumptionInput = findViewById(R.id.newItemConsumption);
        quantityInput = findViewById(R.id.newItemQuantity);
        usageHoursInput = findViewById(R.id.newItemUsageHours);
        usageDaysInput = findViewById(R.id.newItemUsageDays);

        findViewById(R.id.backViewNavigation).setOnClickListener(this);
        findViewById(R.id.buttonItemCancel).setOnClickListener(this);
        findViewById(R.id.buttonItemAdd).setOnClickListener(this);

        final String transferredData = getIntent().getStringExtra("KEY");

        if (transferredData.equals(Constant.FAVOURITE_DEVICE)){
            findViewById(R.id.buttonItemPick).setVisibility(View.GONE);
            findViewById(R.id.orText).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.buttonItemPick).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonItemPick).setOnClickListener(this);
            findViewById(R.id.orText).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view){

        if (view.getId() == R.id.buttonItemCancel || view.getId() == R.id.backViewNavigation){
            super.onBackPressed();
        }
        else if (view.getId() == R.id.buttonItemPick){

        }
        else if (view.getId() == R.id.buttonItemAdd){
            String  name = nameInput.getEditText().getText().toString(),
            consumption = consumptionInput.getEditText().getText().toString(),
            quantity = quantityInput.getEditText().getText().toString(),
            usageHours = usageHoursInput.getEditText().getText().toString(),
            usageDays = usageDaysInput.getEditText().getText().toString();

            if( name.length() == 0){
                sendWarningToast("Insert Device Name!");
            }
            if (validation && (consumption.length() == 0 || Integer.parseInt(consumption) <= 0 )){
                sendWarningToast("Consumption must be greater than 0!");
            }
            if (validation && (quantity.length() == 0 || Integer.parseInt(quantity) < 1 )){
                sendWarningToast("At least 1 device must be used!");
            }
            if (validation && (usageHours.length() == 0 || Integer.parseInt(usageHours) < 1 )) {
                sendWarningToast("Must be used at least 1 minute!");
            }
            if (validation && (usageDays.length() == 0 || Integer.parseInt(usageDays) < 1 )) {
                sendWarningToast("Must be used at least 1 day!");
            }

            System.out.println("Item Name "        + name);
            System.out.println("Item Consumption " + consumption);
            System.out.println("Item Quantity "    + quantity);
            System.out.println("Item Usage hours "  + usageHours);
            System.out.println("Item Usage days "   + usageDays);

            if (validation){
                Toast.makeText(NewDeviceActivity.this, "New Device Added!", Toast.LENGTH_LONG).show();
                super.onBackPressed();
            }
        }
    }

    private void sendWarningToast(String message){
        Toast.makeText(NewDeviceActivity.this, message, Toast.LENGTH_SHORT).show();
        validation = false;
    }
}
