package advanced.android.ebcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity  extends AppCompatActivity implements View.OnClickListener  {

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        profileNameInput = findViewById(R.id.newProfileName);
        profileDescriptionInput = findViewById(R.id.newProfileDescription);
        profilePriceInput = findViewById(R.id.newProfilePrice);

        findViewById(R.id.buttonProfileUpdate).setOnClickListener(this);
        findViewById(R.id.buttonProfileDelete).setOnClickListener(this);
        findViewById(R.id.back_view).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseHelper.close();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();

        Constant animation = new Constant();
        animation.startAnimation(view,R.anim.blink,getApplicationContext());

        if (view.getId() == R.id.buttonProfileUpdate){



            boolean validation = true;

            String  profileName = profileNameInput.getEditText().getText().toString(),
                    profileDescription = profileDescriptionInput.getEditText().getText().toString(),
                    profilePrice = profilePriceInput.getEditText().getText().toString();

            if (profileName.length() == 0){
                validation = sendWarningToast("Insert Profile Name!");
            }

            if (validation && profileDescription.length() == 0 ){
                validation = sendWarningToast("Insert Description Name!");
            }
            String checkPriceValue = profilePrice;
            if (validation && (profilePrice.length() == 0 || Integer.parseInt(checkPriceValue) <= 0 )){
                validation = sendWarningToast("Cost must be greater than 0!");
            }

            if (validation){

                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                setResult(Activity.RESULT_OK, returnIntent);

                Log.d("PROFILE", profileName +", " + profileDescription + ", " + profilePrice);
                updateProfile();
                finish();
            }
        }
        else if (view.getId() == R.id.buttonProfileDelete || view.getId() == R.id.back_view){
            deleteProfile();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    private boolean sendWarningToast(String message){
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }


    private void updateProfile () {

        Intent receivedIntent = getIntent();

        TextView name = findViewById(R.id.edit_profile_name);
        TextView description = findViewById(R.id.edit_profile_description);
        TextView price = findViewById(R.id.edit_profile_price);

        Integer id = Integer.parseInt(receivedIntent.getStringExtra("ID"));
        String oldName = receivedIntent.getStringExtra("PROFILE_NAME");

        String newName = name.getText().toString();
        String newDescription = description.getText().toString();
        String newPrice = price.getText().toString();


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        mDatabaseHelper.updateProfile(newName,newDescription,newPrice, id, oldName);

    }

    private void deleteProfile () {
        Intent receivedIntent = getIntent();
        Integer id = Integer.parseInt(receivedIntent.getStringExtra("ID"));
        String name = receivedIntent.getStringExtra("PROFILE_NAME");

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.deleteProfile(id,name);
        Toast.makeText(this,"removed " + name + " with id "+ id +" from database.", Toast.LENGTH_SHORT).show();
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMesasge(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
