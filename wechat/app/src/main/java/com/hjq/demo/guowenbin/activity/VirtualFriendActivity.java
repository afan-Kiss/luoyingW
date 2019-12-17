package com.hjq.demo.guowenbin.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.FriendDetailsModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.image.ImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * 虚拟App好友详情
 */
public class VirtualFriendActivity extends MyActivity implements View.OnClickListener {
    @BindView(R.id.vertical_tv)
    TextView vertical_tv;
    @BindView(R.id.vertical_tv1)
    TextView vertical_tv1;
    @BindView(R.id.vertical_iv)
    ImageView vertical_iv;
    @BindView(R.id.vertical_name)
    TextView vertical_name;

    String card;

    FriendDetailsModel friendDetailsModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_virtual_friend;
    }

    @Override
    protected void initView() {
        card = getIntent().getStringExtra("card");
        vertical_tv.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        GetFriendinfo();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vertical_tv:
                Intent intent = new Intent(this, WebViewActivity.class);
                startActivity(intent);

                break;

            default:
                break;


        }


    }

    //获取好友详细信息
    void GetFriendinfo() {
        map.clear();
        map.put("Method", "Ufriends");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Card", card);
        map.put("Username", UserManager.getUser().getUsername());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(GetDate(response.body()));

                            return;

                        }
                        Log.i("gwb", "onSuccess: " + GetDate(response.body()));
                        friendDetailsModel = gson.fromJson(GetDate(response.body()), FriendDetailsModel.class);
                        vertical_name.setText(friendDetailsModel.getNickname());
                        ImageLoader.with(getActivity())
                                .load(friendDetailsModel.getHead_img())
                                .into(vertical_iv);

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
