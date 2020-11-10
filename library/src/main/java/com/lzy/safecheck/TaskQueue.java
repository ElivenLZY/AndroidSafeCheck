package com.lzy.safecheck;

import com.lzy.safecheck.task.ICheckTask;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Create by liuzhiyou on 2020/11/9 18:06
 * Email：1130294881@qq.com
 */
public class TaskQueue {

    private Queue<ICheckTask> mQueue = new ArrayDeque<>();

    public void addTask(ICheckTask task) {
        if (task == null) return;
        mQueue.add(task);
    }

    public ICheckTask pollTask() {
        return mQueue.poll();
    }
}
