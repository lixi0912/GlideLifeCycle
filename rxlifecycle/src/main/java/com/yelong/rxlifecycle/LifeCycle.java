package com.yelong.rxlifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;


public class LifeCycle {
    public static final int INIT = 0;
    public static final int ON_START = 1;
    public static final int ON_STOP = 2;
    public static final int ON_DESTROY = 3;

    public static final int ON_TRIM_MEMORY = 4;
    public static final int ON_LOW_MEMORY = 5;

    @IntDef({INIT, ON_START, ON_STOP, ON_DESTROY, ON_TRIM_MEMORY, ON_LOW_MEMORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LifeCycleState {
    }


    public static <T> Observable.Transformer<T, T> bindUntil(Context context, @LifeCycleState int lifeCycleEvent) {
        if (null == context)
            throw new IllegalArgumentException("context must be given");
        return bind(LifeCycleRetriever.with(context).getLifecycle(), lifeCycleEvent);
    }

    public static <T> Observable.Transformer<T, T> bindUntil(Activity activity, @LifeCycleState int lifeCycleEvent) {
        if (null == activity)
            throw new IllegalArgumentException("activity must be given");
        return bind(LifeCycleRetriever.with(activity).getLifecycle(), lifeCycleEvent);
    }

    public static <T> Observable.Transformer<T, T> binUntil(Fragment fragment, @LifeCycleState int lifeCycleEvent) {
        if (null == fragment)
            throw new IllegalArgumentException("fragment must be given");
        return bind(LifeCycleRetriever.with(fragment).getLifecycle(), lifeCycleEvent);
    }

    public static <T> Observable.Transformer<T, T> bindUntil(android.support.v4.app.Fragment fragment, @LifeCycleState int lifeCycleEvent) {
        if (null == fragment)
            throw new IllegalArgumentException("fragment must be given");
        return bind(LifeCycleRetriever.with(fragment).getLifecycle(), lifeCycleEvent);
    }

    public static <T> Observable.Transformer<T, T> bindUntil(FragmentActivity activity, @LifeCycleState int lifeCycleEvent) {
        if (null == activity)
            throw new IllegalArgumentException("activity must be given");
        return bind(LifeCycleRetriever.with(activity).getLifecycle(), lifeCycleEvent);
    }

    private static <T> Observable.Transformer<T, T> bind(final BehaviorSubject<Integer> lifeCycle, final int event) {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> source) {
                return source.takeUntil(lifeCycle.takeFirst(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer lifecycleEvent) {
                        return event == lifecycleEvent;
                    }
                }));
            }
        };
    }
}
