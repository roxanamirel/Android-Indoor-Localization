package indoors.aalto.indoorlocalization;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class AccelerometerEventListener implements SensorEventListener {

    private static final String TAG = AccelerometerEventListener.class.toString();
    private static final float NS2S = 1.0f / 1000000000.0f;

    private Activity mActivity;
    private Traveller traveller;
    private TextView mTextView;

    public AccelerometerEventListener(Activity activity) {
        mActivity = activity;
        traveller = new Traveller();
        mTextView = (TextView) mActivity.findViewById(R.id.wifiConnection);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (traveller.getLastTimeStamp() != 0) {

            float dt = (event.timestamp - traveller.getLastTimeStamp()) * NS2S;

            if (dt > 0f) {

                float zAcceleration = event.values[2];

                float initialVelocity = traveller.getVelocity();

                float finalVelocity = initialVelocity + zAcceleration * dt;
                traveller.setVelocity(finalVelocity);

                float finalDistance = initialVelocity * dt
                        + traveller.getVelocity() * dt / 2f;

                mTextView.setText("Distance = " + String.format("%.3f", finalDistance));
            } else {
                traveller.setVelocity(0f);
            }
        }

        traveller.setLastTimeStamp(event.timestamp);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
