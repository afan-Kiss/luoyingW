package com.hjq.demo.mine_chenmo.chatui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.google.gson.Gson;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.mine_chenmo.activity.NewChatActivity;
import com.hjq.demo.mine_chenmo.chatui.bean.AudioMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.FileMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.ImageMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.Message;
import com.hjq.demo.mine_chenmo.chatui.bean.MsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.MsgSendStatus;
import com.hjq.demo.mine_chenmo.chatui.bean.MsgType;
import com.hjq.demo.mine_chenmo.chatui.bean.TextMsgBody;
import com.hjq.demo.mine_chenmo.chatui.bean.VideoMsgBody;
import com.hjq.demo.mine_chenmo.chatui.util.GlideUtils;
import com.hjq.demo.mine_chenmo.chatui.widget.BubbleImageView;
import com.hjq.demo.model.ResponseBody;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.TimeUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {
    public Map<String, String> map = new HashMap<>();
    Gson gson = new Gson();
    PopupWindow popupWindow;
    private static final int TYPE_SEND_TEXT = 1;
    private static final int TYPE_RECEIVE_TEXT = 2;
    private static final int TYPE_SEND_IMAGE = 3;
    private static final int TYPE_RECEIVE_IMAGE = 4;
    private static final int TYPE_SEND_VIDEO = 5;
    private static final int TYPE_RECEIVE_VIDEO = 6;
    private static final int TYPE_SEND_FILE = 7;
    private static final int TYPE_RECEIVE_FILE = 8;
    private static final int TYPE_SEND_AUDIO = 9;
    private static final int TYPE_RECEIVE_AUDIO = 10;

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
    private static final int SEND_FILE = R.layout.item_file_send;
    private static final int RECEIVE_FILE = R.layout.item_file_receive;
    private static final int RECEIVE_AUDIO = R.layout.item_audio_receive;
    private static final int SEND_AUDIO = R.layout.item_audio_send;
    /*
    private static final int SEND_LOCATION = R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;*/

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public ChatAdapter(Context context, List<Message> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<Message>() {
            @Override
            protected int getItemType(Message entity) {
                boolean isSend = entity.getSenderId().equals(NewChatActivity.mSenderId);
                if (MsgType.TEXT == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
                } else if (MsgType.IMAGE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
                } else if (MsgType.VIDEO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_VIDEO : TYPE_RECEIVE_VIDEO;
                } else if (MsgType.FILE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_FILE : TYPE_RECEIVE_FILE;
                } else if (MsgType.AUDIO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_AUDIO : TYPE_RECEIVE_AUDIO;
                }
                return 0;
            }
        });
        getMultiTypeDelegate().registerItemType(TYPE_SEND_TEXT, SEND_TEXT)
                .registerItemType(TYPE_RECEIVE_TEXT, RECEIVE_TEXT)
                .registerItemType(TYPE_SEND_IMAGE, SEND_IMAGE)
                .registerItemType(TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE)
                .registerItemType(TYPE_SEND_VIDEO, SEND_VIDEO)
                .registerItemType(TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO)
                .registerItemType(TYPE_SEND_FILE, SEND_FILE)
                .registerItemType(TYPE_RECEIVE_FILE, RECEIVE_FILE)
                .registerItemType(TYPE_SEND_AUDIO, SEND_AUDIO)
                .registerItemType(TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        setContent(helper, item);

        setStatus(helper, item);
        setOnClick(helper, item);
        setPopWindow(helper, item);
    }

    private void setPopWindow(BaseViewHolder helper, Message item) {
        // 长按弹出收藏、撤回等功能
        View chat_item_layout_content = helper.getView(R.id.chat_item_layout_content);
        RelativeLayout rlAudio = helper.getView(R.id.rlAudio);
        BubbleImageView bivPic = helper.getView(R.id.bivPic);
        //文字
        if (null != chat_item_layout_content && item.getBody() != null) {
            chat_item_layout_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        if (((TextMsgBody) item.getBody()).getMessage().length() > 0) {
                            showPopWindow(v, item, helper.getAdapterPosition());
                        }
                    } catch (Exception e) {
                    }

                    return false;
                }
            });
        }

//        //录音
        if (null != rlAudio) {
            rlAudio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v, item, helper.getAdapterPosition());
                    return false;
                }
            });
        }

        //图片
        if (null != bivPic) {
            bivPic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v, item, helper.getAdapterPosition());
                    return false;
                }
            });
        }
    }


    private void setStatus(BaseViewHolder helper, Message item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof TextMsgBody
                || msgContent instanceof AudioMsgBody || msgContent instanceof VideoMsgBody || msgContent instanceof FileMsgBody) {
            //只需要设置自己发送的状态
            MsgSendStatus sentStatus = item.getSentStatus();
            boolean isSend = item.getSenderId().equals(NewChatActivity.mSenderId);
            if (isSend) {
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, true).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            }
        } else if (msgContent instanceof ImageMsgBody) {
            boolean isSend = item.getSenderId().equals(NewChatActivity.mSenderId);
            if (isSend) {
                MsgSendStatus sentStatus = item.getSentStatus();
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            } else {

            }
        }


    }

    private void setContent(BaseViewHolder helper, Message item) {
        // GlideUtils.loadChatImage(mContext, item.getHead_img(), (ImageView) helper.getView(R.id.chat_item_header));
        //ImageLoader.with(mContext).load(item.getHead_img()).into(helper.getView(R.id.chat_item_header));
        ImageView imageView = helper.getView(R.id.chat_item_header);
        GlideUtils.loadRoundedCornersImage(mContext, item.getHead_img(), imageView);
        TextView mTV_time = helper.getView(R.id.item_tv_time);
        long sendTime = item.getSentTime();
        long currentTime = System.currentTimeMillis();
        //如果大于5分钟
        if (currentTime - sendTime > 5 * 60 * 1000) {
            mTV_time.setVisibility(View.VISIBLE);
            mTV_time.setText(TimeUtils.getTimeString(item.getSentTime()));
        } else {
            mTV_time.setVisibility(View.GONE);
        }
        if (item.getMsgType().equals(MsgType.TEXT)) {
            TextMsgBody msgBody = (TextMsgBody) item.getBody();
            try {

                if (msgBody == null || msgBody.getMessage() == null || msgBody.getMessage().length() == 0) {
                    TextView txtTv = helper.getView(R.id.chat_item_content_text);
                    txtTv.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    txtTv.setLayoutParams(layoutParams);
                    LinearLayout linearLayout = ((LinearLayout) txtTv.getParent());
                    linearLayout.setBackgroundColor(Color.TRANSPARENT);
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    linearLayout.setLayoutParams(layoutParams1);

                    if (item.getTargetId().equals("left")) {
                        txtTv.setText("你撤回了一条消息");
                    } else {
                        txtTv.setText("对方撤回了一条消息");
                    }

                    txtTv.setBackground(null);
                    GlideUtils.dismissImage(imageView);
                } else {
                    TextView txtTv = helper.getView(R.id.chat_item_content_text);
                    txtTv.setText(msgBody.getMessage());
                    if (item.getTargetId().equals("right")) {
                        txtTv.setBackgroundResource(R.drawable.message_text_receive);
                    } else {
                        txtTv.setBackgroundResource(R.drawable.message_text_send);
                    }


                    if (item.getTargetId().equals("right")) {
                        txtTv.setGravity(Gravity.CENTER);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                        txtTv.setLayoutParams(layoutParams);
                        LinearLayout linearLayout = ((LinearLayout) txtTv.getParent());
                        linearLayout.setBackgroundColor(Color.TRANSPARENT);
                        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams1.addRule(RelativeLayout.RIGHT_OF, R.id.chat_item_header);
                        linearLayout.setLayoutParams(layoutParams1);
                    } else {
                        txtTv.setGravity(Gravity.CENTER);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                        txtTv.setLayoutParams(layoutParams);
                        LinearLayout linearLayout = ((LinearLayout) txtTv.getParent());
                        linearLayout.setBackgroundColor(Color.TRANSPARENT);
                        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams1.addRule(RelativeLayout.LEFT_OF, R.id.chat_item_header);
                        linearLayout.setLayoutParams(layoutParams1);
                    }


                    //targetId = "left"
                    GlideUtils.showImage(imageView);
//                    helper.setText(R.id.chat_item_content_text, msgBody.getMessage());
                }
            } catch (Exception e) {
                helper.setText(R.id.chat_item_content_text, "你撤回了一条消息");
            }
        } else if (item.getMsgType().equals(MsgType.IMAGE)) {
            ImageMsgBody msgBody = (ImageMsgBody) item.getBody();
            if (TextUtils.isEmpty(msgBody.getThumbPath())) {
                GlideUtils.loadChatImage(mContext, msgBody.getThumbUrl(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                File file = new File(msgBody.getThumbPath());
                if (file.exists()) {
                    GlideUtils.loadChatImage(mContext, msgBody.getThumbPath(), (ImageView) helper.getView(R.id.bivPic));
                } else {
                    GlideUtils.loadChatImage(mContext, msgBody.getThumbUrl(), (ImageView) helper.getView(R.id.bivPic));
                }
            }
        } else if (item.getMsgType().equals(MsgType.VIDEO)) {
            VideoMsgBody msgBody = (VideoMsgBody) item.getBody();
            File file = new File(msgBody.getExtra());
            if (file.exists()) {
                GlideUtils.loadChatImage(mContext, msgBody.getExtra(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                GlideUtils.loadChatImage(mContext, msgBody.getExtra(), (ImageView) helper.getView(R.id.bivPic));
            }
        } else if (item.getMsgType().equals(MsgType.FILE)) {
            FileMsgBody msgBody = (FileMsgBody) item.getBody();
            helper.setText(R.id.msg_tv_file_name, msgBody.getDisplayName());
            helper.setText(R.id.msg_tv_file_size, msgBody.getSize() + "B");
        } else if (item.getMsgType().equals(MsgType.AUDIO)) {
            AudioMsgBody msgBody = (AudioMsgBody) item.getBody();
            helper.setText(R.id.tvDuration, msgBody == null ? "" + "\"" : msgBody.getDuration() + "\"");
//            helper.setText(R.id.chat_item_content_text, "你撤回了一条消息");

        }

    }

    private void showPopWindow(View view, Message item, int positon) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentview = inflater.inflate(R.layout.popwindow_chat, null);//自己的弹框布局


        autoHiden(contentview, item);
        //收藏
        TextView mPop_collect_tv = contentview.findViewById(R.id.pop_collect_tv);
        mPop_collect_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getMsgType().equals(MsgType.TEXT)) {
                    TextMsgBody msgBody = (TextMsgBody) item.getBody();
                    messageColect(msgBody.getMessage(), 1);
                } else if (item.getMsgType().equals(MsgType.IMAGE)) {
                    ImageMsgBody msgBody = (ImageMsgBody) item.getBody();
                    messageColect(msgBody.getThumbUrl(), 2);
                }

            }
        });
        //复制
        TextView mPop_copy_tv = contentview.findViewById(R.id.pop_copy_tv);
        mPop_copy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getMsgType().equals(MsgType.TEXT)) {
                    TextMsgBody msgBody = (TextMsgBody) item.getBody();
                    copy(msgBody.getMessage());
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    ToastUtils.show("已复制");
                }

            }
        });
        //删除
        TextView mPop_delete_tv = contentview.findViewById(R.id.pop_delete_tv);
        mPop_delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                //删除消息
                List<FrendsMessageEntity> msgList = DBHelper.queryMessageAllAsc(getData().get(positon).getToUid());
                for (int i = 0; i < msgList.size(); i++) {
                    if (TextUtils.isEmpty(msgList.get(i).getMessage_id()) && msgList.get(i).getMessage_id() == null) {
                        MyApplication.getDaoSession().getFrendsMessageEntityDao().deleteInTx(msgList.get(i));
                    }
                    if (!TextUtils.isEmpty(msgList.get(i).getMessage_id()) && msgList.get(i).getMessage_id().equals(item.getMsgId())) {
                        MyApplication.getDaoSession().getFrendsMessageEntityDao().deleteInTx(msgList.get(i));
                    }
                }

                getData().remove(positon);
                notifyDataSetChanged();
                ToastUtils.show("已删除");

            }
        });
        //撤回
        TextView mPop_recall_tv = contentview.findViewById(R.id.pop_recall_tv);
        mPop_recall_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recallMessage(item, positon);
            }
        });
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(contentview, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        contentview.setOnKeyListener(new View.OnKeyListener() {//监听系统返回键
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();

                    return true;
                }
                return false;
            }
        });
        popupWindow.showAsDropDown(view, -50, -160);
    }

    private void autoHiden(View root, Message item) {


        if (item.getMsgType().compareTo(MsgType.TEXT) == 0) {
            //收藏
            if (root.getId() == R.id.pop_collect_tv) {

            } else if (root.getId() == R.id.pop_copy_tv) {

            } else if (root.getId() == R.id.pop_delete_tv) {

            } else if (root.getId() == R.id.pop_recall_tv) {

            } else if (root.getId() == R.id.pop_recall_transt) {

            }
        }




    }


    private void setOnClick(BaseViewHolder helper, Message item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof AudioMsgBody) {
            helper.addOnClickListener(R.id.rlAudio);
        }
        if (msgContent instanceof ImageMsgBody) {
            helper.addOnClickListener(R.id.bivPic);
        }
    }


    //收藏消息
    void messageColect(String cval, int ctype) {
        map.clear();
        map.put("Method", "Collection");
        map.put("Cval", cval);
        map.put("Ctype", ctype + "");
        map.put("Fid", UserManager.getUser().getUser_id());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("123123123", "onSuccess: " + GetDate(response.body()));
                        if (CheckDate(response.body()).getState() != 1) {
                            CheckDate(response.body()).getMsg();
                            return;
                        }
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(mContext, "服务器错误", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                });


    }

    //撤回消息
    void recallMessage(Message mMessage, int position) {
        map.clear();
        map.put("Method", "Dchat");
        map.put("Rid", mMessage.getMsgId());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("123123123", "onSuccess: " + GetDate(response.body()));
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                        if (CheckDate(response.body()).getState() != 1) {
                            CheckDate(response.body()).getMsg();
                            return;
                        }

                        mMessage.setMsgType(MsgType.TEXT);
                        messageUpdate(mMessage, position);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(mContext, "服务器错误", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                });


    }

    private void messageUpdate(Message mMessage, int position) {
        mMessage.setBody(null);
        mMessage.setMsgType(MsgType.TEXT);
        updateMsg(mMessage);
        //修改消息
        List<FrendsMessageEntity> msgList = DBHelper.queryMessageAllAsc(getData().get(position).getToUid());
        for (int i = 0; i < msgList.size(); i++) {
            if (!TextUtils.isEmpty(msgList.get(i).getMessage_id()) && msgList.get(i).getMessage_id().equals(mMessage.getMsgId())) {
                msgList.get(i).setContent("");
                msgList.get(i).setContentType(1);
                FrendsMessageEntity frendsMessageEntity = msgList.get(i);
                MyApplication.getDaoSession().getFrendsMessageEntityDao().update(frendsMessageEntity);
            }
        }
//
//                        //更改消息列表内容
        MessageListEntity message = new MessageListEntity();
        if (position == getData().size() - 1) {
//                            message.setId(mMessage.getFrendsMessageId());
            message.card = msgList.get(position).card;
            message.currentuserCard = UserManager.getUser().getCard();
            message.disturb = 0 + "";
            message.duration = msgList.get(position).getDuration() + "";
            message.group_id = "";
            message.group_img = msgList.get(position).getToUserImage();
            message.group_name = msgList.get(position).getToUserName();
            message.message_id = mMessage.getMsgId();
            message.messCount = 0;

            message.nickname = msgList.get(position).getUserName();
            message.user_card = UserManager.getUser().getCard();
            message.rclass = 1 + "";
            message.rtime = TimeUtils.getTimeLong() + "";
            message.rtype = mMessage.getRtype();
            message.head_img = mMessage.getHead_img();
            if (mMessage.getTargetId().equals("left")) {
                message.rval = "对方回了一条消息";
            } else {
                message.rval = "你撤回了一条消息";
            }

            message.message_id = mMessage.getMsgId();
            List<MessageListEntity> spList = DBHelper.getUserMessageList();
            for (int i = 0; i < spList.size(); i++) {
                if (spList.get(i).getNickname().equals(message.nickname)) {
                    DBHelper.deleteMessageList(spList.get(i).id);
                }
                if (spList.get(i).getMessage_id() != null) {
                    if (spList.get(i).getMessage_id().equals(message.getMessage_id())) {
                        message.setId(spList.get(i).getId());
//                                        DBHelper.updateMessageList(message);
                    }
                }
            }


        }

        notifyDataSetChanged();
    }

    private void updateMsg(final Message mMessgae) {
//        scrollToPosition(getItemCount() - 1);
        int position = 0;
        mMessgae.setSentStatus(MsgSendStatus.SENT);
        //更新单个子条目
        for (int i = 0; i < getData().size(); i++) {
            Message mAdapterMessage = getData().get(i);
            if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                position = i;
            }
        }
        notifyItemChanged(position);
    }

    public ResponseBody CheckDate(String date) {
        ResponseBody responseBody = gson.fromJson(RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date))), ResponseBody.class);

        return responseBody;
    }

    public String GetDate(String date) {
        return RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date)));
    }

    //复制
    private void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }


    /**
     * 對法撤回方法
     *
     * @param messageId 對法消息的id
     */
    public void replaceReceviver(String messageId) {
        Message message = null;
        int positon = -1;
        try {
            for (int i = 0; i < getData().size(); i++) {
                if (messageId.equals(getData().get(i).getMsgId())) {
                    message = getData().get(i);
                    positon = i;
                }
            }
            messageUpdate(message, positon);
        } catch (Exception e) {
            Log.i(TAG, "replaceReceviver: 撤回失敗---" + e.getMessage());
        }
    }

}
