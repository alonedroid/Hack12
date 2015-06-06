package mikamiyusen.com.hack12.utility;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import org.androidannotations.annotations.EBean;

import lombok.Getter;
import rx.subjects.BehaviorSubject;

@EBean
public class Proximity implements SensorEventListener {

    @Getter
    private BehaviorSubject<Boolean> actioning = BehaviorSubject.create(false);

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case android.hardware.Sensor.TYPE_PROXIMITY:
                this.actioning.onNext(event.values[0] == 0);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }
}