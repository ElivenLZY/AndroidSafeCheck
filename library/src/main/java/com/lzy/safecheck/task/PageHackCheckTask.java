package com.lzy.safecheck.task;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.lzy.safecheck.utils.SafeCheckActivityLifecycle;
import com.lzy.safecheck.utils.Utils;

import androidx.fragment.app.FragmentActivity;

/**
 * Create by liuzhiyou on 2020/11/12 10:36
 * Email：1130294881@qq.com
 */
public class PageHackCheckTask extends AbstractCheckTask {

    public static final String TAG = RootCheckTask.class.getSimpleName();

    public PageHackCheckTask(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected boolean check() {
        final Application app = mActivity.getApplication();
        app.registerActivityLifecycleCallbacks(new SafeCheckActivityLifecycle() {
            @Override
            public void onForeground(Activity activity) {

            }

            @Override
            public void onBackground(Activity activity) {
                Toast.makeText(app.getApplicationContext(), Utils.getAppName(app) + "已退至后台", Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }


}
