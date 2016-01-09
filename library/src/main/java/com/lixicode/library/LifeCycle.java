package com.lixicode.library;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An interface for listening to Activity/Fragment lifecycle events.
 */
public interface LifeCycle {
    int INIT=0;
    int ON_START = 1;
    int ON_STOP = 2;
    int ON_DESTROY = 3;
    int ON_TRIM_MEMORY = 4;
    int ON_LOW_MEMORY = 5;

    @IntDef({INIT,ON_START, ON_STOP, ON_DESTROY, ON_TRIM_MEMORY, ON_LOW_MEMORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LifeCycleState {}
    /**
     * Adds the given listener to the set of listeners managed by this Lifecycle implementation.
     */
    void addListener(LifecycleListener listener);

    /**
     * Removes the given listener from the set of listeners managed by this Lifecycle implementation,
     * returning {@code true} if the listener was removed sucessfully, and {@code false} otherwise.
     * <p/>
     * <p>This is an optimization only, there is no guarantee that every added listener will
     * eventually be removed.
     */
    void removeListener(LifecycleListener listener);
}
