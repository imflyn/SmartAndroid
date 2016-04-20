package com.flyn.smartandroid.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.flyn.smartandroid.app.Application;
import com.flyn.smartandroid.app.manager.ActivityManager;

import java.lang.reflect.Constructor;

public abstract class SmartActivity extends AppCompatActivity {

    protected Activity mContext;
    protected Handler mHandler;
    protected UIPresenter uiPresenter;
    protected UIHelper mUIHelper;
    protected View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        mContext = this;
        mHandler = Application.getInstance().getHandler();
        mUIHelper = new UIHelper();
        mUIHelper.setDefaultLoadingDialogFragment(defaultLoadingDialog());
        mUIHelper.onCreate(savedInstanceState);
        setContentView();
        initUIPresenter();
        this.uiPresenter.onCreate(savedInstanceState);
        this.uiPresenter.setUiHelper(mUIHelper);
        findViews();
        setListener();
        initView(savedInstanceState);

    }

    private void setContentView() {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        mRootView = getLayoutInflater().inflate(layoutId(), viewGroup, false);
        setContentView(mRootView);
    }

    private void initUIPresenter() {
        Class<? extends UIPresenter> clz = getUIPresenterClz();

        try {
            Constructor<? extends UIPresenter> constructor = clz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            uiPresenter = constructor.newInstance(mRootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.uiPresenter.onSaveInstanceState(outState);
        mUIHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.uiPresenter.onRestoreInstanceState(savedInstanceState);
        mUIHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.uiPresenter.onStart();
        mUIHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.uiPresenter.onResume();
        mUIHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.uiPresenter.onPause();
        mUIHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.uiPresenter.onStop();
        mUIHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
        this.uiPresenter.onDestroy();
        mUIHelper.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.uiPresenter.onConfigurationChanged(newConfig);
        mUIHelper.onConfigurationChanged(newConfig);
    }

    protected abstract int layoutId();

    protected void findViews() {
        uiPresenter.findViews();
    }

    protected void initView(Bundle savedInstanceState) {
        uiPresenter.initView(savedInstanceState);
    }

    protected void setListener() {
        uiPresenter.setListener();
    }

    protected abstract Class<? extends UIPresenter> getUIPresenterClz();

    protected abstract UIPresenter getUiPresenter();

    protected Class<? extends DialogFragment> defaultLoadingDialog() {
        return null;
    }
}
