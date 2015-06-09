package mikamiyusen.com.hack12.utility;

import android.util.Log;

import lombok.Getter;
import rx.subjects.BehaviorSubject;

public class Visitor {

    private static final int VISIT_DEGREE = 7;

    private static final double NOISE = 0.0;

    @Getter
    private BehaviorSubject<Boolean> visit = BehaviorSubject.create(false);

    private boolean initRotate = true;

    private double baseRotate;

    public void checkVisitor(double newRotate) {
        Log.d("rotate", newRotate+"ms");
        if (newRotate == NOISE) return;

        if (this.initRotate) {
            this.baseRotate = newRotate;
            this.initRotate = false;
        } else if (Math.abs(this.baseRotate - newRotate) > VISIT_DEGREE) {
            this.visit.onNext(true);
            this.baseRotate = newRotate;
        }
    }

    public void init() {
        this.initRotate = true;
        this.visit.onNext(false);
    }
}
