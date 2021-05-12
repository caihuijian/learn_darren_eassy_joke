package com.example.processgaurd;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * Created by hjcai on 2021/4/30.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class MyJobService extends JobService {
    private static final int JOB_WAKE_UP_ID = 0x11;
    JobScheduler jobScheduler;
    int jobId;

    // 服务启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("hjcai", "onStartCommand: ");
        JobInfo.Builder jobBuilder = new JobInfo.Builder(
                JOB_WAKE_UP_ID, new ComponentName(this, MyJobService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobBuilder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            jobBuilder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            jobBuilder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            jobBuilder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
        } else {
            // 开启一个轮寻 每隔30秒查询
            jobBuilder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
        }

        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobBuilder.setPersisted(true);  // 设置设备重启时，执行该任务
        jobId = jobScheduler.schedule(jobBuilder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // 轮询的执行体
        // 看MainService有没有被杀死
        // 如果杀死了就重新启动
        Log.e("hjcai", "onStartJob: ");
        boolean messageServiceAlive = serviceAlive(GuardService.class.getName());
        if (!messageServiceAlive) {
            startForegroundService(new Intent(this, GuardService.class));
        }

        boolean messageServiceAlive2 = serviceAlive(MainService.class.getName());
        if (!messageServiceAlive2) {
            startForegroundService(new Intent(this, MainService.class));
        }

        jobFinished(params, false);
        // jobScheduler.cancel(jobId); //取消指定定时任务
        // jobScheduler.cancelAll(); //取消所有指定定时任务

        // Return true from this method if your job needs to continue running,the job remains active until you call jobFinished(android.app.job.JobParameters, boolean)
        return true;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有在运行
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        Log.e("hjcai", "serviceAlive: isWork " + isWork);
        return isWork;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
