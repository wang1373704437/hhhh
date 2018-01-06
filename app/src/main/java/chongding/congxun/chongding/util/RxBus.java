package chongding.congxun.chongding.util;

import com.hwangjr.rxbus.Bus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public final class RxBus {
    private static Bus sBus;
    public synchronized static Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}