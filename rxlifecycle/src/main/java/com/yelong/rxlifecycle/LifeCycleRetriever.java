package com.yelong.rxlifecycle;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixi
 * @description <生命周期嗅探者>
 * @date 16/1/7
 */
public final class LifeCycleRetriever implements Handler.Callback {
    private static final String FRAGMENT_TAG = "com.lixicode.lifecyclecore.manager";
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final String TAG = "LifeCycleRetriever";

    final private Map<android.support.v4.app.FragmentManager, SupportLifeCycleFragment> pendingSupportLifeCycleFragments = new HashMap<>(0);
    final private Map<android.app.FragmentManager, LifeCycleFragment> pendingLifeCycleFragments = new HashMap<>(0);
    private final Handler mHandler;
    private final LifeCycleProvider mApplicationLifeCycle = new ApplicationLifeCycle();

    private LifeCycleRetriever() {
        mHandler = new Handler(Looper.getMainLooper(), this/*Callback*/);
    }

    static LifeCycleRetriever getInstance() {
        return LifeCycleRetrieverHolder.mInstance;
    }

    private static class LifeCycleRetrieverHolder {
        private static LifeCycleRetriever mInstance = new LifeCycleRetriever();
    }

    public static LifeCycleProvider with(Context context) {
        return getInstance().get(context);
    }

    public static LifeCycleProvider with(Fragment context) {
        return getInstance().get(context);
    }

    public static LifeCycleProvider with(android.app.Fragment context) {
        return getInstance().get(context);
    }

    public static LifeCycleProvider with(FragmentActivity context) {
        return getInstance().get(context);
    }

    public static LifeCycleProvider with(Activity context) {
        return getInstance().get(context);
    }

    private LifeCycleProvider get(Context context) {
        if (context instanceof FragmentActivity) {
            return get((FragmentActivity) context);
        } else if (context instanceof Activity) {
            return get((Activity) context);
        } else if (context instanceof ContextWrapper) {
            return get(((ContextWrapper) context).getBaseContext());
        }
        return mApplicationLifeCycle;
    }


    private LifeCycleProvider get(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        android.app.FragmentManager fm = activity.getFragmentManager();
        return getLifeCycleFragment(fm, null);
    }

    private LifeCycleProvider get(FragmentActivity activity) {
        assertNotDestroyed(activity);
        android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();
        return getSupportLifeCycleFragment(fm, null);
    }

    private LifeCycleProvider get(android.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return get(fragment.getActivity());
        }
        android.app.FragmentManager fm = fragment.getChildFragmentManager();
        return getLifeCycleFragment(fm, fragment);
    }

    private LifeCycleProvider get(android.support.v4.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException(
                    "You cannot start a load on a fragment before it is attached");
        }
        android.support.v4.app.FragmentManager fm = fragment.getChildFragmentManager();
        return getSupportLifeCycleFragment(fm, fragment);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    final LifeCycleFragment getLifeCycleFragment(android.app.FragmentManager fm, android.app.Fragment parentHint) {
        LifeCycleFragment current = (LifeCycleFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (null == current) {
            current = pendingLifeCycleFragments.get(fm);
            if (null == current) {
                current = new LifeCycleFragment();
                pendingLifeCycleFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    final SupportLifeCycleFragment getSupportLifeCycleFragment(android.support.v4.app.FragmentManager fm, android.support.v4.app.Fragment parentHint) {
        SupportLifeCycleFragment current = (SupportLifeCycleFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (null == current) {
            current = pendingSupportLifeCycleFragments.get(fm);
            if (null == current) {
                current = new SupportLifeCycleFragment();
                pendingSupportLifeCycleFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
                key = fm;
                removed = pendingLifeCycleFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                android.support.v4.app.FragmentManager supportFm = (android.support.v4.app.FragmentManager) message.obj;
                key = supportFm;
                removed = pendingSupportLifeCycleFragments.remove(supportFm);
                break;
            default:
                handled = false;
                break;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }
}
