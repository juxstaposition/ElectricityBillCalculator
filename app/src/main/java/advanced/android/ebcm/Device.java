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
        if (name.length() > 0 && name != null) {
            this.name = name;
        }
    }

    public int getWattage() {
        return wattage;
    }

    public void setWattage(int wattage) {
        if (wattage > 0) {
            this.wattage = wattage;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if (hours > 0) {
            this.hours = hours;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        if (minutes > 0) {
            this.minutes = minutes;
        }
    }

    public int getDays_month() {
        return days_month;
    }

    public void setDays_month(int days_month) {
        if (days_month > 0) {
            this.days_month = days_month;
        }
    }
}
