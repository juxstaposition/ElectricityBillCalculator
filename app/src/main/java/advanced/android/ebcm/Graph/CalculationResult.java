package advanced.android.ebcm.Graph;

public class CalculationResult implements Comparable {

    private String itemName;
    private int power;
    private int quantity;
    private float usageTime;
    private int usageDays;
    private float results;


    public CalculationResult(String itemName, int power, int quantity, int hours, int minutes, int usageDays) {
        this.itemName = itemName;
        this.power = power;
        this.quantity = quantity;
        this.usageTime = (float) ((hours*60)+ minutes)/60;
        this.usageDays = usageDays;
        this.results = calculateResults();
    }


    private float calculateResults() {
        float results;
        results = this.power * this.quantity * this.usageTime * this.usageDays;//w * h
        return results / 1000;
    }

    @Override
    public int compareTo(Object o) {
        CalculationResult c = (CalculationResult) o;
        return (int) Math.round(c.results - this.results);
    }

    public float getResults() {
        return results;
    }

    public float getUsageTimeTotal() {
        if (this.usageDays > 0) {
            return this.usageTime * this.usageDays;
        } else {
            return this.usageTime;
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

    public float getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(float usageTime) {
        this.usageTime = usageTime;
    }

    public int getUsageDays() {
        return usageDays;
    }

    public void setUsageDays(int usageDays) {
        this.usageDays = usageDays;
    }
}
