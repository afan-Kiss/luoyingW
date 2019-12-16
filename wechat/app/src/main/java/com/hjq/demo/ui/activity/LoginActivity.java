package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.helper.InputTextHelper;
import com.hjq.demo.model.UserInfoModel;
import com.hjq.demo.other.IntentKey;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.PreKeys;
import com.hjq.demo.util.PrefUtils;
import com.hjq.widget.view.PasswordEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 登录界面
 */
public final class LoginActivity extends MyActivity {


    @BindView(R.id.ll_login_body)
    LinearLayout mBodyLayout;

    /**
     * 输入账号文本框
     */
    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    /**
     * 输入密码文本框
     */
    @BindView(R.id.et_login_password)
    EditText mPasswordView;
    /**
     * 登录按钮
     */
    @BindView(R.id.btn_login)
    Button mCommitView;
    /**
     * 忘记密码
     */
    @BindView(R.id.btn_forgetr)
    TextView button_forgetr;
    /**
     * 注册按钮
     */
    @BindView(R.id.btn_register)
    TextView Register_ll;

    /**
     * logo 缩放比例
     */
    private final float mLogoScale = 0.8f;
    /**
     * 动画时间
     */
    private final int mAnimTime = 300;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() == 11 &&
                                mPasswordView.getText().toString().length() >= 6;
                    }
                })
                .build();


    }

    @Override
    protected void initData() {
        if (UserManager.getUser() != null) {
            mPhoneView.setText(UserManager.getUser().getPhone_number());
        }

        if (!TextUtils.isEmpty(PrefUtils.getString(this,PreKeys.USERNAME,""))){
            mPhoneView.setText(PrefUtils.getString(this,PreKeys.USERNAME,""));
            mPasswordView.setText(PrefUtils.getString(this,PreKeys.PASSWORD,""));
        }

        PasswordEditText text;

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivityForResult(RegisterActivity.class, new ActivityCallback() {

            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                // 如果已经注册成功，就执行登录操作
                if (resultCode == RESULT_OK && data != null) {
                    mPhoneView.setText(data.getStringExtra(IntentKey.PHONE));
                    mPasswordView.setText(data.getStringExtra(IntentKey.PASSWORD));
                    onClick(mCommitView);
                }
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.btn_register,R.id.btn_forgetr})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (mPhoneView.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                } else {
                    showLoading();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showComplete();
                            // 处理登录
                            login();
                        }
                    }, 1000);
                }
                break;

            case R.id.btn_register:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forgetr:
                //跳转忘记密码界面
                startActivity(new Intent(this,PasswordForgetActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * 登录请求
     */
    private void login() {
        showLoading();
        map.clear();
        map.put("Method", "Login");
        map.put("Deviceid", "12345676");
        map.put("Devicetype", "2");
        map.put("Password", mPasswordView.getText().toString());
//        map.put("Username", mEtAreaCode.getText().toString() + "-" + mPhoneView.getText().toString());
        map.put("Username", mPhoneView.getText().toString());

        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        //startActivityFinish(HomeActivity.class);
                        UserInfoModel userInfoModel = gson.fromJson(GetDate(response.body()), UserInfoModel.class);
                        Log.i("888888", "onSuccess: " + GetDate(response.body()));
                        UserManager.save(userInfoModel);
                        PrefUtils.putString(LoginActivity.this, PreKeys.USERNAME, mPhoneView.getText().toString());
                        PrefUtils.putString(LoginActivity.this, PreKeys.PASSWORD, mPasswordView.getText().toString());
                        startActivity(HomeActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast(R.string.error_network);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}