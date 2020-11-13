[![](https://jitpack.io/v/elivenlzy/androidsafecheck.svg)](https://jitpack.io/#elivenlzy/androidsafecheck)

> 目录

* 功能列表
* 使用方法
* 功能介绍

> 功能列表：

1. 防调试检测
2. 网络代理检测
3. Root检测
4. 模拟器检测
5. 正版签名检测
6. 界面劫持检测

> 使用方法

**一，一键快速集成，在APP启动页加上如下代码即可获得本库提供的默认的全部功能：**

```
        
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

            override fun OnTaskEvent(iSafeCheck: ISafeCheck?, taskEvent: TaskEvent?) {
                Log.d(TAG, "onTaskEvent: ${taskEvent?.tag} result is ${taskEvent?.result}")
            }

            override fun onComplete() {
                Toast.makeText(this@MainActivity, "安全检查完成", Toast.LENGTH_SHORT).show()
            }

        })   
     
```

**二，功能定制，基于上面的快速集成，最快只需两步:**

1. 新建一个task类，继承 AbstractCheckTask ，重写check方法，实现检测逻辑。
2. 将此task类添加到TaskQueue队列中即可

**检测不通过的话，默认会弹出一个检测不通过的警告弹窗，如果需要自定义处理检测不通过的操作，有两种办法：**

1. 重写 AbstractCheckTask 中的 interruptCheck 方法
2. 在启动检查时传入的 OnTaskListener 的 OnTaskEvent 方法中，根据
   taskEvent.result 和 taskEvent.tag
   来判断处理不通过的task，然后根据处理结果来是否调用 iSafeCheck.check
   方法继续执行后面的检查task

> 功能介绍

**1. 防调试检测**

在应用启动时会启动一个定时任务，每1秒检查一次，持续检查5分钟；定时任务启动成功即认为该任务通过。

判定为被调试的几种触发条件：
* 非调试模式下debug标识为true
* 非调试模式下被外部调试器连接
* 本地的23946端口被占用
* 本地存在TracerPid的调试文件

触发以上任何一种条件应用进程都会被强制杀掉

**2. 网络代理检测**

在应用启动时会检查一次当前网络是否被代理，如果被网络代理则检查失败，会弹窗阻塞后续任务执行。

开发者也可以在每次应用访问网络前都调用一次 Utils.isWifiProxy 方法执行一次检查。

**3. Root检测**

在应用启动时会检查一次当前设备是否被Root过，如果被Root，会弹窗阻塞后续任务执行。

**4. 模拟器检测**

在应用启动时会检查一次当前设备是否在模拟器上运行，如果是，会弹窗阻塞后续任务执行。

**5. 正版签名检测**

在应用启动时会检查一次当前应用的签名是否正版签名一致，如果不一致，会弹窗阻塞后续任务执行。

需要传入正版签名的sha1，可以使用命令：keytool -v -list -keystore <签名文件路径> 来获取

**6. 界面劫持检测**

在应用启动时会注册一个应用前后台状态的监听，如果应用最上层的页面没有在前台显示，会弹窗一个退到后台的提示；监听注册成功成功即认为该任务通过。

---------------------

#### 持续完善中...

