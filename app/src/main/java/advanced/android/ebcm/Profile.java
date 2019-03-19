package advanced.android.ebcm;

public class Profile {

    private int id;
    private String name;
    private String description;
    private Device[] devices;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }


    public Profile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Profile(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


}
