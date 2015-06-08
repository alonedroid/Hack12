package mikamiyusen.com.hack12;

import android.app.Activity;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import mikamiyusen.com.hack12.utility.SNSMobilePush;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Bean
    SNSMobilePush push;

    @Click
    void fadeOut() {
        pushMessage("fadeout");
    }

    @Click
    void fadeIn() {
        pushMessage("fadein");
    }

    @Background
    void pushMessage(String message) {
        this.push.send(message);
        Log.d("push", message);
    }
}
