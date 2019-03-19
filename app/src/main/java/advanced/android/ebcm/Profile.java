package advanced.android.ebcm;

public class Profile {
    private String name;
    private Device[] devices;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }
}
