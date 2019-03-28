package advanced.android.ebcm;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.*;

public class Profile {

    private int id;
    private String name;
    private String price;
    private String description;
    private Device[] devices;

    LinearLayout profileForm = null;
    LinearLayout firstLine = null;
    LinearLayout secondLine = null;

    TextView profileName = null;
    TextView profileExpense = null;
    TextView profileDescription;
    public ImageView clipDelete = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }


    public Profile(String name, String description,String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Profile(int id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void generateProfile(final Context context, LinearLayout myVerticalLayout){

        // Request for a screen resolution pixels to be transformed into dp
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        // Instantiation of base linear layout for profile panel
        profileForm = new LinearLayout(
                /* Assigning theme of panel */
                new ContextThemeWrapper(context, R.style.ProfileFormStyle), null, 0
        );
        // Setting margins, with each component new Layout parameters have to be instantiated
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                     LinearLayout.LayoutParams.WRAP_CONTENT);
        // setMargins(left,top,right,bottom)
        lp.setMargins( convertDpToPx(20,dm),convertDpToPx(10,dm), convertDpToPx(20,dm),0 );

        profileForm.setLayoutParams(lp);
        profileForm.setOrientation(LinearLayout.VERTICAL);
        // setting panel to be clickable and adding function
        profileForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //original profile
//                Constant animation = new Constant();
//                animation.startAnimation(profileForm,R.anim.blink,context);
//                Toast.makeText(context, name +", id=" + id, Toast.LENGTH_SHORT).show();

                //delete SQLite test
                DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
                mDatabaseHelper.deleteProfile(getId(),getName());
                Toast.makeText(context,"removed " + getName() + " from database.", Toast.LENGTH_SHORT).show();

            }
        });

        // Panel is made of linear layouts, first line contains titles
        firstLine = new LinearLayout(context);
        firstLine.setOrientation(LinearLayout.HORIZONTAL);
        firstLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT));
        // Creating new text component for title
        TextView profileTitle = new TextView(
                new ContextThemeWrapper(context, R.style.ProfileNameTitleStyle),null,0
        );
        profileTitle.setText(R.string.name_title);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                             LinearLayout.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(convertDpToPx(25,dm),convertDpToPx(5,dm),convertDpToPx(50,dm),0);
        profileTitle.setLayoutParams(nameParams);
        firstLine.addView(profileTitle);

        TextView profileTitleCost = new TextView(
                new ContextThemeWrapper(context, R.style.ProfileNameTitleStyle),null,0
        );
        profileTitleCost.setText(R.string.price_title);

        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                              LinearLayout.LayoutParams.WRAP_CONTENT);
        priceParams.setMargins(convertDpToPx(25,dm),convertDpToPx(5,dm),convertDpToPx(50,dm),0);
        profileTitleCost.setLayoutParams(priceParams);
        firstLine.addView(profileTitleCost);

        /*CoordinatorLayout supportLayout = new CoordinatorLayout(context);
        supportLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                                                                         CoordinatorLayout.LayoutParams.MATCH_PARENT));
        clipDelete = new ImageView(
                new ContextThemeWrapper(context, R.style.DeleteClipArt),null,0
        );
        CoordinatorLayout.LayoutParams lllp = (CoordinatorLayout.LayoutParams) supportLayout.getLayoutParams();
        lllp.gravity = Gravity.RIGHT;
        clipDelete.setLayoutParams(lllp);*/

        secondLine = new LinearLayout(context);
        secondLine.setOrientation(LinearLayout.HORIZONTAL);

        profileName = new TextView(context);
        profileName.setText(name);
        profileName.setTextSize(30);
        profileName.setTextColor(Color.BLACK);
        secondLine.addView(profileName);

        profileDescription = new TextView(context);
        profileDescription.setText(description);
        profileDescription.setTextSize(30);
        profileDescription.setTextColor(Color.BLACK);
        secondLine.addView(profileDescription);

        profileExpense = new TextView(context);
        profileExpense.setText(price +"€/kWh");
        profileExpense.setTextSize(30);
        profileExpense.setTextColor(Color.BLACK);
        secondLine.addView(profileExpense);

        profileForm.addView(firstLine);
        profileForm.addView(secondLine);
/*
        supportLayout.addView(profileForm);
        supportLayout.addView(clipDelete);*/

        myVerticalLayout.addView(profileForm);
    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

}
