package com.lzy.safecheck.task;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.R;
import com.lzy.safecheck.utils.SignCheckUtil;
import com.lzy.safecheck.utils.Utils;

/**
 * App正版签名校验task
 * Create by 2020/6/16 from liuzhiyou
 **/
public class SignCheckTask extends AbstractCheckTask {

    public static final String TAG = SignCheckTask.class.getSimpleName();

    private String mRightCer;

    public SignCheckTask(FragmentActivity mActivity, String rightCer) {
        super(mActivity);
        this.mRightCer = rightCer;
    }

    @Override
    protected boolean check() {
        SignCheckUtil signCheck = new SignCheckUtil(mActivity, mRightCer);
        Utils.log(getTag(), "sha1: " + signCheck.getCer());
        return signCheck.check();
    }

    @Override
    protected void interruptCheck() {
        showNoticeDig();
    }

    protected void showNoticeDig() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(getString(R.string.safe_check_sign_warning_msg))
                .setNegativeButton(getString(R.string.safe_check_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.exitApp();
                    }
                })
                .create()
                .show();
    }


}
