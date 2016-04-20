package com.flyn.smartandroid.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyn.smartandroid.app.Application;

import java.lang.reflect.Constructor;

public abstract class SmartFragment extends Fragment
{


    protected Activity    mContext;
    protected Handler     mHandler;
    protected UIPresenter uiPresenter;
    protected UIHelper    mUIHelper;
    private   View        mRootView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (null == mRootView)
        {
            mRootView = inflater.inflate(layoutId(), container, false);
            initUIPresenter();
            this.uiPresenter.onCreate(savedInstanceState);
            this.uiPresenter.setUiHelper(mUIHelper);
        }
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mHandler = Application.getInstance().getHandler();
        mUIHelper = new UIHelper();
        mUIHelper.setDefaultLoadingDialogFragment(defaultLoadingDialog());
        mUIHelper.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.uiPresenter.onCreate(savedInstanceState);
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
            Constructor<? extends UIPresenter> constructor = clz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            uiPresenter = constructor.newInstance(mRootView);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        this.uiPresenter.onSaveInstanceState(outState);
        mUIHelper.onSaveInstanceState(outState);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        this.uiPresenter.onStart();
        mUIHelper.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.uiPresenter.onResume();
        mUIHelper.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.uiPresenter.onPause();
        mUIHelper.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.uiPresenter.onStop();
        mUIHelper.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.uiPresenter.onDestroy();
        mUIHelper.onDestroy();

        if (null != mRootView)
        {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        this.uiPresenter.onConfigurationChanged(newConfig);
        mUIHelper.onConfigurationChanged(newConfig);
    }

    protected abstract int layoutId();

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

    protected abstract UIPresenter getUiPresenter();

    protected Class<? extends DialogFragment> defaultLoadingDialog()
    {
        return null;
    }

    public void refresh()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commitAllowingStateLoss();
    }


}
