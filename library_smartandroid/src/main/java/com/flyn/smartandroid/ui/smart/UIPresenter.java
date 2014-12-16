package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.flyn.smartandroid.app.Application;


/**
 * Created by flyn on 2014-11-02.
 */
public abstract class UIPresenter
{
    protected Activity mContext;
    protected Handler mHandler;
    protected View mRootView;
    protected UIHelper uiHelper;

    protected UIPresenter(View rootView)
    {
        this.mContext = (Activity) rootView.getContext();
        this.mRootView = rootView;
        this.mHandler = Application.getInstance().getHandler();
    }

    protected void onCreate(Bundle savedInstanceState)
    {

    }

    protected void onStart()
    {

    }

    protected void onResume()
    {

    }

    protected void onPause()
    {

    }

    protected void onStop()
    {

    }

    protected void onDestory()
    {

    }

    protected void onSaveInstanceState(Bundle outState)
    {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
    }

    public void onConfigurationChanged(Configuration newConfig)
    {

    }

    protected void setUiHelper(UIHelper uiHelper)
    {
        this.uiHelper = uiHelper;
    }

    protected abstract void findViews();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();


    public String getString(int resId)
    {
        return mContext.getString(resId);
    }

    public Drawable getDrawable(int resId)
    {
        return mContext.getResources().getDrawable(resId);
    }

    public ActionBar getSupportActionBar()
    {
        if (mContext instanceof ActionBarActivity)
        {
            return ((ActionBarActivity) mContext).getSupportActionBar();
        }
        return null;
    }
}
