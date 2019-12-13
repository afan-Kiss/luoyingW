package com.hjq.demo.mine_chenmo.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.session.UserManager;
import com.hjq.image.ImageLoader;

import butterknife.BindView;

/**
 * @Author 陈末
 * @Time 2019-11-14 22:48
 * @Title 我的二维码
 * @desc
 */
public class QrcodeActivity extends MyActivity {
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.img_head_icon)
    ImageView img_head_icon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_txt)
    TextView tv_txt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected void initView() {
        tv_name.setText(UserManager.getUser().getNickname());
        if (UserManager.getUser().getSignature().equals("")) {
            tv_txt.setText("未设置个签");
        } else {
            tv_txt.setText(UserManager.getUser().getSignature());
        }

        ImageLoader.with(getActivity())
                .load(UserManager.getUser().getQr_img_url())
                .into(img_qrcode);
        ImageLoader.with(getActivity())
                .load(UserManager.getUser().getHead_img())
                .into(img_head_icon);
    }

    @Override
    protected void initData() {

    }
}
