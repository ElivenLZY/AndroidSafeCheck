package com.lzy.safecheck.task;

import android.content.DialogInterface;

import com.lzy.safecheck.R;
import com.lzy.safecheck.TaskEvent;
import com.lzy.safecheck.listener.OnTaskEventListener;
import com.lzy.safecheck.listener.OnTaskListener;
import com.lzy.safecheck.utils.Utils;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

/**
 * Create by liuzhiyou on 2020/11/9 16:35
 * Email：1130294881@qq.com
 */
public abstract class AbstractCheckTask implements ICheckTask {

    protected FragmentActivity    mActivity;
    protected OnTaskEventListener mOnTaskEventListener;

    public AbstractCheckTask(FragmentActivity activity) {
        this.mActivity = activity;
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

    /**
     * 具体的检查逻辑
     */
    protected abstract boolean check();

    /**
     * 检查不通过时处理
     */
    protected void interruptCheck() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(String.format(getString(R.string.safe_check_fail_msg), getTag()))
                .setNegativeButton(getString(R.string.safe_check_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callTaskEventListener(true, false);
                    }
                })
                .setNeutralButton(getString(R.string.safe_check_exit), new DialogInterface.OnClickListener() {
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
            mOnTaskEventListener.onEvent(getTaskEvent(result), true);
        }
    }

    /**
     * 调用 {@link OnTaskEventListener} 的 onEvent 方法
     *
     * @param result        检查通过检查
     * @param callTaskEvent 是否调用{@link OnTaskListener} 的 onTaskEvent 方法
     */
    protected void callTaskEventListener(boolean result, boolean callTaskEvent) {
        if (mOnTaskEventListener != null) {
            mOnTaskEventListener.onEvent(getTaskEvent(result), callTaskEvent);
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
