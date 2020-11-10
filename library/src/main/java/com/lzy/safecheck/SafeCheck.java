package com.lzy.safecheck;

import com.lzy.safecheck.listener.OnTaskEventListener;
import com.lzy.safecheck.listener.OnTaskListener;
import com.lzy.safecheck.task.ICheckTask;

/**
 * 安全检查
 * Create by 2020/6/16 from liuzhiyou
 **/
public class SafeCheck implements ISafeCheck {

    private static final String TAG = SafeCheck.class.getSimpleName();

    private OnTaskListener mOnTaskListener;

    private TaskQueue mTaskQueue;

    private SafeCheck(Builder builder) {
        mOnTaskListener = builder.mOnTaskListener;
        mTaskQueue = builder.mTaskQueue;
    }

    public void startCheck() {
        mOnTaskListener.onStart();
        check();
    }

    @Override
    public void check() {
        final ICheckTask task = pollTask();
        if (task == null) {
            mOnTaskListener.onComplete();
            return;
        }

        task.execute(new OnTaskEventListener() {
            @Override
            public void OnTaskEvent(TaskEvent taskEvent) {
                mOnTaskListener.OnTaskEvent(SafeCheck.this, taskEvent);
                if (taskEvent.result) check();
            }
        });
    }

    private ICheckTask pollTask() {
        if (mTaskQueue == null) return null;
        return mTaskQueue.pollTask();
    }

    public static final class Builder {
        private TaskQueue mTaskQueue;
        private OnTaskListener mOnTaskListener;

        public Builder() {
        }

        public Builder setTaskQueue(TaskQueue taskQueue) {
            mTaskQueue = taskQueue;
            return this;
        }

        public Builder setOnTaskListener(OnTaskListener onTaskListener) {
            this.mOnTaskListener = onTaskListener;
            return this;
        }

        public SafeCheck build() {
            return new SafeCheck(this);
        }
    }


}
