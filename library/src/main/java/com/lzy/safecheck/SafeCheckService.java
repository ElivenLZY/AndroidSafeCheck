package com.lzy.safecheck;

import com.lzy.safecheck.listener.OnTaskListener;

/**
 * 安全检查管理
 * Create by 2020/6/17 from liuzhiyou
 **/
public class SafeCheckService {

    private static boolean debugLog = false;
    private static String TAG = "SafeCheck";


    public static void setLogTag(String tag) {
        TAG=tag;
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setLogEnabled(boolean var0) {
        debugLog = var0;
    }

    public static boolean isDebugLog() {
        return debugLog;
    }

    /**
     * @return true: 继续执行 false：停止执行
     **/
    public static void startCheck(TaskQueue taskQueue, OnTaskListener onTaskListener) {
        new SafeCheck.Builder()
                .setTaskQueue(taskQueue)
                .setOnTaskListener(onTaskListener)
                .build()
                .startCheck();
    }
}
