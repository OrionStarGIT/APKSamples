package com.ainirobot.zoom;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialog extends Dialog {
    /* Constructor */
    private CustomDialog(Context context) {
        super(context);
    }
    private CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    private CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    /* Builder */
    public static class Builder {
        private TextView tvTitle, tvWarning, tvInfo;
        private EditText appKey, appSecret;
        private Button btnCancel, btnConfirm;
        public View mLayout;
        private View.OnClickListener mButtonCancelClickListener;
        private View.OnClickListener mButtonConfirmClickListener;
        public CustomDialog mDialog;
        public Builder(Context context) {
//            mDialog = new CustomDialog(context, R.style.custom_dialog);
            mDialog = new CustomDialog(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 加载布局文件
            mLayout = inflater.inflate(R.layout.dialog_custom, null, false);
            // 添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvTitle = (TextView) mLayout.findViewById(R.id.tv_title);
            tvWarning = (TextView) mLayout.findViewById(R.id.tv_warning);
            tvInfo = (TextView) mLayout.findViewById(R.id.tv_title);
            appKey = (EditText) mLayout.findViewById(R.id.appKey);
            appSecret = (EditText) mLayout.findViewById(R.id.appSecret);
            btnCancel = (Button) mLayout.findViewById(R.id.btn_cancel);
            btnConfirm = (Button) mLayout.findViewById(R.id.btn_confirm);
            appKey.setText(MainActivity.APP_KEY);
            appSecret.setText(MainActivity.APP_SECRET);
        }
        /**
         * 设置 Dialog 标题
         */
        public Builder setTitle(String title) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
            return this;
        }
        /**
         * 设置 Warning
         */
        public Builder setWarning(String waring) {
            tvWarning.setText(waring);
            if (waring == null || waring.equals("")) {
                tvWarning.setVisibility(View.GONE);
            }
            return this;
        }
        /**
         * 设置 Info
         */
        public Builder setInfo(String message) {
            tvInfo.setText(message);
            return this;
        }
        /**
         * 获取 appSecret
         * @return
         */
        public String getappSecret() {
            return appSecret.getText().toString();
        }
        /**
         * 获取 appSecret
         * @return
         */
        public String getAppKey() {
            return appKey.getText().toString();
        }
        /**
         * 设置取消按钮文字和监听
         */
        public Builder setButtonCancel(String text, View.OnClickListener listener) {
            btnCancel.setText(text);
            mButtonCancelClickListener = listener;
            return this;
        }
        /**
         * 设置确认按钮文字和监听
         */
        public Builder setButtonConfirm(String text, View.OnClickListener listener) {
            btnConfirm.setText(text);
            mButtonConfirmClickListener = listener;
            return this;
        }
        public CustomDialog create() {
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    mButtonCancelClickListener.onClick(view);
                }
            });
            btnConfirm.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    mButtonConfirmClickListener.onClick(view);
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }
}
