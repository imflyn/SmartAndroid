package com.flyn.smartandroid.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyn.smartandroid.R;
import com.flyn.smartandroid.app.Application;

/**
 * Created by flyn on 2014-11-04.
 */
public class MaterialDialog extends DialogFragment
{

    private String                   title;
    private String                   message;
    private String[]                 itemArray;
    private String[]                 buttonArray;
    private OnClickListener          onClickListener;
    private Dialog.OnCancelListener  onCancelListener;
    private Dialog.OnDismissListener onDismissListener;

    private TextView     tv_title;
    private TextView     tv_message;
    private Button       btn_accept;
    private Button       btn_cancel;
    private LinearLayout ll_buttons;
    private LinearLayout ll_items;

    private static MaterialDialog newInstance()
    {
        MaterialDialog frag = new MaterialDialog();
        Bundle bundle = new Bundle();
        frag.setArguments(bundle);
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState)
        {
            title = savedInstanceState.getString("title");
            message = savedInstanceState.getString("message");
            itemArray = savedInstanceState.getStringArray("itemArray");
            buttonArray = savedInstanceState.getStringArray("buttonArray");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dialog_material, container, false);

        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_message = (TextView) rootView.findViewById(R.id.tv_message);
        btn_accept = (Button) rootView.findViewById(R.id.btn_accept);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        ll_buttons = (LinearLayout) rootView.findViewById(R.id.ll_buttons);
        ll_items = (LinearLayout) rootView.findViewById(R.id.ll_items);

        tv_message.setMovementMethod(new ScrollingMovementMethod());
        setTitle(title);
        setMessage(message);

        btn_accept.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(final View v)
            {
                Application.getInstance().getHandler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dismiss();

                        if (null != onClickListener)
                        {
                            onClickListener.onDialogPositiveClick(MaterialDialog.this);
                        }
                    }
                }, 500);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(final View v)
            {
                Application.getInstance().getHandler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dismiss();

                        if (null != onClickListener)
                        {

                            onClickListener.onDialogNegativeClick(MaterialDialog.this);
                        }
                    }
                }, 500);
            }
        });

        setTitle(title);
        setMessage(message);
        setButtons(itemArray, buttonArray);
        setItems(itemArray);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            tv_message.setMaxHeight(Resources.getSystem().getDisplayMetrics().heightPixels * 1 / 3);
        } else
        {
            tv_message.setMaxHeight(Resources.getSystem().getDisplayMetrics().heightPixels * 2 / 5);
        }

        setCancelable(true);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setOnCancelListener(this.onCancelListener);
        getDialog().setOnDismissListener(this.onDismissListener);
        setAllowEnterTransitionOverlap(true);
        setAllowReturnTransitionOverlap(true);

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);

        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putStringArray("itemArray", itemArray);
        bundle.putStringArray("buttonArray", buttonArray);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (tv_message == null)
        {
            return;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            tv_message.setMaxHeight(Resources.getSystem().getDisplayMetrics().heightPixels * 1 / 3);
        } else
        {
            tv_message.setMaxHeight(Resources.getSystem().getDisplayMetrics().heightPixels * 2 / 5);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if (null != onCancelListener)
        {
            onCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (null != onDismissListener)
        {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public MaterialDialog show(FragmentManager fragmentManager)
    {
        show(fragmentManager, MaterialDialog.class.getSimpleName());
        return this;
    }

    public void setTitle(String title)
    {

        this.title = title;
        if (tv_title == null)
        {
            return;
        }
        if (TextUtils.isEmpty(title))
        {

            this.tv_title.setVisibility(View.GONE);
        } else
        {
            this.tv_title.setText(title);
            this.tv_title.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(String message)
    {

        this.message = message;

        if (tv_message == null)
        {
            return;
        }

        if (TextUtils.isEmpty(message))
        {

            this.tv_message.setVisibility(View.GONE);
        } else
        {
            this.tv_message.setText(message);
            this.tv_message.setVisibility(View.VISIBLE);
        }
    }

    private void setButtons(String[] itemArray, String[] buttonArray)
    {

        this.buttonArray = buttonArray;

        if (this.ll_buttons == null || this.btn_cancel == null || this.btn_accept == null)
        {
            return;
        }

        if (null != itemArray && itemArray.length > 0)
        {
            this.ll_buttons.setVisibility(View.GONE);
        } else if (null != buttonArray)
        {
            if (buttonArray.length > 1)
            {
                this.btn_cancel.setText(buttonArray[1]);
            } else
            {
                this.btn_cancel.setVisibility(View.GONE);
            }
            this.btn_accept.setText(buttonArray[0]);
        }
    }

    private void setItems(String[] itemArray)
    {

        this.itemArray = itemArray;

        if (null == itemArray || itemArray.length == 0 || this.ll_items == null)
        {
            return;
        }

        this.ll_items.setVisibility(View.VISIBLE);

        for (int i = 0; i < itemArray.length; i++)
        {
            final int position = i;

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_material_item, this.ll_items, false);

            Button button = (Button) view.findViewById(R.id.btn_material_item);
            button.setText(itemArray[i]);

            button.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {

                    Application.getInstance().getHandler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (null != onClickListener)
                            {
                                onClickListener.onDialogItemClick(MaterialDialog.this, position);
                            }
                        }
                    }, 500);
                }
            });

            this.ll_items.addView(view);
        }
    }

    public void setOnDialogClickListener(OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }


    public void setOnCancelListener(Dialog.OnCancelListener onCancelListener)
    {
        this.onCancelListener = onCancelListener;
    }

    public void setOnDismissListener(Dialog.OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
    }

    public interface OnClickListener
    {

        public void onDialogItemClick(DialogFragment dialogFragment, int position);

        public void onDialogPositiveClick(DialogFragment dialogFragment);

        public void onDialogNegativeClick(DialogFragment dialogFragment);

    }

    public static class Builder
    {
        private String   title;
        private String   message;
        private String[] itemArray;
        private String[] buttonArray;

        private OnClickListener          onClickListener;
        private Dialog.OnCancelListener  onCancelListener;
        private Dialog.OnDismissListener onDismissListener;

        public Builder setTitle(String title)
        {
            this.title = title;
            return this;
        }

        public Builder setTitle(int resId)
        {
            this.title = Application.getInstance().getString(resId);
            return this;
        }

        public Builder setMessage(String message)
        {
            this.message = message;
            return this;
        }

        public Builder setMessage(int resId)
        {
            this.message = Application.getInstance().getString(resId);
            return this;
        }


        public Builder setButtons(int[] buttonRes)
        {
            if (buttonRes.length > 2 || buttonRes.length == 0)
            {
                throw new IllegalArgumentException("ButtonRes's length can not be greater than 2 or length can not be zero");
            }

            this.buttonArray = new String[buttonRes.length];

            for (int i = 0; i < buttonRes.length; i++)
            {
                buttonArray[i] = Application.getInstance().getResources().getString(buttonRes[i]);
            }

            return this;
        }

        public Builder setButtons(String[] buttonArray)
        {
            if (buttonArray.length > 2 || buttonArray.length == 0)
            {
                throw new IllegalArgumentException("buttonArray length can not be greater than 2 length can not be zero");
            }

            System.arraycopy(buttonArray, 0, this.buttonArray, buttonArray.length - 1, buttonArray.length);

            return this;
        }

        public Builder setItems(int[] itemRes)
        {
            if (null == itemRes || itemRes.length == 0)
            {
                throw new IllegalArgumentException("Parameter itemRes can not be null and length can not be zero");
            }

            this.itemArray = new String[itemRes.length];

            for (int i = 0; i < itemRes.length; i++)
            {
                itemArray[i] = Application.getInstance().getResources().getString(itemRes[i]);
            }

            return this;

        }

        public Builder setItems(String[] itemArray)
        {
            if (null == itemArray || itemArray.length == 0)
            {
                throw new IllegalArgumentException("Parameter itemArray can not be null and length can not be zero");
            }

            System.arraycopy(itemArray, 0, this.itemArray, itemArray.length - 1, itemArray.length);

            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener)
        {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
        {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener)
        {
            this.onClickListener = onClickListener;
            return this;
        }

        public MaterialDialog create()
        {
            final MaterialDialog materialDialog = MaterialDialog.newInstance();

            materialDialog.setTitle(title);
            materialDialog.setMessage(message);
            materialDialog.setButtons(itemArray, buttonArray);
            materialDialog.setItems(itemArray);

            materialDialog.setOnDialogClickListener(onClickListener);
            materialDialog.setOnDismissListener(onDismissListener);
            materialDialog.setOnCancelListener(onCancelListener);

            return materialDialog;
        }

        public MaterialDialog show(FragmentManager fragmentManager)
        {

            return create().show(fragmentManager);
        }

    }
}
