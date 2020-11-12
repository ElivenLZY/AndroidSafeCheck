package com.lzy.safecheck.listener;

import com.lzy.safecheck.ISafeCheck;
import com.lzy.safecheck.TaskEvent;

/**
 * Create by liuzhiyou on 2020/8/6 17:24
 * Email：1130294881@qq.com
 */
public interface OnTaskListener {
    void onStart();

    void onTaskEvent(ISafeCheck iSafeCheck, TaskEvent taskEvent);

    void onComplete();
}
