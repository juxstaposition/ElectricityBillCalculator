package advanced.android.ebcm.Device;

import advanced.android.ebcm.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
    private Integer power;
    private Integer profile_parent;

    public LinearLayout deviceForm;
    public ImageView clipDeleteDevice;
    public CoordinatorLayout supportLayout;


    private TextView deviceName;
    private TextView devicePower;
    private TextView deviceTime;
    private TextView deviceDays;


    public Device(int id, String name, int quantity, int hours, int minutes, int days, int power, int profile_parent) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.hours = hours;
        this.minutes = minutes;
        this.days = days;
        this.power = power;
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

    public void setName(String name, int quantity) {
        this.name = name;
        String displayQuantity = name + "(" + quantity + ")";
        this.deviceName.setText(displayQuantity);
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }



    public void setTime(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
        String newTime = hours + ":" + minutes;
        this.deviceTime.setText(newTime);
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
        String newDays = Integer.toString(days);
        this.deviceDays.setText(newDays);
    }

    public void setDeviceName(String name, int quantity){

    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
        String displayConsumption = String.valueOf(power);
        this.devicePower.setText(displayConsumption);
    }

    public CoordinatorLayout getLayout() {
        return supportLayout;
    }

    public Integer getProfile_parent() {
        return profile_parent;
    }

    public void setProfile_parent(Integer profile_parent) {
        this.profile_parent = profile_parent;
    }


    public void generateDevice(final Context context, LinearLayout deviceLayout){

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int colWidth = (screenWidth - convertDpToPx(18*2,dm) ) / 6;

        String titlesFont = "sans-serif-black";
        int titlesTextSize = 12;
        String descriptionsFont = "serif-monospace";
        int descriptionsTextSize = 18;

        if (screenWidth > 700 && screenWidth < 1200){
            titlesTextSize = 14;
            descriptionsTextSize = 20;
        }
        else if (screenWidth >= 1200){
            titlesTextSize = 18;
            descriptionsTextSize = 24;
        }



        // Instantiation of base linear layout for device panel
        deviceForm = new LinearLayout(context);
        deviceForm.setBackgroundResource(R.drawable.profile_view);
        // Setting margins, with each component new Layout parameters have to be instantiated
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(convertDpToPx(0,dm),convertDpToPx(5,dm),convertDpToPx(0,dm),convertDpToPx(0,dm));
        deviceForm.setLayoutParams(lp);
        deviceForm.setOrientation(LinearLayout.HORIZONTAL);
        // setting panel to be clickable and adding function
        // needs to be done where object is instantiated

        // Panel is made of linear layouts, first line contains titles
        LinearLayout firstCol = generatedDescriptionColumn(context,colWidth*5/2);

        // Creating new text component for title
        TextView deviceTitle = createTextView("Name",context, titlesFont,titlesTextSize);

        firstCol.addView(deviceTitle);
        String displayName;
        if (quantity > 1 ){
            displayName = name + "(" + quantity + ")";
        }
        else{
            displayName = name;
        }
        deviceName = createTextView(displayName,context, descriptionsFont,descriptionsTextSize);
        firstCol.addView(deviceName);




        supportLayout = new CoordinatorLayout(context);
        supportLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT));

        CoordinatorLayout.LayoutParams lllp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        lllp.setMargins(convertDpToPx(0,dm),convertDpToPx(-8,dm),convertDpToPx(-25/2,dm),convertDpToPx(0,dm));
        lllp.gravity = Gravity.RIGHT;
        clipDeleteDevice = new ImageView(context);
        clipDeleteDevice.setBackgroundResource(R.drawable.delete_button_clipart);
        clipDeleteDevice.setLayoutParams(lllp);


        LinearLayout secondCol = generatedDescriptionColumn(context,colWidth*29/25);
        TextView powerTitle = createTextView("Power",context,titlesFont,titlesTextSize);
        secondCol.addView(powerTitle);
        devicePower = createTextView(Integer.toString(power), context, descriptionsFont, descriptionsTextSize);
        secondCol.addView(devicePower);

        LinearLayout thirdCol = generatedDescriptionColumn(context,colWidth*29/25);
        TextView timeTitle = createTextView("Time",context,titlesFont,titlesTextSize);
        thirdCol.addView(timeTitle);
        deviceTime = createTextView(hours + ":" + minutes, context, descriptionsFont, descriptionsTextSize);
        thirdCol.addView(deviceTime);

        LinearLayout fourthCol = generatedDescriptionColumn(context,colWidth*29/25);
        TextView daysTitle = createTextView("Days", context,titlesFont,titlesTextSize);
        fourthCol.addView(daysTitle);
        deviceDays = createTextView(Integer.toString(days), context, descriptionsFont, descriptionsTextSize);
        fourthCol.addView(deviceDays);

        deviceForm.addView(firstCol);
        deviceForm.addView(secondCol);
        deviceForm.addView(thirdCol);
        deviceForm.addView(fourthCol);

        supportLayout.addView(deviceForm);
        supportLayout.addView(clipDeleteDevice);

        deviceLayout.addView(supportLayout);
    }

    private LinearLayout generatedDescriptionColumn(Context context,int widthSize) {

        LinearLayout column = new LinearLayout(context);
        column.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthSize,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.setMargins(convertDpToPx(5,dm),5,0,0);

        column.setLayoutParams(lp);

        return column;

    }

    private TextView createTextView(String text, Context context, String style,int textSize){

        TextView newTextView = new TextView(context);
        newTextView.setText(text);
        newTextView.setTextSize(textSize);
        newTextView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
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
