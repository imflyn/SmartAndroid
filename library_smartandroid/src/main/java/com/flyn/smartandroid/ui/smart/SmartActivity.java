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
        this.uiPresenter.onCreate(savedInstanceState);
        initLayout();
        findViews();
        setListener();
        initView(savedInstanceState);

    }

    private void initUIPresenter()
    {
        Class<? extends UIPresenter> clz = getUIPresenterClz();

        try
        {
            Constructor<? extends UIPresenter> constructor = clz.getDeclaredConstructor(Activity.class);
            constructor.setAccessible(true);
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
        this.uiPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        this.uiPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.uiPresenter.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.uiPresenter.onStop();
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
        this.uiPresenter.onStop();
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
        this.uiPresenter.onDestory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        this.uiPresenter.onConfigurationChanged(newConfig);
    }

    protected void initLayout()
    {
        setContentView(uiPresenter.initLayout());
    }

    protected void findViews()
    {
        uiPresenter.findViews();
    }

    protected void initView(Bundle savedInstanceState)
    {
        uiPresenter.initView(savedInstanceState);
    }

    protected void setListener()
    {
        uiPresenter.setListener();
    }

    protected abstract Class<? extends UIPresenter> getUIPresenterClz();

}
