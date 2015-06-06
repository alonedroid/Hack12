package mikamiyusen.com.hack12.utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import mikamiyusen.com.hack12.R;

@EBean
public class Sound {

    @RootContext
    Context context;

    private SoundPool soundPool;

    private int[] soundId = new int[4];

    @AfterInject
    void init() {
        this.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        this.soundId[0] = this.soundPool.load(this.context, R.raw.hello, 0);
        this.soundId[1] = this.soundPool.load(this.context, R.raw.bye, 0);
        this.soundId[2] = this.soundPool.load(this.context, R.raw.welcome, 0);
        this.soundId[3] = this.soundPool.load(this.context, R.raw.yell, 0);
    }

    public void sayHello() {
        Log.d("test", "hello");
        speak(0);
    }

    public void sayBye() {
        Log.d("test", "bye");
        speak(1);
    }

    public void sayWelcome() {
        Log.d("test", "welcome");
        speak(2);
    }

    public void sayYell() {
        Log.d("test", "yell");
        speak(3);
    }

    private void speak(int index) {
        this.soundPool.play(this.soundId[index], 1.0F, 1.0F, 0, 0, 1.0F);
    }

    public void release() {
        this.soundPool.release();
    }
}