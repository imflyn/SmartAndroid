package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.flyn.smartandroid.app.Application;


/**
 * Created by flyn on 2014-11-02.
 */
public abstract class UIPresenter
{
    protected Activity mContext;
    protected Handler  mHandler;
    protected View     mRootView;
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

    protected void onDestroy()
    {

    }

    protected void onSaveInstanceState(Bundle outState)
    {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
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

    public String getString(int resId, Object... args)
    {
        return mContext.getString(resId, args);
    }

    public Drawable getDrawable(int resId)
    {
        return mContext.getResources().getDrawable(resId);
    }

    public Resources getResources()
    {
        return mContext.getResources();
    }

    public int getColor(int color)
    {
        return mContext.getResources().getColor(color);
    }

    public ActionBar getSupportActionBar()
    {
        if (mContext instanceof ActionBarActivity)
        {
            return ((ActionBarActivity) mContext).getSupportActionBar();
        }
        return null;
    }

    public FragmentManager getSupportFragmentManager()
    {
        if (mContext instanceof FragmentActivity)
        {
            return ((FragmentActivity) mContext).getSupportFragmentManager();
        }
        return null;
    }


}
