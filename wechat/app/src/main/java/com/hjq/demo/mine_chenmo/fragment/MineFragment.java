package com.hjq.demo.mine_chenmo.fragment;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mine_chenmo.activity.QrcodeActivity;
import com.hjq.demo.mine_chenmo.activity.UserInfoActivity;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.CollectionActivity;
import com.hjq.demo.ui.activity.LoginActivity;
import com.hjq.demo.ui.activity.PasswordForgetActivity;
import com.hjq.demo.ui.activity.ScanQrcodeActivity;
import com.hjq.demo.ui.activity.SettingActivity;
import com.hjq.demo.ui.activity.TokenActivity;
import com.hjq.image.ImageLoader;
import com.hjq.widget.layout.SettingBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author 陈末
 * @Time 2019-11-14 16:07
 * @Title 我的界面
 * @desc
 */
public class MineFragment extends MyLazyFragment {
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.stb_userinfo)
    SettingBar stb_userinfo;
    @BindView(R.id.img_user_icon)
    ImageView img_user_icon;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        tv_name.setText(UserManager.getUser().getNickname());
        ImageLoader.with(getActivity())
                .load(UserManager.getUser().getHead_img())
                .into(img_user_icon);
        stb_userinfo.setLeftHint("账号： " + UserManager.getUser().getPhone_number());
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        startActivity(ScanQrcodeActivity.class);
    }

    @OnClick({R.id.stb_userinfo, R.id.stb_qrcode, R.id.stb_collect, R.id.stb_dynamic, R.id.stb_token, R.id.stb_setting, R.id.stb_update_pwd, R.id.tv_quit_login, R.id.tv_qiehuan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stb_userinfo:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.stb_qrcode:
                startActivity(QrcodeActivity.class);
                break;
            case R.id.stb_collect:
                startActivity(CollectionActivity.class);
                break;
            case R.id.stb_dynamic:
                break;
            case R.id.stb_token:
                startActivity(TokenActivity.class);
                break;
            case R.id.stb_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.stb_update_pwd:
                startActivity(PasswordForgetActivity.class);
                break;
            case R.id.tv_quit_login:
            case R.id.tv_qiehuan:
                UserManager.clear();
                startActivity(LoginActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

}
