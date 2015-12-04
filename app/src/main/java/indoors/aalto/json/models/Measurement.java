package indoors.aalto.json.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using=MeasurementDeserializer.class)
public class Measurement {

    private String name;

    private List<AccessPoint> accessPoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public boolean containsAccessPoint(AccessPoint accessPointToLookFor) {
        for (AccessPoint ap : accessPoints) {
            if (ap.getBssid().equals(accessPointToLookFor.getBssid())) {
                return true;
            }
        }
        return false;
    }

    public AccessPoint getAccessPointByBSSID(String bssid) {
        for (AccessPoint ap : accessPoints) {
            if (ap.getBssid().equals(bssid)) {
                return ap;
            }
        }
        return null;
    }
}
