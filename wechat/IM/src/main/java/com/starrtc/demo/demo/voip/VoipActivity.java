package com.starrtc.demo.demo.voip;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.image.ImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.starrtc.demo.R;
import com.starrtc.demo.api.API;
import com.starrtc.demo.api.MyStatus;
import com.starrtc.demo.api.ResponseBody;
import com.starrtc.demo.api.VideoModel;
import com.starrtc.demo.database.CoreDB;
import com.starrtc.demo.database.HistoryBean;
import com.starrtc.demo.demo.BaseActivity;
import com.starrtc.demo.demo.MLOC;
import com.starrtc.demo.utils.AEvent;
import com.starrtc.demo.utils.ApiURLUtils;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.api.XHConstants;
import com.starrtc.starrtcsdk.api.XHCustomConfig;
import com.starrtc.starrtcsdk.api.XHSDKHelper;
import com.starrtc.starrtcsdk.api.XHVoipManager;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;
import com.starrtc.starrtcsdk.core.audio.StarRTCAudioManager;
import com.starrtc.starrtcsdk.core.player.StarPlayer;
import com.starrtc.starrtcsdk.core.pusher.XHCameraRecorder;
import com.starrtc.starrtcsdk.core.pusher.XHScreenRecorder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class VoipActivity extends BaseActivity implements View.OnClickListener {

    private XHVoipManager voipManager;

    private StarPlayer targetPlayer;
    private StarPlayer selfPlayer;
    private Chronometer timer;
    private ImageView head_image;
    private TextView targetid_text;
    public static String ACTION = "ACTION";
    public static String RING = "RING";
    public static String CALLING = "CALLING";
    private String action;
    private String targetId;
    private String aid;
    private String send;
    private Boolean isTalking = false;

    private StarRTCAudioManager starRTCAudioManager;
    private XHSDKHelper xhsdkHelper;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip);
        if ("2".equals(getIntent().getStringExtra("send"))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5 * 1000); //设置暂停的时间 5 秒
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Voipheartbeat();
                            }
                        }, 0, 2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        send = getIntent().getStringExtra("send");
        head_image = findViewById(R.id.head_img);
        targetid_text = findViewById(R.id.targetid_text);
        //手机号
        targetId = getIntent().getStringExtra("targetId");
        if ("2".equals(send)) {//jie
            //参数
            aid = getIntent().getStringExtra("aid");
            //昵称
            targetid_text.setText(getIntent().getStringExtra("nickname"));
            //头像
            ImageLoader.with(this)
                    .load(getIntent().getStringExtra("head_img"))
                    .into(head_image);
            initSDK();
        } else {
            getServer();
        }
    }

    private void initSDK() {
        starRTCAudioManager = StarRTCAudioManager.create(this.getApplicationContext());
        starRTCAudioManager.start(new StarRTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StarRTCAudioManager.AudioDevice selectedAudioDevice, Set availableAudioDevices) {
                MLOC.d("onAudioDeviceChanged ", selectedAudioDevice.name());
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        voipManager = XHClient.getInstance().getVoipManager();
        voipManager.setRecorder(new XHCameraRecorder());

        voipManager.setRtcMediaType(XHConstants.XHRtcMediaTypeEnum.STAR_RTC_MEDIA_TYPE_VIDEO_AND_AUDIO);
        addListener();
        action = getIntent().getStringExtra(ACTION);
        targetPlayer = (StarPlayer) findViewById(R.id.voip_surface_target);
        selfPlayer = (StarPlayer) findViewById(R.id.voip_surface_self);
        selfPlayer.setZOrderMediaOverlay(true);
        timer = (Chronometer) findViewById(R.id.timer);
        targetPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTalking) {
                    findViewById(R.id.talking_view).setVisibility(findViewById(R.id.talking_view).getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                }
            }
        });
        findViewById(R.id.calling_hangup).setOnClickListener(this);
        findViewById(R.id.talking_hangup).setOnClickListener(this);
        findViewById(R.id.switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voipManager.switchCamera();
            }
        });
        findViewById(R.id.screen_btn).setOnClickListener(this);

        findViewById(R.id.mic_btn).setSelected(true);
        findViewById(R.id.mic_btn).setOnClickListener(this);
        findViewById(R.id.camera_btn).setSelected(true);
        findViewById(R.id.camera_btn).setOnClickListener(this);


        if (action.equals(CALLING)) {
            showCallingView();
            MLOC.d("newVoip", "call");

            xhsdkHelper = new XHSDKHelper();
            xhsdkHelper.setDefaultCameraId(1);
            StarPlayer player = findViewById(R.id.voip_surface_target);
            xhsdkHelper.startPerview(this, player);

            voipManager.call(this, targetId, new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    xhsdkHelper.stopPerview();
                    xhsdkHelper = null;
                    MLOC.d("newVoip", "call success! RecSessionId:" + data);
                }

                @Override
                public void failed(String errMsg) {
                    MLOC.d("newVoip", "call failed");
                    MyStatus.videoStatus = false;
                    stopAndFinish();
                }
            });


        } else {
            MLOC.d("newVoip", "onPickup");
            onPickup();
        }
    }

    private void setupViews() {
        voipManager.setupView(selfPlayer, targetPlayer, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("newVoip", "setupView success");
            }

            @Override
            public void failed(String errMsg) {
                MLOC.d("newVoip", "setupView failed");
                MyStatus.videoStatus = false;
                stopAndFinish();
            }
        });
    }

    public void addListener() {
        AEvent.addListener(AEvent.AEVENT_VOIP_INIT_COMPLETE, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_BUSY, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_REFUSED, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_HANGUP, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_CONNECT, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_ERROR, this);
        AEvent.addListener(AEvent.AEVENT_VOIP_TRANS_STATE_CHANGED, this);
    }

    public void removeListener() {
        MLOC.canPickupVoip = true;
        AEvent.removeListener(AEvent.AEVENT_VOIP_INIT_COMPLETE, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_BUSY, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_REFUSED, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_HANGUP, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_CONNECT, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_ERROR, this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_TRANS_STATE_CHANGED, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyStatus.videoStatus = false;
        MLOC.canPickupVoip = false;
        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_VOIP);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setConversationId(targetId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean, true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        addListener();
    }

    @Override
    public void onDestroy() {
        removeListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(VoipActivity.this).setCancelable(true)
                .setTitle("是否挂断?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        timer.stop();
                        voipManager.hangup(new IXHResultCallback() {
                            @Override
                            public void success(Object data) {
                                MyStatus.videoStatus = false;
                                Upavserver("4");
                                removeListener();
                                stopAndFinish();
                            }

                            @Override
                            public void failed(final String errMsg) {
                                MLOC.d("", "AEVENT_VOIP_ON_STOP errMsg:" + errMsg);
                                MLOC.showMsg(VoipActivity.this, errMsg);
                            }
                        });
                    }
                }
        ).show();
    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, final Object eventObj) {
        super.dispatchEvent(aEventID, success, eventObj);
        switch (aEventID) {
            case AEvent.AEVENT_VOIP_REV_BUSY:
                MLOC.d("", "对方线路忙");
                MLOC.showMsg(VoipActivity.this, "对方线路忙");
                if (xhsdkHelper != null) {
                    xhsdkHelper.stopPerview();
                    xhsdkHelper = null;
                }
                Upavserver("3");
                MyStatus.videoStatus = false;
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_REFUSED:
                MLOC.d("", "对方拒绝通话");
                MLOC.showMsg(VoipActivity.this, "对方拒绝通话");
                if (xhsdkHelper != null) {
                    xhsdkHelper.stopPerview();
                    xhsdkHelper = null;
                }
                Upavserver("3");
                MyStatus.videoStatus = false;
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_HANGUP:
                MLOC.d("", "对方已挂断");
                Upavserver("4");
                MLOC.showMsg(VoipActivity.this, "对方已挂断");
                MyStatus.videoStatus = false;
                timer.stop();
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_CONNECT:
                mTimer = new Timer();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5 * 1000); //设置暂停的时间 5 秒
                            mTimer = new Timer();
                            mTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Voipheartbeat();
                                }
                            }, 0, 2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                MLOC.d("", "对方允许通话");
                showTalkingView();
                break;
            case AEvent.AEVENT_VOIP_REV_ERROR:
                MLOC.d("", (String) eventObj);
                if (xhsdkHelper != null) {
                    xhsdkHelper.stopPerview();
                    xhsdkHelper = null;
                }
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_TRANS_STATE_CHANGED:
                findViewById(R.id.state).setBackgroundColor(((int) eventObj == 0) ? 0xFFFFFF00 : 0xFF299401);
                break;
        }
    }


    private void showCallingView() {
        findViewById(R.id.calling_view).setVisibility(View.VISIBLE);
        findViewById(R.id.talking_view).setVisibility(View.GONE);
    }

    private void showTalkingView() {
        isTalking = true;
        findViewById(R.id.calling_view).setVisibility(View.GONE);
        findViewById(R.id.talking_view).setVisibility(View.VISIBLE);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        setupViews();
    }

    private void onPickup() {
        voipManager.accept(this, targetId, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("newVoip", "onPickup OK! RecSessionId:" + data);
            }

            @Override
            public void failed(String errMsg) {
                MLOC.d("newVoip", "onPickup failed ");
                stopAndFinish();
            }
        });
        showTalkingView();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.calling_hangup) {//取消
            Upavserver("4");
            voipManager.cancel(new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    stopAndFinish();
                }

                @Override
                public void failed(String errMsg) {
                    stopAndFinish();
                }
            });
            if (xhsdkHelper != null) {
                xhsdkHelper.stopPerview();
                xhsdkHelper = null;
            }

        } else if (i == R.id.talking_hangup) {//挂断
            Upavserver("4");
            voipManager.hangup(new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    Upavserver("4");
                    MyStatus.videoStatus = false;
                    stopAndFinish();
                }

                @Override
                public void failed(String errMsg) {
                    Upavserver("4");
                    MyStatus.videoStatus = false;
                    stopAndFinish();
                }
            });

        } else if (i == R.id.screen_btn) {
            if (!XHCustomConfig.getInstance(this).getHardwareEnable()) {
                MLOC.showMsg(this, "需要打开硬编模式");
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mRecorder != null) {
                    findViewById(R.id.screen_btn).setSelected(false);
                    voipManager.resetRecorder(new XHCameraRecorder());
                    mRecorder = null;
                } else {
                    if (mMediaProjectionManager == null) {
                        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                    }
                    Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, REQUEST_CODE);
                }
            } else {
                MLOC.showMsg(this, "系统版本过低，无法使用录屏功能");
            }

        } else if (i == R.id.camera_btn) {
            if (findViewById(R.id.camera_btn).isSelected()) {
                findViewById(R.id.camera_btn).setSelected(false);
                voipManager.setVideoEnable(false);
            } else {
                findViewById(R.id.camera_btn).setSelected(true);
                voipManager.setVideoEnable(true);
            }

        } else if (i == R.id.mic_btn) {
            if (findViewById(R.id.mic_btn).isSelected()) {
                findViewById(R.id.mic_btn).setSelected(false);
                voipManager.setAudioEnable(false);
            } else {
                findViewById(R.id.mic_btn).setSelected(true);
                voipManager.setAudioEnable(true);
            }

        }
    }

    private static final int REQUEST_CODE = 1;
    private MediaProjectionManager mMediaProjectionManager;
    private XHScreenRecorder mRecorder;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRecorder = new XHScreenRecorder(this, resultCode, data);
        voipManager.resetRecorder(mRecorder);
        findViewById(R.id.screen_btn).setSelected(true);
    }

    private void stopAndFinish() {
        if (starRTCAudioManager != null) {
            starRTCAudioManager.stop();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        VoipActivity.this.finish();
    }


    /**
     * 获取服务器地址
     */
    public void getServer() {
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Getavserver");
        map.put("Sinkey", getIntent().getStringExtra("Sinkey"));
        map.put("Username", getIntent().getStringExtra("Username"));
        map.put("Card", getIntent().getStringExtra("card"));
        map.put("Type", "2");
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBody responseBody = CheckDate(response.body());
                        Log.d("获取服务器地址:", GetDate(response.body()));
                        if (responseBody.getState() != 1) {
                            Toast.makeText(VoipActivity.this, responseBody.getMsg() + "", Toast.LENGTH_SHORT).show();
                            VoipActivity.this.finish();
                            return;
                        }
                        VideoModel mVideoModel = gson.fromJson(GetDate(response.body()), VideoModel.class);
                        if (!TextUtils.isEmpty(mVideoModel.getHead_img())) {
                            ImageLoader.with(VoipActivity.this)
                                    .load(mVideoModel.getHead_img())
                                    .into(head_image);
                        }
                        /*Intent service = new Intent(VoipActivity.this, KeepLiveService.class);
                        service.putExtra("Username",getIntent().getStringExtra("Username"));
                        startService(service);*/
                        targetid_text.setText(mVideoModel.getNickname());
                        aid = String.valueOf(mVideoModel.getAv_id());
                        initSDK();
                    }
                });
    }

    /**
     * 修改通话状态
     */
    public void Upavserver(String status) {
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Upavserver");
        map.put("Sinkey", getIntent().getStringExtra("Sinkey"));
        map.put("Username", getIntent().getStringExtra("Username"));
        map.put("Avid", send.equals("1") ? aid : getIntent().getStringExtra("aid"));
        map.put("Vtime", "1");
        map.put("State", status);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("修改通话状态", GetDate(response.body()));
                        if (CheckDate(response.body()).getState() != 1) {
                            return;
                        }
                        finish();
                    }
                });
    }


    /**
     * 心跳
     */
    public void Voipheartbeat() {
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Voipheartbeat");
        map.put("Sinkey", getIntent().getStringExtra("Sinkey"));
        map.put("Username", getIntent().getStringExtra("Username"));
        map.put("Avid", send.equals("1") ? aid : getIntent().getStringExtra("aid"));
        map.put("Type", send);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            Toast.makeText(VoipActivity.this, "对方异常!", Toast.LENGTH_SHORT).show();
                            mTimer.cancel();
                            finish();
                            return;
                        }
                    }
                });
    }

}
