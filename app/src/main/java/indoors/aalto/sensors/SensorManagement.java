package indoors.aalto.sensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by rroxa_000 on 11/25/2015.
 */
public class SensorManagement {

    public static List<Sensor> getAvailableSensorsOnDevice(SensorManager mSensorManager){
        return mSensorManager.getSensorList(Sensor.TYPE_ALL);
    }
}
