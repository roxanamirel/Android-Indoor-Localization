package indoors.aalto.indoorlocalization;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import indoors.aalto.json.models.AccessPoint;
import indoors.aalto.json.models.Measurement;
import indoors.aalto.json.models.Measurements;
import indoors.aalto.utils.Utils;
import indoors.aalto.wifi.MyScanResult;
import indoors.aalto.wifi.WIFIManagement;

public class MainActivityController {

    private static final String TAG = "MainActivityController";
    private static final int INTERVAL_SECONDS_FOR_SCAN = 5;

    private Activity activity;
    private WifiScanReceiver wifiReceiver;
    private WifiManager wifiManager;
    private int noScans = 0;
    private Measurements own_measurements;
    private AccelerometerEventListener accelerometerEventListener;

    public MainActivityController(Activity activity) {
        this.activity = activity;
        own_measurements = new Measurements();
        wifiManager = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiScanReceiver(wifiManager, this);
    }

    public void scanForWifi() {
        activity.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        scanForWiFi(INTERVAL_SECONDS_FOR_SCAN);
    }

    public void unregisterWifiReceiver() {
        activity.unregisterReceiver(wifiReceiver);
    }

    public void scanForWiFi(final int intervalSeconds) {
        if (noScans == 5) {
            Log.e(TAG, "5 measurements have been taken. Saving to file...");
            TextView tx = (TextView)activity.findViewById(R.id.wifiConnection);
            tx.setText("5 Measurements have been taken");
            Utils.writeMeasurementsToJSONToFile(activity, own_measurements);

            unregisterWifiReceiver();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wifiManager.startScan();
                scanForWiFi(intervalSeconds);
                noScans++;

            }
        }, intervalSeconds * 1000);
    }

    public void addMeasurement(Measurement measurement) {
        own_measurements.getMeasurements().add(measurement);
    }

    public void updateUIWithScanResults(List<MyScanResult> finalResults) {
        ArrayAdapter<MyScanResult> wifiAdapter = new ArrayAdapter<>
                (activity, R.layout.simplerow, finalResults);
        ListView listView = (ListView) activity.findViewById(R.id.sensorlistView);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(wifiAdapter);
    }

    public void showWiFiInfo() {
        TextView wifiTextView = (TextView) activity.findViewById(R.id.wifiConnection);
        wifiTextView.setText(WIFIManagement.getWiFiConnectionInfo(wifiManager));
    }

    public int getNoScans() {
        return noScans;
    }

    public Measurements getOwn_measurements() {
        return own_measurements;
    }

    public Measurements getAverageMeasurements(List<Measurements> measurements) {
        Measurements N = new Measurements();

        for (Measurements M : measurements) {
            for (Measurement m : M.getMeasurements()) {
                if (!N.containsMeasurement(m)) {
                    N.getMeasurements().add(m);
                } else {
                    Measurement N_m = N.getMeasurementByName(m.getName());
                    for (AccessPoint ap : m.getAccessPoints()) {
                        if (!N_m.containsAccessPoint(ap)) {
                            N_m.getAccessPoints().add(ap);
                        } else {
                            AccessPoint N_m_ap = N_m.getAccessPointByBSSID(ap.getBssid());
                            double previousLevel = N_m_ap.getLevel();
                            N_m_ap.setLevel(previousLevel + ap.getLevel());
                            N_m_ap.incrementCounter();
                        }
                    }
                }
            }
        }

        for (Measurement m : N.getMeasurements()) {
            for (AccessPoint ap : m.getAccessPoints()) {
                double previousLevel = ap.getLevel();
                double avg = previousLevel / (double)ap.getCounter();
                ap.setLevel(avg);
            }
        }

        return N;
    }

    public void getDistanceFromAccelerometer() {
        SensorManager mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accelerometerEventListener = new AccelerometerEventListener(activity);
        mSensorManager.registerListener(accelerometerEventListener,accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
