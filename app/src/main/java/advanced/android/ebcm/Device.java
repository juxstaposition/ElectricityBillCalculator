package advanced.android.ebcm;

public class Device {
    protected String name;
    protected int wattage;
    protected int quantity;
    protected int hours;
    protected int minutes;
    protected int days_month;

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public int getWattage() {
        return wattage;
    }

    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {

            this.hours = hours;

    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDays_month() {
        return days_month;
    }

    public void setDays_month(int days_month) {
        this.days_month = days_month;
    }

}
