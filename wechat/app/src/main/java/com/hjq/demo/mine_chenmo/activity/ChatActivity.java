package com.hjq.demo.mine_chenmo.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.appsys.EventArgs;
import com.hjq.demo.appsys.EventType;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.mine_chenmo.adapter.ChatAdapter;
import com.hjq.demo.model.CharModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author 陈末
 * @Time 2019-11-17 18:33
 * @Title 单聊
 * @desc
 */
public class ChatActivity extends MyActivity {
    @BindView(R.id.rc_chat)
    RecyclerView rc_chat;
    @BindView(R.id.et_txt)
    EditText et_txt;
    @BindView(R.id.TitleBar)
    TitleBar titleBar;

    String name;
    ChatAdapter chatAdapter;

    String Form_uid;

    Handler handler = new Handler();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        Form_uid = getIntent().getStringExtra("card");
        name = getIntent().getStringExtra("username");
    }

    @Override
    protected void initData() {
        handler.postDelayed(runnable, 1000);
        titleBar.setTitle(name);
        chatAdapter = new ChatAdapter(getActivity());
        rc_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_chat.setAdapter(chatAdapter);
        chatAdapter.setData(new ArrayList<>());
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Uchat();
            handler.postDelayed(this, 5 * 1000);
        }
    };


    @OnClick({R.id.btn_fasong})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fasong:
                Rchat();
                break;
            default:
                break;
        }
    }


    /**
     * 发送消息
     */
    private void Rchat() {
        showLoading();
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Rchat");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Rval", et_txt.getText().toString());
        map.put("Rtype", "1");
        map.put("Rclass", "1");
        //  map.put("Duration", "1");
        map.put("Form_uid", Form_uid);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            toast(CheckDate(response.body()).getMsg());
                            return;
                        }
                        insertMessage();
                        chatAdapter.addItem(UserManager.getUser().getNickname() + ":" + et_txt.getText().toString());
                        et_txt.setText("");
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

    /**
     * 添加消息发送记录到本地数据库
     */
    void insertMessage() {
        if (!TextUtils.isEmpty(et_txt.getText().toString())) {
            FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
            frendsMessageEntity.setContent(et_txt.getText().toString());//消息内容
            frendsMessageEntity.setContentType(1);//1=文本消息 2=图片 3=音频
            frendsMessageEntity.setToUserImage("是大二先生呀");
            frendsMessageEntity.setDuration(0);//语音时长秒
            frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
            frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
            frendsMessageEntity.setMessageType(1);//消息类型 1好友 2群组 3虚拟app
            frendsMessageEntity.setToUid("18402995814");
            frendsMessageEntity.setToUserImage("https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg");
//            DBHelper.insertMessage(frendsMessageEntity);
            toast("消息存储到数据库中");
        }

    }

    /**
     * 刷新消息列表
     */
    private void Uchat() {
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Uchat");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());

        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        CharModel charModel;
                        try {
                            charModel = gson.fromJson(GetDate(response.body()), CharModel.class);
                            for (int i = 0; i < charModel.getAll_array().size(); i++) {
                                chatAdapter.addItem(charModel.getAll_array().get(i).getNickname()
                                        + ":" + charModel.getAll_array().get(i).getRval());
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        toMessage();
        super.onBackPressed();
    }

    /**
     * 跳转至消息界面
     */
    void toMessage() {
        EventArgs eventArgs = new EventArgs();
        eventArgs.setEventType(EventType.Event_ToMessage);
        EventBus.getDefault().post(eventArgs);
    }

    @Override
    public void finish() {
        toMessage();
        super.finish();
    }
}
