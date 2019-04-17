package advanced.android.ebcm.Graph;

public class CalculationResult implements Comparable {

    private String itemName;
    private int power;
    private int quantity;
    private float usageTimeMinutesTotal;
    private int usageDays;
    private float results;


    public CalculationResult(String itemName, int power, int quantity, int hours, int minutes, int usageDays) {
        this.itemName = itemName;
        this.power = power;
        this.quantity = quantity;
        this.usageTimeMinutesTotal = (float) (hours*60) + (float) (minutes);
        this.usageDays = usageDays;
        this.results = calculateResults();
    }


    private float calculateResults() {
        float results;

        float hoursTotal = (this.usageTimeMinutesTotal * this.usageDays)/60;

        results =  ((float) this.power / 1000) * this.quantity * hoursTotal;//w * h
        return results ;
    }

    @Override
    public int compareTo(Object o) {
        CalculationResult c = (CalculationResult) o;
        return  Math.round(c.results - this.results);
    }

    public float getResults() {
        return results;
    }

    public float getUsageTimeTotal() {
        if (this.usageDays > 0) {
            return this.usageTimeMinutesTotal * this.usageDays;
        } else {
            return this.usageTimeMinutesTotal;
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUsageTimeMinutesTotal() {
        return usageTimeMinutesTotal;
    }

    public void setUsageTimeMinutesTotal(float usageTimeMinutesTotal) {
        this.usageTimeMinutesTotal = usageTimeMinutesTotal;
    }

    public int getUsageDays() {
        return usageDays;
    }

    public void setUsageDays(int usageDays) {
        this.usageDays = usageDays;
    }

}
