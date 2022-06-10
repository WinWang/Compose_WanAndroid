package com.winwang.composewanandroid.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Stack;

/**
 * Activity状态追踪器
 * 1、解决传统在Activity基类中监听Activity生命周期的弊端
 * 2、此类在Application基类中注册自动监听Activity生命周期（一行代码即可）
 * 3、实时掌握当前应用是否在前台运行
 * 4、该类适用于API4.0++
 *
 * @author zhang.zheng
 * @version 2018-05-08
 */
@SuppressLint("NewApi")
public class ActivityTracker implements Application.ActivityLifecycleCallbacks {
    Context applicationContext;
    private Stack<Activity> mActivityStack = new Stack<>();
    private boolean mIsForeground;
    private int mActiveCount;
    private long timestamp;

    private static volatile ActivityTracker mActivityTracker;

    private ActivityTracker() {
    }

    public static ActivityTracker getInstance() {
        if (mActivityTracker == null) {
            synchronized (ActivityTracker.class) {
                if (mActivityTracker == null) {
                    mActivityTracker = new ActivityTracker();
                }
            }
        }
        return mActivityTracker;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void register(Application application) {
        applicationContext = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public boolean hasActivity(Class clazz) {
        for (Activity activity : mActivityStack) {
            if (clazz == activity.getClass()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 应用是否在前台
     */
    public boolean isForeground() {
        return mIsForeground;
    }

    /**
     * 获取activity栈个数
     */
    public int getStackSize() {
        return mActivityStack.size();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity == null)
            return;
        try {
            mActivityStack.push(activity);
        } catch (Exception e) {
            AppLogUtil.e("e:" + e);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mActiveCount == 0) {
            timestamp = System.currentTimeMillis();
        }
        mActiveCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mIsForeground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActiveCount--;
        if (mActiveCount == 0) {
            mIsForeground = false;
            timestamp = System.currentTimeMillis() - timestamp;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == null)
            return;
        try {
            mActivityStack.remove(activity);
        } catch (Exception e) {
            AppLogUtil.e("e:" + e);
        }
    }

    /**
     * 画中画页面都是singleTask
     * 当一个画中画的activity位于栈顶，并且处于画中画模式，此时打开新的activity时，不能使用当前画中画的context启动新页面。否则新开启的页面会进入到画中画模式。
     */
    public Activity getTrackTop() {
        if (mActivityStack.size() > 0) {
            for (int i = mActivityStack.size() - 1; i >= 0; i--) {
                Activity activity = mActivityStack.elementAt(i);
//                if (!SuperPlayerManager.INSTANCE.isPictureInPictureActivity(activity)) {
//                    return activity;
//                }
            }
        }
        return null;
    }

}
