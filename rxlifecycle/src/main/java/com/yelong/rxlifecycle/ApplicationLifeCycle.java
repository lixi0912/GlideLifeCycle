package com.yelong.rxlifecycle;


import rx.subjects.BehaviorSubject;


final class ApplicationLifeCycle implements  LifeCycleProvider{


    private final BehaviorSubject<Integer> lifecycleSubject = BehaviorSubject.create();


    ApplicationLifeCycle() {
    }

    public void onStart() {
        lifecycleSubject.onNext(LifeCycle.ON_START);
    }

    public void onStop() {
        lifecycleSubject.onNext(LifeCycle.ON_STOP);
    }

    public void onDestroy() {
        lifecycleSubject.onNext(LifeCycle.ON_DESTROY);
    }

    public void onTrimMemory(int level) {
        lifecycleSubject.onNext(LifeCycle.ON_TRIM_MEMORY);
    }

    public void onLowMemory() {
        lifecycleSubject.onNext(LifeCycle.ON_LOW_MEMORY);
    }

    public BehaviorSubject<Integer> getLifecycle(){
        return  lifecycleSubject;
    }
}
