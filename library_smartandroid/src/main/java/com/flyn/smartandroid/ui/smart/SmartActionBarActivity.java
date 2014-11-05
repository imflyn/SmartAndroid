package com.flyn.smartandroid.ui.smart;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;

import com.flyn.smartandroid.app.Application;
import com.flyn.smartandroid.app.manager.ActivityManager;

import java.lang.reflect.Constructor;

public abstract class SmartActionBarActivity extends ActionBarActivity
{
    protected Activity mContext;
    protected Handler mHandler;
    protected UIPresenter uiPresenter;
    protected UIHelper mUIHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

    private void setContentView()
    {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        View rootView = getLayoutInflater().inflate(layoutId(), viewGroup, false);
        rootView.setId(((Object) this).hashCode());
        setContentView(rootView);
    }

    private void initUIPresenter()
    {
        Class<? extends UIPresenter> clz = getUIPresenterClz();
        try
        {
            Constructor<? extends UIPresenter> constructor = clz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            uiPresenter = constructor.newInstance(findViewById(((Object) this).hashCode()));
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
        mUIHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        this.uiPresenter.onRestoreInstanceState(savedInstanceState);
        mUIHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.uiPresenter.onStart();
        mUIHelper.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.uiPresenter.onResume();
        mUIHelper.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        this.uiPresenter.onPause();
        mUIHelper.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.uiPresenter.onStop();
        mUIHelper.onStop();
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
        this.uiPresenter.onDestory();
        mUIHelper.onDestory();
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

    protected Class<? extends DialogFragment> defaultLoadingDialog()
    {
        return null;
    }


    public static class MyTabListener<T extends Fragment> implements TabListener
    {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public MyTabListener(Activity activity, String tag, Class<T> clz)
        {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        /* The following are each of the ActionBar.TabListener callbacks */
        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft)
        {
            // Check if the fragment is already initialized
            if (mFragment == null)
            {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else
            {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft)
        {
            if (mFragment != null)
            {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft)
        {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
