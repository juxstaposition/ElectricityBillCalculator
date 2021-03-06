package advanced.android.ebcm.Device;

import advanced.android.ebcm.Essentials.Constant;
import advanced.android.ebcm.Essentials.DatabaseHelper;
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
import org.json.JSONException;
import org.json.JSONObject;

import static advanced.android.ebcm.Essentials.Constant.EDIT_DEVICE;

public class NewDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout nameInput, consumptionInput, quantityInput,
                    usageHoursInput, usageMinutesInput, usageDaysInput;

    int profileParent, deviceId;

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

        findViewById(R.id.backViewNavigation).setOnClickListener(this);
        findViewById(R.id.buttonItemCancel).setOnClickListener(this);
        findViewById(R.id.buttonItemAdd).setOnClickListener(this);
        findViewById(R.id.buttonItemPick).setOnClickListener(this);

        if (getIntent().getStringExtra("KEY").equals(EDIT_DEVICE)){

            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit_device_item);

            Button btnUpdate = findViewById(R.id.buttonItemAdd);
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
            overridePendingTransition(R.anim.blink,0);
        }
        else if (view.getId() == R.id.buttonItemAdd){
            animation.startAnimation(view,R.anim.blink,getApplicationContext());
            // request input content
            String name = nameInput.getEditText().getText().toString();
            String quantity = quantityInput.getEditText().getText().toString();
            String consumption = consumptionInput.getEditText().getText().toString();
            String usageDays = usageDaysInput.getEditText().getText().toString();
            Intent returnIntent = new Intent();

            String usageHours = "0";
            String usageMinutes = "0";
            if(usageHoursInput.getEditText().getText().toString().length() != 0) {
                usageHours = usageHoursInput.getEditText().getText().toString();
            }
            if(usageMinutesInput.getEditText().getText().toString().length() != 0) {
                usageMinutes = usageMinutesInput.getEditText().getText().toString();
            }
            // validation of input parameters
            boolean validation =true;

            if( name.length() == 0){
                validation = setTextError(nameInput,"Insert Device Name!");
            }
            if (consumption.length() == 0 || Integer.parseInt(consumption) <= 0){
                validation = setTextError(consumptionInput,"Power must be greater than 0!");
            }
            if (quantity.length() == 0 || Integer.parseInt(quantity) < 1 ){
                validation = setTextError(quantityInput,"At least 1 device must be used!");
            }


            if(Integer.parseInt(usageHours) > 24){
                validation = setTextError(usageHoursInput,"Invalid time format");
            }

            if (Integer.parseInt(usageMinutes) > 59 ) {
                validation = setTextError(usageMinutesInput,"Invalid time format");
            }

            if (Integer.parseInt(usageHours) == 24 && Integer.parseInt(usageMinutes) > 0 ) {
                validation = setTextError(usageMinutesInput,"Invalid time format");
            }

            if (Integer.parseInt(usageHours) == 0 && Integer.parseInt(usageMinutes) == 0 ) {
                validation = setTextError(usageMinutesInput,"Invalid time format");
            }


            if (usageDays.length() == 0 || Integer.parseInt(usageDays) < 1 ){
                validation = setTextError(usageDaysInput,"Must be used at least 1 day!");
            }

            if (usageDays.length() == 0 || Integer.parseInt(usageDays) > 30 ){
                validation = setTextError(usageDaysInput,"For calculation purposes\nmonth can have max 30 days!");

            }

            if (validation){


                if (getIntent().getStringExtra("KEY").equals(EDIT_DEVICE)){
                    updateDevice(Integer.parseInt(consumption),Integer.parseInt(quantity),Integer.parseInt(usageHours),
                                 Integer.parseInt(usageMinutes),Integer.parseInt(usageDays));

                } else if (profileParent == -1) {
                    Toast.makeText(this, "profileParent"+profileParent, Toast.LENGTH_SHORT).show();
                }
                else {

                    returnIntent.putExtra("DEVICE_NAME", name);
                    returnIntent.putExtra("DEVICE_CONSUMPTION", consumption);
                    returnIntent.putExtra("DEVICE_QUANTITY", quantity);
                    returnIntent.putExtra("DEVICE_USAGE_HOURS", usageHours);
                    returnIntent.putExtra("DEVICE_USAGE_MINUTES", usageMinutes);
                    returnIntent.putExtra("DEVICE_USAGE_DAYS", usageDays);

                    addDevice(name, Integer.parseInt(quantity), Integer.parseInt(usageHours), Integer.parseInt(usageMinutes),
                            Integer.parseInt(usageDays), Integer.parseInt(consumption), profileParent);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }// end if button item add
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bundle mBundle = data.getExtras();

        if (requestCode == Constant.PICK_AN_ITEM_REQ_CODE && resultCode == Activity.RESULT_OK) {

            try {
                JSONObject jsonObj = new JSONObject(mBundle.getString("NAME"));
                nameInput.getEditText().setText(jsonObj.get("itemName").toString());
                consumptionInput.getEditText().setText(jsonObj.get("power").toString());
            } catch (JSONException e) {
                Log.e("JSONERROR", e.getLocalizedMessage());
            }
        }
    }

    private void addDevice(String name, int quantity, int usageHours, int usageMinutes, int usageDays, int consumption, int profileParent) {

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        boolean insertData = mDatabaseHelper.addDeviceData(name,quantity,usageHours,usageMinutes,usageDays,consumption,profileParent);

        if (insertData) {
            sendWarningToast("Device Added");
        } else {
            sendWarningToast("Something went wrong");
        }

        mDatabaseHelper.close();
    }

    private void updateDevice( int consumption ,int quantity, int usageHours, int usageMinutes ,int usageDays) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        String name = nameInput.getEditText().getText().toString();

        mDatabaseHelper.updateDevice(name,consumption,quantity,usageHours,usageMinutes,usageDays, deviceId);

        mDatabaseHelper.close();

        sendWarningToast("Device updated");

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
     * @param message   Message to be displayed
     * @return  return helps to set false result, always false
     */
    private void sendWarningToast(String message){
        Toast.makeText(NewDeviceActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean setTextError(TextInputLayout textLayout, String message){
        textLayout.getEditText().setError(message);
        return false;
    }
}
