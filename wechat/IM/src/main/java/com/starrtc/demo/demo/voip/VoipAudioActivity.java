package com.starrtc.demo.demo.voip;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.starrtc.demo.demo.service.KeepLiveService;
import com.starrtc.demo.utils.AEvent;
import com.starrtc.demo.utils.ApiURLUtils;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.api.XHConstants;
import com.starrtc.starrtcsdk.api.XHVoipManager;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;
import com.starrtc.starrtcsdk.core.audio.StarRTCAudioManager;
import com.starrtc.starrtcsdk.core.pusher.XHCameraRecorder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;

public class VoipAudioActivity extends BaseActivity implements View.OnClickListener {

    private XHVoipManager voipManager;
    private Chronometer timer;

    public static String ACTION = "ACTION";
    public static String RING = "RING";
    public static String CALLING = "CALLING";
    private ImageView head_image;
    private TextView targetid_text;
    private String action;
    private String targetId;
    private String aid;
    private  String send;
    private ImageView head_img;
    private StarRTCAudioManager starRTCAudioManager;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_audio);
        if ("2".equals(getIntent().getStringExtra("send"))){
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
        head_img = findViewById(R.id.head_img);
        targetid_text  = findViewById(R.id.targetid_text);
        if ("2".equals(send)) {
            //参数
            aid  = getIntent().getStringExtra("aid");
            //昵称
            targetid_text.setText(getIntent().getStringExtra("nickname"));
            //头像
            if (!TextUtils.isEmpty(getIntent().getStringExtra("head_img"))){
                ImageLoader.with(this)
                        .load(getIntent().getStringExtra("head_img"))
                        .into(head_img);
            }
            initSDK();
        } else {
            getServer();
        }

    }

    public void initSDK(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);


        starRTCAudioManager = StarRTCAudioManager.create(this.getApplicationContext());
        starRTCAudioManager.start(new StarRTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StarRTCAudioManager.AudioDevice selectedAudioDevice, Set availableAudioDevices) {
                MLOC.d("onAudioDeviceChanged ",selectedAudioDevice.name());
            }
        });

        voipManager = XHClient.getInstance().getVoipManager();
        voipManager.setRtcMediaType(XHConstants.XHRtcMediaTypeEnum.STAR_RTC_MEDIA_TYPE_AUDIO_ONLY);
        voipManager.setRecorder(new XHCameraRecorder());
        addListener();

        targetId = getIntent().getStringExtra("targetId");
        action = getIntent().getStringExtra(ACTION);
        timer = (Chronometer) findViewById(R.id.timer);

        /* ((TextView)findViewById(R.id.targetid_text)).setText(targetId);
        ((ImageView)findViewById(R.id.head_img)).setImageResource(MLOC.getHeadImage(VoipAudioActivity.this,targetId));
        findViewById(R.id.head_bg).setBackgroundColor(ColorUtils.getColor(VoipAudioActivity.this,targetId));
        ((CircularCoverView)findViewById(R.id.head_cover)).setCoverColor(Color.parseColor("#000000"));
        int cint = DensityUtils.dip2px(VoipAudioActivity.this,45);
        ((CircularCoverView)findViewById(R.id.head_cover)).setRadians(cint, cint, cint, cint,0);*/

        findViewById(R.id.hangup).setOnClickListener(this);

        if(action.equals(CALLING)){
            showCallingView();
            MLOC.d("newVoip","call");
            voipManager.audioCall(this,targetId, new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    MLOC.d("newVoip","call success");
                }
                @Override
                public void failed(String errMsg) {
                    MLOC.d("newVoip","call failed");
                    MyStatus.AudioStatus = false;
                    stopAndFinish();
                }
            });
        }else{
            MLOC.d("newVoip","onPickup");
            onPickup();
        }
    }

    private void setupView(){
        voipManager.setupView(null, null, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("newVoip","setupView success");

            }

            @Override
            public void failed(String errMsg) {
                MLOC.d("newVoip","setupView failed");
                MyStatus.AudioStatus = false;
                stopAndFinish();
            }
        });
    }

    public void addListener(){
        AEvent.addListener(AEvent.AEVENT_VOIP_INIT_COMPLETE,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_BUSY,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_REFUSED,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_HANGUP,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_CONNECT,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_ERROR,this);
    }

    public void removeListener(){
        MLOC.canPickupVoip = true;
        AEvent.removeListener(AEvent.AEVENT_VOIP_INIT_COMPLETE,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_BUSY,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_REFUSED,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_HANGUP,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_CONNECT,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_ERROR,this);
    }

    @Override
    public void onResume(){
        super.onResume();
        MLOC.canPickupVoip = false;
        MyStatus.AudioStatus = false;
        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_VOIP);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setConversationId(targetId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean,true);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        addListener();
    }

    @Override
    public void onDestroy(){
        removeListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(VoipAudioActivity.this).setCancelable(true)
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
                                MyStatus.AudioStatus = false;
                                Upavserver("4");
                                stopAndFinish();
                            }

                            @Override
                            public void failed(final String errMsg) {
                                Upavserver("4");
                                MLOC.d("","AEVENT_VOIP_ON_STOP errMsg:"+errMsg);
                                MLOC.showMsg(VoipAudioActivity.this,errMsg);
                            }
                        });
                    }
                 }
        ).show();
    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, final Object eventObj) {
        super.dispatchEvent(aEventID,success,eventObj);
        switch (aEventID){
            case AEvent.AEVENT_VOIP_REV_BUSY:
                MLOC.d("","对方线路忙");
                MLOC.showMsg(VoipAudioActivity.this,"对方线路忙");
                Upavserver("2");
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_REFUSED:
                MLOC.d("","对方拒绝通话");
                MyStatus.AudioStatus = false;
                Upavserver("3");
                MLOC.showMsg(VoipAudioActivity.this,"对方拒绝通话");
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_HANGUP:
                MLOC.d("","对方已挂断");
                Upavserver("4");
                MyStatus.AudioStatus = false;
                MLOC.showMsg(VoipAudioActivity.this,"对方已挂断");
                timer.stop();
                stopAndFinish();
                break;
            case AEvent.AEVENT_VOIP_REV_CONNECT:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2 * 1000); //设置暂停的时间 5 秒
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
                MLOC.d("","对方允许通话");
                showTalkingView();
                break;
            case AEvent.AEVENT_VOIP_REV_ERROR:
                MLOC.d("",(String) eventObj);
                stopAndFinish();
                break;
        }
    }

    private void showCallingView(){
        MLOC.d("","showCallingView");
        findViewById(R.id.calling_txt).setVisibility(View.VISIBLE);
        findViewById(R.id.timer).setVisibility(View.INVISIBLE);
    }

    private void showTalkingView(){
        MLOC.d("","showTalkingView");
        findViewById(R.id.calling_txt).setVisibility(View.INVISIBLE);
        findViewById(R.id.timer).setVisibility(View.VISIBLE);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        setupView();
    }

    private void onPickup(){
        voipManager.accept(this,targetId, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("newVoip","onPickup OK ");
            }
            @Override
            public void failed(String errMsg) {
                MLOC.d("newVoip","onPickup failed ");
                MyStatus.AudioStatus = false;
                stopAndFinish();
            }
        });
        showTalkingView();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.hangup) {
            Upavserver("4");
            voipManager.hangup(new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    MyStatus.AudioStatus = false;
                    stopAndFinish();
                }

                @Override
                public void failed(String errMsg) {
                    MyStatus.AudioStatus = false;
                    stopAndFinish();
                }
            });

        }
    }

    private void stopAndFinish(){
        if(starRTCAudioManager !=null){
            starRTCAudioManager.stop();
        }
        if (mTimer !=null){
            mTimer.cancel();
        }
        VoipAudioActivity.this.finish();
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
        map.put("Type", "1");
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBody responseBody = CheckDate(response.body());
                        if (responseBody.getState() != 1) {
                            Toast.makeText(VoipAudioActivity.this, responseBody.getMsg()+"", Toast.LENGTH_SHORT).show();
                            VoipAudioActivity.this.finish();
                            return;
                        }
                        VideoModel mVideoModel = gson.fromJson(GetDate(response.body()), VideoModel.class);
                        MLOC.VOIP_SERVER_URL = mVideoModel.getServer_ip();
                        Intent service = new Intent(VoipAudioActivity.this, KeepLiveService.class);
                        service.putExtra("Username",getIntent().getStringExtra("Username"));
                        startService(service);
                        if (!TextUtils.isEmpty(mVideoModel.getHead_img())){
                            ImageLoader.with(VoipAudioActivity.this)
                                    .load(mVideoModel.getHead_img())
                                    .into(head_img);
                        }
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
        map.put("Avid", send.equals("1")?aid:getIntent().getStringExtra("aid"));
        map.put("Vtime", "1");
        map.put("State", status);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
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
        map.put("Avid", send.equals("1")?aid:getIntent().getStringExtra("aid"));
        map.put("Type", send);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("视频心跳:",GetDate(response.body()));
                        if (CheckDate(response.body()).getState() != 1) {
                            Toast.makeText(VoipAudioActivity.this, "对方异常!", Toast.LENGTH_SHORT).show();
                            mTimer.cancel();
                            finish();
                            return;
                        }
                    }
                });
    }
}
