package advanced.android.ebcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_enter,0);
        setContentView(R.layout.activity_help_instructions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
