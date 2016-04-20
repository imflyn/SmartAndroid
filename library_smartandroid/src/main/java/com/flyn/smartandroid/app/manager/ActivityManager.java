package com.flyn.smartandroid.app.manager;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {

    private static ActivityManager instance = new ActivityManager();
    private Stack<Activity> mActivityStack;

    private ActivityManager() {
        super();
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        return mActivityStack;
    }

    /**
     * push ctivity to stack top
     */
    public void addActivity(Activity activity) {
        getActivityStack().add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = getActivityStack().lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = getActivityStack().lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            removeActivity(activity);
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
        activity = null;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : getActivityStack()) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public Activity getActivity(Class<?> cls) {
        for (Activity activity : getActivityStack()) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return new Activity();
    }

    /**
     * 获取所有运行中的activity
     *
     * @return
     */
    public Stack<Activity> getAllActivity() {
        return getActivityStack();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Stack<Activity> stack = getActivityStack();

        for (int i = 0, size = stack.size(); i < size; i++) {
            if (null != stack.get(i)) {
                stack.get(i).finish();
            }
        }
        stack.clear();
    }

    public void clear() {
        finishAllActivity();
        getActivityStack().clear();
        instance = null;
    }

}
