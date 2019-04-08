package advanced.android.ebcm.Graph;

import advanced.android.ebcm.R;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemDetailsAdapter extends ArrayAdapter<CalculationResult> {

    public ItemDetailsAdapter(Context context, ArrayList<CalculationResult> results) {
        super(context, 0, results);
    }

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

        name.setText(result.getItemName());
        units.setText(Double.toString(result.getResults()) + " kWh");
        time.setText(Double.toString(result.getUsageTimeTotal()) + " hours");
        power.setText(Double.toString(result.getPower()) + " W");

//        return super.getView(position, convertView, parent);
        return convertView;
    }

}
