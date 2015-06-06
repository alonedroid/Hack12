package mikamiyusen.com.hack12;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;

import mikamiyusen.com.hack12.utility.RestClient;
import mikamiyusen.com.hack12.utility.Sensor;
import mikamiyusen.com.hack12.utility.Sound;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @RestService
    RestClient restClient;

    @Bean
    Sound sound;

    @Bean
    Sensor sensor;

    @AfterViews
    void initViews() {
        this.sensor.getProximity().getActioning()
                .subscribe(this::start);
        this.sensor.getAccelerometer().getVisitor().getVisit().distinctUntilChanged().asObservable()
                .filter(visit -> visit)
                .subscribe(visit -> sayWelcome());
        this.sensor.getAccelerometer().getNervous().getNervous().distinctUntilChanged().asObservable()
                .filter(nervous -> nervous)
                .subscribe(nervous -> sayYell());
    }

    @Override
    protected void onStop() {
        this.sound.release();
        this.sensor.unregisterSensor();
        super.onStop();
    }

    protected void start(boolean startAction) {
        if (startAction) {
            this.sound.sayHello();
        } else {
            this.sound.sayBye();
            logout();
        }
    }

    private void sayWelcome() {
        this.sound.sayWelcome();
        new Handler().postDelayed(() -> this.sensor.getAccelerometer().getVisitor().init(), 1000);
    }

    private void sayYell() {
        this.sound.sayYell();
        new Handler().postDelayed(() -> this.sensor.getAccelerometer().getNervous().init(), 1000);
    }

    @Background
    void logout() {
        try {
            this.restClient.requestMacRock();
        }catch(Exception e){
            Log.d("error", e.toString());
        }
    }
}
