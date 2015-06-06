package mikamiyusen.com.hack12;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

abstract public class SensorActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;

    float[] gravity = new float[3];

    float[] geomagnetic = new float[3];

    float[] rotationMatrix = new float[9];

    float[] attitude = new float[3];

    private boolean isActioning;

    private double baseRotate;

    private double beforePitch;

    private boolean initPitch = true;

    private boolean initRotate = true;

    private int pitchCount;

    private int changePitchCount;

    public void initRequest() {
        this.initPitch = true;
        this.initRotate = true;
    }

    protected void init() {
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    protected void initViews() {
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                actionProximity(event.values[0] == 0);
                setActioning(event.values[0] == 0);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                this.geomagnetic = event.values.clone();
                calculateRotate();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                this.gravity = event.values.clone();
                calculateRotate();
                break;
        }
    }

    private void calculateRotate() {
        if (this.geomagnetic == null || this.gravity == null) return;
        if (!isActioning) return;

        SensorManager.getRotationMatrix(this.rotationMatrix, null, this.gravity, this.geomagnetic);
        SensorManager.getOrientation(this.rotationMatrix, this.attitude);

        checkRotate(radianToDegree(this.attitude[0]));
        checkVib(radianToDegree(this.attitude[1]));
    }

    private void checkVib(double newPitch) {
        if (this.initPitch) {
            this.beforePitch = newPitch;
            this.initPitch = false;
        } else if (this.beforePitch != newPitch) {
            this.beforePitch = newPitch;
            if (this.changePitchCount++ > 12) {
                actionYell();
                this.changePitchCount = 0;
            }
        }
        if (this.pitchCount++ > 30) {
            this.pitchCount = 0;
            this.changePitchCount = 0;
        }
    }

    private void checkRotate(double newRotate) {
        if (this.initRotate) {
            this.baseRotate = newRotate;
            this.initRotate = false;
        } else if (Math.abs(this.baseRotate - newRotate) > 45) {
            actionTurn();
            this.baseRotate = newRotate;
        }
    }

    private double radianToDegree(float radian) {
        double degree = Math.floor(Math.toDegrees(radian));
        if (degree >= 0) {
            return degree;
        } else {
            return 360 + degree;
        }
    }

    abstract protected void actionProximity(boolean isNear);

    abstract protected void actionTurn();

    abstract protected void actionYell();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterSensor();
    }

    void setActioning(boolean isActioning) {
        if (isActioning) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isActioning = isActioning;
    }

    private void unregisterSensor() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this);
            this.sensorManager = null;
        }
    }
}