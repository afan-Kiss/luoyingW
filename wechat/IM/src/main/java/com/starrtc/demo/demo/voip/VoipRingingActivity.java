package com.starrtc.demo.demo.voip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.image.ImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.starrtc.demo.R;
import com.starrtc.demo.api.API;
import com.starrtc.demo.api.MyStatus;
import com.starrtc.demo.database.CoreDB;
import com.starrtc.demo.database.HistoryBean;
import com.starrtc.demo.demo.BaseActivity;
import com.starrtc.demo.demo.MLOC;
import com.starrtc.demo.demo.service.KeepLiveService;
import com.starrtc.demo.utils.AEvent;
import com.starrtc.demo.utils.ApiURLUtils;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class VoipRingingActivity extends BaseActivity implements View.OnClickListener {

    private String targetId;
    private String aid;
    private String ip;
    private String nickname;
    private String head_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voip_ringing);
        addListener();
        targetId = getIntent().getStringExtra("targetId");
        findViewById(R.id.ring_hangoff).setOnClickListener(this);
        findViewById(R.id.ring_pickup).setOnClickListener(this);
        //昵称
        TextView targetid_text  = findViewById(R.id.targetid_text);
        targetid_text.setText(getIntent().getStringExtra("nickname")+"");
        //头像
        ImageView head_img = findViewById(R.id.head_img);
        ImageLoader.with(this)
                .load(getIntent().getStringExtra("head_img"))
                .into(head_img);
        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_VOIP);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setConversationId(targetId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean,true);

    }

    public void addListener(){
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_HANGUP,this);
        AEvent.addListener(AEvent.AEVENT_VOIP_REV_ERROR,this);
    }

    public void removeListener(){
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_HANGUP,this);
        AEvent.removeListener(AEvent.AEVENT_VOIP_REV_ERROR,this);
    }

    @Override
    public void dispatchEvent(final String aEventID, boolean success, final Object eventObj) {
        super.dispatchEvent(aEventID,success,eventObj);
        switch (aEventID){
            case AEvent.AEVENT_VOIP_REV_HANGUP:
                MLOC.d("","对方已挂断");
                MLOC.showMsg(VoipRingingActivity.this,"对方已挂断");
                finish();
                break;
            case AEvent.AEVENT_VOIP_REV_ERROR:
                MLOC.showMsg(VoipRingingActivity.this, (String) eventObj);
                finish();
                break;
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        addListener();
    }

    @Override
    public void onStop(){
        super.onStop();
        removeListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ring_hangoff) {
            MyStatus.videoStatus = false;
            Upavserver("3");
            XHClient.getInstance().getVoipManager().refuse(new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    Upavserver("3");
                    finish();
                }

                @Override
                public void failed(String errMsg) {
                    Upavserver("3");
                    finish();
                }
            });

        } else if (i == R.id.ring_pickup) {
            Upavserver("1");
        }
    }

    /**
     * 修改通话状态
     */
    public void Upavserver(final String status) {
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Upavserver");
        map.put("Sinkey", getIntent().getStringExtra("Sinkey"));
        map.put("Username", getIntent().getStringExtra("Username"));
        map.put("Avid", getIntent().getStringExtra("aid"));
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
                        //KeepLiveService.setVoipServerUrl(getIntent().getStringExtra("ip"));
                        //跳转
                        if ("1".equals(status)){
                            KeepLiveService.setVoipServerUrl(getIntent().getStringExtra("ip"));
                            Intent intent = new Intent(VoipRingingActivity.this, VoipActivity.class);
                            intent.putExtra("targetId", targetId);
                            intent.putExtra("ip",getIntent().getStringExtra("ip"));
                            intent.putExtra("aid", getIntent().getStringExtra("aid"));
                            intent.putExtra("nickname", getIntent().getStringExtra("nickname"));
                            intent.putExtra("head_img", getIntent().getStringExtra("head_img"));
                            intent.putExtra("Username", getIntent().getStringExtra("Username"));
                            intent.putExtra("Sinkey",getIntent().getStringExtra("Sinkey"));
                            intent.putExtra("send","2");
                            intent.putExtra(VoipActivity.ACTION, VoipActivity.RING);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
    }
}
