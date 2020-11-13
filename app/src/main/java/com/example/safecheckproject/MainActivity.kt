package com.example.safecheckproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lzy.safecheck.ISafeCheck
import com.lzy.safecheck.SafeCheckService
import com.lzy.safecheck.TaskEvent
import com.lzy.safecheck.TaskQueue
import com.lzy.safecheck.listener.OnTaskListener
import com.lzy.safecheck.task.*

/**
 *
 * Create by liuzhiyou on 2020/8/6 5:24 PM
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SafeCheckService.setLogEnabled(true)

        val taskQueue = TaskQueue()
        taskQueue.addTask(DebugCheckTask(this))
        taskQueue.addTask(PageHackCheckTask(this))
        taskQueue.addTask(SignCheckTask(this, RIGHT_CER))
        taskQueue.addTask(RootCheckTask(this))
        taskQueue.addTask(SimulatorCheckTask(this))
        taskQueue.addTask(NetProxyCheckTask(this, false))
        SafeCheckService.startCheck(taskQueue, object : OnTaskListener {
            override fun onStart() {
                Log.d(TAG, "安全检查开始")
            }

            override fun onTaskEvent(iSafeCheck: ISafeCheck?, taskEvent: TaskEvent?) {
                Log.d(TAG, "${taskEvent?.tag} isCheckPass is ${taskEvent?.isCheckPass}")
            }

            override fun onComplete() {
                Log.d(TAG, "安全检查完成")
                Toast.makeText(this@MainActivity, "安全检查完成", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        const val TAG = "lzy"
        const val RIGHT_CER = "B4:22:FA:A3:21:D9:7B:CB:BF:56:CB:63:65:07:3F:4C:85:5F:AA:D0"
    }

}