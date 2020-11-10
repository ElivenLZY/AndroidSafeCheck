package com.lzy.safecheck;

/**
 * Create by liuzhiyou on 2020/8/6 17:25
 * Email：1130294881@qq.com
 */
public class TaskEvent {
    public String tag;
    public boolean result; //true: 检查通过 false：不通过，需要手动处理

    public TaskEvent(String tag, boolean result) {
        this.tag = tag;
        this.result = result;
    }
}
