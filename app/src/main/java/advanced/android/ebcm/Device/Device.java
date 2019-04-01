package advanced.android.ebcm.Device;

public class Device {

    private int id;
    private String name;
    private Integer quantity;
    private Integer hours;
    private Integer minutes;
    private Integer days;
    private String group;
    private Integer consumption;
    private Integer profile_parent;

    public Device(int id, String name, Integer quantity, Integer hours, Integer minutes, Integer days, String group, Integer consumption, Integer profile_parent) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.hours = hours;
        this.minutes = minutes;
        this.days = days;
        this.group = group;
        this.consumption = consumption;
        this.profile_parent = profile_parent;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public Integer getProfile_parent() {
        return profile_parent;
    }

    public void setProfile_parent(Integer profile_parent) {
        this.profile_parent = profile_parent;
    }



}
