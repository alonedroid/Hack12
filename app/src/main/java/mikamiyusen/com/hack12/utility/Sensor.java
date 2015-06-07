package mikamiyusen.com.hack12.utility;

import android.content.Context;
import android.hardware.SensorManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.concurrent.TimeUnit;

import lombok.Getter;

@EBean
public class Sensor {

    @RootContext
    Context context;

    @Getter
    private Proximity proximity;

    @Getter
    private Accelerometer accelerometer;

    private SensorManager sensorManager;

    @AfterInject
    protected void init() {
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.proximity = Proximity_.getInstance_(this.context);
        this.accelerometer = Accelerometer_.getInstance_(this.context);
    }

    @AfterViews
    protected void initViews() {
        this.sensorManager.registerListener(this.proximity, this.sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_UI);
        this.proximity.getActioning()
                .filter(actioning -> actioning)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribe(actioning -> registerAccelerometerListener());
        this.proximity.getActioning()
                .filter(actioning -> !actioning)
                .subscribe(actioning -> unregisterAccelerometerListener());
    }

    private void registerAccelerometerListener() {
        this.sensorManager.registerListener(this.accelerometer, this.sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this.accelerometer, this.sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterAccelerometerListener() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this.accelerometer);
        }
    }

    public void unregisterSensor() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this.proximity);
            this.sensorManager = null;
        }
    }
}