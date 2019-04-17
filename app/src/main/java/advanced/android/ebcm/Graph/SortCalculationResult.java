package advanced.android.ebcm.Graph;

import java.util.Comparator;

public class SortCalculationResult implements Comparator<CalculationResult> {

    public int compare(CalculationResult a, CalculationResult b)
    {
        return (int) (b.getResults() - a.getResults());
    }
}
