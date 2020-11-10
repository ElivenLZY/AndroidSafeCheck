package com.lzy.safecheck.task;

import com.lzy.safecheck.listener.OnTaskEventListener;

public interface ICheckTask {

    /**
     * 执行任务
     *
     **/
    void execute(OnTaskEventListener onTaskEventListener);

    String getTag();
}
