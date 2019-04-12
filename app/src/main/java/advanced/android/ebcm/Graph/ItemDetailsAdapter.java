package advanced.android.ebcm.Graph;

import advanced.android.ebcm.Graph.CalculationResult;
import advanced.android.ebcm.R;
import advanced.android.ebcm.ResultGraph;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;

//import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ItemDetailsAdapter extends ArrayAdapter<CalculationResult> {

    public ItemDetailsAdapter(Context context, ArrayList<CalculationResult> results) {
        super(context, 0, results);
    }

    public ItemDetailsAdapter(Context context, ArrayList<CalculationResult> results, boolean pickItems) {

        super(context, 0, results);
        this.pickItems = pickItems;
    }
    boolean pickItems = false;
    @NotNull
    @Override
    public View getView(int position, @Nullable View convertView, @NotNull ViewGroup parent) {
        CalculationResult result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_details, parent, false);
        }

        TextView name = convertView.findViewById(R.id.item_name);
        TextView units = convertView.findViewById(R.id.item_units);
        TextView time = convertView.findViewById(R.id.item_total_time);
        TextView power = convertView.findViewById(R.id.item_power);

//        if (pickItems){
//            name.setText(result.getItemName());
//            power.setVisibility(View.GONE);
//            time.setVisibility(View.GONE);
//            double Power = result.getPower();
//            DecimalFormat numberFormat = new DecimalFormat("#");
//            units.setText(numberFormat.format(Power) + " W");
//        } else {
            name.setText(result.getItemName());
            units.setText(ResultGraph.round(result.getResults(),2) + " kWh");

//            BigDecimal usageTotal = ResultGraph.round(result.getUsageTimeTotal(), 2);
            int hours = (int)(result.getUsageTimeTotal() / 60);
            float minutes = (result.getUsageTimeTotal() / 60)%1;
            BigDecimal usageTotal = ResultGraph.round((float)hours + minutes, 2);

            time.setText(usageTotal + " hours/minutes");

            power.setText(result.getPower() + " W");
       // }


//        return super.getView(position, convertView, parent);
        return convertView;
    }

}
