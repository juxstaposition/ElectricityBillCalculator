package advanced.android.ebcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput = null, profileDescriptionInput = null, profilePriceInput = null;
    InputMethodManager imm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        profileNameInput = findViewById(R.id.newProfileName);
        profileDescriptionInput = findViewById(R.id.newProfileDescription);
        profilePriceInput = findViewById(R.id.newProfilePrice);

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonProfileCancel).setOnClickListener(this);
        findViewById(R.id.back_view).setOnClickListener(this);

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

        if (view.getId() == R.id.buttonProfileAdd){



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

                Toast.makeText(NewProfileActivity.this, "New Profile Created!", Toast.LENGTH_LONG).show();
                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                setResult(Activity.RESULT_OK, returnIntent);
                imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0);
                finish();
            }
        }
        else if (view.getId() == R.id.buttonProfileCancel || view.getId() == R.id.back_view){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0);
            finish();
        }
    }

    private boolean sendWarningToast(String message){
        Toast.makeText(NewProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }
}
