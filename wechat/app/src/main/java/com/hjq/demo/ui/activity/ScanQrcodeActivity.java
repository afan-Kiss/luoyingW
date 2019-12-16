package com.hjq.demo.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.googleauthenticator.GoogleAuthenticator;
import com.hjq.demo.guowenbin.activity.FrienProfileActivity;
import com.hjq.demo.guowenbin.activity.UserInformationActivity;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * @Author 陈末
 * @Time 2019-11-14 23:20
 * @Title 二维码扫描
 * @desc
 */
public class ScanQrcodeActivity extends MyActivity implements QRCodeView.Delegate {
    @BindView(R.id.zbarview)
    ZBarView mZBarView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_qrcode;
    }

    @Override
    protected void initView() {
        mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        mZBarView.setType(BarcodeType.TWO_DIMENSION, null); // 二维条码
        mZBarView.setDelegate(this);
//        =Utils.px2dip(this,Utils.getScreenHeight(this));
//        mZBarView.setTop(Utils.dp2px(this,50));

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("result = ", result);
        if (result.contains("otpauth")) {
            //扫描的是添加令牌
            String userName = result.substring((result.indexOf("totp") + 5), (result.indexOf("?")));
            String password = result.substring(result.indexOf("=") + 1);
            insertToken(userName, password);
        } else {
            //添加好友
            getQrfriend(result.substring(result.indexOf("=") + 1));
        }
    }

    private void insertToken(String account, String password) {
        String key = GoogleAuthenticator.genSecret(account, password);
        TokenEntity entity = new TokenEntity();
        entity.code = GoogleAuthenticator.check_code(key, System.currentTimeMillis());
        entity.name = account;
        entity.key = password;
        entity.card = UserManager.getUser().getCard();
        DBHelper.insertToken(entity);
        finish();
    }
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    /**
     * 扫码结果
     */
    private void getQrfriend(String Cardtxt) {
//        toast(Cardtxt);
        showLoading();
        map.clear();
        map.put("Method", "Qrfriend");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
//        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Username", "test");
        map.put("Card", Cardtxt);
        Log.i("Card", Cardtxt);

        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            String msg = TextUtils.isEmpty(CheckDate(response.body()).getMsg()) ? "数据有误" : CheckDate(response.body()).getMsg();
                            MessageDialog(msg);
                            return;
                        }
                        AddFriendModel qrcodeModel = gson.fromJson(GetDate(response.body()), AddFriendModel.class);

                        Intent intent = new Intent();
                        if (qrcodeModel.getFsate().equals("1")) {
                            intent.setClass(ScanQrcodeActivity.this, FrienProfileActivity.class);
                        } else {
                            intent.setClass(ScanQrcodeActivity.this, UserInformationActivity.class);
                        }
                        intent.putExtra("addFriendModel", qrcodeModel);
                        intent.putExtra("card", Cardtxt);
                        startActivity(intent);
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
    protected void onResume() {
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        super.onResume();
    }

    @Override
    public void MessageDialog(String msg) {
//        super.MessageDialog(msg);
        new MessageDialog.Builder(this)
                // 标题可以不用填写
                .setTitle("提示")
                // 内容必须要填写
                .setMessage(msg)
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(null)
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

}
