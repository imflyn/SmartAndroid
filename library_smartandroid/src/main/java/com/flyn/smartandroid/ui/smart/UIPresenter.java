package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyn.smartandroid.app.Application;


/**
 * Created by flyn on 2014-11-02.
 */
public abstract class UIPresenter
{
    protected UIHelper mUIHelper;
    protected Handler mHandler;
    private Activity mActivity;
    private View mRootView;

    protected UIPresenter(Activity activity)
    {
        this.mActivity = activity;
        mUIHelper = new UIHelper();
        mUIHelper.setDefaultLoadingDialogFragment(defaultLoadingDialog());
        mHandler = Application.getInstance().getHandler();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        mUIHelper.onCreate(savedInstanceState);
    }

    public void onStart()
    {
        mUIHelper.onStart();
    }

    public void onResume()
    {
        mUIHelper.onResume();
    }

    public void onPause()
    {
        mUIHelper.onPause();
    }

    public void onStop()
    {
        mUIHelper.onStop();
    }

    public void onDestory()
    {
        mUIHelper.onDestory();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        mUIHelper.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        mUIHelper.onRestoreInstanceState(savedInstanceState);
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        mUIHelper.onConfigurationChanged(newConfig);
    }

    protected View initLayout()
    {
        if (null == mRootView)
        {
            mRootView = LayoutInflater.from(mActivity).inflate(layoutId(), null);
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return mRootView;
    }

    protected View initLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (null == mRootView)
        {
            mRootView = inflater.inflate(layoutId(), container, false);
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return mRootView;
    }

    protected abstract int layoutId();

    protected abstract void findViews();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();


    protected Class<? extends DialogFragment> defaultLoadingDialog()
    {
        return null;
    }
}
