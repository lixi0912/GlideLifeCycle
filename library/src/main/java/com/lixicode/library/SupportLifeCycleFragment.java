package com.lixicode.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import com.lixicode.library.util.Util;

import java.util.HashSet;

/**
 * @author 陈晓辉
 * @description <>
 * @date 16/1/7
 */
public class SupportLifeCycleFragment extends Fragment {
    private final ActivityFragmentLifecycle lifecycle;
    private final HashSet<SupportLifeCycleFragment> childLifeCycleFragments =
            new HashSet<>();

    public SupportLifeCycleFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @SuppressLint("ValidFragment")
    public SupportLifeCycleFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }


    private SupportLifeCycleFragment rootRequestManagerFragment;


    private void registerFragmentWithRoot(FragmentActivity activity) {
        unregisterFragmentWithRoot();
        rootRequestManagerFragment = LifeCycleRetriever.getInstance()
                .getSupportLifeCycleFragment(activity.getSupportFragmentManager(), null);
        if (rootRequestManagerFragment != this) {
            rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }


    private void unregisterFragmentWithRoot() {
        if (rootRequestManagerFragment != null) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
    }

    private void addChildRequestManagerFragment(SupportLifeCycleFragment child) {
        childLifeCycleFragments.add(child);
    }

    private void removeChildRequestManagerFragment(SupportLifeCycleFragment child) {
        childLifeCycleFragments.remove(child);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!Util.maybeTheActivityDestroyed(activity)) {
            try {
                registerFragmentWithRoot(getActivity());
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
    public void onLowMemory() {
        lifecycle.onLowMemory();
    }

    void setParentFragmentHint(Fragment parentFragmentHint) {
        if (parentFragmentHint != null && parentFragmentHint.getActivity() != null) {
            registerFragmentWithRoot(parentFragmentHint.getActivity());
        }
    }

}
