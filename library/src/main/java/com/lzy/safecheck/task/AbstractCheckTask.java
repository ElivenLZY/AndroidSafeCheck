package com.lzy.safecheck.task;

import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.listener.OnTaskEventListener;
import com.lzy.safecheck.R;
import com.lzy.safecheck.TaskEvent;
import com.lzy.safecheck.utils.Utils;

/**
 * Create by liuzhiyou on 2020/11/9 16:35
 * Email：1130294881@qq.com
 */
public abstract class AbstractCheckTask implements ICheckTask {

    protected FragmentActivity mActivity;
    protected OnTaskEventListener mOnTaskEventListener;

    public AbstractCheckTask(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    final public void execute(OnTaskEventListener onTaskEventListener) {
        this.mOnTaskEventListener = onTaskEventListener;
        execute();
    }

    protected void execute() {
        boolean checkPass = check();
        if (!checkPass) {
            callTaskEventListener(false);
            interruptCheck();
            return;
        }
        callTaskEventListener(true);
    }

    protected abstract boolean check();

    protected void interruptCheck() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(String.format(getString(R.string.safe_check_fail_msg), getTag()))
                .setNegativeButton(getString(R.string.safe_check_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.exitApp();
                    }
                })
                .create()
                .show();
    }

    protected void callTaskEventListener(boolean result) {
        if (mOnTaskEventListener != null) {
            mOnTaskEventListener.OnTaskEvent(getTaskEvent(result));
        }
    }

    protected TaskEvent getTaskEvent(boolean result) {
        return new TaskEvent(getTag(), result);
    }

    @Override
    public String getTag() {
        return getClass().getSimpleName();
    }

    public String getString(@StringRes int resId) {
        return mActivity.getResources().getString(resId);
    }

}
