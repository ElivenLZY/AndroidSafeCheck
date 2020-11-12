package com.lzy.safecheck.task;

import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.utils.DebugCheckUtil;

/**
 * 调试检查task
 * Create by 2020/6/22 from liuzhiyou
 **/
public class DebugCheckTask extends AbstractCheckTask {

    public static final String TAG = DebugCheckTask.class.getSimpleName();

    public DebugCheckTask(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected boolean check() {
        DebugCheckUtil.getInstance(mActivity.getApplication()).check(true);
        return true;
    }

}
