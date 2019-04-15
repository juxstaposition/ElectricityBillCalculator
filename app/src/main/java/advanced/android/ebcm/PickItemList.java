package advanced.android.ebcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import advanced.android.ebcm.Graph.CalculationResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class PickItemList extends AppCompatActivity implements View.OnClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<CalculationResult>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_item_list);

        findViewById(R.id.backViewNavigationItemPick).setOnClickListener(this);

        expListView = findViewById(R.id.list_expand);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);


        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });


        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });


        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });


        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent returnIntent = new Intent();
                CalculationResult extra = listDataChild.get( listDataHeader.get(groupPosition)).get( childPosition);
                JSONObject jsonObjectExtra = new JSONObject();
                try {
                    jsonObjectExtra.put("itemName", extra.getItemName());
                    jsonObjectExtra.put("power", extra.getPower());
                    jsonObjectExtra.put("quantity", 1);
                    jsonObjectExtra.put("usageTimeMinutesTotal", 1);
                    jsonObjectExtra.put("usageDays", 1);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                returnIntent.putExtra("NAME", jsonObjectExtra.toString());

                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                setResult(Activity.RESULT_OK,returnIntent);
                finish();

                return false;
            }
        });
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.backViewNavigationItemPick){
            failureClosing();
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }


    @Override
    public void onBackPressed(){
        failureClosing();
    }

    public void failureClosing(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<CalculationResult>>();

        // Adding child data
        listDataHeader.add("Lamps");
        listDataHeader.add("Living/Bedroom/Study Room");
        listDataHeader.add("Kitchen Appliances");
        listDataHeader.add("Home Accessories");


        // Adding child data
        List<CalculationResult> lamps = new ArrayList<>();
        lamps.add(new CalculationResult("Fluorescent Lamp", 40,1, 1, 1, 1));
        lamps.add(new CalculationResult("Light Bulb", 40,1, 1, 1, 1));
        lamps.add(new CalculationResult("LED Light Bulb", 60,1, 1, 1, 1));
        lamps.add(new CalculationResult("LED Tubes", 100,1, 1, 1, 1));

        List<CalculationResult> rooms = new ArrayList<>();
        rooms.add(new CalculationResult("TV", 40,1, 1, 1, 1));
        rooms.add(new CalculationResult("Air Conditioner", 1500,1, 1, 1, 1));
        rooms.add(new CalculationResult("Table Fan", 50,1, 1, 1, 1));
        rooms.add(new CalculationResult("Computer", 200,1, 1, 1, 1));
        rooms.add(new CalculationResult("Laptop", 40,1, 1, 1, 1));
        rooms.add(new CalculationResult("Computer Gaming", 500,1, 1, 1, 1));
        rooms.add(new CalculationResult("Router", 5,1, 1, 1, 1));


        List<CalculationResult> kitchenAppliances = new ArrayList<>();
        kitchenAppliances.add(new CalculationResult("Refrigerator", 300,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Microwave Oven", 1000,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Water Kettle", 1500,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Rice Cooker", 1000,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Toaster", 800,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Dishwasher", 1200,1, 1, 1, 1));


        List<CalculationResult> homeAccessories = new ArrayList<>();
        //livingRoom.add("TV");
        homeAccessories.add(new CalculationResult("Washing Machine", 500,1, 1, 1, 1));
        homeAccessories.add(new CalculationResult("Clothes Iron", 1100,1, 1, 1, 1));
        homeAccessories.add(new CalculationResult("Vacuum Cleaner", 300,1, 1, 1, 1));
        homeAccessories.add(new CalculationResult("Clothes Dryer", 1800,1, 1, 1, 1));
        homeAccessories.add(new CalculationResult("Hair Dryer", 1500,1, 1, 1, 1));
        homeAccessories.add(new CalculationResult("Water Heater", 500,1, 1, 1, 1));


        listDataChild.put(listDataHeader.get(0), lamps); // Header, Child data
        listDataChild.put(listDataHeader.get(1), rooms);
        listDataChild.put(listDataHeader.get(2), kitchenAppliances);
        listDataChild.put(listDataHeader.get(3), homeAccessories);
    }
}
