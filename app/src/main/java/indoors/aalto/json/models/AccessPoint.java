package indoors.aalto.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessPoint {

    private String name;
    private String capabilities;
    private double level;
    private int frequency;
    private String bssid;
    private String ssid;

    private transient int counter = 1;

    public String getCapabilities() {
        return capabilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    @JsonProperty("BSSID")
    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    @JsonProperty("SSID")
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public void incrementCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
