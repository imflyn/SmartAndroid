package com.flyn.smartandroid.ui.smart;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by flyn on 2014-11-02.
 */
public class UIHelper
{

    public DialogFragment mLoadingDialogFragment;
    public DialogFragment mCustomDialogFragment;


    public void onCreate(Bundle bundle)
    {

    }

    public void onStart()
    {

    }

    public void onResume()
    {

    }

    public void onPause()
    {

    }

    public void onStop()
    {

    }

    public void onDestroy()
    {
        hiddenLoadingDialog();
        hiddenDialogFragment();
    }

    public void onSaveInstanceState(Bundle outState)
    {
    }

    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
    }


    public void showLoadingDialog()
    {

    }

    public void showLoadingDialog(Class<? extends DialogFragment> clz)
    {

    }

    public void hiddenLoadingDialog()
    {
        if (mCustomDialogFragment != null)
        {
            mCustomDialogFragment.dismiss();
        }
    }

    public void showDialogFragment(Class<? extends DialogFragment> clz)
    {

    }

    public void hiddenDialogFragment()
    {
        if (mLoadingDialogFragment != null)
        {
            mLoadingDialogFragment.dismiss();
        }
    }

    public void setDefaultLoadingDialogFragment(Class<? extends DialogFragment> clz)
    {

    }
}
