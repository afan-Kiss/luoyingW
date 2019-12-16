package com.hjq.demo.guowenbin.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.model.QrcodeModel;
import com.hjq.image.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加好友
 */
public class UserInformationActivity extends MyActivity implements View.OnClickListener {
    @BindView(R.id.user_add_friends)
    TextView user_add_friends;
    @BindView(R.id.img_user_head)
    ImageView img_user_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_user_id)
    TextView tv_user_id;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_geqian)
    TextView tv_geqian;

    QrcodeModel qrcodeModel;
    AddFriendModel addFriendModel;
    Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_information;
    }

    @Override
    protected void initView() {
        user_add_friends.setOnClickListener(this);
        qrcodeModel = getIntent().getParcelableExtra("qrcodeinfo");
        addFriendModel = getIntent().getParcelableExtra("addFriendModel");
    }

    @Override
    protected void initData() {
        if (qrcodeModel == null) {
            if (addFriendModel == null)
                return;
            tv_name.setText(addFriendModel.getNickname());
            tv_user_id.setText("账号: " + addFriendModel.getUsername());
            tv_geqian.setText(addFriendModel.getSignature());
            tv_address.setText(addFriendModel.getRegion_country() + addFriendModel.getRegion_province() + addFriendModel.getRegion_city());
            ImageLoader.with(this)
                    .load(addFriendModel.getHead_img())
                    .into(img_user_head);


        } else {
            tv_name.setText(qrcodeModel.getNickname());
            tv_user_id.setText("账号: " + qrcodeModel.getUsername());
            tv_geqian.setText(qrcodeModel.getSignature());
            tv_address.setText(qrcodeModel.getRegion_country() + qrcodeModel.getRegion_province() + qrcodeModel.getRegion_city());
            ImageLoader.with(this)
                    .load(qrcodeModel.getHead_img())
                    .into(img_user_head);
        }
    }

    @OnClick({R.id.user_add_friends})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add_friends:
                intent = new Intent(this, FriendVerificationActivity.class);
                intent.putExtra("addFriendModel", addFriendModel);
                startActivity(intent);
                break;
        }

    }
}
