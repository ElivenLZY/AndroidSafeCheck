package com.lzy.safecheck.task;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.R;
import com.lzy.safecheck.utils.Utils;

/**
 * root检查task
 * Create by 2020/6/22 from liuzhiyou
 **/
public class RootCheckTask extends AbstractCheckTask {

    public static final String TAG = RootCheckTask.class.getSimpleName();

    public RootCheckTask(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected boolean check() {
        return Utils.isDeviceRooted();
    }

    @Override
    protected void interruptCheck() {
        showNoticeDig();
    }

    public void showNoticeDig() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(getString(R.string.safe_check_root_warning_msg))
                .setNegativeButton(getString(R.string.safe_check_root_warning_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callTaskEventListener(true,false);
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

}
