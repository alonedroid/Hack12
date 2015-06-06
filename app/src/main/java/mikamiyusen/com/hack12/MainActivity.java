package mikamiyusen.com.hack12;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import mikamiyusen.com.hack12.utility.Sound;

@EActivity(R.layout.activity_main)
public class MainActivity extends SensorActivity {

    @Bean
    Sound sound;

    @AfterInject
    void initChild() {
        super.init();
    }

    @AfterViews
    void initViewsChild() {
        super.initViews();
    }

    @Override
    protected void onStop() {
        this.sound.release();
        super.onDestroy();
    }

    @Override
    protected void actionProximity(boolean isNear) {
        if (isNear) {
            this.sound.sayHello();
        } else {
            this.sound.sayBye();
        }
    }

    @Override
    protected void actionTurn() {
        this.sound.sayWelcome();
    }

    @Override
    protected void actionYell() {
        this.sound.sayYell();
    }

    @Click
    void mainImage() {
        initRequest();
    }
}
