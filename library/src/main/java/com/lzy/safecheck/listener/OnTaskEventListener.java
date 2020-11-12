package com.lzy.safecheck.listener;

import com.lzy.safecheck.TaskEvent;

/**
 * Create by liuzhiyou on 2020/11/6 18:23
 * Email：1130294881@qq.com
 */
public interface OnTaskEventListener {
    /**
     * @param taskEvent     当前任务的事件信息
     * @param callTaskEvent 是否调用{@link OnTaskListener} 的 onTaskEvent 方法
     */
    void onEvent(TaskEvent taskEvent, boolean callTaskEvent);
}
