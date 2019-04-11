package advanced.android.ebcm.Device;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.PickItemList;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static advanced.android.ebcm.Constant.EDIT_DEVICE;

public class NewDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout nameInput = null, consumptionInput = null,
        quantityInput = null, usageHoursInput = null, usageMinutesInput = null, usageDaysInput = null;
    int profileParent, deviceId;
    Button btnUpdate;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_device);

        nameInput = findViewById(R.id.newItemName);
        consumptionInput = findViewById(R.id.newItemConsumption);
        quantityInput = findViewById(R.id.newItemQuantity);
        usageHoursInput = findViewById(R.id.newItemUsageHours);
        usageMinutesInput = findViewById(R.id.newItemUsageMinutes);
        usageDaysInput = findViewById(R.id.newItemUsageDays);

        if (getIntent().getStringExtra("KEY").equals(EDIT_DEVICE)){
            title= findViewById(R.id.title);
            title.setText(R.string.edit_device_item);

            btnUpdate = findViewById(R.id.buttonItemAdd);
            btnUpdate.setText(R.string.update);

            deviceId = Integer.parseInt(getIntent().getStringExtra("DEVICE_ID"));
            nameInput.getEditText().setText(getIntent().getStringExtra("DEVICE_NAME"));
            consumptionInput.getEditText().setText(getIntent().getStringExtra("DEVICE_CONSUMPTION"));
            quantityInput.getEditText().setText(getIntent().getStringExtra("DEVICE_QUANTITY"));
            usageHoursInput.getEditText().setText(getIntent().getStringExtra("DEVICE_USAGE_HOURS"));
            usageMinutesInput.getEditText().setText(getIntent().getStringExtra("DEVICE_USAGE_MINUTES"));
            usageDaysInput.getEditText().setText(getIntent().getStringExtra("DEVICE_USAGE_DAYS"));
        } else {
            profileParent = Integer.parseInt(getIntent().getStringExtra("PROFILE_ID"));
        }



        findViewById(R.id.backViewNavigation).setOnClickListener(this);
        findViewById(R.id.buttonItemCancel).setOnClickListener(this);
        findViewById(R.id.buttonItemAdd).setOnClickListener(this);


        findViewById(R.id.buttonItemPick).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonItemPick).setOnClickListener(this);
        findViewById(R.id.orText).setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

    @Override
    public void onClick(View view){
        Constant animation = new Constant();

        if (view.getId() == R.id.buttonItemCancel){
            animation.startAnimation(view,R.anim.blink,getApplicationContext());
            finish();
        }
        else if (view.getId() == R.id.backViewNavigation){
            finish();
        }
        else if (view.getId() == R.id.buttonItemPick){
            Intent intent = new Intent(getApplicationContext(), PickItemList.class);
            animation.startAnimation(view,R.anim.blink,getApplicationContext());
            startActivityForResult(intent, Constant.PICK_AN_ITEM_REQ_CODE);
            overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_left_exit);
        }
        else if (view.getId() == R.id.buttonItemAdd){
            if (getIntent().getStringExtra("KEY").equals(EDIT_DEVICE)){
                updateDevice();

            } else if (profileParent == -1) {
                Toast.makeText(this, "profileParent"+profileParent, Toast.LENGTH_SHORT).show();
            }
            else {

                animation.startAnimation(view,R.anim.blink,getApplicationContext());
                // request input content
                String  name = nameInput.getEditText().getText().toString(),
                consumption = consumptionInput.getEditText().getText().toString(),
                quantity = quantityInput.getEditText().getText().toString(),
                usageHours = usageHoursInput.getEditText().getText().toString(),
                usageMinutes = usageMinutesInput.getEditText().getText().toString(),
                usageDays = usageDaysInput.getEditText().getText().toString();
                Intent returnIntent = new Intent();

                // validation of input parameters
                boolean validation = validate(name,consumption,quantity,usageHours,usageMinutes,usageDays);

                if (validation){
                    Toast.makeText(NewDeviceActivity.this, "New Device Added!", Toast.LENGTH_LONG).show();

                    returnIntent.putExtra("DEVICE_NAME", name);
                    returnIntent.putExtra("DEVICE_CONSUMPTION", consumption);
                    returnIntent.putExtra("DEVICE_QUANTITY", quantity);
                    returnIntent.putExtra("DEVICE_USAGE_HOURS", usageHours);
                    returnIntent.putExtra("DEVICE_USAGE_MINUTES", usageMinutes);
                    returnIntent.putExtra("DEVICE_USAGE_DAYS", usageDays);

                    String group = "testGroup";

                    addDevice(name, Integer.parseInt(quantity), Integer.parseInt(usageHours), Integer.parseInt(usageMinutes),
                            Integer.parseInt(usageDays), group, Integer.parseInt(consumption), profileParent);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bundle mBundle = data.getExtras();
        if (requestCode == Constant.PICK_AN_ITEM_REQ_CODE && resultCode == Activity.RESULT_OK) {
            nameInput.getEditText().setText(mBundle.getString("NAME"));
        }
    }



    private void addDevice(String name, int quantity, int usageHours, int usageMinutes, int usageDays, String group, int consumption, int profileParent) {
        Log.d("NEW_DEVICE/addDevice", "name: "+ name +", consumption: "+ consumption + ", quantity: "+ quantity +", usageHours: "+
                usageHours +", usageMinutes: "+ usageMinutes +", usageDays: "+ usageDays + ", profileParent: "+ profileParent);

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        boolean insertData = mDatabaseHelper.addDeviceData(name,quantity,usageHours,usageMinutes,usageDays,consumption,profileParent);

        if (insertData) {
            sendWarningToast("Device Successfully Added");
        } else {
            sendWarningToast("Something went wrong");
        }

        mDatabaseHelper.close();
    }

    private void updateDevice() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        String name = nameInput.getEditText().getText().toString();
        int consumption =Integer.parseInt(consumptionInput.getEditText().getText().toString());
        int quantity = Integer.parseInt(quantityInput.getEditText().getText().toString());
        int usageHours = Integer.parseInt(usageHoursInput.getEditText().getText().toString());
        int usageMinutes = Integer.parseInt(usageMinutesInput.getEditText().getText().toString());
        int usageDays = Integer.parseInt(usageDaysInput.getEditText().getText().toString());

        mDatabaseHelper.updateDevice(name,consumption,quantity,usageHours,usageMinutes,usageDays, deviceId);

        mDatabaseHelper.close();

        sendWarningToast("Device in Database updated");

        Intent returnIntent = new Intent();

        returnIntent.putExtra("DEVICE_ID", deviceId);
        returnIntent.putExtra("DEVICE_NAME", name);
        returnIntent.putExtra("DEVICE_CONSUMPTION", consumption);
        returnIntent.putExtra("DEVICE_QUANTITY", quantity);
        returnIntent.putExtra("DEVICE_USAGE_HOURS", usageHours);
        returnIntent.putExtra("DEVICE_USAGE_MINUTES", usageMinutes);
        returnIntent.putExtra("DEVICE_USAGE_DAYS", usageDays);

        setResult(RESULT_OK, returnIntent);
        finish();

    }

    /**
     *
     * @param name          Name of the device from user input
     * @param consumption   Consumption of the device from user input
     * @param quantity      Quantity of the devices from user input
     * @param usageHours    Hours from user input
     * @param usageMinutes  Minutes from user input
     * @param usageDays     Days fom user input
     * @return returns boolean result of validation
     */
    private boolean validate(String name, String consumption, String quantity, String usageHours, String usageMinutes, String usageDays){
        boolean validation = true;

        if( name.length() == 0){
            validation = sendWarningToast("Insert Device Name!");
        }
        if (validation && (consumption.length() == 0 || Integer.parseInt(consumption) <= 0 ) ){
            validation = sendWarningToast("Consumption must be greater than 0!");
        }
        if (validation && (quantity.length() == 0 || Integer.parseInt(quantity) < 1 )){
            validation = sendWarningToast("At least 1 device must be used!");
        }

        // if hours is empty but minutes is correct, automatically fills  hours
        if (validation && usageHours.length() == 0 &&  usageMinutes.length() > 0 ){
            if (Integer.parseInt(usageMinutes) > 59 ){
                validation = sendWarningToast("Invalid time format");
            }
            else{
                usageHours = "0";
            }
        }


        if (validation && (usageHours.length() == 0 || Integer.parseInt(usageHours) < 1 &&
                (usageMinutes.length() == 0 || Integer.parseInt(usageMinutes) < 1)) ){
            validation = sendWarningToast("Usage must be used at least 1 minute!");
        }
        //            if (validation && (usageMinutes.length() == 0 || Integer.parseInt(usageMinutes) < 1)) {
        //                validation = sendWarningToast("Must be used at least 1 minute!");
        //            }
        if (validation && (usageDays.length() == 0 || Integer.parseInt(usageDays) < 1) ){
            validation = sendWarningToast("Must be used at least 1 day!");
        }

                /*
                if (validation && Integer.parseInt(usageHours) >= 24 || Integer.parseInt(usageMinutes) >= 59 ||
                                  (Integer.parseInt(usageHours) == 24 || Integer.parseInt(usageMinutes) < 0  ) ){
                    validation = sendWarningToast("Invalid time format");
                }*/
        return validation;
    }// end validate

    /**
     * @param message   Message to be displayed
     * @return  return helps to set false result, always false
     */
    private boolean sendWarningToast(String message){
        Toast.makeText(NewDeviceActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }
}
