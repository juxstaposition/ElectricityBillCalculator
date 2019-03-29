package advanced.android.ebcm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.*;

public class Profile {

    private int id;
    private String name;
    private String price;
    private String description;

    LinearLayout profileForm;

    TextView profileName;
    TextView profileExpense;
    TextView profileDescription;
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

    public void setDescription(String description) {
        this.description = description;
    }



    public Profile(String name, String description, String price) {
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

        String titlesFont = "sans-serif-black";
        int titlesTextSize = 14;
        String descriptionsFont = "serif-monospace";
        int descriptionsTextSize = 16;
        // Instantiation of base linear layout for profile panel
        profileForm = new LinearLayout(
                /* Assigning theme of panel */
                new ContextThemeWrapper(context, R.style.ProfileFormStyle), null, 0
        );
        // Setting margins, with each component new Layout parameters have to be instantiated
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        params.setMargins(convertDpToPx(5,dm),convertDpToPx(0,dm), convertDpToPx(0,dm),0);
        profileDescription.setLayoutParams(params);

        /*CoordinatorLayout supportLayout = new CoordinatorLayout(context);
        supportLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                                                                         CoordinatorLayout.LayoutParams.MATCH_PARENT));
        clipDelete = new ImageView(
                new ContextThemeWrapper(context, R.style.DeleteClipArt),null,0
        );
        CoordinatorLayout.LayoutParams lllp = (CoordinatorLayout.LayoutParams) supportLayout.getLayoutParams();
        lllp.gravity = Gravity.RIGHT;
        clipDelete.setLayoutParams(lllp);*/


        // should be changed to constrained layout
        LinearLayout thirdLine = new LinearLayout(context);
        thirdLine.setOrientation(LinearLayout.HORIZONTAL);
        thirdLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        LinearLayout firstCol = generatedDescriptionColumn("Price","Cost",context,titlesFont,titlesTextSize);
        LinearLayout secondCol = generatedDescriptionColumn(price + "€/kWh","0"+"€/month",context,descriptionsFont,descriptionsTextSize);
        LinearLayout thirdCol = generatedDescriptionColumn("Power","Time",context,titlesFont,titlesTextSize);
        LinearLayout fourthCol = generatedDescriptionColumn("0"+"W","0"+"h/month",context,descriptionsFont,descriptionsTextSize);

        thirdLine.addView(firstCol);
        thirdLine.addView(secondCol);
        thirdLine.addView(thirdCol);
        thirdLine.addView(fourthCol);

        profileForm.addView(firstLine);
        profileForm.addView(profileDescription);
        profileForm.addView(thirdLine);
/*
        supportLayout.addView(profileForm);
        supportLayout.addView(clipDelete);*/

        myVerticalLayout.addView(profileForm);
    }

    private LinearLayout generatedDescriptionColumn(String firstTextView, String secondTextView,
                                                    Context context,String titlesFont,int titlesSize) {

        LinearLayout column = new LinearLayout(context);
        column.setOrientation(LinearLayout.VERTICAL);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // setMargins(left,top,right,bottom)
        lp.setMargins( convertDpToPx(5,dm),convertDpToPx(0,dm), convertDpToPx(5,dm),0 );
        column.setLayoutParams(lp);

        TextView profileTitlePrice = createTextView(firstTextView, context, titlesFont,titlesSize);
        column.addView(profileTitlePrice);
        TextView profileTitleCost = createTextView(secondTextView, context, titlesFont, titlesSize);
        column.addView(profileTitleCost);

        return column;
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

}
