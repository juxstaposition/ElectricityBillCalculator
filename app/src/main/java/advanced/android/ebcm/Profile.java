package advanced.android.ebcm;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Profile {

    private int id;
    private String name;
    private String expense;
    private String description;
    private Device[] devices;

    LinearLayout profileForm = null;
    LinearLayout firstLine = null;
    LinearLayout secondLine = null;

    TextView profileName = null;
    TextView profileExpense = null;


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


    public Profile(String name, String description,String expense) {
        this.name = name;
        this.description = description;
        this.expense = expense;
    }

    public Profile(int id, String name, String description, String expense) {
        this.id = id;
        this.name = name;
        this.expense = expense;
        this.description = description;
    }

    public void generateProfile(Context context, LinearLayout myVerticalLayout){
        profileForm = new LinearLayout(new ContextThemeWrapper(context, R.style.ProfileFormStyle), null, 0 );
        profileForm.setOrientation(LinearLayout.VERTICAL);

        firstLine = new LinearLayout(context);
        firstLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        firstLine.setOrientation(LinearLayout.HORIZONTAL);

        TextView profileTitle = new TextView(new ContextThemeWrapper(context, R.style.ProfileNameTitle),null,0);
        profileTitle.setText("Name");
        firstLine.addView(profileTitle);

        TextView profileTitleCost = new TextView(new ContextThemeWrapper(context, R.style.ProfileNameTitle),null,0);
        profileTitleCost.setText("Expense");
        firstLine.addView(profileTitleCost);

        ImageView clipDelete = new ImageView(new ContextThemeWrapper(context, R.style.DeleteClipArt),null,0);
        firstLine.addView(clipDelete);

        secondLine = new LinearLayout(context);
        secondLine.setOrientation(LinearLayout.HORIZONTAL);

        profileName = new TextView(context);
        profileName.setText(name);
        profileName.setTextSize(30);
        profileName.setTextColor(Color.BLACK);
        secondLine.addView(profileName);

        profileExpense = new TextView(context);
        profileExpense.setText(expense+"€/kWh");
        profileExpense.setTextSize(30);
        profileExpense.setTextColor(Color.BLACK);
        secondLine.addView(profileExpense);

        profileForm.addView(firstLine);
        profileForm.addView(secondLine);

        myVerticalLayout.addView(profileForm);
    }

}
