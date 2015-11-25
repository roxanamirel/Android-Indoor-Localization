package indoors.aalto.wifi;

import android.net.wifi.ScanResult;

/**
 * Created by rroxa_000 on 11/25/2015.
 */
public class MyScanResult {
    private ScanResult scanResult;
    private int signalStrength;
    private double distanceFromAP;


    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public void setDistanceFromAP(double distanceFromAP) {
        this.distanceFromAP = distanceFromAP;
    }

    public String toString(){
        return scanResult.toString() + "signalStrength: " + signalStrength + " distanceFromAP: " + distanceFromAP + " m" ;

    }


}
