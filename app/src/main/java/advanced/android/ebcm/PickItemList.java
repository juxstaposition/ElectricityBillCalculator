package advanced.android.ebcm;

import advanced.android.ebcm.Graph.CalculationResult;
import advanced.android.ebcm.Graph.ItemDetailsAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PickItemList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_item_list);

        ListView listView = findViewById(R.id.pick_listView);
        ArrayList <CalculationResult> pickList = loadData();
        final ItemDetailsAdapter ida = new ItemDetailsAdapter(this, pickList, true);
//        final ArrayAdapter<String> aa =
//                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemDetailsList);
        listView.setAdapter(ida);

    }
    private ArrayList<CalculationResult> loadData() {

        ArrayList<CalculationResult> resultList = new ArrayList<>();
        resultList.add(new CalculationResult("Lamps", 60.0, 4, 4, 16));
        resultList.add(new CalculationResult("Coffee Machine", 1000.0, 1, .5, 30));
        resultList.add(new CalculationResult("TV", 500.0, 1, 6, 20));
        resultList.add(new CalculationResult("Vacuum Cleaner", 900.0, 1, 2, 6));
        return resultList;
    }
}
