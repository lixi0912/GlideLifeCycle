package com.yelong.rxlifecycle;

import rx.Observable;
import rx.subjects.BehaviorSubject;


interface LifeCycleProvider {

     void onStart();

     void onStop() ;

     void onDestroy();

     void onTrimMemory(int level);

     void onLowMemory();

    Observable<Integer> lifecycle();

    BehaviorSubject<Integer> getLifecycle();
}
