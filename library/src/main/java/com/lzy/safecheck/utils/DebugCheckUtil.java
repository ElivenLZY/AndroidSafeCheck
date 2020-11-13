package com.lzy.safecheck.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Debug;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.safecheck.SafeCheckService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 调试检查
 * 严格模式下1秒检查一次，检查5分钟
 * 普通模式下10秒检查一次，检查1分钟
 * Created by liuzhiyou on 2020-08-05.
 */
public class DebugCheckUtil {
    private static final String TAG = DebugCheckUtil.class.getSimpleName();
    private static volatile DebugCheckUtil instance;
    private Application application;
    private static final int MODE_STRICT_TIME = 5 * 60 * 1000;
    private static final int MODE_NORMAL_TIME = 60 * 1000;
    private static final int MODE_STRICT_INTERVAL_TIME = 1000;
    private static final int MODE_NORMAL_INTERVAL_TIME = 10000;
    private static final int CHECK_DEFAULT_PORT = 23946;
    private volatile boolean isStopCheck;

    private volatile boolean isStrictMode;
    private long executionTime;
    private Thread checkThread;

    private DebugCheckUtil(Application application) {
        this.application = application;
    }

    public static DebugCheckUtil getInstance(Application application) {
        if (instance == null) {
            synchronized (DebugCheckUtil.class) {
                if (instance == null) {
                    instance = new DebugCheckUtil(application);
                }
            }
        }
        return instance;
    }

    /**
     * 是否启动严格模式，严格模式耗资源
     *
     * @param isStrictMode
     */
    public void check(boolean isStrictMode) {
        this.isStrictMode = isStrictMode;
        this.isStopCheck = false;
        this.executionTime = 0;
        if (checkThread != null && checkThread.isAlive()) return;
        checkThread = new Thread(new CheckRunnable(), TAG);
        checkThread.start();
    }

    private boolean loopCheck(boolean isStrictMode) {
        if (!Utils.isBuildConfigDebug(application)) {//非调试模式
            Utils.log(" release debuggable run  exit ");
            if (isDebuggable(application)) {//开启了debug
                return true;
            }
            boolean isDebug = Debug.isDebuggerConnected();//被调试器连接了
            Utils.log(" debugger connected  exit " + isDebug);
            if (isDebug) {
                return true;
            }
        }
        if (!isStrictMode) return false;

        boolean portUsing = isLocalPortUsing(CHECK_DEFAULT_PORT);
        Utils.log(" portUsing  using exit " + portUsing);
        if (portUsing) {
            return true;
        }

        boolean underTraced = isUnderTraced();
        Utils.log(" traced exit underTraced : " + underTraced);
        if (underTraced) {
            return true;
        }
        return false;
    }

    private void killSelf() {
//        ActivityUtils.finishAllActivities();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean isDebuggable(Application app) {
        return 0 != (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }


    /**
     * pid调试检测
     */
    private boolean isUnderTraced() {
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/status"));
            int tracerPid = 0;
            for (; ; ) {
                String str = localBufferedReader.readLine();
                if (TextUtils.isEmpty(str) || !str.contains("TracerPid")) continue;
                String tracerPidStr = str.substring(str.indexOf(":") + 1).trim();
                if (TextUtils.isEmpty(tracerPidStr)) break;
                tracerPid = Integer.valueOf(tracerPidStr);
                break;
            }
            Utils.log(" tracerPid " + tracerPid);
            localBufferedReader.close();
            return tracerPid > 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 本地调试端口检测
     */
    public boolean isLocalPortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return flag;
    }

    public boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return flag;
    }


    private final class CheckRunnable implements Runnable {

        @Override
        public void run() {
            while (!isStopCheck && executionTime <= (isStrictMode ? MODE_STRICT_TIME : MODE_NORMAL_TIME)) {
                try {
                    boolean isTrack = loopCheck(isStrictMode);
                    Utils.log(" check run is track :  " + isTrack + " thread id : " + Thread.currentThread().getId() + " execution time : " + executionTime);
                    if (isTrack) {
                        Log.e(SafeCheckService.getTAG(), "应用被调试，强制退出！");
                        killSelf();
                        break;
                    }
                    long sleepTime = isStrictMode ? MODE_STRICT_INTERVAL_TIME : MODE_NORMAL_INTERVAL_TIME;
                    SystemClock.sleep(sleepTime);
                    executionTime += sleepTime;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onDestroy() {
        isStopCheck = true;
        executionTime = 0;
    }
}

