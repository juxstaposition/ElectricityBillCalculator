package advanced.android.ebcm.Device;

import advanced.android.ebcm.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Device {

    private int id;
    private String name;
    private Integer quantity;
    private Integer hours;
    private Integer minutes;
    private Integer days;
    private String group;
    private Integer consumption;
    private Integer profile_parent;

    public LinearLayout deviceForm;
    public ImageView clipDelete;
    public CoordinatorLayout supportLayout;


    private TextView deviceName;
    private TextView deviceConsumption;
    private TextView deviceQuantity;
    private TextView deviceHours;
    private TextView deviceMinutes;
    private TextView deviceTotalUsage;


    public Device(int id, String name, int quantity, int hours, int minutes, int days, String group, int consumption, int profile_parent) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.hours = hours;
        this.minutes = minutes;
        this.days = days;
        this.group = group;
        this.consumption = consumption;
        this.profile_parent = profile_parent;
    }


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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public Integer getProfile_parent() {
        return profile_parent;
    }

    public void setProfile_parent(Integer profile_parent) {
        this.profile_parent = profile_parent;
    }




    public void generateDevice(final Context context, LinearLayout myVerticalLayout){

        String titlesFont = "sans-serif-black";
        int titlesTextSize = 14;
        String descriptionsFont = "serif-monospace";
        int descriptionsTextSize = 16;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        // Instantiation of base linear layout for profile panel
        deviceForm = new LinearLayout(
                /* Assigning theme of panel */
                new ContextThemeWrapper(context, R.style.ProfileFormStyle), null, 0
        );
        // Setting margins, with each component new Layout parameters have to be instantiated
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deviceForm.setLayoutParams(lp);
        deviceForm.setOrientation(LinearLayout.VERTICAL);
        // setting panel to be clickable and adding function
        // needs to be done where object is instantiated

        // Panel is made of linear layouts, first line contains titles
        LinearLayout firstLine = new LinearLayout(context);
        firstLine.setOrientation(LinearLayout.VERTICAL);
        firstLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        firstLine.setGravity(Gravity.CENTER);
        // Creating new text component for title

        TextView deviceTitle = createTextView("Name",context, titlesFont,titlesTextSize);
        firstLine.addView(deviceTitle);


        deviceName = createTextView(name,context, "cursive",25);
        deviceName.setTextColor(Color.BLACK);
        firstLine.addView(deviceName);


        deviceConsumption = createTextView(Integer.toString(consumption),context, descriptionsFont,16);
        deviceConsumption.setLayoutParams(setMargin(5,0,0,0,dm));

        supportLayout = new CoordinatorLayout(context);
        supportLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT));
        clipDelete = new ImageView(
                new ContextThemeWrapper(context, R.style.DeleteClipArt),null,0
        );
        CoordinatorLayout.LayoutParams lllp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        lllp.setMargins(convertDpToPx(0,dm),convertDpToPx(-5,dm),convertDpToPx(-5,dm),convertDpToPx(0,dm));
        lllp.gravity = Gravity.RIGHT;
        clipDelete.setLayoutParams(lllp);


        // should be changed to constrained layout
        LinearLayout thirdLine = new LinearLayout(context);
        thirdLine.setOrientation(LinearLayout.HORIZONTAL);
        thirdLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        LinearLayout firstCol = generatedDescriptionColumn(context);
        addTitlesToDescription(firstCol,"Price:","Cost:",titlesFont,titlesTextSize,context);

        LinearLayout secondCol = generatedDescriptionColumn(context);
        deviceQuantity = createTextView(Integer.toString(quantity), context,descriptionsFont,descriptionsTextSize);
        secondCol.addView(deviceQuantity);
        deviceTotalUsage = createTextView(0+"€/month", context, descriptionsFont, descriptionsTextSize);
        secondCol.addView(deviceTotalUsage);

//        LinearLayout thirdCol = generatedDescriptionColumn(context);
//        addTitlesToDescription(thirdCol,"Power:","Time:",titlesFont,titlesTextSize,context);
//
//        LinearLayout fourthCol = generatedDescriptionColumn(context);
//        profilePower = createTextView(0+"W", context,descriptionsFont,descriptionsTextSize);
//        fourthCol.addView(profilePower);
//        profileTime = createTextView(0+"€/month", context, descriptionsFont, descriptionsTextSize);
//        fourthCol.addView(profileTime);

        thirdLine.addView(firstCol);
        thirdLine.addView(secondCol);
//        thirdLine.addView(thirdCol);
//        thirdLine.addView(fourthCol);

        deviceForm.addView(firstLine);
        deviceForm.addView(deviceQuantity);
        deviceForm.addView(thirdLine);

        supportLayout.addView(deviceForm);
        supportLayout.addView(clipDelete);

        myVerticalLayout.addView(supportLayout);
    }

    private LinearLayout generatedDescriptionColumn(Context context) {

        LinearLayout column = new LinearLayout(context);
        column.setOrientation(LinearLayout.VERTICAL);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // setMargins(left,top,right,bottom)
        lp.setMargins( convertDpToPx(5,dm),convertDpToPx(0,dm), convertDpToPx(5,dm),0 );
        column.setLayoutParams(lp);

        return column;
    }

    private void addTitlesToDescription(LinearLayout layout,String firstTitle, String secondTitle,
                                        String font, int size, Context context){
        TextView profileFirstTitle = createTextView(firstTitle, context, font,size);
        layout.addView(profileFirstTitle);
        TextView profileSecondTitle = createTextView(secondTitle, context, font, size);
        layout.addView(profileSecondTitle);
    }

    private TextView createTextView(String text, Context context, String style,int textSize){

        TextView newTextView = new TextView(context);
        newTextView.setText(text);
        newTextView.setTextSize(textSize);
        newTextView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        newTextView.setLayoutParams(nameParams);
        Typeface typeface = Typeface.create(style, Typeface.NORMAL);
        newTextView.setTypeface(typeface);

        return newTextView;
    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

    /**
     * Note Function cannot be used if you want view param height or width to be equal to match_parent
     *
     * @param left      left margin
     * @param top       top margin
     * @param right     right margin
     * @param bottom    bottom margin
     * @param dm        display metrics
     * @return          returns layout params with set margin in dp and wrapped content
     */
    private LinearLayout.LayoutParams setMargin(int left,int top,int right, int bottom, DisplayMetrics dm) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPx(left, dm), convertDpToPx(top, dm), convertDpToPx(right, dm), bottom);
        return params;
    }

}
