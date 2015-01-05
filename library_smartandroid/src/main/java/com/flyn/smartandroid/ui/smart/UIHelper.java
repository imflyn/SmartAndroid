package com.flyn.smartandroid.ui.smart;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by flyn on 2014-11-02.
 */
public class UIHelper
{

    public DialogFragment mLoadingDialogFragment;
    public DialogFragment mCustomDialogFragment;
    public Dialog         mDialog;


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
        dismissDialog();
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
        if (mLoadingDialogFragment != null)
        {
            mLoadingDialogFragment.dismiss();
        }
    }

    public void cancelLoadingDialog()
    {
        if (mLoadingDialogFragment != null && mLoadingDialogFragment.getDialog() != null)
        {
            mLoadingDialogFragment.getDialog().cancel();
        }
    }

    public void showDialogFragment(Class<? extends DialogFragment> clz)
    {

    }

    public void showDialogFragment(FragmentManager fragmentManager)
    {
        if (mCustomDialogFragment != null)
        {
            mCustomDialogFragment.show(fragmentManager, mCustomDialogFragment.getClass().getSimpleName());
        }
    }

    public void hiddenDialogFragment()
    {
        if (mCustomDialogFragment != null)
        {
            mCustomDialogFragment.dismiss();
        }
    }

    public void cancelDialogFragment()
    {
        if (mCustomDialogFragment != null && mCustomDialogFragment.getDialog() != null)
        {
            mCustomDialogFragment.getDialog().cancel();
        }
    }

    public void setDefaultLoadingDialogFragment(Class<? extends DialogFragment> clz)
    {

    }

    public void showDialog()
    {
        if (mDialog != null && !mDialog.isShowing())
        {
            mDialog.show();
        }
    }

    public void dismissDialog()
    {
        if (mDialog != null && mDialog.isShowing())
        {
            mDialog.dismiss();
        }
    }
}
