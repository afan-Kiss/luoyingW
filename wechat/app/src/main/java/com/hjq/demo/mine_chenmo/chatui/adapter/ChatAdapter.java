package com.hjq.demo.mine_chenmo.chatui.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.hjq.demo.R;
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
import com.hjq.demo.util.TimeUtils;

import java.io.File;
import java.util.List;

public class ChatAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

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
        //setPopWindow(helper);
    }

    private void setPopWindow(BaseViewHolder helper) {
        // 长按弹出收藏、撤回等功能
        LinearLayout chat_item_layout_content = helper.getView(R.id.chat_item_layout_content);
        RelativeLayout rlAudio = helper.getView(R.id.rlAudio);
        BubbleImageView bivPic = helper.getView(R.id.bivPic);
        if (null != chat_item_layout_content) {
            chat_item_layout_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v);
                    return false;
                }
            });
        }

        if (null != rlAudio) {
            rlAudio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v);
                    return false;
                }
            });
        }
        if (null != bivPic) {
            bivPic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v);
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
        GlideUtils.loadRoundedCornersImage(mContext, item.getHead_img(), helper.getView(R.id.chat_item_header));
        TextView mTV_time = helper.getView(R.id.item_tv_time);
        long sendTime = item.getSentTime();
        long currentTime = System.currentTimeMillis();
        //如果大于5分钟
        if (currentTime - sendTime > 5*60*1000){
            mTV_time.setVisibility(View.VISIBLE);
            mTV_time.setText(TimeUtils.getTimeString(item.getSentTime()));
        } else {
            mTV_time.setVisibility(View.GONE);
        }
        if (item.getMsgType().equals(MsgType.TEXT)) {
            TextMsgBody msgBody = (TextMsgBody) item.getBody();
            helper.setText(R.id.chat_item_content_text, msgBody.getMessage());
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
            helper.setText(R.id.tvDuration, msgBody.getDuration() + "\"");
        }

    }

    private void showPopWindow(View view) {
        PopupWindow popupWindow;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentview = inflater.inflate(R.layout.popwindow_chat, null);//自己的弹框布局
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
        popupWindow.showAsDropDown(view, 0, -280);
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

}
