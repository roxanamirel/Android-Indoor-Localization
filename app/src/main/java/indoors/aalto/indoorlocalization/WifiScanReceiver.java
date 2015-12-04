package indoors.aalto.indoorlocalization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import indoors.aalto.json.models.AccessPoint;
import indoors.aalto.json.models.Measurement;
import indoors.aalto.wifi.MyScanResult;
import indoors.aalto.wifi.WIFIManagement;

public class WifiScanReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private MainActivityController controller;

    public WifiScanReceiver(WifiManager wifiManager, MainActivityController controller)     {
        this.wifiManager = wifiManager;
        this.controller = controller;
    }

    public void onReceive(Context c, Intent intent) {
        Log.e("Scan ", controller.getNoScans() + "");
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            List<ScanResult> results = WIFIManagement.getAccessPoints(wifiManager);
            List<MyScanResult> finalResults = new ArrayList<MyScanResult>();
            Measurement measurement = new Measurement();
            measurement.setName("Measurement " + controller.getNoScans());
            List<AccessPoint> accessPoints = new ArrayList<>();
            for (ScanResult scanResult : results) {
                MyScanResult result = new MyScanResult();
                int level = wifiManager.calculateSignalLevel(scanResult.level, 5);
                double distanceFromAP = WIFIManagement.calculateDistance(scanResult.level, scanResult.frequency);
                result.setScanResult(scanResult);
                result.setSignalStrength(level);
                result.setDistanceFromAP(distanceFromAP);
                finalResults.add(result);
                AccessPoint ap = new AccessPoint();
                ap.setBssid(scanResult.BSSID);
                ap.setSsid(scanResult.SSID);
                ap.setCapabilities(scanResult.capabilities);
                ap.setFrequency(scanResult.frequency);
                ap.setLevel(scanResult.level);
                ap.setName("ap" + results.indexOf(result));
                accessPoints.add(ap);
            }
            measurement.setAccessPoints(accessPoints);
            controller.addMeasurement(measurement);
            controller.updateUIWithScanResults(finalResults);
        }
    }
}

