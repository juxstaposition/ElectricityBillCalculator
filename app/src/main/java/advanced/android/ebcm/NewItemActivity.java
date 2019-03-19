package advanced.android.ebcm;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout nameInput = null, consumptionInput = null,
        quantityInput = null, usageHoursInput = null, usageDaysInput = null;

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

        nameInput = findViewById(R.id.newItemName);
        consumptionInput = findViewById(R.id.newItemConsumption);
        quantityInput = findViewById(R.id.newItemQuantity);
        usageHoursInput = findViewById(R.id.newItemUsageHours);
        usageDaysInput = findViewById(R.id.newItemUsageDays);

        findViewById(R.id.buttonItemCancel).setOnClickListener(this);
        findViewById(R.id.buttonItemAdd).setOnClickListener(this);
        findViewById(R.id.buttonItemPick).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if (view.getId() == R.id.buttonItemCancel){
            super.onBackPressed();
        }
        else if (view.getId() == R.id.buttonItemPick){

        }
        else if (view.getId() == R.id.buttonItemAdd){
            boolean validation = true;

            String  name = nameInput.getEditText().getText().toString(),
                    consumption = consumptionInput.getEditText().getText().toString(),
                    quantity = quantityInput.getEditText().getText().toString(),
                    usageHours = usageHoursInput.getEditText().getText().toString(),
                    usageDays = usageDaysInput.getEditText().getText().toString();

            if( name.length() == 0){
                Toast.makeText(NewItemActivity.this, "Insert Device Name!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && (consumption.length() == 0 || Integer.parseInt(consumption) <= 0 )){
                Toast.makeText(NewItemActivity.this, "Consumption must be greater than 0!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && (quantity.length() == 0 || Integer.parseInt(quantity) < 1 )){
                Toast.makeText(NewItemActivity.this, "At least 1 device must be used!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && (usageHours.length() == 0 || Integer.parseInt(usageHours) < 1 )) {
                Toast.makeText(NewItemActivity.this, "Must be used at least 1 minute!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && (usageDays.length() == 0 || Integer.parseInt(usageDays) < 1 )) {
                Toast.makeText(NewItemActivity.this, "Must be used at least 1 day!", Toast.LENGTH_SHORT).show();
                validation = false;
            }


            System.out.println("Item Name "        + name);
            System.out.println("Item Consumption " + consumption);
            System.out.println("Item Quantity "    + quantity);
            System.out.println("Item Usage hours "  + usageHours);
            System.out.println("Item Usage days "   + usageDays);

            if (validation){
                Toast.makeText(NewItemActivity.this, "New Device Added!", Toast.LENGTH_LONG).show();
                super.onBackPressed();
            }
        }

    }
}
