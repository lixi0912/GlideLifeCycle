package com.lixicode.library;

public interface LifecycleListener {
    void onLifeCycleStateChange(@LifeCycle.LifeCycleState int life, int level);
}
