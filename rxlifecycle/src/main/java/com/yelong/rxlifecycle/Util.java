package com.yelong.rxlifecycle;

import android.app.Activity;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lixi
 * @description <>
 * @date 16/1/7
 */
class Util {

    public static <T> List<T> getSnapshot(Collection<T> other) {
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }


    public static boolean maybeTheActivityDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return true;
        }
        return false;
    }
}
