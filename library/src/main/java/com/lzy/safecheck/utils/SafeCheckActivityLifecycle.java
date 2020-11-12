package com.lzy.safecheck.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by liuzhiyou on 2020/11/12 10:28
 * Email：1130294881@qq.com
 */
public abstract class SafeCheckActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private int     mForegroundCount = 0;
    private int     mConfigCount     = 0;
    private boolean mIsBackground    = false;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (mConfigCount < 0) {
            ++mConfigCount;
        } else {
            ++mForegroundCount;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (mIsBackground) {
            mIsBackground = false;
            postStatus(activity, true);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (activity.isChangingConfigurations()) {
            --mConfigCount;
        } else {
            --mForegroundCount;
            if (mForegroundCount <= 0) {
                mIsBackground = true;
                postStatus(activity, false);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    private void postStatus(final Activity activity, final boolean isForeground) {
        if (isForeground) {
            onForeground(activity);
        } else {
            onBackground(activity);
        }
    }


    public abstract void onForeground(Activity activity);

    public abstract void onBackground(Activity activity);

}
