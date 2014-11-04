package com.flyn.smartandroid.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyn.smartandroid.R;
import com.flyn.smartandroid.util.L;

/**
 * Created by flyn on 2014-11-04.
 */
public class MaterialDialog extends DialogFragment
{

    public static MaterialDialog newInstance()
    {
        MaterialDialog frag = new MaterialDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        L.i("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        L.i("onCreateView");
        View rootView = inflater.inflate(R.layout.dialog_material, container, false);


        return rootView;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        L.i("onCreateDialog");
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);


        return dialog;
    }

    public void show(FragmentManager fragmentManager)
    {
        show(fragmentManager, MaterialDialog.class.getSimpleName());

    }
}
