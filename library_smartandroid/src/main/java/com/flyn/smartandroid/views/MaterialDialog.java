package com.flyn.smartandroid.views;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.Arrays;

/**
 * Created by flyn on 2014-11-04.
 */
public class MaterialDialog extends DialogFragment {

	private String title;
	private String message;
	private String[] itemArray;
	private String[] buttonArray;
	private View.OnClickListener positiveBtnClickListener;
	private View.OnClickListener negativeBtnClickListener;
	private OnClickListener onItemClickListener;

	private TextView tv_title;
	private TextView tv_message;
	private Button btn_accpet;
	private Button btn_cancel;
	private LinearLayout ll_buttons;
	private LinearLayout ll_items;

	private static MaterialDialog newInstance() {
		MaterialDialog frag = new MaterialDialog();
		Bundle dundle = new Bundle();
		frag.setArguments(dundle);
		frag.setRetainInstance(true);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_material, container, false);

		tv_title = (TextView) rootView.findViewById(R.id.tv_title);
		tv_message = (TextView) rootView.findViewById(R.id.tv_message);
		btn_accpet = (Button) rootView.findViewById(R.id.btn_accpet);
		btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
		ll_buttons = (LinearLayout) rootView.findViewById(R.id.ll_buttons);
		ll_items = (LinearLayout) rootView.findViewById(R.id.ll_items);

		tv_message.setMovementMethod(new ScrollingMovementMethod());
		setTitle(title);
		setMessage(message);

		btn_accpet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						dismiss();

						if (null != positiveBtnClickListener) {
							positiveBtnClickListener.onClick(v);
						}
					}
				}, 500);
			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						dismiss();

						if (null != negativeBtnClickListener) {
							negativeBtnClickListener.onClick(v);
						}
					}
				}, 500);
			}
		});

		setTitle(title);
		setMessage(message);
		setButtons(itemArray, buttonArray);
		setItems(itemArray);

		setCancelable(true);

		setAllowEnterTransitionOverlap(true);
		setAllowReturnTransitionOverlap(true);

		return rootView;

	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onViewCreated(view, savedInstanceState);
	}

	public MaterialDialog show(FragmentManager fragmentManager) {
		show(fragmentManager, MaterialDialog.class.getSimpleName());

		return this;
	}

	public void setTitle(String title) {

		this.title = title;

		if (tv_title == null)
			return;

		if (TextUtils.isEmpty(title)) {

			this.tv_title.setVisibility(View.GONE);
		} else {
			this.tv_title.setText(title);
			this.tv_title.setVisibility(View.VISIBLE);
		}
	}

	public void setMessage(String message) {

		this.message = message;

		if (tv_message == null)
			return;

		if (TextUtils.isEmpty(message)) {

			this.tv_message.setVisibility(View.GONE);
		} else {
			this.tv_message.setText(message);
			this.tv_message.setVisibility(View.VISIBLE);
		}
	}

	public void setPositiveBtnClickListener(View.OnClickListener positiveBtnClickListener) {
		this.positiveBtnClickListener = positiveBtnClickListener;
	}

	public void setNegativeBtnClickListener(View.OnClickListener negativeBtnClickListener) {
		this.negativeBtnClickListener = negativeBtnClickListener;
	}

	private void setButtons(String[] itemArray, String[] buttonArray) {

		this.buttonArray = buttonArray;

		if (this.ll_buttons == null || this.btn_cancel == null || this.btn_accpet == null)
			return;

		if (null != itemArray && itemArray.length > 0) {
			this.ll_buttons.setVisibility(View.GONE);
		} else if (null != buttonArray) {
			if (buttonArray.length >= 1) {
				this.btn_cancel.setText(buttonArray[1]);
			} else {
				this.btn_cancel.setVisibility(View.GONE);
			}
			this.btn_accpet.setText(buttonArray[0]);
		}
	}

	private void setItems(String[] itemArray) {

		this.itemArray = itemArray;

		if (null == itemArray || itemArray.length == 0 || this.ll_items == null)
			return;

		this.ll_items.setVisibility(View.VISIBLE);

		for (int i = 0; i < itemArray.length; i++) {
			final int position = i;

			View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_material_item, this.ll_items, false);

			Button button = (Button) view.findViewById(R.id.btn_material_item);
			button.setText(itemArray[i]);

			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (null != onItemClickListener) {
								onItemClickListener.onClick(MaterialDialog.this, position);
							}
						}
					}, 500);
				}
			});

			this.ll_items.addView(view);
		}
	}

	private void setOnItemClickListener(OnClickListener onClickListener) {
		this.onItemClickListener = onClickListener;
	}

	public static class Builder {
		private String title;
		private String message;
		private String[] itemArray;
		private String[] buttonArray;

		private View.OnClickListener positiveBtnClickListener;
		private View.OnClickListener negativeBtnClickListener;
		private OnClickListener onItemClickListener;

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setPositiveBtnClickListener(View.OnClickListener positiveBtnClickListener) {
			this.positiveBtnClickListener = positiveBtnClickListener;
			return this;
		}

		public Builder setNegativeBtnClickListener(View.OnClickListener negativeBtnClickListener) {
			this.negativeBtnClickListener = negativeBtnClickListener;
			return this;

		}

		public Builder setButtons(int[] buttonRes) {
			if (buttonRes.length > 2 || buttonRes.length == 0) {
				throw new IllegalArgumentException(
						"ButtonRes's length can not be greater than 2 or length can not be zero");
			}

			this.buttonArray = new String[buttonRes.length];

			for (int i = 0; i < buttonRes.length; i++) {
				buttonArray[i] = Resources.getSystem().getString(buttonRes[i]);
			}

			return this;
		}

		public Builder setButtons(String[] buttonArray) {
			if (buttonArray.length > 2 || buttonArray.length == 0) {
				throw new IllegalArgumentException(
						"buttonArray length can not be greater than 2 length can not be zero");
			}

			this.buttonArray = Arrays.copyOf(buttonArray, buttonArray.length);

			return this;
		}

		public Builder setItems(int[] itemRes, OnClickListener clickListener) {
			if (null == itemRes || itemRes.length == 0) {
				throw new IllegalArgumentException("Parameter itemRes can not be null and length can not be zero");
			}

			this.itemArray = new String[itemRes.length];

			for (int i = 0; i < itemRes.length; i++) {
				itemArray[i] = Resources.getSystem().getString(itemRes[i]);
			}
			this.onItemClickListener = clickListener;

			return this;

		}

		public Builder setItems(String[] itemArray, OnClickListener clickListener) {
			if (null == itemArray || itemArray.length == 0) {
				throw new IllegalArgumentException("Parameter itemArray can not be null and length can not be zero");
			}

			this.itemArray = Arrays.copyOf(itemArray, itemArray.length);
			this.onItemClickListener = clickListener;
			return this;
		}

		public MaterialDialog create() {
			final MaterialDialog materialDialog = MaterialDialog.newInstance();

			materialDialog.setTitle(title);
			materialDialog.setMessage(message);
			materialDialog.setPositiveBtnClickListener(positiveBtnClickListener);
			materialDialog.setNegativeBtnClickListener(negativeBtnClickListener);
			materialDialog.setButtons(itemArray, buttonArray);
			materialDialog.setItems(itemArray);

			materialDialog.setOnItemClickListener(onItemClickListener);
			return materialDialog;
		}

		public MaterialDialog show(FragmentManager fragmentManager) {

			return create().show(fragmentManager);
		}

	}

	public interface OnClickListener {

		public void onClick(DialogFragment dialogFragment, int position);
	}
}
