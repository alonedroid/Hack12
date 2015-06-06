package mikamiyusen.com.hack12;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class SubActivity extends Activity implements SensorEventListener {
    protected final static double RAD2DEG = 180 / Math.PI;
    SensorManager sensorManager;
    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];
    Toast toast;

    @AfterInject
    public void init() {
        initSensor();
    }

    @AfterViews
    public void initViews() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }
        if (geomagnetic != null && gravity != null) {
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
            SensorManager.getOrientation(rotationMatrix, attitude);
            if (toast == null) {
                toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
            }
            StringBuffer sb = new StringBuffer();
            sb.append(String.valueOf((int) (attitude[0] * RAD2DEG))).append("\n");
            sb.append(String.valueOf((int) (attitude[1] * RAD2DEG))).append("\n");
            sb.append(String.valueOf((int) (attitude[2] * RAD2DEG))).append("\n");
            toast.setText(sb.toString());
            toast.show();
        }
    }
}
