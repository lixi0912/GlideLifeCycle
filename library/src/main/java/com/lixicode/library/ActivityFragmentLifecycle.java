package com.lixicode.library;


import com.lixicode.library.util.Util;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A {@link LifeCycle} implementation for tracking and notifying
 * listeners of {@link android.app.Fragment} and {@link android.app.Activity} lifecycle events.
 */
class ActivityFragmentLifecycle implements LifeCycle {

    private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>(0));

    @LifeCycleState
    int state;

    ActivityFragmentLifecycle() {
        state = INIT;
    }

    @Override
    public void addListener(LifecycleListener listener) {
        if (null != listener) {
            lifecycleListeners.add(listener);
            listener.onLifeCycleStateChange(state, 0);
        }
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    public void onStart() {
        state = ON_START;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onLifeCycleStateChange(ON_START, 0);
        }
    }

    public void onStop() {
        state = ON_STOP;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onLifeCycleStateChange(ON_STOP, 0);
        }
    }

    public void onDestroy() {
        state = ON_DESTROY;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onLifeCycleStateChange(ON_DESTROY, 0);
        }
    }

    public void onTrimMemory(int level) {
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onLifeCycleStateChange(ON_TRIM_MEMORY, level);
        }
    }

    public void onLowMemory() {
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onLifeCycleStateChange(ON_LOW_MEMORY, 0);
        }
    }
}
