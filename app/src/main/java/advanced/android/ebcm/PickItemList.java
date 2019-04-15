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
    private int lastExpandedPosition = -1;

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
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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
        listDataHeader.add("Kitchen Appliances");
        listDataHeader.add("Living Room");

        // Adding child data
        List<CalculationResult> lamps = new ArrayList<>();
        lamps.add(new CalculationResult("Light Bulb", 40,1, 1, 1, 1));
        lamps.add(new CalculationResult("Light Bulb", 60,1, 1, 1, 1));
//        lamps.add("Light bulb 40W");
//        lamps.add("Light bulb 60W");

        List<CalculationResult> kitchenAppliances = new ArrayList<>();
        kitchenAppliances.add(new CalculationResult("Refrigerator", 1000,1, 1, 1, 1));
        kitchenAppliances.add(new CalculationResult("Micro Wave Oven", 500,1, 1, 1, 1));
//        kitchenAppliances.add("Refrigerator 1000W");
//        kitchenAppliances.add("Micro Oven 500W");


        List<CalculationResult> livingRoom = new ArrayList<>();
        //livingRoom.add("TV");
        livingRoom.add(new CalculationResult("Micro Wave Oven", 500,1, 1, 1, 1));



        listDataChild.put(listDataHeader.get(0), lamps); // Header, Child data
        listDataChild.put(listDataHeader.get(1), kitchenAppliances);
        listDataChild.put(listDataHeader.get(2), livingRoom);
    }
}
