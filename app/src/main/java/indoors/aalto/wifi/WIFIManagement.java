package indoors.aalto.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by rroxa_000 on 11/25/2015.
 */
public class WIFIManagement {

    public static String getWiFiConnectionInfo(WifiManager wifiManager) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.toString();
    }
    public static List<ScanResult> getAccessPoints(WifiManager wifiManager){
        return wifiManager.getScanResults();
    }
    public static double calculateDistance(double levelInDb, double freqInMHz)    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
}
