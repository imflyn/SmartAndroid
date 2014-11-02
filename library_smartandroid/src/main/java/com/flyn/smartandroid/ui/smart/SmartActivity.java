package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.flyn.smartandroid.app.Application;
import com.flyn.smartandroid.app.manager.ActivityManager;

import java.lang.reflect.Constructor;

public abstract class SmartActivity extends FragmentActivity
{

    protected Activity mContext;
    protected Handler mHandler;
    protected UIPresenter uiPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        mHandler = Application.getInstance().getHandler();
        mContext = this;
        initUIPresenter();
        if (null != uiPresenter)
        {
            this.uiPresenter.onCreate(savedInstanceState);
        }
        findViews();
        setListener();
        initView(savedInstanceState);

    }

    private void initUIPresenter()
    {
        Class<? extends UIPresenter> clz = getUIPresenterClz();

        try
        {
            Constructor<? extends UIPresenter> constructor = clz.getConstructor(Activity.class);
            uiPresenter = constructor.newInstance(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (null != uiPresenter)
        {
            this.uiPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != uiPresenter)
        {
            this.uiPresenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
        if (null != uiPresenter)
        {
            this.uiPresenter.onDestory();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (null != uiPresenter)
        {
            this.uiPresenter.onConfigurationChanged(newConfig);
        }
    }

    protected void initLayout()
    {
        if (null != uiPresenter)
        {
            setContentView(uiPresenter.initLayout());
        }
    }

    protected void findViews()
    {
        if (null != uiPresenter)
        {
            uiPresenter.findViews();
        }
    }

    protected void initView(Bundle savedInstanceState)
    {
        if (null != uiPresenter)
        {
            uiPresenter.initView(savedInstanceState);
        }
    }

    protected void setListener()
    {
        if (null != uiPresenter)
        {
            uiPresenter.setListener();
        }
    }

    protected abstract Class<? extends UIPresenter> getUIPresenterClz();

}
