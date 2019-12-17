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
import com.hjq.demo.daerxiansheng.sql.GroupEntity;
import com.hjq.demo.daerxiansheng.sql.GroupUserEntity;
import com.hjq.demo.guowenbin.adapter.GroupChatAdapter;
import com.hjq.demo.mine_chenmo.activity.NewChatActivity;
import com.hjq.demo.model.GroupListModel;
import com.hjq.demo.session.Tezheng;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @Author 陈末
 * @Time 2019-11-15 22:46
 * @Title 群聊列表
 * @desc
 */
public class GroupChatActivity extends MyActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rc_group_chat)
    RecyclerView rc_group_chat;
    List<GroupListModel.AllArrayBean> mydata = new ArrayList<>();
    GroupChatAdapter groupChatAdapter;
    GroupListModel groupListModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_chat;
    }

    @Override
    protected void initView() {
        GroupList();
        groupChatAdapter = new GroupChatAdapter(R.layout.item_group_chat, mydata, this);
        rc_group_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_group_chat.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rc_group_chat.setAdapter(groupChatAdapter);

    }

    @Override
    protected void initData() {
        groupChatAdapter.setOnItemClickListener(this);

    }

    void GroupList() {
        map.clear();
        map.put("Method", "GroupList");
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
                        groupListModel = gson.fromJson(GetDate(response.body()), GroupListModel.class);
                        Tezheng.Features_group = groupListModel.getFeatures_group();
                        groupChatAdapter.setNewData(groupListModel.getAll_array());
                        Log.i("GroupList", "onSuccess: " + GetDate(response.body()));
                        //给本地数据库存储群组信息
                        if (groupListModel != null && groupListModel.all_array != null && groupListModel.all_array.size() > 0) {
                            addGroup(groupListModel.all_array);
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
     * 给本地数据库存储群组信息
     *
     * @param groupListModel
     */
    void addGroup(List<GroupListModel.AllArrayBean> groupListModel) {
        if (groupListModel != null && groupListModel.size() > 0) {
            for (GroupListModel.AllArrayBean item : groupListModel) {
                GroupEntity groupEntity = new GroupEntity();
                List<GroupEntity> list = DBHelper.isGroup(item.card);
                groupEntity.disturb = Integer.parseInt(item.disturb);//是否勿扰(0否 1是)
                groupEntity.groupNotic = item.group_notic;//群组公告
                groupEntity.groupName = item.group_name;//群组名称
                groupEntity.groupImg = item.group_img;//群组头像
                groupEntity.card = item.card;//群组唯一标识
                groupEntity.userCard = UserManager.getUser().getCard();//当前用户card
                if (list != null && list.size() > 0) {
                    groupEntity.id = list.get(0).id;
                    DBHelper.insertGroupReplace(groupEntity);
                } else {
                    DBHelper.insertGroup(groupEntity);
                }
                if (item.user_list != null && item.user_list.size() > 0) {
                    for (GroupListModel.AllArrayBean.UserListBean userItem : item.user_list) {
                        GroupUserEntity groupUserEntity = new GroupUserEntity();
                        //查询当前群组中是否有该用户
                        List<GroupUserEntity> groupUserList = DBHelper.isGroupUser(userItem.card);
                        groupUserEntity.groupCard = item.card;//群组唯一标识
                        groupUserEntity.username = userItem.username;//用户名
                        groupUserEntity.nickname = userItem.nickname;//用户昵称
                        groupUserEntity.head_img = userItem.head_img;//用户头像
                        groupUserEntity.fsate = Integer.parseInt(userItem.fsate);//是否好友(1是 0否)
                        groupUserEntity.founder = Integer.parseInt(userItem.founder);//用户权限(1群主 2管理 3普通成员)
                        groupUserEntity.user_id = userItem.user_id;//用户ID
                        groupUserEntity.card = userItem.card;//用户唯一标识
                        if (groupUserList != null && groupUserList.size() > 0) {
                            groupUserEntity.id = groupUserList.get(0).id;
                            DBHelper.insertGroupUserReplace(groupUserEntity);
                        } else {
                            DBHelper.insertGroupUser(groupUserEntity);
                        }


                    }
                }
            }
        }

    }

    @Override
    protected void onResume() {
        GroupList();
        super.onResume();
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(this, CreatGroupActivity.class);
        startActivity(intent);
        super.onRightClick(v);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewChatActivity.class);
        intent.putExtra("card", groupListModel.getAll_array().get(position).getCard());
        intent.putExtra("username", groupListModel.getAll_array().get(position).getUser_list().get(position).getUsername());
        intent.putExtra("imageUrl", groupListModel.getAll_array().get(position).getGroup_img());
        intent.putExtra("nickName", groupListModel.getAll_array().get(position).getGroup_name());
        intent.putExtra("Rtype", "2");


        startActivity(intent);
    }
}
