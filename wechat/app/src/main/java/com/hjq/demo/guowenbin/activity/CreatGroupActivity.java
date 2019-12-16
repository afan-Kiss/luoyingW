package com.hjq.demo.guowenbin.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.guowenbin.adapter.CreatGroupAdapter;
import com.hjq.demo.guowenbin.adapter.GroupChatAdapter;
import com.hjq.demo.model.FriendListModel;
import com.hjq.demo.session.Tezheng;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CreatGroupActivity extends MyActivity {
    @BindView(R.id.TitleBar)
    TitleBar mTitlebar;
    @BindView(R.id.et_sreach)
    EditText et_sreach;
    @BindView(R.id.rl_sreach)
    RelativeLayout mRlSreach;
    @BindView(R.id.rc_friend)
    RecyclerView mRvList;
    private String url = "https://c-ssl.duitang.com/uploads/item/201806/07/20180607185957_fjrFt.thumb.700_0.jpeg";
    GroupChatAdapter groupChatAdapter;

    //Adapter
    private CreatGroupAdapter mAdapter;
    //
    private List<FriendListModel.AllArrayBean> mList = new ArrayList<>();
    //
    private List<String> idList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return (R.layout.activity_creat_group);
    }

    @Override
    protected void initView() {
        initRecyclView();
        mTitlebar.getRightView().setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    protected void initData() {
        map.clear();
        map.put("Method", "FriendList");
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
                        Log.i("gwb1008", "onSuccess: " + GetDate(response.body()));

                        FriendListModel friendListModel = gson.fromJson(GetDate(response.body()), FriendListModel.class);
                        Tezheng.Features_friends = friendListModel.getFeatures_friends();
                        mAdapter.setNewData(friendListModel.getAll_array());

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
    public void onRightClick(View v) {
        CreatGroup();


    }

    private void initRecyclView() {
        mAdapter = new CreatGroupAdapter(R.layout.item_contact, mList, this);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendListModel.AllArrayBean bean = (FriendListModel.AllArrayBean) adapter.getData().get(position);
            if (view.getId() == R.id.cb_select) {
                if (idList.contains(bean.getCard())) {
                    if (idList.size() > 0) {
                        for (int i = 0; i < idList.size(); i++) {
                            if (idList.get(i).equals(bean.getCard())) {
                                idList.remove(i);
                                break;
                            }

                        }
                    }
                } else {
                    idList.add(bean.getCard());
                }
            }
        });
    }

    void CreatGroup() {
        map.put("Method", "Rgroup");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Groupname", "chuishen");
        map.put("Groupimg ", url);
        if (idList.size() > 0) {
            StringBuffer path = new StringBuffer();
            for (int i = 0; i < idList.size(); i++) {
                path.append(idList.get(i) + "|");
            }
            map.put("Card", path.toString().substring(0, path.toString().length() - 1));
            OkGo.<String>post(API.BASE_API)
                    .params("Data", ApiURLUtils.GetDate(map))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                            if (CheckDate(response.body()).getState() != 1) {
                                MessageDialog(CheckDate(response.body()).getMsg());
                                return;

                            }

                            Log.i("123123", "onSuccess: " + GetDate(response.body()));
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


    }


}