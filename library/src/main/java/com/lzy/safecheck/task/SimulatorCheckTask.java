package com.lzy.safecheck.task;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.R;
import com.lzy.safecheck.utils.Utils;

/**
 * 模拟器检查task
 * Create by 2020/6/22 from liuzhiyou
 **/
public class SimulatorCheckTask extends AbstractCheckTask {

    public static final String TAG = SimulatorCheckTask.class.getSimpleName();

    public SimulatorCheckTask(FragmentActivity mActivity) {
        super(mActivity);
    }

    @Override
    protected boolean check() {
        return Utils.isEmulator(mActivity);
    }

    @Override
    protected void interruptCheck() {
        showNoticeDig();
    }

    protected void showNoticeDig() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(getString(R.string.safe_check_simu_warning_msg))
                .setNegativeButton(getString(R.string.safe_check_root_warning_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callTaskEventListener(true);
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
