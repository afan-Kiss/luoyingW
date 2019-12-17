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
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.VirtualAppEntity;
import com.hjq.demo.guowenbin.adapter.VirtualAdApter;
import com.hjq.demo.model.FriendListModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 虚拟Ap
 */
public class VirtualActivity extends MyActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.virtual_rc)
    RecyclerView recyclerView;
    private FriendListModel friendListModel;
    VirtualAdApter virtualAdApter;
    private List<FriendListModel.AllArrayBean> mDateList = new ArrayList<>();

    Intent intent;

    @Override

    protected int getLayoutId() {
        return R.layout.activity_virtual;
    }

    @Override
    protected void initView() {
        virtualAdApter = new VirtualAdApter(R.layout.virtual_rc_item, mDateList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        virtualAdApter.setOnItemClickListener(this);
        recyclerView.setAdapter(virtualAdApter);


    }

    @Override
    protected void initData() {
        Updatebuddylist();


    }


    void Updatebuddylist() {
        map.clear();
        map.put("Method", "Uapp");
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

                        Log.i("gwb", "onSuccess: " + GetDate(response.body()));
                        friendListModel = gson.fromJson(GetDate(response.body()), FriendListModel.class);
                        intent = new Intent();
                        intent.setClass(getActivity(), VirtualFriendActivity.class);
                        virtualAdApter.setNewData(friendListModel.getAll_array());
                        if (friendListModel.all_array != null && friendListModel.all_array.size() > 0) {
                            // TODO: 2019-11-26 将获取到的虚拟app列表中的值调用addVirtual方法，实体类就用方法调用的这个
                        }
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

    /**
     * 添加虚拟app到本地数据库
     *
     * @param list
     */
    void addVirtual(List<VirtualAppEntity> list) {
        for (VirtualAppEntity item : list) {
            List<VirtualAppEntity> virtualAppEntityList = DBHelper.isVirtual(item.app_number);
            VirtualAppEntity entity = new VirtualAppEntity();
            entity.app_founder = item.app_founder;
            entity.app_img = item.app_img;
            entity.app_number = item.app_number;
            entity.appLink = item.appLink;
            entity.appName = item.appName;
            entity.userCard = UserManager.getUser().getCard();
            if (virtualAppEntityList != null && virtualAppEntityList.size() > 0) {
                entity.id = item.id;
                DBHelper.insertVirtualAppReplace(entity);
            } else {
                DBHelper.insertVirtualApp(entity);
            }

        }

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        intent.putExtra("card", friendListModel.getAll_array().get(position).getCard());
        startActivity(intent);


    }
}
