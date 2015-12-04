package indoors.aalto.indoorlocalization;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import indoors.aalto.json.models.AccessPoint;
import indoors.aalto.json.models.Measurement;
import indoors.aalto.json.models.Measurements;
import indoors.aalto.sensors.SensorManagement;
import indoors.aalto.utils.DirPath;
import indoors.aalto.utils.MeasurementStructure;
import indoors.aalto.utils.Utils;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private MainActivityController controller;

    private SensorManager mSensorManager;

    private ListView listView;
    private TextView wifiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFields();
    }

    private void initializeFields() {
        initializeUIFields();
        initializeNonUIFields();
    }

    private void initializeUIFields() {
        listView = (ListView) findViewById(R.id.sensorlistView);
        wifiTextView = (TextView) findViewById(R.id.wifiConnection);
    }

    private void initializeNonUIFields() {
        controller = new MainActivityController(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onPause() {
        controller.unregisterWifiReceiver();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        listView.setVisibility(View.INVISIBLE);
        wifiTextView.setText("");

        switch (item.getItemId()) {
            case R.id.action_listSensors:
                listAllSensorsOnDevice(this.listView);
                break;
            case R.id.action_listWiFi:
                controller.showWiFiInfo();
                break;
            case R.id.action_scanWiFi:
                controller.scanForWifi();
                break;
            case R.id.action_getLoc1:
                prepareMeasurementsData(0, DirPath.measurement_data.name());
                break;
            case R.id.action_getLoc2:
                prepareMeasurementsData(1, DirPath.measurement_data.name());
                break;
            case R.id.action_check141:
                prepareMeasurementsData(0, DirPath.A141_near.getPath());
                break;
            case R.id.action_owncheck141:
                computeEuclidianDistance(controller.getOwn_measurements());
                break;
            case R.id.action_accelerometer:
                controller.getDistanceFromAccelerometer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @param loc              0 or 1 according to the desired location
     * @param measurement_path the path for the measurements in the measured data
     */
    public void prepareMeasurementsData(int loc, String measurement_path) {
        List<Measurements> measurement_data = Utils.importData(measurement_path, this);
        // get the measurements associated to one of the files in measurement_data
        Measurements measurements = measurement_data.get(loc);

        computeEuclidianDistance( measurements);
    }

    private void computeEuclidianDistance(Measurements measurements) {
        listView.setVisibility(View.VISIBLE);
        Map<String, List<Measurements>> reference_data = Utils.importReferenceMeasures(this);
        List<MeasurementStructure> structuresLoc = getMeasurementStructures(reference_data, measurements);
        ArrayAdapter<MeasurementStructure> measurementsAdapter = new ArrayAdapter<>
                (this, R.layout.simplerow, structuresLoc);
        listView.setAdapter(measurementsAdapter);
    }


    private List<MeasurementStructure> getMeasurementStructures(Map<String, List<Measurements>> reference_data, Measurements measurements1) {
        List<MeasurementStructure> measurementStructures = new ArrayList<>();

        for (Map.Entry<String, List<Measurements>> entry : reference_data.entrySet()) {
            MeasurementStructure measurementStructure = new MeasurementStructure(entry.getKey());
            measurementStructure.setMeasurementsForRoom(reference_data.get(entry.getKey()));
            Measurements avgMeasurements = controller.getAverageMeasurements(measurementStructure.getMeasurementsForRoom());
            measurementStructures.add(measurementStructure);
            measurementStructure.addDistance(computeDistance(measurements1,avgMeasurements));
        }
        return measurementStructures;
    }



    private MeasurementStructure getClosestMeasurementStructure(List<MeasurementStructure> measurementStructures) {
        MeasurementStructure closestStructure = null;
        double minDistance = Double.MAX_VALUE;
        for (MeasurementStructure measurementStructure : measurementStructures) {
            if (measurementStructure.getMinDistance() < minDistance) {
                minDistance = measurementStructure.getMinDistance();
                closestStructure = measurementStructure;
            }
        }
        return closestStructure;
    }

    private double computeDistance(Measurements measurement_data, Measurements ref_data) {
        double D = 0.0;
        for (Measurement measurement : measurement_data.getMeasurements()) {
            for (Measurement ref_measurement : ref_data.getMeasurements()) {
                if (ref_measurement.getName().equalsIgnoreCase(measurement.getName())) {
                    for (AccessPoint accessPoint : measurement.getAccessPoints()) {
                        for (AccessPoint ref_access_point : ref_measurement.getAccessPoints()) {
                            if (accessPoint.getBssid().equals(ref_access_point.getBssid())) {
                                double difference = accessPoint.getLevel() - ref_access_point.getLevel();
                                double pow_diff = Math.pow(difference, 2.0);
                                D += pow_diff;
                            }
                        }
                    }
                }
            }
        }
        return Math.sqrt(D);
    }

    public void listAllSensorsOnDevice(View view) {
        listView.setVisibility(view.VISIBLE);
        ArrayAdapter<Sensor> sensorAdapter = new ArrayAdapter<Sensor>
                (this, R.layout.simplerow, SensorManagement.getAvailableSensorsOnDevice(mSensorManager));
        listView.setAdapter(sensorAdapter);
    }
}
