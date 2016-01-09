package com.lixicode.library;

/**
 * A {@link LifeCycle} implementation for tracking and notifying
 * listeners of {@link android.app.Application} lifecycle events.
 * <p/>
 * <p> Since there are essentially no {@link android.app.Application} lifecycle events, this class
 * simply defaults to notifying new listeners that they are started. </p>
 */
class ApplicationLifeCycle implements LifeCycle {

    @Override
    public void addListener(LifecycleListener listener) {
        if (null != listener)
            listener.onLifeCycleStateChange(ON_START, 0);
    }

    @Override
    public void removeListener(LifecycleListener listener) {

    }
}
