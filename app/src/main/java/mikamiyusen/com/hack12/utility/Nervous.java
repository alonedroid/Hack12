package mikamiyusen.com.hack12.utility;

import lombok.Getter;
import rx.subjects.BehaviorSubject;

public class Nervous {

    private static final int NERVOUS_NUM = 15;

    private static final int NERVOUS_RESET_COUNT = 40;

    @Getter
    private BehaviorSubject<Boolean> nervous = BehaviorSubject.create(false);

    private boolean initPitch = true;

    private double beforePitch;

    private int pitchCount;

    private int changePitchCount;

    public void checkNervous(double newPitch) {
        if (this.initPitch) {
            this.beforePitch = newPitch;
            this.initPitch = false;
        } else if (this.beforePitch != newPitch) {
            this.beforePitch = newPitch;
            countUpNervous();
        }
        countUpChecker();
    }

    private void countUpNervous() {
        if (this.changePitchCount++ > NERVOUS_NUM) {
            this.nervous.onNext(true);
            this.changePitchCount = 0;
        }
    }

    private void countUpChecker() {
        if (this.pitchCount++ > NERVOUS_RESET_COUNT) {
            this.pitchCount = 0;
            this.changePitchCount = 0;
        }
    }

    public void init() {
        this.initPitch = true;
        this.nervous.onNext(false);
    }
}