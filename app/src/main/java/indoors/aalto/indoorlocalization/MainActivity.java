package indoors.aalto.indoorlocalization;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import indoors.aalto.sensors.SensorManagement;
import indoors.aalto.wifi.MyScanResult;
import indoors.aalto.wifi.WIFIManagement;


public class MainActivity extends Activity {
    private SensorManager mSensorManager;
    private WifiManager wifiManager;
    private WifiScanReceiver wifiReceiver;

    private ListView listView;
    private TextView wifiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.sensorlistView);
        wifiTextView = (TextView) findViewById(R.id.wifiConnection);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiReceiver = new WifiScanReceiver();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        listView.setVisibility(View.INVISIBLE);
        int id = item.getItemId();
        wifiTextView.setText("");
        switch (id) {
            case R.id.action_listSensors:
                listAllSensorsOnDevice(this.listView);
                break;
            case R.id.action_listWiFi:
                wifiTextView.setText(WIFIManagement.getWiFiConnectionInfo(wifiManager));
                break;
            case R.id.action_scanWiFi:
                registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiManager.startScan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listAllSensorsOnDevice(View view) {
        listView.setVisibility(view.VISIBLE);
        ArrayAdapter<Sensor> sensorAdapter = new ArrayAdapter<Sensor>
                (this, R.layout.simplerow, SensorManagement.getAvailableSensorsOnDevice(mSensorManager));
        listView.setAdapter(sensorAdapter);
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            listView.setVisibility(View.VISIBLE);

            int state = wifiManager.getWifiState();
            if (state == WifiManager.WIFI_STATE_ENABLED) {
                List<ScanResult> results = WIFIManagement.getAccessPoints(wifiManager);
                List<MyScanResult> finalResults = new ArrayList<MyScanResult>();
                for (ScanResult scanResult : results) {
                    MyScanResult result = new MyScanResult();
                    int level = wifiManager.calculateSignalLevel(scanResult.level, 5);
                    double distanceFromAP = WIFIManagement.calculateDistance(scanResult.level,scanResult.frequency);
                    result.setScanResult(scanResult);
                    result.setSignalStrength(level);
                    result.setDistanceFromAP(distanceFromAP);
                    finalResults.add(result);
                }
                ArrayAdapter<MyScanResult> wifiAdapter = new ArrayAdapter<>
                        (getApplicationContext(), R.layout.simplerow, finalResults);
                listView.setAdapter(wifiAdapter);
            }


        }
    }

}
