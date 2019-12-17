package com.hjq.demo.guowenbin.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.guowenbin.adapter.AddFriendsAdapter;
import com.hjq.demo.model.AddFriendListModel;
import com.hjq.demo.model.SendAddFriendModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 同意好友请求
 */
public class AddNewFriendActivity extends MyActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private AddFriendsAdapter addFriendsAdapter;
    private List<AddFriendListModel.NewArrayBean> mDateList = new ArrayList<>();
    @BindView(R.id.add_new_friends)
    RecyclerView mRecyclerView;
    AddFriendListModel addFriendListModel;

    private String code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_friend;
    }

    @Override
    protected void initView() {
        addFriendsAdapter = new AddFriendsAdapter(R.layout.add_friend_rc_item, mDateList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        addFriendsAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(addFriendsAdapter);
        UpAddFridnds();
    }


    @Override
    protected void initData() {


    }


    //好友信息列表
    void UpAddFridnds() {
        map.clear();
        map.put("Method", "FriendList");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());

                            return;

                        }
                        Log.i("guowenbin", "onSuccess: " + (GetDate(response.body())));
                        addFriendListModel = gson.fromJson(GetDate(response.body()), AddFriendListModel.class);
                        addFriendsAdapter.setNewData(addFriendListModel.getNew_array());

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

    //同意好友请求
    void Agreefriendrequest() {
        map.clear();
        map.put("Method", "Adoptfriends");
        map.put("Card", code);
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());

                            return;

                        }
                        SendAddFriendModel sendAddFriendModel = gson.fromJson(GetDate(response.body()), SendAddFriendModel.class);
                        toast(sendAddFriendModel.getMsg());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivityFinish(intent);
                        UpAddFridnds();

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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {



    }

    @Override
    protected void onResume() {
//        Agreefriendrequest();
        UpAddFridnds();
        super.onResume();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        code = addFriendListModel.getNew_array().get(position).getCard();
        switch (view.getId()) {
            case R.id.add_friend_agree:
                Agreefriendrequest();
                break;
        }

    }
}
