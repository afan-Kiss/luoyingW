package com.hjq.demo.rong;//package com.hjq.demo.rong;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongVoIPIntent;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.signalingkit.RCSCallClient;
import io.rong.signalingkit.RCSCallSession;
import io.rong.signalingkit.callmanager.IRCSReceivedCallListener;


/**
 * 融云视频 类
 */
public class RongVoice {

    public static final String RONG_TOKEN = "rong_token";
    private static final String TAG = "RongVoice";
    private Activity mActivity;

    //    public static String appkey = "z3v5yqkbv8v30";
    public static String appkey = "bmdehs6pbamks";

    private String rongUserId;

    private static String getUserName(AddFriendModel qrcodeModel) {
//        return "17306001832";
        return qrcodeModel.getUsername();
    }


    //    public static final String token1 = "ifD+g3G5yOd5u4WwVipNNM2yq+hfEluLjZ78E1qo4hFt+F9Wn9MiOOeOEoF6P0ekIgMfPz/y9zpH3fcfKkESX4Sb7ELIGs/NRbgu5/klaX4PKqeYRU6qrHOU4chaKbGs";
//    public static final String token1 = "wYlXZda3ZMyJkjoqAevEut0RT2zpPYllcD16Ke8xKDvi3iX6ekZ2vpHM74wGylrnkMvmDvRWepPRc3V1NNBLIPGkUEVUXFS/";
//    public static final String token1 = "zgAeNVyjPpABRVDB5rTeDd0RT2zpPYllcD16Ke8xKDvi3iX6ekZ2vuriZeZe3M9BhKFonWF1JcbRc3V1NNBLIMJZLRpa5cEX";


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

    //    public static final String appkey = "z3v5yqkbv8v30";
//    public static final String token1 = "wYlXZda3ZMyJkjoqAevEut0RT2zpPYllcD16Ke8xKDvi3iX6ekZ2vpHM74wGylrnkMvmDvRWepPRc3V1NNBLIPGkUEVUXFS/";
//     public static final String token1 = "zgAeNVyjPpABRVDB5rTeDd0RT2zpPYllcD16Ke8xKDvi3iX6ekZ2vuriZeZe3M9BhKFonWF1JcbRc3V1NNBLIMJZLRpa5cEX";

    public static String getToken() {
        String toke = SPUtils.getString(RONG_TOKEN, "未获取融云token,请");
        log(toke);
//        return token1;
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

                            //17306001832
//                            RCSCall.startSingleCall(mActivity, "17306001832", RCSCallCommon.CallMediaType.AUDIO);
//                            RCSCall.startSingleCall(mActivity, getUserName(qrcodeModel), RCSCallCommon.CallMediaType.AUDIO);
//                            RongCallKit.startSingleCall(mActivity, getUserName(qrcodeModel), RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
                            /**
                             * 发起音频通话
                             */

                            RongCallSession profile = RongCallClient.getInstance().getCallSession();
                            if (profile != null && profile.getStartTime() > 0) {
                                ToastUtils.show(profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                                        mActivity.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                                        mActivity.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail));
                                return;
                            }
                            ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                            if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                                ToastUtils.show(mActivity.getString(io.rong.callkit.R.string.rc_voip_call_network_error));
                                return;
                            }

                            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
                            intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
                            intent.putExtra("targetId", getUserName(qrcodeModel));
                            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage(mActivity.getPackageName());
                            mActivity.startActivity(intent);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }


                    });
                } else {
//                    RCSCall.startSingleCall(mActivity, "17306001832", RCSCallCommon.CallMediaType.AUDIO);
//                    RCSCall.startSingleCall(mActivity, getUserName(qrcodeModel), RCSCallCommon.CallMediaType.AUDIO);
//                    RongCallKit.startSingleCall(mActivity, getUserName(qrcodeModel), RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
                    /**
                     * 发起音频通话
                     */

                    RongCallSession profile = RongCallClient.getInstance().getCallSession();
                    if (profile != null && profile.getStartTime() > 0) {
                        ToastUtils.show(profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                                mActivity.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                                mActivity.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail));
                        return;
                    }
                    ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                        ToastUtils.show(mActivity.getString(io.rong.callkit.R.string.rc_voip_call_network_error));
                        return;
                    }

                    Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
                    intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
                    intent.putExtra("targetId", getUserName(qrcodeModel));
                    intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage(mActivity.getPackageName());
                    mActivity.startActivity(intent);

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
//                          RCSCall.startSingleCall(mActivity, getUserName(qrcodeModel), RCSCallCommon.CallMediaType.VIDEO);
//                            RongCallKit.startSingleCall(mActivity, getUserName(qrcodeModel), RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                            /**
                             * 发起音频通话
                             */

                            RongCallSession profile = RongCallClient.getInstance().getCallSession();
                            if (profile != null && profile.getStartTime() > 0) {
                                ToastUtils.show(profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                                        mActivity.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                                        mActivity.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail));
                                return;
                            }
                            ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                            if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                                ToastUtils.show(mActivity.getString(io.rong.callkit.R.string.rc_voip_call_network_error));
                                return;
                            }

                            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
                            intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
                            intent.putExtra("targetId", getUserName(qrcodeModel));
                            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage(mActivity.getPackageName());
                            mActivity.startActivity(intent);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                } else {
//                    RCSCall.startSingleCall(mActivity, getUserName(qrcodeModel), RCSCallCommon.CallMediaType.VIDEO);
//                    RongCallKit.startSingleCall(mActivity, getUserName(qrcodeModel), RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);

                    /**
                     * 发起音频通话
                     */

                        RongCallSession profile = RongCallClient.getInstance().getCallSession();
                        if (profile != null && profile.getStartTime() > 0) {
                            ToastUtils.show(profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                                            mActivity.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                                            mActivity.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail));
                            return;
                        }
                        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                            ToastUtils.show(mActivity.getString(io.rong.callkit.R.string.rc_voip_call_network_error));
                            return;
                        }

                        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
                        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
                        intent.putExtra("targetId", getUserName(qrcodeModel));
                        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage(mActivity.getPackageName());
                       mActivity.startActivity(intent);


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
                //Toast.makeText(mActivity, "头像获取", Toast.LENGTH_SHORT).show();
                //log("头像获取");


//                JoinRoom.start(mActivity);

                IRCSReceivedCallListener callListener = new IRCSReceivedCallListener() {
                    @Override
                    public void onReceivedCall(final RCSCallSession callSession) {
                        RLog.d(TAG, "onReceivedCall");
                        //Toast.makeText(mActivity, "收到直播", Toast.LENGTH_LONG);
                        RCSCallClient.getInstance().startVoIPActivity(mActivity, callSession, true);
                    }
                };
                RCSCallClient.getInstance().setReceivedCallListener(callListener);


                RongCallClient.setReceivedCallListener(new IRongReceivedCallListener() {
                    @Override
                    public void onReceivedCall(RongCallSession rongCallSession) {
                        RongCallSession profile = RongCallClient.getInstance().getCallSession();
                        if (profile != null && profile.getStartTime() > 0) {
                            ToastUtils.show(profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                                    mActivity.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                                    mActivity.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail));
                            return;
                        }
                        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                            ToastUtils.show(mActivity.getString(io.rong.callkit.R.string.rc_voip_call_network_error));
                            return;
                        }

                        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
                        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
                        intent.putExtra("targetId", rongCallSession.getTargetId());
                        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage(mActivity.getPackageName());
                        mActivity.startActivity(intent);
                    }

                    @Override
                    public void onCheckPermission(RongCallSession rongCallSession) {

                    }
                });

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
