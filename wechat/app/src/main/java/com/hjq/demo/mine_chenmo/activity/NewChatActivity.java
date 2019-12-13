package com.hjq.demo.mine_chenmo.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.base.BaseActivity;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.guowenbin.activity.GroupInfoActivity;
import com.hjq.demo.helper.KeyboardUtils;
import com.hjq.demo.mine_chenmo.chatui.adapter.ChatAdapter;
import com.hjq.demo.mine_chenmo.chatui.bean.AudioMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.FileMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.ImageMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.Message;
import com.hjq.demo.mine_chenmo.chatui.bean.MsgSendStatus;
import com.hjq.demo.mine_chenmo.chatui.bean.MsgType;
import com.hjq.demo.mine_chenmo.chatui.bean.TextMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.VideoMsgBody;
import com.hjq.demo.mine_chenmo.chatui.util.ChatUiHelper;
import com.hjq.demo.mine_chenmo.chatui.util.FileUtils;
import com.hjq.demo.mine_chenmo.chatui.util.LogUtil;
import com.hjq.demo.mine_chenmo.chatui.util.PictureFileUtil;
import com.hjq.demo.mine_chenmo.chatui.widget.MediaManager;
import com.hjq.demo.mine_chenmo.chatui.widget.RecordButton;
import com.hjq.demo.mine_chenmo.chatui.widget.StateButton;
import com.hjq.demo.model.CharModel;
import com.hjq.demo.model.DynamicUpload;
import com.hjq.demo.model.ResponseBody;
import com.hjq.demo.other.AppConfig;
import com.hjq.demo.other.EventBusManager;
import com.hjq.demo.other.IntentKey;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.ImageActivity;
import com.hjq.demo.ui.activity.LoginActivity;
import com.hjq.demo.ui.activity.PhotoActivity;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.RxActivityTool;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.TimeUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.starrtc.demo.demo.voip.VoipActivity;
import com.starrtc.demo.demo.voip.VoipAudioActivity;
import com.starrtc.demo.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.rv_chat_list)
    RecyclerView mRvChat;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.bottom_layout)
    RelativeLayout mRlBottomLayout;//表情,添加底部布局
    @BindView(R.id.ivAdd)
    ImageView mIvAdd;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.btn_send)
    StateButton mBtnSend;//发送按钮
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;//录音图片
    @BindView(R.id.btnAudio)
    RecordButton mBtnAudio;//录音按钮
    @BindView(R.id.rlEmotion)
    LinearLayout mLlEmotion;//表情布局
    @BindView(R.id.llAdd)
    LinearLayout mLlAdd;//添加布局
    @BindView(R.id.swipe_chat)
    SwipeRefreshLayout mSwipeRefresh;//下拉刷新
    @BindView(R.id.rlVideo)
    RelativeLayout mRlVideo;
    @BindView(R.id.include_header)
    View include_header;
    @BindView(R.id.common_toolbar_title)
    TextView common_toolbar_title;
    @BindView(R.id.rlCamera)
    RelativeLayout mRlCamera;
    @BindView(R.id.rlCollection)
    RelativeLayout mRlCollection;

    private String cards;
    private RelativeLayout relativeLayout;

    private ChatAdapter mAdapter;
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";
    public static final int REQUEST_CODE_IMAGE = 0000;
    public static final int REQUEST_CODE_VEDIO = 1111;
    public static final int REQUEST_CODE_FILE = 2222;
    public static final int REQUEST_CODE_CAMERA = 3333;

    BaseDialog messageDialog;
    private ImageView ivAudio;
    private View view;

    //me
    String Form_uid;
    String userName;
    String imageUrl;
    String nickName;
    String card;
    String username;
    String Rtype;
    Gson gson = new Gson();
    private boolean DialogIsShow = false;
    private int page = 0;
    List<FrendsMessageEntity> mFrendsMessageEntity = new ArrayList<>();
    MyReceiver receiver;
    IntentFilter filter;
    private int mCurrentVoicePosition = -1;
    private boolean mCurrentSend;
    private File mFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.ChatActivityIsTop = true;
        setContentView(R.layout.activity_new_chat);

        EventBusManager.register(this);

        initContent();
        receiver = new MyReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.new_message");
        registerReceiver(receiver, filter);
        Form_uid = getIntent().getStringExtra("card");
        MyApplication.mCurrentChatCard = Form_uid;
        Rtype = getIntent().getStringExtra("Rtype");
        userName = getIntent().getStringExtra("username");
        imageUrl = getIntent().getStringExtra("imageUrl");
        nickName = getIntent().getStringExtra("nickName");

        common_toolbar_title.setText(nickName);
        page = (int) (DBHelper.queryMessageAll(Form_uid)/20);
        relativeLayout = include_header.findViewById(R.id.common_toolbar_add);
        Log.i("shuai", "initData: ");
        if (Rtype.equals("1")) {
            relativeLayout.setVisibility(View.GONE);
            DBHelper.getFrendMessagePageDesc(0, Form_uid);
        } else if (Rtype.equals("2")) {
            relativeLayout.setVisibility(View.VISIBLE);
        }

        getOldMessage();
    }

    //    @Override
//    protected int getLayoutId()
//    {
//        return R.layout.activity_new_chat;
//    }

//    @Override
//    protected void initView()
//    {
//
//    }
//
//    @Override
//    protected void initData()
//    {
//
//    }

    @Subscribe
    public void onEvent(String event) {
        Log.i("shuai", "onEvent: ");
        if (event.equals("你的账号在其他设备登录") && !DialogIsShow) {
            showESCDialog("你的账号在其他设备登录");
            return;
        }
        if (event.equals("更新消息")) {
            //Uchat();
            //getNewMessage();
            return;
        }
    }


    /**
     * 发送消息
     * Rtype=消息类型(1=好友 2=群组 3=虚拟APP)
     * Rclass=1=文本消息 2=图片 3=音频
     */
    private void Rchat(String txt, String rclass, long time, Message mMessgae) {
        Log.i("shuai", "Rchat: ");
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Rchat");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        if (rclass.equals("1")) {
            map.put("Rval", RxEncryptTool.base64Encode2(txt));
        } else {
            map.put("Rval", txt);
        }
        map.put("Rtype", Rtype);
        map.put("Rclass", rclass);
        map.put("Duration", time + "");
        map.put("Form_uid", Form_uid);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            updateFaildMsg(mMessgae);
                            return;
                        }
                        //添加消息记录
                        FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
                        frendsMessageEntity.setContent(txt);//消息内容
                        frendsMessageEntity.setContentType(Integer.parseInt(rclass));//1=文本消息 2=图片 3=音频
                        frendsMessageEntity.setToUserName(nickName);//用户昵称
                        frendsMessageEntity.setDuration(time);//语音时长秒
                        frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
                        frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
                        frendsMessageEntity.setMessageType(Integer.parseInt(Rtype));//消息类型 1好友 2群组 3虚拟app
                        frendsMessageEntity.setToUid(Form_uid);//好友card
                        frendsMessageEntity.setCard(UserManager.getUser().getCard());//当前用户card 标记多用户
                        frendsMessageEntity.setUserName("");//用户电话
                        frendsMessageEntity.setToUserImage(imageUrl);// 用户头像
                        frendsMessageEntity.setToType("1");//消息类型 1发送 2接收
                        mFrendsMessageEntity.add(frendsMessageEntity);
                        DBHelper.insertMessage(frendsMessageEntity);
                        //发送成功
                        updateMsg(mMessgae);

                        //保存消息到消息列表数据库
                        MessageListEntity message = new MessageListEntity();

                        message.card = Form_uid;
                        message.currentuserCard = UserManager.getUser().getCard();
                        message.disturb = 0 + "";
                        message.duration = time + "";
                        message.group_id = "";
                        message.group_img = frendsMessageEntity.getToUserImage();
                        message.group_name = frendsMessageEntity.getToUserName();
                        message.message_id = "";
                        message.messCount = 0;
                        message.nickname = nickName;
                        message.user_card = UserManager.getUser().getCard();
                        message.rclass = rclass;
                        message.rtime = TimeUtils.getTimeLong() + "";
                        message.rtype = Rtype;
                        message.head_img = imageUrl;
                        message.rval = txt;
                        List<MessageListEntity> spList = DBHelper.getUserMessageList();
                        for (int i = 0; i < spList.size(); i++) {
                            if (spList.get(i).getNickname().equals(message.nickname)) {
                                DBHelper.deleteMessageList(spList.get(i).id);
                            }
                        }

                        DBHelper.insertMessageList(message);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        updateFaildMsg(mMessgae);
                    }

                });
    }

    /**
     * 刷新消息列表
     */
    private void Uchat() {
        Log.i("shuai", "Uchat: ");
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Uchat");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());

        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            Log.i("shuai", "return: " + GetDate(response.body()));

                            return;
                        }
                        Log.i("shuai", "onSuccess: " + GetDate(response.body()));

                        CharModel charModel;
                        FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
                        try {
                            charModel = gson.fromJson(GetDate(response.body()), CharModel.class);
                            if (charModel.getAll_array().size() == 0) {
                                return;
                            }
                            for (int i = 0; i < charModel.getAll_array().size(); i++) {
                                //添加消息记录
                                frendsMessageEntity.setContentType(Integer.parseInt(charModel.getAll_array().get(i).getRclass()));//1=文本消息 2=图片 3=音频
                                frendsMessageEntity.setToUserName(nickName);//用户昵称
                                frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
                                frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
                                frendsMessageEntity.setMessageType(Integer.parseInt(Rtype));//消息类型 1好友 2群组 3虚拟app
                                frendsMessageEntity.setToUid(Form_uid);
                                frendsMessageEntity.setCard(UserManager.getUser().getCard());
                                frendsMessageEntity.setUserName("");//用户电话
                                frendsMessageEntity.setToUserImage(charModel.getAll_array().get(i).getHead_img());
                                frendsMessageEntity.setToType("2");//消息类型 1发送 2接收
                                frendsMessageEntity.setContent(GetDate(charModel.getAll_array().get(i).getRval()));//消息内容
                                if (charModel.getAll_array().get(i).getRclass().equals("1")) {
                                    shoudao(charModel.getAll_array().get(i).getRval()
                                            , charModel.getAll_array().get(i).getHead_img());
                                }
                                if (charModel.getAll_array().get(i).getRclass().equals("2")) {
                                    shoudao_img(charModel.getAll_array().get(i).getRval()
                                            , charModel.getAll_array().get(i).getHead_img());
                                }
                                if (charModel.getAll_array().get(i).getRclass().equals("3")) {
                                    frendsMessageEntity.setDuration(charModel.getAll_array().get(i).getDuration());//语音时长秒
                                    shoudao_amr(charModel.getAll_array().get(i).getRval(),
                                            charModel.getAll_array().get(i).getDuration()
                                            , charModel.getAll_array().get(i).getHead_img());

                                }
                            }
                        } catch (Exception e) {
                            Log.i("shuai异常：", e.toString());
                        }
                        mFrendsMessageEntity.add(frendsMessageEntity);
                        DBHelper.insertMessage(frendsMessageEntity);
                    }

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        for (int i = 0; i <mFrendsMessageEntity.size() ; i++) {
//            DBHelper.insertMessage(mFrendsMessageEntity.get(i));
//        }
//        mFrendsMessageEntity = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.ChatActivityIsTop = false;
        unregisterReceiver(receiver);
        EventBusManager.unregister(this);
    }

    protected void initContent() {
        Log.i("shuai", "initContent: ");
        ButterKnife.bind(this);
        mAdapter = new ChatAdapter(this, new ArrayList<Message>());
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(mLinearLayout);
        mRvChat.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(this);
        initChatUi();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            final boolean isSend = mAdapter.getItem(position).getSenderId().equals(NewChatActivity.mSenderId);
            if (view.getId() == R.id.bivPic) {
                ArrayList<String> images = new ArrayList<>();
                images.add(((ImageMsgBody) mAdapter.getData().get(position).getBody()).getThumbUrl());
                ImageActivity.start(NewChatActivity.this, images, 0);
                return;
            }

            if (ivAudio != null && mCurrentVoicePosition != position){
                if (mCurrentSend) {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                } else {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                }
                ivAudio = null;
                MediaManager.reset();
            }


            if (ivAudio != null) {
                if (isSend) {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                } else {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                }
                ivAudio = null;
                mCurrentVoicePosition = -1;
                mCurrentSend = false;
                MediaManager.reset();
            } else {
                mCurrentVoicePosition = position;
                mCurrentSend = isSend;
                ivAudio = view.findViewById(R.id.ivAudio);
                MediaManager.reset();
                if (isSend) {
                    ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                } else {
                    ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                }
                AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
                drawable.start();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        MediaManager.playSound(NewChatActivity.this, ((AudioMsgBody) mAdapter.getData().get(position).getBody()).getLocalPath(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (isSend) {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                                } else {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                                }

                                MediaManager.release();
                            }
                        });

                    }
                }.start();

            }


        });


    }

    //收到文本消息
    private void shoudao(String txt, String head_img) {
        Log.i("shuai", "shoudao: ");
        List<Message> mReceiveMsgList = new ArrayList<Message>();
        Message mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
        mMessgaeText.setHead_img(head_img);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mTextMsgBody.setMessage(GetDate(txt));
        mMessgaeText.setBody(mTextMsgBody);
        mReceiveMsgList.add(mMessgaeText);
        mAdapter.addData(mMessgaeText);

        updateMsg(mMessgaeText);
    }

    //收到图片消息
    private void shoudao_img(String url, String head_img) {
        List<Message> mReceiveMsgList = new ArrayList<Message>();
        Message mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
        mMessgaeImage.setHead_img(head_img);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        mImageMsgBody.setThumbUrl(GetDate(url));
        mMessgaeImage.setBody(mImageMsgBody);
        mReceiveMsgList.add(mMessgaeImage);
        mAdapter.addData(mMessgaeImage);

        updateMsg(mMessgaeImage);
    }

    //收到语音消息
    private void shoudao_amr(String url, long time, String head_img) {
        List<Message> mReceiveMsgList = new ArrayList<>();
        final Message mMessgae = getBaseReceiveMessage(MsgType.AUDIO);
        mMessgae.setHead_img(head_img);
        AudioMsgBody mFileMsgBody = new AudioMsgBody();
        mFileMsgBody.setLocalPath(GetDate(url));
        mFileMsgBody.setDuration(time);
        mMessgae.setBody(mFileMsgBody);
        mReceiveMsgList.add(mMessgae);
        mAdapter.addData(mMessgae);

        updateMsg(mMessgae);
    }

    //获取历史消息
    private void getOldMessage() {
        Log.i("shuai", "getOldMessage: ");
        //List<FrendsMessageEntity> msgList = DBHelper.queryMessageAsc( page, Form_uid);
        List<FrendsMessageEntity> msgList = DBHelper.queryMessageAsc(page, Form_uid);
        List<Message> mReceiveMsgList = new ArrayList<>();
        if (page > 0){
            List<FrendsMessageEntity> msgList1 = DBHelper.queryMessageDesc(--page, Form_uid);
            for (int i = 0; i < msgList1.size(); i++) {
                switch (msgList1.get(i).getContentType()) {//消息类型
                    case 1: //文本消息
                        Message mMessgaeText;
                        if (msgList1.get(i).getToType().equals("1")) {//true 发送---false为当前账户
                            mMessgaeText = getBaseSendMessage(MsgType.TEXT);
                            mMessgaeText.setHead_img(UserManager.getUser().getHead_img());
                        } else {
                            mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
                            mMessgaeText.setHead_img(msgList1.get(i).getToUserImage());
                        }
                        TextMsgBody mTextMsgBody = new TextMsgBody();
                        mTextMsgBody.setMessage(msgList1.get(i).getContent());//文本消息内容
                        mMessgaeText.setBody(mTextMsgBody);
                        mReceiveMsgList.add(mMessgaeText);
                        break;
                    case 2: //图片消息
                        Message mMessgaeImage;
                        if (msgList1.get(i).getCard().equals(Form_uid)) {//true 为当前账户---false为其他人
                            mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
                            mMessgaeImage.setHead_img(UserManager.getUser().getHead_img());
                        } else {
                            mMessgaeImage = getBaseSendMessage(MsgType.IMAGE);
                            mMessgaeImage.setHead_img(msgList1.get(i).getToUserImage());

                        }
                        ImageMsgBody mImageMsgBody = new ImageMsgBody();
                        mImageMsgBody.setThumbUrl(msgList1.get(i).getContent());//图片地址
                        mMessgaeImage.setBody(mImageMsgBody);
                        mReceiveMsgList.add(mMessgaeImage);
                        break;
                    case 3: //语音消息
                        Message audioMessage;
                        if (msgList1.get(i).getCard().equals(Form_uid)) {//true 为当前账户---false为其他人
                            audioMessage = getBaseReceiveMessage(MsgType.AUDIO);
                            audioMessage.setHead_img(UserManager.getUser().getHead_img());
                        } else {
                            audioMessage = getBaseSendMessage(MsgType.AUDIO);
                            audioMessage.setHead_img(msgList1.get(i).getToUserImage());
                        }
                        AudioMsgBody mFileMsgBody = new AudioMsgBody();
                        mFileMsgBody.setLocalPath(msgList1.get(i).getContent());//语音地址
                        mFileMsgBody.setDuration(msgList1.get(i).getDuration());//语音时间
                        audioMessage.setBody(mFileMsgBody);
                        mReceiveMsgList.add(audioMessage);
                        break;

                }
            }
        }



        for (int i = 0; i < msgList.size(); i++) {
            Log.i("shuai", "getOldMessage: " + i);
            switch (msgList.get(i).getContentType()) {//消息类型
                case 1: //文本消息
                    Log.i("shuai", "getOldMessage:文本消息 " + i);
                    Message mMessgaeText;
                    if (msgList.get(i).getToType().equals("1")) {//true 发送---false为当前账户
                        mMessgaeText = getBaseSendMessage(MsgType.TEXT);
                        mMessgaeText.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
                        mMessgaeText.setHead_img(msgList.get(i).getToUserImage());
                    }
                    TextMsgBody mTextMsgBody = new TextMsgBody();
                    mTextMsgBody.setMessage(msgList.get(i).getContent());//文本消息内容
                    mMessgaeText.setBody(mTextMsgBody);
                    mMessgaeText.setSentTime(msgList.get(i).getTime());
                    mReceiveMsgList.add(mMessgaeText);
                    break;
                case 2: //图片消息
                    Log.i("shuai", "getOldMessage:图片消息 " + i);
                    Message mMessgaeImage;
                    if (msgList.get(i).getToType().equals("1")) {//true 为当前账户---false为其他人
                        mMessgaeImage = getBaseSendMessage(MsgType.IMAGE);
                        mMessgaeImage.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
                        mMessgaeImage.setHead_img(msgList.get(i).toUserImage);
                    }

                    ImageMsgBody mImageMsgBody = new ImageMsgBody();
                    mImageMsgBody.setThumbUrl(msgList.get(i).getContent());//图片地址
                    mMessgaeImage.setBody(mImageMsgBody);
                    //mImageMsgBody.setSentTime(msgList.get(i).getTime());
                    mReceiveMsgList.add(mMessgaeImage);
                    break;
                case 3: //语音消息
                    Message audioMessage;
                    if (msgList.get(i).getToType().equals("1")) {//true 为当前账户---false为其他人
                        audioMessage = getBaseSendMessage(MsgType.AUDIO);
                        audioMessage.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        audioMessage = getBaseReceiveMessage(MsgType.AUDIO);
                        audioMessage.setHead_img(msgList.get(i).toUserImage);
                    }

                    AudioMsgBody audioMsgBody = new AudioMsgBody();
                    audioMsgBody.setLocalPath(msgList.get(i).getContent());//语音地址
                    audioMsgBody.setDuration(msgList.get(i).getDuration());//语音时间
                    audioMessage.setBody(audioMsgBody);
                    mReceiveMsgList.add(audioMessage);
                    break;

            }
        }
        mAdapter.addData(mReceiveMsgList);
        //mAdapter.notifyDataSetChanged();
        mRvChat.scrollToPosition(mAdapter.getItemCount()-1);
        mSwipeRefresh.setRefreshing(false);


    }

    @Override
    public void onRefresh() {
        if (page == 0){
            mSwipeRefresh.setRefreshing(false);
            Toast.makeText(this,"暂无更多消息",Toast.LENGTH_SHORT).show();
            return;
        }
        List<FrendsMessageEntity> msgList = DBHelper.queryMessageDesc(--page, Form_uid);
        List<Message> mReceiveMsgList = new ArrayList<>();

        for (int i = 0; i < msgList.size(); i++) {
            switch (msgList.get(i).getContentType()) {//消息类型
                case 1: //文本消息
                    Message mMessgaeText;
                    if (msgList.get(i).getToType().equals("1")) {//true 发送---false为当前账户
                        mMessgaeText = getBaseSendMessage(MsgType.TEXT);
                        mMessgaeText.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
                        mMessgaeText.setHead_img(msgList.get(i).getToUserImage());
                    }
                    TextMsgBody mTextMsgBody = new TextMsgBody();
                    mTextMsgBody.setMessage(msgList.get(i).getContent());//文本消息内容
                    mMessgaeText.setBody(mTextMsgBody);
                    mReceiveMsgList.add(mMessgaeText);
                    break;
                case 2: //图片消息
                    Message mMessgaeImage;
                    if (msgList.get(i).getCard().equals(Form_uid)) {//true 为当前账户---false为其他人
                        mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
                        mMessgaeImage.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        mMessgaeImage = getBaseSendMessage(MsgType.IMAGE);
                        mMessgaeImage.setHead_img(msgList.get(i).getToUserImage());

                    }
                    ImageMsgBody mImageMsgBody = new ImageMsgBody();
                    mImageMsgBody.setThumbUrl(msgList.get(i).getContent());//图片地址
                    mMessgaeImage.setBody(mImageMsgBody);
                    mReceiveMsgList.add(mMessgaeImage);
                    break;
                case 3: //语音消息
                    Message audioMessage;
                    if (msgList.get(i).getCard().equals(Form_uid)) {//true 为当前账户---false为其他人
                        audioMessage = getBaseReceiveMessage(MsgType.AUDIO);
                        audioMessage.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        audioMessage = getBaseSendMessage(MsgType.AUDIO);
                        audioMessage.setHead_img(msgList.get(i).getToUserImage());
                    }
                    AudioMsgBody mFileMsgBody = new AudioMsgBody();
                    mFileMsgBody.setLocalPath(msgList.get(i).getContent());//语音地址
                    mFileMsgBody.setDuration(msgList.get(i).getDuration());//语音时间
                    audioMessage.setBody(mFileMsgBody);
                    mReceiveMsgList.add(audioMessage);
                    break;

            }
        }
        mAdapter.addData(0, mReceiveMsgList);
        mSwipeRefresh.setRefreshing(false);
    }

    private void initChatUi() {
        //mBtnAudio
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(mLlContent)
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
                .bindEmojiData();
        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRvChat.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getItemCount() > 0) {
                                mRvChat.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });
        //点击空白区域关闭键盘
        mRvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                mEtContent.clearFocus();
                mIvEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });
        //
        ((RecordButton) mBtnAudio).setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                LogUtil.d("录音结束回调");
                File file = new File(audioPath);
                if (file.exists()) {
                    sendAudioMessage(audioPath, time);
                }
            }
        });

    }

    @OnClick({R.id.btn_send, R.id.rlPhoto, R.id.rlVoice, R.id.rlVideo, R.id.rlLocation,
            R.id.rlFile, R.id.common_toolbar_back, R.id.common_toolbar_add,R.id.rlCamera,R.id.rlCollection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendTextMsg(mEtContent.getText().toString());
                mEtContent.setText("");
                break;
             //照片
            case R.id.rlPhoto:
                PictureFileUtil.openGalleryPic(NewChatActivity.this, REQUEST_CODE_IMAGE);
                break;
             //视频
            case R.id.rlVideo:
                Intent video = new Intent(NewChatActivity.this, VoipActivity.class);
                video.putExtra("Sinkey", UserManager.getUser().getLoginkey());
                video.putExtra("Username", UserManager.getUser().getPhone_number());
                video.putExtra("card", getIntent().getStringExtra("card"));
                video.putExtra("targetId", getIntent().getStringExtra("username"));
                video.putExtra("send", "1");
                video.putExtra(VoipAudioActivity.ACTION, VoipAudioActivity.CALLING);
                startActivity(video);
                break;
             //语音通话
            case R.id.rlVoice:
                Intent voice = new Intent(NewChatActivity.this, VoipAudioActivity.class);
                voice.putExtra("Sinkey", UserManager.getUser().getLoginkey());
                voice.putExtra("Username", UserManager.getUser().getPhone_number());
                voice.putExtra("card", getIntent().getStringExtra("card"));
                voice.putExtra("targetId", getIntent().getStringExtra("username"));
                voice.putExtra("send", "1");
                voice.putExtra(VoipAudioActivity.ACTION, VoipAudioActivity.CALLING);
                startActivity(voice);
                break;
            case R.id.rlFile:
                PictureFileUtil.openFile(NewChatActivity.this, REQUEST_CODE_FILE);
                break;
            case R.id.common_toolbar_back:
                KeyboardUtils.hideKeyboard(mEtContent);
                finish();
                break;
            case R.id.rlLocation:
                break;
            case R.id.common_toolbar_add:
                Intent intent2 = new Intent();
                intent2.setClass(this, GroupInfoActivity.class);
                intent2.putExtra("cards", Form_uid);
                startActivity(intent2);
                break;
            case R.id.rlCamera:
                    XXPermissions.with(this)
                            .permission(Permission.CAMERA)
                            .request(new OnPermission() {
                                @Override
                                public void hasPermission(List<String> granted, boolean isAll) {
                                    // 点击拍照
                                    launchCamera();
                                }

                                @Override
                                public void noPermission(List<String> denied, boolean quick) {
                                    if (quick) {
                                        ToastUtils.show(R.string.common_permission_fail);
                                        XXPermissions.gotoPermissionSettings(NewChatActivity.this, true);
                                    } else {
                                        ToastUtils.show(R.string.common_permission_hint);
                                    }
                                }
                            });

            case R.id.rlCollection:
                Log.e("haohai","111111111111111");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    LogUtil.d("获取到的文件路径:" + filePath);
                    sendFileMessage(mSenderId, mTargetId, filePath);
                    break;
                case REQUEST_CODE_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        LogUtil.d("获取图片路径成功:" + media.getPath());
                        sendImageMessage(media);
                    }
                    break;
                case REQUEST_CODE_VEDIO:
                    // 视频选择结果回调
                    List<LocalMedia> selectListVideo = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListVideo) {
                        LogUtil.d("获取视频路径成功:" + media.getPath());
                        sendVedioMessage(media);
                    }
                    break;
                    //相机
                case REQUEST_CODE_CAMERA:
                    if (mFile.exists() && mFile.isFile()){
                        // 图片选择结果回调
//                        List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
//                        for (LocalMedia media : selectListPic) {
//                            LogUtil.d("获取图片路径成功:" + media.getPath());
//                            sendImageMessage(media);
//                        }
                    }

//
                    break;
            }
        }
    }

    //文本消息
    private void sendTextMsg(String hello) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show();
        }
        final Message mMessgae = getBaseSendMessage(MsgType.TEXT);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mMessgae.setHead_img(UserManager.getUser().getHead_img());
        mTextMsgBody.setMessage(hello);
        //mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setBody(mTextMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        Rchat(hello, "1", 0, mMessgae);
    }


    //图片消息
    private void sendImageMessage(final LocalMedia media) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show();
        }
        final Message mMessgae = getBaseSendMessage(MsgType.IMAGE);
        mMessgae.setHead_img(UserManager.getUser().getHead_img());
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        mImageMsgBody.setThumbUrl(media.getCompressPath());
        //mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        try {
            uploadpicture(mImageMsgBody.getThumbUrl(), "2", 0, mMessgae);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //视频消息
    private void sendVedioMessage(final LocalMedia media) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show();
        }
        final Message mMessgae = getBaseSendMessage(MsgType.VIDEO);
        //生成缩略图路径
        String vedioPath = media.getPath();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(vedioPath);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
        String imgname = System.currentTimeMillis() + ".jpg";
        String urlpath = Environment.getExternalStorageDirectory() + "/" + imgname;
        File f = new File(urlpath);
        try {
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            LogUtil.d("视频缩略图路径获取失败：" + e.toString());
            e.printStackTrace();
        }
        VideoMsgBody mImageMsgBody = new VideoMsgBody();
        mImageMsgBody.setExtra(urlpath);
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        //模拟两秒后发送成功
        updateMsg(mMessgae);

    }

    //文件消息
    private void sendFileMessage(String from, String to, final String path) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show();
        }
        final Message mMessgae = getBaseSendMessage(MsgType.FILE);
        FileMsgBody mFileMsgBody = new FileMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDisplayName(FileUtils.getFileName(path));
        mFileMsgBody.setSize(FileUtils.getFileLength(path));
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }

    //语音消息
    private void sendAudioMessage(final String path, long time) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show();
        }
        final Message mMessgae = getBaseSendMessage(MsgType.AUDIO);
        mMessgae.setHead_img(UserManager.getUser().getHead_img());
        AudioMsgBody mFileMsgBody = new AudioMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDuration(time);
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        try {
            uploadpicture(mFileMsgBody.getLocalPath(), "3", time, mMessgae);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Message getBaseSendMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(UUID.randomUUID() + "");
        mMessgae.setSenderId(mSenderId);
        mMessgae.setTargetId(mTargetId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENT);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }


    private Message getBaseReceiveMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(UUID.randomUUID() + "");
        mMessgae.setSenderId(mTargetId);
        mMessgae.setTargetId(mSenderId);
        mMessgae.setSentTime(System.currentTimeMillis());
        //mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }


    private void updateMsg(final Message mMessgae) {
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        int position = 0;
        mMessgae.setSentStatus(MsgSendStatus.SENT);
        //更新单个子条目
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            Message mAdapterMessage = mAdapter.getData().get(i);
            if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                position = i;
            }
        }
        mAdapter.notifyItemChanged(position);
    }


    public ResponseBody CheckDate(String date) {
        ResponseBody responseBody = gson.fromJson(RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date))), ResponseBody.class);

        return responseBody;
    }

    public String GetDate(String date) {
        return RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date)));
    }


    /**
     * 上传图片
     *
     * @param
     */
    public void uploadpicture(String path, String type, long s, Message mMessgae) throws IOException {
        Log.i("yuyin", "onSuccess: " + path);
        Map<String, String> map = new HashMap<>();
        map.clear();
        map.put("Method", "Upload");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        fileList.add(file);
        OkGo.<String>post(API.BASE_API)
                .addFileParams("File", fileList)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            updateFaildMsg(mMessgae);
                            return;
                        }
                        Log.i("yuyin", "onSuccess: " + GetDate(response.body()));
                        DynamicUpload data = gson.fromJson(GetDate(response.body()), DynamicUpload.class);

                        Rchat(data.getImg(), type, s, mMessgae);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        updateFaildMsg(mMessgae);
                    }
                });
    }


    //获取最新消息
//    private void getNewMessage() {
//        List<FrendsMessageEntity> msgList = DBHelper.queryMessageDesc(page, Form_uid);
//
//        List<Message> mReceiveMsgList = new ArrayList<>();
//
//
//            switch (msgList.get(i).getContentType()) {//消息类型
//                case 1: //文本消息
//                    Log.i("shuai", "getOldMessage:文本消息 " + i);
//                    Message mMessgaeText;
//                    if (msgList.get(i).getToType().equals("1")) {//true 发送---false为当前账户
//                        mMessgaeText = getBaseSendMessage(MsgType.TEXT);
//                        mMessgaeText.setHead_img(UserManager.getUser().getHead_img());
//                    } else {
//                        mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
//                        mMessgaeText.setHead_img(msgList.get(i).getToUserImage());
//                    }
//                    TextMsgBody mTextMsgBody = new TextMsgBody();
//                    mTextMsgBody.setMessage(msgList.get(i).getContent());//文本消息内容
//                    mMessgaeText.setBody(mTextMsgBody);
//
//                    //文本消息去重
////                    if (msgAdapterData.getBody() instanceof TextMsgBody) {
////                        TextMsgBody tmb = (TextMsgBody) msgAdapterData.getBody();
////                        if (tmb.getMessage().equals(mTextMsgBody.getMessage())) {
////                            return;
////                        }
////                    }
//                    mReceiveMsgList.add(mMessgaeText);
//                    break;
//                case 2: //图片消息
//                    Log.i("shuai", "getOldMessage:图片消息 " + i);
//                    Message mMessgaeImage;
//                    if (msgList.get(i).getToType().equals("1")) {//true 为当前账户---false为其他人
//                        mMessgaeImage = getBaseSendMessage(MsgType.IMAGE);
//                        mMessgaeImage.setHead_img(UserManager.getUser().getHead_img());
//                    } else {
//                        mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
//                        mMessgaeImage.setHead_img(msgList.get(i).getToUserImage());
//                    }
//
//                    ImageMsgBody mImageMsgBody = new ImageMsgBody();
//                    mImageMsgBody.setThumbUrl(msgList.get(i).getContent());//图片地址
//                    mMessgaeImage.setBody(mImageMsgBody);
//
//                    //图片去重
////                    if (msgAdapterData.getBody() instanceof ImageMsgBody) {
////                        ImageMsgBody tmb = (ImageMsgBody) msgAdapterData.getBody();
////                        if (tmb.getThumbUrl().equals(mImageMsgBody.getThumbUrl())) {
////                            return;
////                        }
////                    }
//
//                    mReceiveMsgList.add(mMessgaeImage);
//                    break;
//                case 3: //语音消息
//                    Message audioMessage = null;
//                    if (msgList.get(i).getToType().equals("1")) {//true 为当前账户---false为其他人
//                        audioMessage = getBaseSendMessage(MsgType.AUDIO);
//                        audioMessage.setHead_img(UserManager.getUser().getHead_img());
//                    } else {
//                        audioMessage = getBaseReceiveMessage(MsgType.AUDIO);
//                        audioMessage.setHead_img(msgList.get(i).toUserImage);
//                    }
//
//                    AudioMsgBody audioMsgBody = new AudioMsgBody();
//                    audioMsgBody.setLocalPath(msgList.get(i).getContent());//语音地址
//                    audioMsgBody.setDuration(msgList.get(i).getDuration());//语音时间
//                    audioMessage.setBody(audioMsgBody);
//
//                    //语音去重
////                    if (msgAdapterData.getBody() instanceof AudioMsgBody) {
////                        AudioMsgBody tmb = (AudioMsgBody) msgAdapterData.getBody();
////                        if (tmb.getLocalPath().equals(audioMsgBody.getLocalPath())) {
////                            return;
////                        }
////                    }
//                    mReceiveMsgList.add(audioMessage);
//                    break;
//
//            }
//        }
//
//
//        mAdapter.addData(mAdapter.getData().size(), mReceiveMsgList);
//        mAdapter.notifyDataSetChanged();
//        //mSwipeRefresh.setRefreshing(false);
//
//
//        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
//    }

    public void showESCDialog(String msg) {
        DialogIsShow = true;
        try {
            MessageDialog.Builder builder = new MessageDialog.Builder(this).setTitle("提示") // 标题可以不用填写
                    .setMessage(msg)
                    .setConfirm("确定")
                    .setCancelable(false)
                    .setCancel(null) // 设置 null 表示不显示取消按钮
                    .setAutoDismiss(false)
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            DialogIsShow = false;
                            RxActivityTool.skipActivityAndFinishAll(NewChatActivity.this, LoginActivity.class);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            DialogIsShow = false;
                        }
                    });
            messageDialog = builder.create();
            messageDialog.show();
        } catch (Exception e) {
            DialogIsShow = false;
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            FrendsMessageEntity msg = intent.getParcelableExtra("MessageListEntity");
            Message msgEntry = null;
            switch (msg.getContentType()) {//消息类型
                case 1: //文本消息

                    if (msg.getToType().equals("1")) {//true 发送---false为当前账户
                        msgEntry = getBaseSendMessage(MsgType.TEXT);
                        msgEntry.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        msgEntry = getBaseReceiveMessage(MsgType.TEXT);
                        msgEntry.setHead_img(msg.getToUserImage());
                    }
                    TextMsgBody mTextMsgBody = new TextMsgBody();
                    mTextMsgBody.setMessage(msg.getContent());//文本消息内容
                    msgEntry.setBody(mTextMsgBody);
                    msgEntry.setSentTime(msg.getTime());
                    break;
                case 2: //图片消息
                    if (msg.getToType().equals("1")) {//true 为当前账户---false为其他人
                        msgEntry = getBaseSendMessage(MsgType.IMAGE);
                        msgEntry.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        msgEntry = getBaseReceiveMessage(MsgType.IMAGE);
                        msgEntry.setHead_img(msg.toUserImage);
                    }

                    ImageMsgBody mImageMsgBody = new ImageMsgBody();
                    mImageMsgBody.setThumbUrl(msg.getContent());//图片地址
                    msgEntry.setBody(mImageMsgBody);
                    break;
                case 3: //语音消息
                    if (msg.getToType().equals("1")) {//true 为当前账户---false为其他人
                        msgEntry = getBaseSendMessage(MsgType.AUDIO);
                        msgEntry.setHead_img(UserManager.getUser().getHead_img());
                    } else {
                        msgEntry = getBaseReceiveMessage(MsgType.AUDIO);
                        msgEntry.setHead_img(msg.toUserImage);
                    }

                    AudioMsgBody audioMsgBody = new AudioMsgBody();
                    audioMsgBody.setLocalPath(msg.getContent());//语音地址
                    audioMsgBody.setDuration(msg.getDuration());//语音时间
                    msgEntry.setBody(audioMsgBody);
                    break;

            }
            mAdapter.addData(mAdapter.getData().size(),msgEntry);
            mAdapter.notifyDataSetChanged();
            mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }


    private void updateFaildMsg(final Message mMessgae) {
        //mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        int position = 0;
        mMessgae.setSentStatus(MsgSendStatus.FAILED);
        //更新单个子条目
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            Message mAdapterMessage = mAdapter.getData().get(i);
            if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                position = i;
            }
        }
        mAdapter.notifyItemChanged(position);
    }


    /**
     * 启动系统相机
     */
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            mFile = createCameraFile();
            if (mFile != null && mFile.exists()) {

                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 通过 FileProvider 创建一个 Content 类型的 Uri 文件
                    imageUri = FileProvider.getUriForFile(this, AppConfig.getPackageName() + ".provider", mFile);
                } else {
                    imageUri = Uri.fromFile(mFile);
                }
                // 对目标应用临时授权该 Uri 所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // 将拍取的照片保存到指定 Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,REQUEST_CODE_CAMERA);
            } else {
                ToastUtils.show(R.string.photo_picture_error);
            }
        } else {
            ToastUtils.show(R.string.photo_launch_fail);
        }
    }


    /**
     * 创建一个拍照图片文件对象
     */
    private File createCameraFile() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!folder.exists() || !folder.isDirectory()) {
            if (!folder.mkdirs()) {
                folder = Environment.getExternalStorageDirectory();
            }
        }

        try {
            return File.createTempFile("IMG", ".jpg", folder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
