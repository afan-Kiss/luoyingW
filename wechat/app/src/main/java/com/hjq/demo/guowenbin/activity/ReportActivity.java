package com.hjq.demo.guowenbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;

public class ReportActivity extends MyActivity {
    @BindView(R.id.report_friend_et)
    EditText report_friend_et;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView() {
        setRightTitle(R.string.friend_report);
        getTitleBar().getRightView().setTextColor(getResources().getColor(R.color.apptheme));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        if (TextUtils.isEmpty(report_friend_et.getText().toString())){
            ToastUtils.show("请输入您的举报内容");
        }else {
            ToastUtils.show("举报成功");
            finish();
        }
    }
}
