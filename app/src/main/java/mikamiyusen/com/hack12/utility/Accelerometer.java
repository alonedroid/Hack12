package mikamiyusen.com.hack12.utility;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.androidannotations.annotations.EBean;

import lombok.Getter;

@EBean
public class Accelerometer implements SensorEventListener {

    @Getter
    private Visitor visitor = new Visitor();

    @Getter
    private Nervous nervous = new Nervous();

    private float[] gravity = new float[3];

    private float[] geomagnetic = new float[3];

    private float[] rotationMatrix = new float[9];

    private float[] attitude = new float[3];

    @Override
    public void onSensorChanged(SensorEvent event) {
        setValues(event);
        calculateAccelerometer();
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    private void setValues(SensorEvent event) {
        switch (event.sensor.getType()) {
            case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
                this.geomagnetic = event.values.clone();
                break;
            case android.hardware.Sensor.TYPE_ACCELEROMETER:
                this.gravity = event.values.clone();
                break;
        }
    }

    private void calculateAccelerometer() {
        if (this.geomagnetic == null || this.gravity == null) return;

        SensorManager.getRotationMatrix(this.rotationMatrix, null, this.gravity, this.geomagnetic);
        SensorManager.getOrientation(this.rotationMatrix, this.attitude);

        this.visitor.checkVisitor(radianToDegree(this.attitude[0]));
        this.nervous.checkNervous(radianToDegree(this.attitude[1]));
    }

    private double radianToDegree(float radian) {
        double degree = Math.floor(Math.toDegrees(radian));
        if (degree >= 0) {
            return degree;
        } else {
            return 360 + degree;
        }
    }
}