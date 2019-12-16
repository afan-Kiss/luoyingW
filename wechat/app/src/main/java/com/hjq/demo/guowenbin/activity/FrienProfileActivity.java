package com.hjq.demo.guowenbin.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mine_chenmo.activity.NewChatActivity;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.model.DeleteFriend;
import com.hjq.demo.model.FriendDetailsModel;
import com.hjq.demo.model.FriendListModel;
import com.hjq.demo.model.QrcodeModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.FriendCircleActivity;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.ui.dialog.FriendMenuDialog;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.image.ImageLoader;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.view.SwitchButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * 朋友详细资料
 */
public class FrienProfileActivity extends MyActivity implements View.OnClickListener, SwitchButton.OnCheckedChangeListener {
    @BindView(R.id.friend_remarks_sb)
    RelativeLayout friend_remarks_sb;
    @BindView(R.id.friend_circle_sb_)
    SettingBar friend_circle_sb_;
    @BindView(R.id.friend_contact_sb)
    SettingBar friend_contact_sb;
    @BindView(R.id.friend_blacklist_sb)
    SettingBar friend_blacklist_sb;
    @BindView(R.id.img_user_head)
    ImageView img_user_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_user_id)
    TextView tv_user_id;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_levl)
    TextView tv_levl;
    @BindView(R.id.tv_geqian)
    TextView tv_geqian;
    @BindView(R.id.delete_friend)
    TextView delete_friend;
    @BindView(R.id.send_message_friend)
    TextView send_message_friend;
    @BindView(R.id.group_info_name)
    TextView group_info_name;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.switch_btn)
    SwitchButton switch_btn;
    QrcodeModel qrcodeModel;
    String card, cards, black;
    String getUsername;
    FriendDetailsModel friendDetailsModel;
    AddFriendModel addFriendModel;
    FriendListModel friendListModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_frien_profile;
    }

    @Override
    protected void initView() {
        friend_remarks_sb.setOnClickListener(this);
        friend_circle_sb_.setOnClickListener(this);
        friend_contact_sb.setOnClickListener(this);
        friend_blacklist_sb.setOnClickListener(this);
        send_message_friend.setOnClickListener(this);
        delete_friend.setOnClickListener(this);
        switch_btn.setOnCheckedChangeListener(this);
        black = getIntent().getStringExtra("black");
        addFriendModel = getIntent().getParcelableExtra("addFriendModel");
        card = getIntent().getStringExtra("card");
        getUsername = getIntent().getStringExtra("username");
        friendListModel = getIntent().getParcelableExtra(" friendListModel");
        cards = getIntent().getStringExtra("cards");


        try {
            cards = friendDetailsModel.getCard();
        } catch (Exception e) {

        }

        setRightIcon(R.drawable.icon_more_friend);



    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        showPopwindow();
    }

    @Override
    protected void initData() {
        if (qrcodeModel == null && addFriendModel == null) {
            GetUserinfo();
        } else if (qrcodeModel != null) {
            tv_user_name.setText(TextUtils.isEmpty(qrcodeModel.getRemarkname()) ? getResources().getString(R.string.not_write) : qrcodeModel.getRemarkname());
            tv_user_id.setText("账号: " + qrcodeModel.getUsername());
            tv_levl.setText("LV: " + qrcodeModel.getMember_grade());
            tv_geqian.setText(TextUtils.isEmpty(qrcodeModel.getSignature()) ? getResources().getString(R.string.not_write) : qrcodeModel.getSignature());
            String address = qrcodeModel.getRegion_country() + qrcodeModel.getRegion_province() + qrcodeModel.getRegion_city();
            tv_address.setText(TextUtils.isEmpty(address) ? getResources().getString(R.string.not_write) : address);
            ImageLoader.with(this)
                    .load(qrcodeModel.getHead_img())
                    .into(img_user_head);

        } else if (addFriendModel != null) {
            tv_user_id.setText("账号: " + addFriendModel.getUsername());
            tv_geqian.setText(TextUtils.isEmpty(addFriendModel.getSignature()) ? getResources().getString(R.string.not_write) : addFriendModel.getSignature());
            String address = addFriendModel.getRegion_country() + addFriendModel.getRegion_province() + addFriendModel.getRegion_city();
            tv_address.setText(TextUtils.isEmpty(address) ? getResources().getString(R.string.not_write) : address);
            ImageLoader.with(this)
                    .load(addFriendModel.getHead_img())
                    .into(img_user_head);
            tv_name.setText(addFriendModel.getNickname());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_remarks_sb:
                Intent intent = new Intent(this, AliasActivity.class);
                intent.putExtra("friendDetailsModel", friendDetailsModel);
                intent.putExtra("name", "1");
                startActivity(intent);

                break;
            case R.id.friend_circle_sb_:
                Intent friendIntent = new Intent(FrienProfileActivity.this, FriendCircleActivity.class);
                if (null != addFriendModel) {
                    friendIntent.putExtra("userName", addFriendModel.getUsername());
                }
                startActivity(friendIntent);
                break;
            case R.id.friend_contact_sb:
                break;
            case R.id.send_message_friend:
                if (friendDetailsModel == null) {
                    intent = new Intent();
                    intent.setClass(getActivity(), NewChatActivity.class);
                    intent.putExtra("card", addFriendModel.getCard());
                    intent.putExtra("username", addFriendModel.getUsername());
                    intent.putExtra("imageUrl", addFriendModel.getHead_img());
                    intent.putExtra("nickName", addFriendModel.getNickname());
                    intent.putExtra("Rtype", "1");

                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(getActivity(), NewChatActivity.class);
                    intent.putExtra("card", friendDetailsModel.getCard());
                    intent.putExtra("username", friendDetailsModel.getUsername());
                    intent.putExtra("imageUrl", friendDetailsModel.getHead_img());
                    intent.putExtra("nickName", friendDetailsModel.getNickname());
                    intent.putExtra("Rtype", "1");
                    startActivity(intent);
                }

                //startActivity(NewChatActivity.class);
                break;
            case R.id.delete_friend:
                new MessageDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("温馨提示")
                        // 内容必须要填写
                        .setMessage(getString(R.string.delete_friend))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                deletefriend();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

            default:
                break;
        }


    }
//获取详细信息

    void GetUserinfo() {
        map.clear();
        map.put("Method", "Ufriends");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Card", card);
        map.put("Username", UserManager.getUser().getPhone_number());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(GetDate(response.body()));
                            return;
                        }
                        friendDetailsModel = gson.fromJson(GetDate(response.body()), FriendDetailsModel.class);
                        tv_user_name.setText(TextUtils.isEmpty(friendDetailsModel.getRemarkname()) ? getResources().getString(R.string.not_write) : friendDetailsModel.getRemarkname());
                        tv_user_id.setText("账号: " + friendDetailsModel.getUsername());
                        tv_levl.setText("LV: " + friendDetailsModel.getMember_grade());
                        tv_geqian.setText(TextUtils.isEmpty(friendDetailsModel.getSignature()) ? getResources().getString(R.string.not_write) : friendDetailsModel.getSignature());
                        String address = friendDetailsModel.getRegion_country() + friendDetailsModel.getRegion_province() + friendDetailsModel.getRegion_city();
                        tv_address.setText(TextUtils.isEmpty(address) ? getResources().getString(R.string.not_write) : address);
                        ImageLoader.with(getActivity())
                                .load(friendDetailsModel.getHead_img())
                                .into(img_user_head);
                        tv_name.setText(friendDetailsModel.getNickname());
                        group_info_name.setText(friendDetailsModel.getRemarkname());
                        Log.i("123123", "onSuccess: " + GetDate(response.body()));
                        if (null != black && black.equals("1")) {
                            switch_btn.setChecked(true);

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
                        showComplete();
                    }
                });


    }

    //删除好友
    void deletefriend() {
        map.clear();
        map.put("Method", "Dfriends");
        map.put("Card", card);
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.i("123123123", "onSuccess: " + GetDate(response.body()));

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;

                        }
                        DeleteFriend deleteFriend = gson.fromJson(GetDate(response.body()), DeleteFriend.class);
                        MessageDialog(deleteFriend.getMsg());
                        if ("成功".equals(deleteFriend.getMsg())) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);

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
                        showComplete();
                    }
                });


    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        Blacklist();


    }


    //拉黑用户
    void Blacklist() {
        map.clear();
        map.put("Method", "Blacklist");
        map.put("Card", card);
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("State", black);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;

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
                        showComplete();
                    }
                });


    }

    /**
     * 更换背景图
     */
    public void showPopwindow() {
        // 底部选择框
        new FriendMenuDialog.Builder(getActivity())
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setList("删除","举报")
                .setListener(new FriendMenuDialog.OnListener(){

                    @Override
                    public void onSelected(BaseDialog dialog, int position, Object o) {
                        if (position == 0){
                            new MessageDialog.Builder(getActivity())
                                    // 标题可以不用填写
                                    .setTitle("温馨提示")
                                    // 内容必须要填写
                                    .setMessage(getString(R.string.delete_friend))
                                    // 确定按钮文本
                                    .setConfirm(getString(R.string.common_confirm))
                                    // 取消按钮
                                    .setCancel(getString(R.string.common_cancel))
                                    // 设置点击按钮后不关闭对话框
                                    //.setAutoDismiss(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            deletefriend();
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }else if (position == 1){
                            startActivity(new Intent(FrienProfileActivity.this,ReportActivity.class));
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {

                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
                .show();
    }
}

