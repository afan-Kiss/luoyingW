package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.helper.InputTextHelper;
import com.hjq.demo.model.UserInfoModel;
import com.hjq.demo.other.IntentKey;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.DoubleClickUtils;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.CountdownView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 注册界面
 */
public final class RegisterActivity extends MyActivity {

    /**
     * 输入账号
     */
    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
    /**
     * 获取验证码按钮
     */
    @BindView(R.id.cv_register_countdown)
    CountdownView mCountdownView;

    /**
     * 输入验证码
     */
    @BindView(R.id.et_register_code)
    EditText mCodeView;
    /**
     * 输入密码
     */
    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    /**
     * 再次输入密码
     */
    @BindView(R.id.et_register_password2)
    EditText mPasswordView2;
    /**
     * 提交注册
     */
    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    /**
     * checkbox
     */
    @BindView(R.id.cbx_allow)
    CheckBox mCbxAllow;
    /**
     * 用户协议
     */
    @BindView(R.id.txt_agreement)
    TextView mAgreeMent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() == 11 &&
                                mPasswordView1.getText().toString().length() >= 6 &&
                                mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString());
                    }
                })
                .build();
    }

    @Override
    protected void initData() {

    }

//        //蛇精病,就是要把布局顶上去
//    @Override
//    protected ImmersionBar statusBarConfig() {
////        // 不要把整个布局顶上去
//        return super.statusBarConfig().keyboardEnable(true);
//    }

    @OnClick({R.id.cv_register_countdown, R.id.btn_register_commit,  R.id.txt_agreement})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_register_countdown:
                // 获取验证码按钮
                if (mPhoneView.getText().toString().length() != 11) {

                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                } else {
                    getmCountDode();


                }
                break;
            case R.id.btn_register_commit:
                // 提交注册按钮
                if (!DoubleClickUtils.isDoubleClick()) {
                    if (mCbxAllow.isChecked()) {
                        registereduser();
                    } else {
                        ToastUtils.show("您还没有同意<注册协议>哦");
                    }
                }

                break;

            case R.id.txt_agreement:
                //展示注册协议
                startActivity(new Intent(this, AgreementActivity.class));
                break;
            default:
                break;
        }
    }

    void getmCountDode() {
        map.clear();
        map.put("Method", "Regcode");
        map.put("Phonenumber", mPhoneView.getText().toString().trim());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.i("123", "onSuccess: " + GetDate(response.body()));
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;

                        }
                        Log.i("getcode", "onSuccess: " + GetDate(response.body()));
                        MessageDialog(CheckDate(response.body()).getMsg());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });


    }

    void registereduser() {
        map.clear();
        map.put("Method", "Register");
        map.put("Phonenumber", mPhoneView.getText().toString());
        map.put("Code", mCodeView.getText().toString());
        map.put("Password", mPasswordView1.getText().toString());
        map.put("PassRepeat", mPasswordView2.getText().toString());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.i("123", "onSuccess: " + GetDate(response.body()));
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(IntentKey.PHONE, mPhoneView.getText().toString());
                        intent.putExtra(IntentKey.PASSWORD, mPasswordView1.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                        MessageDialog(CheckDate(response.body()).getMsg());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });

    }
}