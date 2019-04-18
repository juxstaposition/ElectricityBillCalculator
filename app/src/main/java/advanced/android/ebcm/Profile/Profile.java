package advanced.android.ebcm.Profile;

import advanced.android.ebcm.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.*;

public class Profile {

    private int id;
    private String name;
    private String price;
    private String description;
    private String cost;
    private String power;
    private String time;

    public LinearLayout profileForm;
    public CoordinatorLayout supportLayout;

    private TextView profileName;
    private TextView profileDescription;
    private TextView profilePrice;
    private TextView profileCost;
    private TextView profilePower;
    private TextView profileTime;
    public ImageView clipDelete;

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
        profileName.setText(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
        profileDescription.setText(description);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price){
        this.price = price;
        String newPrice = price + "€/kWh";
        profilePrice.setText(newPrice);
    }

    public void setCost (String cost){
        this.cost = cost;
        String newCost = cost+"€/mo.";
        profileCost.setText(newCost);
    }

    public void setPower (String power){
        this.power = power;
        String newPower = power + "W";
        profilePower.setText(newPower);
    }
    public void setTime (String time){
        this.time = time;
        String newTime;
        String[] fullTime = time.split(":");
        if (fullTime[0].length() == 1 ){
            newTime = "0" + fullTime[0] + ":";
        } else {
            newTime = fullTime[0] + ":";
        }

        if(fullTime[1].length() == 1){
            newTime = newTime + "0" + fullTime[1];
        } else {
            newTime = newTime + fullTime[1];
        }

        profileTime.setText(newTime);
    }

    public CoordinatorLayout getLayout(){
        return supportLayout;
    }


    public Profile(String name, String description,String price, String power, String cost, String time) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.power = power;
        this.cost = cost;
        this.time = time;
    }

    public Profile(int id, String name, String description, String price, String power, String cost, String time) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.power = power;
        this.cost = cost;
        this.time = time;
    }

    public void generateProfile(final Context context, LinearLayout myVerticalLayout){

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int colWidth = (screenWidth - convertDpToPx(20*2,dm) ) / 8;

        String titlesFont = "sans-serif-black";
        int titlesTextSize = 14;
        String descriptionsFont = "serif-monospace";
        int descriptionsTextSize = 16;

        // Instantiation of base linear layout for profile panel
        profileForm = new LinearLayout(context);
        profileForm.setBackgroundResource(R.drawable.profile_view);
        // Setting margins, with each component new Layout parameters have to be instantiated
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                     LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(convertDpToPx(0,dm),convertDpToPx(10,dm),convertDpToPx(0,dm),convertDpToPx(0,dm));
        profileForm.setLayoutParams(lp);
        profileForm.setOrientation(LinearLayout.VERTICAL);
        // setting panel to be clickable and adding function
        // needs to be done where object is instantiated

        // Panel is made of linear layouts, first line contains titles
        LinearLayout firstLine = new LinearLayout(context);
        firstLine.setOrientation(LinearLayout.VERTICAL);
        firstLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT));
        firstLine.setGravity(Gravity.CENTER);
        // Creating new text component for title

        TextView profileTitle = createTextView("Name",context, titlesFont,titlesTextSize);
        firstLine.addView(profileTitle);


        profileName = createTextView(name,context, "cursive",25);
        profileName.setTextColor(Color.BLACK);
        firstLine.addView(profileName);


        profileDescription = createTextView(description,context, descriptionsFont,16);
        profileDescription.setLayoutParams(setMargin(5,0,0,0,dm));

        supportLayout = new CoordinatorLayout(context);
        supportLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                                                                         CoordinatorLayout.LayoutParams.MATCH_PARENT));
        clipDelete = new ImageView(context);
        clipDelete.setBackgroundResource(R.drawable.delete_button_clipart);
        CoordinatorLayout.LayoutParams lllp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                                                CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        lllp.setMargins(convertDpToPx(0,dm),convertDpToPx(5,dm),convertDpToPx(-5,dm),convertDpToPx(0,dm));
        lllp.gravity = Gravity.END;
        clipDelete.setLayoutParams(lllp);


        // should be changed to constrained layout
        LinearLayout thirdLine = new LinearLayout(context);
        thirdLine.setOrientation(LinearLayout.HORIZONTAL);
        thirdLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT));


        LinearLayout firstCol = generatedDescriptionColumn(context,colWidth,5);
        addTitlesToDescription(firstCol,"Price:","Cost:",titlesFont,titlesTextSize,context);

        LinearLayout secondCol = generatedDescriptionColumn(context,colWidth*10/3,0);
        profilePrice = createTextView(price+"€/kWh", context,descriptionsFont,descriptionsTextSize);
        secondCol.addView(profilePrice);
        profileCost = createTextView(cost+"€/mo.", context, descriptionsFont, descriptionsTextSize);
        secondCol.addView(profileCost);

        LinearLayout thirdCol = generatedDescriptionColumn(context,colWidth*4/3,0);
        addTitlesToDescription(thirdCol,"Power:","Time:",titlesFont,titlesTextSize,context);

        LinearLayout fourthCol = generatedDescriptionColumn(context,colWidth*2,0);
        profilePower = createTextView(power+"W", context,descriptionsFont,descriptionsTextSize);
        fourthCol.addView(profilePower);
        profileTime = createTextView(time, context, descriptionsFont, descriptionsTextSize);
        fourthCol.addView(profileTime);

        thirdLine.addView(firstCol);
        thirdLine.addView(secondCol);
        thirdLine.addView(thirdCol);
        thirdLine.addView(fourthCol);

        profileForm.addView(firstLine);
        profileForm.addView(profileDescription);
        profileForm.addView(thirdLine);

        supportLayout.addView(profileForm);
        supportLayout.addView(clipDelete);

        myVerticalLayout.addView(supportLayout);
    }

    private LinearLayout generatedDescriptionColumn(Context context,int width,int leftMargin) {

        LinearLayout column = new LinearLayout(context);
        column.setOrientation(LinearLayout.VERTICAL);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        // setMargins(left,top,right,bottom)
        lp.setMargins( convertDpToPx(leftMargin,dm),convertDpToPx(0,dm), convertDpToPx(0,dm),0 );
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
