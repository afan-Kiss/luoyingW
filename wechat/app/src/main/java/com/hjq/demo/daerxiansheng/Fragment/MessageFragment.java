package com.hjq.demo.daerxiansheng.Fragment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.appsys.EventArgs;
import com.hjq.demo.appsys.EventType;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.daerxiansheng.Activity.AddUserActivity;
import com.hjq.demo.daerxiansheng.Activity.SearchActivity;
import com.hjq.demo.daerxiansheng.Adapter.MessageAdapter;
import com.hjq.demo.daerxiansheng.Entity.MessageEntity;
import com.hjq.demo.daerxiansheng.dialog.AddUserDialog;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.daerxiansheng.sql.GroupMessageEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.guowenbin.activity.GroupChatActivity;
import com.hjq.demo.mine_chenmo.activity.NewChatActivity;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.ui.activity.ScanQrcodeActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.starrtc.demo.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-16.
 * 简述: <消息界面>
 */
public class MessageFragment extends MyLazyFragment<HomeActivity> {

    private MessageAdapter adapter;
    @BindView(R.id.recyclerView_content)
    RecyclerView recyclerview_content;
    @BindView(R.id.titlebar_title)
    TitleBar titlebar_title;
    @BindView(R.id.linearlayout_search)
    LinearLayout linearlayout_search;
    private Handler mHandler;
    private Intent intent;
    public List<MessageListEntity> redNumList;
    public int redNum;
    @BindView(R.id.swiper)
    SwipeRefreshLayout mSwipview;
    private int mCurrentChatCardPositon = -1;

    public static MessageFragment newInstance() {
        return new MessageFragment();
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        //        inster();
        //        insertMessage();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerview_content.setLayoutManager(manager);
        adapter = new MessageAdapter(getActivity(), new ArrayList<>());
        recyclerview_content.setAdapter(adapter);
        getContent();
        titlebar_title.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                //弹出popuwindow
                AddUserDialog.Builder builder = new AddUserDialog.Builder()
                        .setContext(getActivity())
                        .setOnClickListener(new AddUserDialog.onClickListener() {
                            @Override
                            public void setOnAddUserClick(AddUserDialog dialog) {

                                startActivity(AddUserActivity.class);
                                dialog.dismiss();
                            }

                            @Override
                            public void setOnQrCodeClick(AddUserDialog dialog) {
                                Intent intent = new Intent(getActivity(), ScanQrcodeActivity.class);
                                intent.putExtra("type", 1);
                                startActivity(intent);
                                dialog.dismiss();
                            }

                            @Override
                            public void setGroupChatClick(AddUserDialog dialog) {
                                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                AddUserDialog dialog = builder.onCreate();
                dialog.show();
            }
        });

        mSwipview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        linearlayout_search.setOnClickListener(v -> startActivity(SearchActivity.class));
        adapter.onItemClickListener(new MessageAdapter.setOnItemClick() {
            @Override
            public void onItemClick(View view, MessageAdapter.ViewHolder viewHolder, int position, MessageListEntity entity, List<MessageListEntity> entityList) {
                Log.i("shuai", "onItemClick");
                redNum = redNum - entity.messCount;
                Message msg = new Message();
                msg.what = redNum;
                mHandler.sendMessage(msg);
                entity.messCount = 0;
                intent = new Intent(getActivity(), NewChatActivity.class);
                //adapter.notifyDataSetChanged();
                if (entity.rtype.equals("1")) {
                    Log.i("shuai", "entity.rtype.equals(\"1\")");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            DBHelper.updateMessageList(entity);//去除未读数量
                            //adapter.setData(DBHelper.getUserMessageList());
                            //adapter.notifyDataSetChanged();
                        }
                    }.start();

                    intent.putExtra("card", entity.card);//发送人唯一标识
                    intent.putExtra("nickName", entity.nickname);
                    intent.putExtra("Rtype", entity.rtype);
                    intent.putExtra("imageUrl", entity.head_img);
                    MyApplication.mCurrentChatCard = entity.card;
                    mCurrentChatCardPositon = position;
                } else if (entity.rtype.equals("2")) {
                    Log.i("shuai", "entity.rtype.equals(\"2\")");
                    intent.setClass(getActivity(), NewChatActivity.class);
                    intent.putExtra("card", entity.card);//群组唯一标识
                    intent.putExtra("imageUrl", entity.group_img);
                    intent.putExtra("nickName", entity.group_name);
                    intent.putExtra("Rtype", entity.rtype);
                    MyApplication.mCurrentChatCard = entity.card;
                    mCurrentChatCardPositon = position;
                }


                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }

            @Override
            public void onDeleteMessageClick(View view, MessageAdapter.ViewHolder viewHolder, int position, MessageListEntity entity, List<MessageListEntity> entityList) {
                Log.i("shuai", "onDeleteMessageClick");
                adapter.closeDelete(viewHolder);
                DBHelper.deleteMessageList(entity.id);
                List<FrendsMessageEntity> msgList = DBHelper.queryMessageAllAsc(entity.card);
                MyApplication.getDaoSession().getFrendsMessageEntityDao().deleteInTx(msgList);
                entityList.remove(entity);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHeaderUserClick(View view, int position, MessageListEntity entity, List<MessageListEntity> entityList) {
                Log.i("shuai", "onItemClick");
                redNum = redNum - entity.messCount;
                Message msg = new Message();
                msg.what = redNum;
                mHandler.sendMessage(msg);
                entity.messCount = 0;
                intent = new Intent(getActivity(), NewChatActivity.class);
                //adapter.notifyDataSetChanged();
                if (entity.rtype.equals("1")) {
                    Log.i("shuai", "entity.rtype.equals(\"1\")");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            DBHelper.updateMessageList(entity);//去除未读数量
//                            adapter.setData(DBHelper.getUserMessageList());
//                            adapter.notifyDataSetChanged();
                        }
                    }.start();

                    intent.putExtra("card", entity.card);//发送人唯一标识
                    intent.putExtra("nickName", entity.nickname);
                    intent.putExtra("Rtype", entity.rtype);
                } else if (entity.rtype.equals("2")) {
                    Log.i("shuai", "entity.rtype.equals(\"2\")");
                    intent.setClass(getActivity(), NewChatActivity.class);
                    intent.putExtra("card", entity.card);//群组唯一标识
                    intent.putExtra("imageUrl", entity.group_img);
                    intent.putExtra("nickName", entity.group_name);
                    intent.putExtra("Rtype", entity.rtype);
                }


                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals("更新消息")) {
            //            Uchat();
            Log.i("shuai", "更新消息");
            getContent();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getContent();
        if (TextUtils.isEmpty(MyApplication.mCurrentChatCard)) return;
        List<MessageListEntity> spList = DBHelper.getUserMessageList();
        for (int i = 0; i < spList.size(); i++) {
            if (spList.get(i).card.equals(MyApplication.mCurrentChatCard)) {
                MessageListEntity entity = spList.get(i);
                entity.messCount = 0;
                DBHelper.updateMessageList(entity);//去除未读数量
            }
        }

        adapter.notifyDataSetChanged();
        mCurrentChatCardPositon = -1;
        MyApplication.mCurrentChatCard = null;
    }


    void getContent() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            List<MessageListEntity> spList = DBHelper.getUserMessageList();
            if (spList != null && spList.size() > 0) {
                adapter.setData(spList);
            }
            return;
        }
        Log.i("shuai", "更新消息getContent");
        //        showLoading();
        map.clear();
        map.put("Method", "Uchat");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        OkGo.<String>get(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            Log.i("shuai", "更新消息getContent getState() != 1 return");
                            toast(CheckDate(response.body()).getMsg());
                            return;
                        }

                        MessageEntity entity = gson.fromJson(GetDate(response.body()), MessageEntity.class);
                        for (MessageListEntity messageListEntity : entity.all_array) {
                            messageListEntity.setMessage_id(messageListEntity.getId() + "");
                        }
                        Gson gson = new Gson();
                        Log.d("adskjfadsf", "onSuccess: " + gson.toJson(entity) + "消息数量" +
                                entity.all_array.size());
                        List<MessageListEntity> spList = null;
                        int messageCount = 0;
                        if (entity != null) {
                            EventArgs<String> eventArgs = new EventArgs<>();
                            eventArgs.setEventType(EventType.Event_MessageCode);
                            eventArgs.setObject(entity.features_chat);
                            EventBus.getDefault().post(eventArgs);
                            if (entity.all_array != null && entity.all_array.size() > 0) {
                                MessageListEntity newListEntity = null;
                                MessageListEntity message = null;
                                boolean isnull;

                                voicePrompt();
                                spList = DBHelper.getUserMessageList();
                                if (spList != null && spList.size() > 0) {
                                    //循环本地存储的消息和请求到的消息是否重复了重复了替换最新的消息
                                    for (int i = 0; i < entity.all_array.size(); i++) {
                                        message = null;
                                        isnull = false;
                                        newListEntity = null;
                                        //添加本地聊天记录
                                        if (entity.all_array.get(i).rtype.equals("1")) {
                                            addFrendMessage(entity.all_array.get(i));
                                        } else if (entity.all_array.get(i).rtype.equals("2")) {
                                            addGroupMessage(entity.all_array.get(i));
                                        }

                                        for (int j = 0; j < spList.size(); j++) {
                                            //好友聊天去除重复消息
                                            if (entity.all_array.get(i).rtype.equals("1")) {
                                                if (entity.all_array.get(i).rtype.equals("1") &&
                                                        spList.get(j).rtype.equals("1")) {
                                                    if (entity.all_array.get(i).card.equals(spList.get(j).card)) {
                                                        if (spList.get(j).messCount > 0) {
                                                            entity.all_array.get(i).messCount =
                                                                    spList.get(j).messCount + 1;
                                                        } else {
                                                            entity.all_array.get(i).messCount++;
                                                        }
                                                        entity.all_array.get(i).id = spList.get(j).id;
                                                        message = entity.all_array.get(i);
                                                        isnull = true;
                                                    } else {
                                                        isnull = false;
                                                        try {
                                                            newListEntity = entity.all_array.get(i).clone();
                                                        } catch (CloneNotSupportedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        newListEntity.messCount = 1;
                                                    }
                                                } else {
                                                    isnull = false;
                                                    try {
                                                        newListEntity = entity.all_array.get(i).clone();
                                                    } catch (CloneNotSupportedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    newListEntity.messCount = 1;
                                                }
                                            } else if (entity.all_array.get(i).rtype.equals("2")) {
                                                if (entity.all_array.get(i).rtype.equals("2") &&
                                                        spList.get(j).rtype.equals("2")) {
                                                    if (entity.all_array.get(i).group_id.equals(spList.get(j).group_id)) {
                                                        if (spList.get(j).messCount > 0) {
                                                            entity.all_array.get(i).messCount =
                                                                    spList.get(j).messCount + 1;
                                                        } else {
                                                            entity.all_array.get(i).messCount++;
                                                        }
                                                        entity.all_array.get(i).id = spList.get(j).id;
                                                        message = entity.all_array.get(i);
                                                        isnull = true;
                                                    } else {
                                                        isnull = false;
                                                        newListEntity = entity.all_array.get(i);
                                                        newListEntity.messCount = 1;
                                                    }
                                                } else {
                                                    isnull = false;
                                                    newListEntity = entity.all_array.get(i);
                                                    newListEntity.messCount = 1;
                                                }
                                            }
                                        }
                                        if (message != null) {
                                            addMessageList(message, message.messCount, true);
                                        } else {
                                            if (!isnull) {
                                                if (newListEntity != null) {
                                                    addMessageList(newListEntity, newListEntity.messCount, false);
                                                    spList = DBHelper.getUserMessageList();
                                                }
                                            }
                                        }
                                    }


                                }

                                //列表无消息
                                else {
                                    String tempRval = entity.all_array.get(0).rval;
                                    if (entity.all_array.get(0).rclass.equals("1")) {
                                        entity.all_array.get(0).rval = RxEncryptTool.setDecrypt(entity.all_array
                                                .get(0).rval);
                                    }
                                    message = entity.all_array.get(0);
                                    message.messCount++;
                                    message.currentuserCard = UserManager.getUser().getCard();
                                    addMessageList(message, message.messCount, false);
                                    if (entity.all_array.size() > 1) {
                                        spList = DBHelper.getUserMessageList();
                                        if (spList != null && spList.size() > 0) {
                                            //循环本地存储的消息和请求到的消息是否重复了重复了替换最新的消息
                                            for (int i = 0; i < entity.all_array.size(); i++) {
                                                message = null;
                                                isnull = false;
                                                newListEntity = null;
                                                //添加本地聊天记录
                                                if (entity.all_array.get(i).rtype.equals("1")) {
                                                    addFrendMessage(entity.all_array.get(i));
                                                } else if (entity.all_array.get(i).rtype.equals("2")) {
                                                    addGroupMessage(entity.all_array.get(i));
                                                }
                                                for (int j = 0; j < spList.size(); j++) {
                                                    //好友聊天去除重复消息
                                                    if (entity.all_array.get(i).rtype.equals("1")) {
                                                        if (entity.all_array.get(i).rtype.equals("1") &&
                                                                spList.get(j).rtype.equals("1")) {
                                                            if (entity.all_array.get(i).card.equals(spList.get(j).card)) {
                                                                if (spList.get(j).messCount > 0) {
                                                                    entity.all_array.get(i).messCount =
                                                                            spList.get(j).messCount +
                                                                                    1;
                                                                } else {
                                                                    entity.all_array.get(i).messCount++;
                                                                }
                                                                entity.all_array.get(i).id = spList.get(j).id;
                                                                message = entity.all_array.get(i);
                                                                isnull = true;
                                                            }
                                                        } else {
                                                            isnull = false;
                                                            try {
                                                                newListEntity = entity.all_array.get(i).clone();
                                                            } catch (CloneNotSupportedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            newListEntity.messCount = 1;
                                                        }
                                                    } else if (entity.all_array.get(i).rtype.equals("2")) {
                                                        if (entity.all_array.get(i).rtype.equals("2") &&
                                                                spList.get(j).rtype.equals("2")) {
                                                            if (entity.all_array.get(i).group_id.equals(spList.get(j).group_id)) {
                                                                if (spList.get(j).messCount > 0) {
                                                                    entity.all_array.get(i).messCount =
                                                                            spList.get(j).messCount +
                                                                                    1;
                                                                } else {
                                                                    entity.all_array.get(i).messCount++;
                                                                }
                                                                entity.all_array.get(i).id = spList.get(j).id;
                                                                message = entity.all_array.get(i);
                                                                isnull = true;
                                                            }
                                                        } else {
                                                            isnull = false;
                                                            try {
                                                                newListEntity = entity.all_array.get(i).clone();
                                                            } catch (CloneNotSupportedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            newListEntity.messCount = 1;
                                                        }
                                                    }
                                                }
                                                if (message != null) {
                                                    addMessageList(message, message.messCount, true);
                                                } else {
                                                    if (!isnull) {
                                                        if (newListEntity != null) {
                                                            addMessageList(newListEntity, newListEntity.messCount, false);
                                                            spList = DBHelper.getUserMessageList();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        message.rval = tempRval;
                                        addFrendMessage(message);
                                    }

                                }
                            }
                            spList = DBHelper.getUserMessageList();
                            adapter.setData(spList);
                            redNumList = spList;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    redNum = 0;
                                    if (redNumList != null) {
                                        for (int i = 0; i < redNumList.size(); i++) {
                                            redNum = redNum + redNumList.get(i).messCount;
                                        }
                                        Message msg = new Message();
                                        msg.what = redNum;
                                        mHandler.sendMessage(msg);
                                    }

                                }
                            }).start();
                        }

//                        CharModel charModel;
//                        FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
//                        try
//                        {
//                            charModel = gson.fromJson(GetDate(response.body()), CharModel.class);
//                            if (charModel.getAll_array().size() == 0)
//                            {
//                                Log.i("shuai", "更新消息getAll_array().size() == 0 return");
//                                return;
//                            }
//                            for (int i = 0; i < charModel.getAll_array().size(); i++)
//                            {
//                                //添加消息记录
//                                frendsMessageEntity.setContentType(Integer.parseInt(charModel.getAll_array()
//                                                                                            .get(i)
//                                                                                            .getRclass()));//1=文本消息 2=图片 3=音频
//                                frendsMessageEntity.setToUserName(charModel.getAll_array()
//                                                                          .get(i)
//                                                                          .getNickname());//用户昵称
//                                frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
//                                frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
//                                frendsMessageEntity.setMessageType(Integer.parseInt(charModel.getAll_array()
//                                                                                            .get(i)
//                                                                                            .getRtype()));//消息类型 1好友 2群组 3虚拟app
//                                frendsMessageEntity.setToUid(charModel.getAll_array()
//                                                                     .get(i)
//                                                                     .getCard());
//                                frendsMessageEntity.setCard(UserManager.getUser().getCard());
//                                frendsMessageEntity.setUserName("");//用户电话
//                                frendsMessageEntity.setToUserImage(charModel.getAll_array()
//                                                                           .get(i)
//                                                                           .getHead_img());
//                                frendsMessageEntity.setToType("2");//消息类型 1发送 2接收
//
//                                if (frendsMessageEntity.getContentType() == 1){
//                                    frendsMessageEntity.setContent(GetDate(charModel.getAll_array()
//                                            .get(i)
//                                            .getRval()));//文本消息内容
//                                }else {
//                                    frendsMessageEntity.setContent(charModel.getAll_array()
//                                            .get(i)
//                                            .getRval());//图片、语音消息内容
//                                }
//
//
//                                frendsMessageEntity.setDuration(charModel.getAll_array()
//                                        .get(i)
//                                        .getDuration());//消息内容
//                            }
//                        }
//                        catch (Exception e)
//                        {
//                            Log.i("shuai异常：", e.toString());
//                        }
//
//                        DBHelper.insertMessage(frendsMessageEntity);
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
     * 添加好友聊天记录
     *
     * @param entity
     */
    void addFrendMessage(MessageListEntity entity) {
        //添加消息记录
        if (!TextUtils.isEmpty(entity.rtype)) {
            if (entity.rtype.equals("1")) {
                if (entity.rclass.equals("1")) {
                    entity.rval = RxEncryptTool.setDecrypt(entity.rval);
                }
                FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
                frendsMessageEntity.setContent(entity.rval);//消息内容
                frendsMessageEntity.setContentType(Integer.parseInt(entity.rclass));//1=文本消息 2=图片 3=音频
                frendsMessageEntity.setToUserName(entity.nickname);//用户昵称
                frendsMessageEntity.setDuration(Integer.parseInt(entity.duration));//语音时长秒
                frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
                frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
                frendsMessageEntity.setMessageType(Integer.parseInt(entity.rtype));//消息类型 1好友 2群组 3虚拟app
                frendsMessageEntity.setToUid(entity.card);//好友card
                frendsMessageEntity.setCard(UserManager.getUser().getCard());//当前用户card标记多用户数据
                frendsMessageEntity.setUserName("");//用户电话
                frendsMessageEntity.setToUserImage(entity.head_img);//用户头像
                frendsMessageEntity.setToType("2");//消息类型 2 接收  1发送
                frendsMessageEntity.setMessage_id(entity.message_id);
                if (frendsMessageEntity.getToUid().equals(MyApplication.mCurrentChatCard)) {
                    Intent intent = new Intent("android.intent.action.new_message");
                    intent.putExtra("MessageListEntity", frendsMessageEntity);
                    getActivity().sendBroadcast(intent);
                }
                DBHelper.insertMessage(frendsMessageEntity);
            }
        }
    }

    void addGroupMessage(MessageListEntity entity) {
        if (!TextUtils.isEmpty(entity.rtype)) {
            if (entity.rtype.equals("2")) {
                if (entity.rclass.equals("1")) {
                    entity.rval = RxEncryptTool.setDecrypt(entity.rval);
                }
                GroupMessageEntity groupMessageEntity = new GroupMessageEntity();
                groupMessageEntity.groupCard = entity.card;//群组唯一标识
                groupMessageEntity.content = entity.rval;//消息内容
                groupMessageEntity.contentType = Integer.parseInt(entity.rclass);//1文本消息 2图片 3音频
                groupMessageEntity.messageType = Integer.parseInt(entity.rtype);//消息类型 1好友 2群组 3虚拟app
                groupMessageEntity.toUid = entity.user_id;//发送对象唯一标识
                groupMessageEntity.toUserName = entity.nickname;//发送对象用户昵称
                groupMessageEntity.Duration = Integer.parseInt(entity.duration);//语音消息时长(秒)
                groupMessageEntity.messageSendingType = 1;//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
                groupMessageEntity.Time = Integer.parseInt(entity.rtime);//时间戳
                groupMessageEntity.userCard = entity.user_card;//用户唯一标识
                groupMessageEntity.toUserImage = entity.head_img;//用户头像
//                if (groupMessageEntity.groupCard.equals(mCurrentChatCard)) {
//                    Intent intent = new Intent("android.intent.action.new_message");
//                    intent.putExtra("MessageListEntity", groupMessageEntity);
//                    getActivity().sendBroadcast(intent);
//                }

                DBHelper.insertMessageGroup(groupMessageEntity);
            }
        }
    }


    /**
     * 添加或者替换消息列表数据
     *
     * @param item
     * @param messageCount
     * @param isReplace
     */
    void addMessageList(MessageListEntity item, int messageCount, boolean isReplace) {
        MessageListEntity message = new MessageListEntity();
        message.card = item.card;
        message.currentuserCard = UserManager.getUser().getCard();
        message.disturb = item.disturb;
        message.duration = item.duration;
        message.group_id = item.group_id;
        message.group_img = item.group_img;
        message.group_name = item.group_name;
        message.message_id = item.message_id;
        message.messCount = messageCount;
        message.nickname = item.nickname;
        message.user_card = item.user_card;
        message.rclass = item.rclass;
        message.rtime = item.rtime;
        message.rtype = item.rtype;
        message.head_img = item.head_img;
        message.rval = item.rval;
        message.message_id = item.message_id;
        //true 替换消息 添加消息
        if (isReplace) {
            message.id = item.id;
            DBHelper.insertMessageListReplace(message);
        } else {

            DBHelper.insertMessageList(message);
            List<MessageListEntity> spList = DBHelper.getUserMessageList();
//            for (int i = 0; i < spList.size(); i++) {
//                if (!TextUtils.isEmpty(spList.get(i).getMessage_id()) && spList.get(i).getMessage_id().equals(item.getMessage_id())){
//                    return;
//                }
//            }
            //addFrendFirstMessage(item);

        }

    }


    @Override
    protected void initData() {


    }

    @Override
    public void onEvent(EventArgs eventArgs) {
//        super.onEvent(eventArgs);
//
//        Log.i("shuai", "onEvent 495");
//        if (eventArgs.getEventType().equals(EventType.Event_RefershMessage))
//        {
//            getContent();
//        }
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }


    /**
     * 语音播报
     **/
    public void voicePrompt() {
        MediaPlayer mPlayer = MediaPlayer.create(getActivity(), R.raw.jieshouxiaoxi);
        mPlayer.start();
    }

    private void reloadData() {
        getContent();
        mSwipview.setRefreshing(false);
    }

    /**
     * 添加好友第一条聊天记录
     *
     * @param entity
     */
    void addFrendFirstMessage(MessageListEntity entity) {
        //添加消息记录
        if (!TextUtils.isEmpty(entity.rtype)) {
            if (entity.rtype.equals("1")) {
                FrendsMessageEntity frendsMessageEntity = new FrendsMessageEntity();
                frendsMessageEntity.setContent(entity.rval);//消息内容
                frendsMessageEntity.setContentType(Integer.parseInt(entity.rclass));//1=文本消息 2=图片 3=音频
                frendsMessageEntity.setToUserName(entity.nickname);//用户昵称
                frendsMessageEntity.setDuration(Integer.parseInt(entity.duration));//语音时长秒
                frendsMessageEntity.setTime(TimeUtils.getTimeLong());//当前时间
                frendsMessageEntity.setMessageSendingType(1);//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
                frendsMessageEntity.setMessageType(Integer.parseInt(entity.rtype));//消息类型 1好友 2群组 3虚拟app
                frendsMessageEntity.setToUid(entity.card);//好友card
                frendsMessageEntity.setCard(UserManager.getUser().getCard());//当前用户card标记多用户数据
                frendsMessageEntity.setUserName("");//用户电话
                frendsMessageEntity.setToUserImage(entity.head_img);//用户头像
                frendsMessageEntity.setToType("2");//消息类型 2 接收  1发送
//                if (frendsMessageEntity.getToUid().equals(MyApplication.mCurrentChatCard)){
//                    Intent intent = new Intent("android.intent.action.new_message");
//                    intent.putExtra("MessageListEntity", frendsMessageEntity);
//                    getActivity().sendBroadcast(intent);
//                }

                DBHelper.insertMessage(frendsMessageEntity);

            }
        }
    }
}
