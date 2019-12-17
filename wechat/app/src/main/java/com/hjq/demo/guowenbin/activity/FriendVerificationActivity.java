package com.hjq.demo.guowenbin.activity;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.model.SendAddFriendModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.widget.view.ClearEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * 朋友验证
 */
public class FriendVerificationActivity extends MyActivity {
    AddFriendModel addFriendModel;
    SendAddFriendModel sendAddFriendModel;
    @BindView(R.id.friend_edit)
    ClearEditText friend_edit;


    @Override

    protected int getLayoutId() {
        return R.layout.activity_friend_verification;
    }

    @Override
    protected void initView() {
        addFriendModel = getIntent().getParcelableExtra("addFriendModel");

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onRightClick(View v) {
        Addfriends();
//
        super.onRightClick(v);
    }

    //添加好友请求
    void Addfriends() {
        map.clear();
        map.put("Method", "Addfriends");
        map.put("Card", addFriendModel.getCard());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Msg", friend_edit.getText().toString());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());

                            return;

                        }
                        sendAddFriendModel = gson.fromJson(GetDate(response.body()), SendAddFriendModel.class);
                        toast(CheckDate(response.body()).getMsg());
                        startActivityFinish(HomeActivity.class);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast(R.string.error_network);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }
}
