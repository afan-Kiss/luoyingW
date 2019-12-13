package com.hjq.demo.daerxiansheng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hjq.demo.R;
import com.shehuan.niv.Utils;

import androidx.annotation.NonNull;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-16.
 * 简述: <添加好友弹窗>
 */
public class AddUserDialog extends Dialog {


    public AddUserDialog(@NonNull Context context) {
        super(context);
    }

    public AddUserDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private int height;
        private int width;
        private onClickListener mOnClickListener;
        private Context context;

        //        public Builder setHeight(int height) {
//            this.height = height;
//            return this;
//        }
//
        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

//        public Builder setWidth(int width) {
//            this.width = width;
//            return this;
//        }

        public Builder setOnClickListener(onClickListener mOnClickListener) {
            this.mOnClickListener = mOnClickListener;
            return this;
        }


        public AddUserDialog onCreate() {
            final AddUserDialog addUserDialog = new AddUserDialog(context, R.style.dialog_transparent);
            LinearLayout linearlayout_adduser, linearlayout_qrcode, linearlayout_group_chat;
            View view = LayoutInflater.from(this.context).inflate(R.layout.dialog_adduser, null, false);
            addUserDialog.setContentView(view);
            Window window = addUserDialog.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            window.setGravity(Gravity.TOP | Gravity.END);
            layoutParams.y = Utils.dp2px(context, 50);
            layoutParams.x = Utils.dp2px(context, 10);
            window.setAttributes(layoutParams);
            linearlayout_adduser = view.findViewById(R.id.linearlayout_adduser);
            linearlayout_qrcode = view.findViewById(R.id.linearlayout_qrcode);
            linearlayout_group_chat = view.findViewById(R.id.linearlayout_group_chat);
            linearlayout_adduser.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.setOnAddUserClick(addUserDialog);
                }
            });
            linearlayout_qrcode.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.setOnQrCodeClick(addUserDialog);
                }
            });
            linearlayout_group_chat.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.setGroupChatClick(addUserDialog);
                }
            });
            return addUserDialog;
        }

    }

    public interface onClickListener {
        void setOnAddUserClick(AddUserDialog dialog);

        void setOnQrCodeClick(AddUserDialog dialog);

        void setGroupChatClick(AddUserDialog dialog);
    }
}
