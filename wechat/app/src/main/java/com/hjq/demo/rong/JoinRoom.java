//package com.hjq.demo.rong;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.hjq.demo.R;
//
//import java.util.List;
//
//import cn.rongcloud.rtc.RTCErrorCode;
//import cn.rongcloud.rtc.RongRTCEngine;
//import cn.rongcloud.rtc.callback.JoinRoomUICallBack;
//import cn.rongcloud.rtc.callback.RongRTCResultUICallBack;
//import cn.rongcloud.rtc.engine.view.RongRTCVideoView;
//import cn.rongcloud.rtc.events.RongRTCEventsListener;
//import cn.rongcloud.rtc.room.RongRTCRoom;
//import cn.rongcloud.rtc.stream.MediaType;
//import cn.rongcloud.rtc.stream.local.RongRTCCapture;
//import cn.rongcloud.rtc.stream.remote.RongRTCAVInputStream;
//import cn.rongcloud.rtc.user.RongRTCLocalUser;
//import cn.rongcloud.rtc.user.RongRTCRemoteUser;
//import io.rong.imlib.model.Message;
//
//public class JoinRoom extends Activity implements RongRTCEventsListener, View.OnClickListener {
//    private static final String TAG = "MainActivity";
//    private RongRTCVideoView local;
//    private LinearLayout remotes;
//    public static String mToken = "Gl3sKfKnIh1I0TuSlWDtI7I6ZiT8q7s0UEaMPWY0lMzUj/bahJYu1CIvww6HceBAqb3q4jQqkT1gB9cqqRFvxSsInDe4SvqdjfjhvsGXe+rnYnDnnye0buRNKy+58WBU";         //用户token 不通的自己修改
//    private String mRoomId = "10086"; //自己可以随意修改
//    private RongRTCRoom mRongRTCRoom;
//    private RongRTCLocalUser mLocalUser;
//    private Button button;
//    private FrameLayout localContainer;
//
//
//    public static void start(Context context) {
//        Intent starter = new Intent(context, JoinRoom.class);
//        context.startActivity(starter);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_activity_layout);
//        initView();
////        getActionBar().setTitle("房间号: " + mRoomId);
//        joinRoom();
//    }
//
//    private void initView() {
//        local = RongRTCEngine.getInstance().createVideoView(this);
//        local.setOnClickListener(this);
//        localContainer = (FrameLayout) findViewById(R.id.local_container);
//        localContainer.addView(local);
//        remotes = (LinearLayout) findViewById(R.id.remotes);
//        button = (Button) findViewById(R.id.finish);
//        button.setVisibility(View.GONE);
//        button.setOnClickListener(this);
//    }
//
//    /**
//     * 加入房间
//     */
//    private void joinRoom() {
//        RongRTCEngine.getInstance().joinRoom(mRoomId, new JoinRoomUICallBack() {
//            @Override
//            protected void onUiSuccess(RongRTCRoom rongRTCRoom) {
//                Toast.makeText(JoinRoom.this, "加入房间成功", Toast.LENGTH_SHORT).show();
//                mRongRTCRoom = rongRTCRoom;
//                mLocalUser = rongRTCRoom.getLocalUser();
//                RongRTCCapture.getInstance().setRongRTCVideoView(local); //设置本地预览视图
//                RongRTCCapture.getInstance().startCameraCapture();       //开始采集数据
//                setEventListener();                                      //设置监听
//                addRemoteUsersView();
//                subscribeAll();                                          //订阅资源
//                publishDefaultStream();                                  //发布资源
//            }
//
//            @Override
//            protected void onUiFailed(RTCErrorCode rtcErrorCode) {
//                Toast.makeText(JoinRoom.this, "加入房间失败 rtcErrorCode：" + rtcErrorCode, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * 注册监听
//     */
//    private void setEventListener() {
//        if (mRongRTCRoom != null) {
//            mRongRTCRoom.registerEventsListener(this);
//        }
//    }
//
//    private void removeListener() {
//        if (mRongRTCRoom != null) {
//            mRongRTCRoom.unRegisterEventsListener(this);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        removeListener();
//        RongRTCEngine.getInstance().quitRoom(mRoomId, new RongRTCResultUICallBack() {
//            @Override
//            public void onUiSuccess() {
//                Toast.makeText(JoinRoom.this, "离开房间成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onUiFailed(RTCErrorCode rtcErrorCode) {
//                Toast.makeText(JoinRoom.this, "离开房间失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * 添加远端用户View
//     */
//    private void addRemoteUsersView() {
//        if (mRongRTCRoom != null) {
//            for (RongRTCRemoteUser remoteUser : mRongRTCRoom.getRemoteUsers().values()) {
//                for (RongRTCAVInputStream inputStream : remoteUser.getRemoteAVStreams()) {
//                    if (inputStream.getMediaType() == MediaType.VIDEO) {
//                        inputStream.setRongRTCVideoView(getNewVideoView());
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 订阅所有当前在房间发布资源的用户
//     */
//    private void subscribeAll() {
//        if (mRongRTCRoom != null) {
//            for (RongRTCRemoteUser remoteUser : mRongRTCRoom.getRemoteUsers().values()) {
//                remoteUser.subscribeAvStream(remoteUser.getRemoteAVStreams(), new RongRTCResultUICallBack() {
//                    @Override
//                    public void onUiSuccess() {
//                        Toast.makeText(JoinRoom.this, "订阅资源成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onUiFailed(RTCErrorCode rtcErrorCode) {
//                        Toast.makeText(JoinRoom.this, "订阅资源成功", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        }
//    }
//
//    /**
//     * 发布资源
//     */
//    private void publishDefaultStream() {
//        if (mLocalUser != null) {
//            mLocalUser.publishDefaultAVStream(new RongRTCResultUICallBack() {
//                @Override
//                public void onUiSuccess() {
//                    Toast.makeText(JoinRoom.this, "发布资源成功", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onUiFailed(RTCErrorCode rtcErrorCode) {
//                    Toast.makeText(JoinRoom.this, "发布资源失败", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//    }
//
//    private RongRTCVideoView getNewVideoView() {
//        Log.i(TAG, "getNewVideoView()");
//        RongRTCVideoView videoView = RongRTCEngine.getInstance().createVideoView(this);
//        videoView.setOnClickListener(this);
//        remotes.addView(videoView, new LinearLayout.LayoutParams(remotes.getHeight(), remotes.getHeight()));
//        remotes.bringToFront();
//        return videoView;
//    }
//
//
//    @Override
//    public void onRemoteUserPublishResource(RongRTCRemoteUser rongRTCRemoteUser, List<RongRTCAVInputStream> list) {
//        for (RongRTCAVInputStream inputStream : rongRTCRemoteUser.getRemoteAVStreams()) {
//            if (inputStream.getMediaType() == MediaType.VIDEO) {
//                inputStream.setRongRTCVideoView(getNewVideoView());
//            }
//        }
//        rongRTCRemoteUser.subscribeAvStream(rongRTCRemoteUser.getRemoteAVStreams(), new RongRTCResultUICallBack() {
//            @Override
//            public void onUiSuccess() {
//                Toast.makeText(JoinRoom.this, "订阅成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onUiFailed(RTCErrorCode rtcErrorCode) {
//                Toast.makeText(JoinRoom.this, "订阅失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onRemoteUserAudioStreamMute(RongRTCRemoteUser rongRTCRemoteUser, RongRTCAVInputStream rongRTCAVInputStream, boolean b) {
//
//    }
//
//    @Override
//    public void onRemoteUserVideoStreamEnabled(RongRTCRemoteUser rongRTCRemoteUser, RongRTCAVInputStream rongRTCAVInputStream, boolean b) {
//
//    }
//
//    @Override
//    public void onRemoteUserUnpublishResource(RongRTCRemoteUser rongRTCRemoteUser, List<RongRTCAVInputStream> list) {
//
//    }
//
//
//    @Override
//    public void onUserJoined(RongRTCRemoteUser rongRTCRemoteUser) {
//
//    }
//
//    @Override
//    public void onUserLeft(RongRTCRemoteUser rongRTCRemoteUser) {
//        for (RongRTCAVInputStream inputStream : rongRTCRemoteUser.getRemoteAVStreams()) {
//            if (inputStream.getMediaType() == MediaType.VIDEO) {
//                remotes.removeView(inputStream.getRongRTCVideoView());
//            }
//        }
//    }
//
//    @Override
//    public void onUserOffline(RongRTCRemoteUser rongRTCRemoteUser) {
//
//    }
//
//    @Override
//    public void onVideoTrackAdd(String s, String s1) {
//
//    }
//
//    @Override
//    public void onFirstFrameDraw(String s, String s1) {
//
//    }
//
//    @Override
//    public void onLeaveRoom() {
//
//    }
//
//    @Override
//    public void onReceiveMessage(Message message) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        quit();
//        super.onBackPressed();
//
//    }
//
//    private void quit() {
//        RongRTCEngine.getInstance().quitRoom(mRongRTCRoom.getRoomId(), new RongRTCResultUICallBack() {
//            @Override
//            public void onUiSuccess() {
//                Toast.makeText(JoinRoom.this, "离开房间成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onUiFailed(RTCErrorCode rtcErrorCode) {
//                Toast.makeText(JoinRoom.this, "离开房间失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
