package advanced.android.ebcm;

public class CalculationResult implements Comparable {

    private String itemName;
    private double power;
    private int quantity;
    private double usageTime;
    private int usageDays;
    private double results;

    public CalculationResult(String itemName, double power, int quantity, double usageTime, int usageDays) {
        this.itemName = itemName;
        this.power = power;
        this.quantity = quantity;
        this.usageTime = usageTime;
        this.usageDays = usageDays;
        this.results = calculateResults();
    }

    public CalculationResult() {
    }

    private double calculateResults() {
        double results = -1.0;
        results = this.power * this.quantity * this.usageTime * this.usageDays;//w * h
        return results / 1000;
    }

    @Override
    public int compareTo(Object o) {
        CalculationResult c = (CalculationResult) o;
        return (int) Math.round(c.results - this.results);
    }

    public double getResults() {
        return results;
    }

    public double getUsageTimeTotal() {
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

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(double usageTime) {
        this.usageTime = usageTime;
    }

    public int getUsageDays() {
        return usageDays;
    }

    public void setUsageDays(int usageDays) {
        this.usageDays = usageDays;
    }
}
