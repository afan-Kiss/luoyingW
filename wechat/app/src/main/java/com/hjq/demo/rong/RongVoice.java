package com.hjq.demo.rong;//package com.hjq.demo.rong;


import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.demo.BuildConfig;
import com.hjq.demo.api.API;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.model.ResponseBody;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import io.rong.signalingkit.RCSCall;
import io.rong.signalingkit.RCSCallClient;
import io.rong.signalingkit.RCSCallCommon;
import io.rong.signalingkit.RCSCallSession;
import io.rong.signalingkit.callmanager.IRCSReceivedCallListener;

/**
 * 融云视频 类
 */
public class RongVoice {

    public static final String RONG_TOKEN = "rong_token";
    private static final String TAG = "RongVoice";
    private Activity mActivity;

    //public static String appkey = "z3v5yqkbv8v30";
    public static String appkey = "bmdehs6pbamks";

    private String rongUserId;

    public RongVoice(Activity mActivity) {
        this.mActivity = mActivity;
    }


    /**
     * 建立融云token 链接
     */
    public void initRongRtcConnect(String token) {
        RongConnect(token);
    }


    public static void saveToken(String token) {
        Log.i(TAG, "saveToken: from login");
        SPUtils.putString(RONG_TOKEN, token);
    }


    public static String getToken() {
        String toke = SPUtils.getString(RONG_TOKEN, "未获取融云token,请");
        log(toke);
        return UserManager.getUser().getToken();
    }


    public static void log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "saveToken: from login");
        }
    }


    /**
     * 开启语音通话
     *
     * @param mActivity
     * @param card      对方点 通信id   手机号
     */
    public static void startVoice(Activity mActivity, String card) {
        getQrfriend(card, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (CheckDate(response.body()).getState() != 1) {
                    String msg = TextUtils.isEmpty(CheckDate(response.body()).getMsg()) ? "数据有误" : CheckDate(response.body()).getMsg();
                    log(msg);
                    Toast.makeText(mActivity, "查询通话id失败" + msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                AddFriendModel qrcodeModel = new Gson().fromJson(GetDate(response.body()), AddFriendModel.class);

                if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {

                    RongVoice.recontect(getToken(), new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {

                        }

                        @Override
                        public void onSuccess(String s) {

                            RCSCall.startSingleCall(mActivity, qrcodeModel.getUsername(), RCSCallCommon.CallMediaType.AUDIO);


                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }


                    });
                } else {
                    RCSCall.startSingleCall(mActivity, qrcodeModel.getUsername(), RCSCallCommon.CallMediaType.AUDIO);
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    /**
     * 扫码结果
     */
    public static void getQrfriend(String Cardtxt, StringCallback stringCallback) {
//        toast(Cardtxt);
        Map map = new HashMap<>();
        map.clear();
        map.put("Method", "Qrfriend");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
//        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Card", Cardtxt);
        Log.i("Card", Cardtxt);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(stringCallback);
    }


    /**
     * 开启语音通话
     *
     * @param mActivity
     * @param card      对方点 通信id   手机号
     */
    public static void startVideo(Activity mActivity, String card) {
        getQrfriend(card, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (CheckDate(response.body()).getState() != 1) {
                    String msg = TextUtils.isEmpty(CheckDate(response.body()).getMsg()) ? "数据有误" : CheckDate(response.body()).getMsg();
                    Toast.makeText(mActivity, msg + msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                AddFriendModel qrcodeModel = new Gson().fromJson(GetDate(response.body()), AddFriendModel.class);

                if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                    RongVoice.recontect(getToken(), new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {

                        }

                        @Override
                        public void onSuccess(String s) {

                            RCSCall.startSingleCall(mActivity, qrcodeModel.getUsername(), RCSCallCommon.CallMediaType.VIDEO);


                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                } else {
                    RCSCall.startSingleCall(mActivity, qrcodeModel.getUsername(), RCSCallCommon.CallMediaType.VIDEO);

                }


            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private static ResponseBody CheckDate(String date) {
        ResponseBody responseBody = new Gson().fromJson(RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date))), ResponseBody.class);
        return responseBody;
    }

    private static String GetDate(String date) {
        return RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date)));
    }


    /**
     * 融云通话sdk启动代码
     * 用户1链接
     * 测试使用
     */
    private void RongConnect(String token) {

        log(token);

        recontect(token, new RongIMClient.ConnectCallback() {
            //        recontect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
//                mActivity.startActivity(new Intent(mActivity, JoinRoom.class));
                Toast.makeText(mActivity, "头像获取", Toast.LENGTH_SHORT).show();
                log("头像获取");


                IRCSReceivedCallListener callListener = new IRCSReceivedCallListener() {
                    @Override
                    public void onReceivedCall(final RCSCallSession callSession) {
                        RLog.d(TAG, "onReceivedCall");
                        Toast.makeText(mActivity, "收到直播", Toast.LENGTH_LONG);
                        RCSCallClient.getInstance().startVoIPActivity(mActivity, callSession, true);
                    }
                };
                RCSCallClient.getInstance().setReceivedCallListener(callListener);

                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String s) {
                        return new UserInfo(s, UserManager.getUser().getNickname(), Uri.parse(UserManager.getUser().getHead_img()));
                    }
                }, false);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }

    private static void recontect(String token, RongIMClient.ConnectCallback connectCallback) {
        RongIMClient.connect(token, connectCallback);
    }


    public String getRongUserId() {
        return rongUserId;
    }

    public void setRongUserId(String rongUserId) {
        this.rongUserId = rongUserId;
    }
}
