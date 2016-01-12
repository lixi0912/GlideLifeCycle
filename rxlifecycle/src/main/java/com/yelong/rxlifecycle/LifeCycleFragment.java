package com.yelong.rxlifecycle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import java.util.HashSet;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author lixi
 * @description <生命周期提供者>
 * @date 16/1/7
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LifeCycleFragment extends android.app.Fragment implements LifeCycleProvider {
    private final BehaviorSubject<Integer> lifecycleSubject = BehaviorSubject.create();

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifeCycle.ON_START);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(LifeCycle.ON_STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(LifeCycle.ON_DESTROY);
    }

    @Override
    public void onTrimMemory(int level) {
        lifecycleSubject.onNext(LifeCycle.ON_TRIM_MEMORY);
    }

    @Override
    public void onLowMemory() {
        lifecycleSubject.onNext(LifeCycle.ON_LOW_MEMORY);
    }

    @Override
    public Observable<Integer> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public BehaviorSubject<Integer> getLifecycle() {
        return lifecycleSubject;
    }

}
