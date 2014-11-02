package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyn.smartandroid.app.Application;

import java.lang.reflect.Constructor;

public abstract class SmartFragment extends Fragment
{


    protected Activity mContext;
    protected Handler mHandler;
    protected UIPresenter uiPresenter;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return uiPresenter.initLayout(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = Application.getInstance().getHandler();
        mContext = getActivity();
        initUIPresenter();
        if (null != uiPresenter)
        {
            this.uiPresenter.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        findViews();
        setListener();
        initView(savedInstanceState);
    }

    private void initUIPresenter()
    {
        if (uiPresenter != null)
        {
            return;
        }

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
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (null != uiPresenter)
        {
            this.uiPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (null != uiPresenter)
        {
            this.uiPresenter.onStop();
        }
    }

    @Override
    public void onDestroy()
    {
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

    public void refresh()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commitAllowingStateLoss();
    }


}
