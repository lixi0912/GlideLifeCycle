package com.lixicode.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import com.lixicode.lifecyclecore.util.Util;

import java.util.HashSet;

/**
 * @author 陈晓辉
 * @description <>
 * @date 16/1/7
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LifeCycleFragment extends android.app.Fragment {

    private final ActivityFragmentLifecycle lifecycle;

    private LifeCycleFragment rootRequestManagerFragment;
    private final HashSet<LifeCycleFragment> childLifeCycleFragments = new HashSet<>(0);

    public LifeCycleFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @SuppressLint("ValidFragment")
    public LifeCycleFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }


    private void registerFragmentWithRoot(Activity activity) {
        unregisterFragmentWithRoot();
        rootRequestManagerFragment = LifeCycleRetriever.getInstance()
                .getLifeCycleFragment(activity.getFragmentManager(), null);
        if (rootRequestManagerFragment != this) {
            rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    private void unregisterFragmentWithRoot() {
        if (null != rootRequestManagerFragment) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
    }

    private void addChildRequestManagerFragment(LifeCycleFragment child) {
        childLifeCycleFragments.add(child);
    }

    private void removeChildRequestManagerFragment(LifeCycleFragment child) {
        childLifeCycleFragments.remove(child);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!Util.maybeTheActivityDestroyed(activity)) {
            try {
                registerFragmentWithRoot(activity);
            } catch (IllegalArgumentException e) {
                //https://github.com/bumptech/glide/issues/497
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterFragmentWithRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
        unregisterFragmentWithRoot();
    }

    @Override
    public void onTrimMemory(int level) {
        lifecycle.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        lifecycle.onLowMemory();
    }

    void setParentFragmentHint(android.app.Fragment parentFragmentHint) {
        if (parentFragmentHint != null && parentFragmentHint.getActivity() != null) {
            registerFragmentWithRoot(parentFragmentHint.getActivity());
        }
    }
}
