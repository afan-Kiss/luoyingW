package com.hjq.demo.ui.activity;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.other.AppConfig;

import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 关于界面
 */
public final class AboutActivity extends MyActivity {
    @BindView(R.id.tv_name_version)
    AppCompatTextView tv_name_version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        tv_name_version.setText(String.format(getResources().getString(R.string.version_name_format), AppConfig.getVersionName()));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.sb_to_score, R.id.sb_setting_about, R.id.sb_version_update})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_to_score:
                break;
            case R.id.sb_setting_about:
                break;
            case R.id.sb_version_update:
//                if (20 > AppConfig.getVersionCode()) {
//
//                    new UpdateDialog.Builder(this)
//                            // 版本名
//                            .setVersionName("v 2.0")
//                            // 文件大小
//                            .setFileSize("10 M")
//                            // 是否强制更新
//                            .setForceUpdate(false)
//                            // 更新日志
//                            .setUpdateLog("到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥")
//                            // 下载 url
//                            .setDownloadUrl("https://raw.githubusercontent.com/getActivity/AndroidProject/master/AndroidProject.apk")
//                            .show();
//                } else {
//                    toast(R.string.update_no_update);
//                }
                break;
        }
    }
}