package mikamiyusen.com.hack12;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.amazonaws.http.UrlHttpClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import mikamiyusen.com.hack12.utility.RestClient;
import mikamiyusen.com.hack12.utility.SNSMobilePush;
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

    SNSMobilePush push;

    @AfterInject
    void init(){
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
    }

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
            pushMessage("play");
            this.welcomeCount = 0;
        } else {
            this.sound.sayBye();
            pushMessage("stop");
            logout();
        }
    }

    private int welcomeCount = 0;

    private void sayWelcome() {
        this.sound.sayWelcome();
        pushMessage(this.welcomeCount++%2==0?"fadeout":"fadein");
        new Handler().postDelayed(() -> this.sensor.getAccelerometer().getVisitor().init(), 1000);
    }

    private void sayYell() {
        pushMessage("Fight！");
        this.sound.sayYell();
        new Handler().postDelayed(() -> this.sensor.getAccelerometer().getNervous().init(), 1000);
    }

    @Background
    void logout() {
        try {
            this.restClient.requestMacRock();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

    @Background
    void pushMessage(String message) {
        try {
            SNSMobilePush.push(this, message);
        } catch (IOException e) {
            Log.d("error", e.toString());
        }
    }

    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }
}
