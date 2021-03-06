package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Essentials.Constant;
import advanced.android.ebcm.Essentials.DatabaseHelper;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;
    TextInputEditText description;

    boolean newProfile = true;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);


        profileNameInput = findViewById(R.id.editProfileName);
        profileDescriptionInput = findViewById(R.id.editProfileDescription);
        profilePriceInput = findViewById(R.id.editProfilePrice);

        AutoCompleteTextView price = findViewById(R.id.edit_profile_price);
        AutoCompleteTextView name = findViewById(R.id.edit_profile_name);

        setProfileSuggestions(price);

        final String transferredData = getIntent().getStringExtra("KEY");

        if (transferredData.equals(Constant.EDIT_PROFILE)) {
            newProfile = false;

            TextView profileTitle = findViewById(R.id.newProfileTitle);
            profileTitle.setText(R.string.edit_profile);

            TextView buttonAdd = findViewById(R.id.buttonProfileAdd);
            buttonAdd.setText(R.string.confirm);

            name.setText(getIntent().getStringExtra("PROFILE_NAME"));

            description = findViewById(R.id.edit_profile_description);
            description.setText(getIntent().getStringExtra("PROFILE_DESCRIPTION"));

            price.setText(getIntent().getStringExtra("PROFILE_PRICE"));
        }

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonDeleteCancel).setOnClickListener(this);
        findViewById(R.id.back_view_edit_profile).setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

    @Override
    public void onBackPressed(){

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onClick(View view) {

        Intent returnIntent = new Intent();

        Constant animation = new Constant();
        animation.startAnimation(view,R.anim.blink,getApplicationContext());

        if (view.getId() == R.id.buttonProfileAdd){

            boolean validation = true;




            String  profileName = profileNameInput.getEditText().getText().toString(),
                    profileDescription = profileDescriptionInput.getEditText().getText().toString(),
                    profilePrice = profilePriceInput.getEditText().getText().toString();

            if (profileName.length() == 0){
                AutoCompleteTextView name = findViewById(R.id.edit_profile_name);
                name.setError("Please insert profile Name!");
                validation = false;
            }

            if (profileDescription.length() == 0 ){
                profileDescription = "";
            }

            if (profilePrice.contains("€")){
                String[] stringSplit = profilePrice.split("€");
                profilePrice = stringSplit[0];
            }

            if (profilePrice.length() == 0 || Float.valueOf(profilePrice.trim()) <= 0 ){
                AutoCompleteTextView price = findViewById(R.id.edit_profile_price);
                price.setError("Price per kWh must be greater than 0!");
                validation = false;
            }

            if (validation){

                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                returnIntent.putExtra("PROFILE_POWER", 0);
                returnIntent.putExtra("PROFILE_COST", 0);
                Log.d("PROFILE", profileName +", " + profileDescription + ", " + profilePrice);

                if (newProfile) {
                    mDatabaseHelper = new DatabaseHelper(this);
                    addProfileToDatabase(profileName, profileDescription, profilePrice);
                    mDatabaseHelper.close();
                }
                else {
                    updateProfile();
                }

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        else if (view.getId() == R.id.buttonDeleteCancel || view.getId() == R.id.back_view_edit_profile){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }


    //      !!!  power and cost added on database side (inside DatabaseHelper)
    private void addProfileToDatabase (String name, String description, String price) {

        mDatabaseHelper.addProfileData(name,description,price);
    }

    private void updateProfile () {

        Intent receivedIntent = getIntent();

        TextView name = findViewById(R.id.edit_profile_name);
        TextView description = findViewById(R.id.edit_profile_description);
        TextView price = findViewById(R.id.edit_profile_price);


        int id = receivedIntent.getIntExtra("PROFILE_ID", -1);


        String newName = name.getText().toString();
        String newDescription = description.getText().toString();
        String newPrice = price.getText().toString();

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.updateProfileMain(newName,newDescription,newPrice, id);
        mDatabaseHelper.close();
    }

    private void setProfileSuggestions(AutoCompleteTextView price){
        String[] COUNTRIES = new String[]{
                "0.086€, Albania","0.198€, Austria","0.288€, Belgium",
                "0.098€, Bulgaria","0.149€, Czech Republic","0.124€, Croatia",
                "0.183€, Cyprus","0.301€, Denmark","0.132€, Estonia",
                "0.160€, Finland","0.176€, France","0.305€, Germany",
                "0.162€, Greece","0.113€, Hungary","0.152€, Iceland",
                "0.208€, Italy","0.065€, Kosovo","0.158€, Latvia",
                "0.111€, Lithuania","0.162€, Luxembourg","0.136€, Malta",
                "0.101€, Moldova","0.100€, Montenegro","0.156€, Netherlands",
                "0.161€, Norway","0.145€, Poland","0.223€, Portugal",
                "0.129€, Romania","0.081€, Republic of Macedonia","0.070€, Serbia",
                "0.144€, Slovakia","0.161€, Slovenia","0.218€, Spain",
                "0.199€, Sweden","0.096€, Turkey","0.038€, Ukraine",
                "0.186€, United Kingdom",
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.autocomplete_view, COUNTRIES);
        price.setAdapter(adapter);


    }
    
}

