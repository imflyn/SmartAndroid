package com.flyn.smartandroid.app;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.flyn.smartandroid.app.manager.AppManager;
import com.flyn.smartandroid.sharedpreferences.SharedPreferenceFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Application extends android.app.Application
{

    private static Application mContext;
    private volatile boolean mRunning = false;
    private ArrayList<AppManager> mAppManagerList = new ArrayList<AppManager>();
    private Handler backgroundHandler;
    private ExecutorService backgroundExecutor;

    public static Application getInstance()
    {
        if (mContext == null)
        {
            throw new IllegalStateException();
        }
        return mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
        init();
    }

    private void init()
    {
        this.backgroundHandler = new Handler(Looper.getMainLooper());
        this.backgroundExecutor = Executors.newCachedThreadPool(new ThreadFactory()
        {
            private AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(@NonNull Runnable runnable)
            {
                Thread thread = new Thread(runnable, "Background executor service #" + atomicInteger.getAndIncrement());
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });

    }

    public boolean isRunning()
    {
        return mRunning;
    }

    public void running()
    {
        this.mRunning = true;
    }

    public synchronized void addManager(AppManager appManager)
    {
        this.mAppManagerList.add(appManager);
    }

    public void clear()
    {
        for (AppManager aMAppManagerList : mAppManagerList)
        {
            aMAppManagerList.onClear();
        }
        SharedPreferenceFactory.clear();
    }

    public synchronized void close()
    {
        mRunning = false;
        clear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void runInBackground(final Runnable runnable)
    {
        backgroundExecutor.submit(runnable);
    }

    public Handler getHandler()
    {
        return backgroundHandler;
    }

    public void runOnUiThread(final Runnable runnable)
    {
        backgroundHandler.post(runnable);
    }

    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis)
    {
        backgroundHandler.postDelayed(runnable, delayMillis);
    }
}
