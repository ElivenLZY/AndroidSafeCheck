package com.lzy.safecheck.task;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.lzy.safecheck.R;
import com.lzy.safecheck.utils.Utils;

/**
 * 网络代理检查task(防抓包)
 * Create by 2020/6/22 from liuzhiyou
 **/
public class NetProxyCheckTask extends AbstractCheckTask {

    public static final String TAG = NetProxyCheckTask.class.getSimpleName();

    protected boolean mIsOpenNetProxyCheck;

    public NetProxyCheckTask(FragmentActivity activity, boolean isOpenNetProxyCheck) {
        super(activity);
        this.mIsOpenNetProxyCheck = isOpenNetProxyCheck;
    }

    @Override
    protected boolean check() {
        if (!mIsOpenNetProxyCheck) return true;
        return !Utils.isWifiProxy(mActivity);
    }

    @Override
    protected void interruptCheck() {
        showNoticeDig();
    }

    protected void showNoticeDig() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.safe_check_warning))
                .setMessage(getString(R.string.safe_check_net_proxy_warning_msg))
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
