package com.smartandroid.ui;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

public abstract class BaseController<D extends ControllerListener>
{

    public static final String NAME = "controller.";
    public static final String SUFFIX = "Controller";
    protected static Handler mHandler = new Handler(Looper.getMainLooper());
    private static ArrayList<ControllerListener> controllerListenersList = new ArrayList<ControllerListener>(5);
    protected D mContext;

    public BaseController(D context)
    {

        this.mContext = context;
    }

    public static void post(final BaseEvent baseEvent)
    {
        mHandler.post(new Runnable()
        {

            @Override
            public void run()
            {
                for (int i = 0, size = controllerListenersList.size(); i < size; i++)
                {
                    controllerListenersList.get(i).onEvent(baseEvent);
                }
            }
        });
    }

    public void onCreate()
    {
        controllerListenersList.add(mContext);
    }

    public void onDestory()
    {
        controllerListenersList.remove(mContext);
    }

    public void onStart()
    {

    }

    public void onResume()
    {

    }

    public void onStop()
    {

    }

    public void onPause()
    {

    }
}
