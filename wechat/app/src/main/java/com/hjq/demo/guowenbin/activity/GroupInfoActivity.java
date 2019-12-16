package com.hjq.demo.guowenbin.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.guowenbin.adapter.GroupInfoAdapter;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.model.AliasModel;
import com.hjq.demo.model.FriendDetailsModel;
import com.hjq.demo.model.GroupListModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupInfoActivity extends MyActivity implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.group_info_rc)
    RecyclerView group_info_rc;
    List<GroupListModel.AllArrayBean.UserListBean> mydata = new ArrayList<>();
    GroupInfoAdapter groupInfoAdapter;
    GroupListModel groupListModel;
    @BindView(R.id.group_info_delete)
    TextView group_info_delete;
    @BindView(R.id.group_info_tv)
    TextView group_info_tv;
    @BindView(R.id.group_info_ll)
    LinearLayout group_info_ll;
    @BindView(R.id.group_info_name)
    TextView group_info_name;
    @BindView(R.id.group_info_tv1)
    TextView group_info_tv1;
    @BindView(R.id.group_info_ll2)
    LinearLayout group_info_ll2;
    String Gcard;
    Intent intent = new Intent();
    Intent intents = new Intent();
    String found, SearchKey;
    FriendDetailsModel friendDetailsModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void initView() {
        Gcard = getIntent().getStringExtra("cards");
        groupInfoAdapter = new GroupInfoAdapter(R.layout.group_info_item, mydata, this);
        group_info_rc.setLayoutManager(new GridLayoutManager(this, 4));
        group_info_rc.setAdapter(groupInfoAdapter);
        group_info_delete.setOnClickListener(this);
        group_info_tv.setOnClickListener(this);
        GroupList();


    }

    @Override
    protected void initData() {
        groupInfoAdapter.setOnItemClickListener(this);
        group_info_ll.setOnClickListener(this);
        group_info_ll2.setOnClickListener(this);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GroupListModel.AllArrayBean.UserListBean model = (GroupListModel.AllArrayBean.UserListBean) adapter.getData().get(position);
        SearchKey = model.getUsername();

        search();


    }

    @Override
    protected void onResume() {
        GroupList();
        super.onResume();
    }

    void GroupList() {
        map.clear();
        map.put("Method", "GroupList");
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

                        groupListModel = gson.fromJson(GetDate(response.body()), GroupListModel.class);
                        if (groupListModel.getAll_array().size() > 0) {
                            for (int i = 0; i < groupListModel.getAll_array().size(); i++) {
                                if (Gcard.equals(groupListModel.getAll_array().get(i).getCard())) {
                                    groupInfoAdapter.setNewData(groupListModel.getAll_array().get(i).getUser_list());
                                    group_info_name.setText(groupListModel.getAll_array().get(i).getGroup_name());
                                    group_info_tv1.setText(groupListModel.getAll_array().get(i).getGroup_notic());
                                    intent.putExtra("getGroup_name", groupListModel.getAll_array().get(i).getGroup_name());
                                    intent.putExtra("getGroup_notic", groupListModel.getAll_array().get(i)
                                            .getGroup_notic());
                                    intent.putExtra("cardss", groupListModel.getAll_array().get(i).getCard());


                                    intents.putExtra("getGroup_names", groupListModel.getAll_array().get(i).getGroup_name());

                                    intents.putExtra("getGroup_notics", groupListModel.getAll_array().get(i)
                                            .getGroup_notic());
                                    intents.putExtra("cardss", groupListModel.getAll_array().get(i).getCard());
                                    found = groupListModel.getAll_array().get(i).getUser_list().get(i).getFounder();

                                    break;

                                }
                            }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_info_delete:
                showDialog(getString(R.string.sign_out_group), 1);
                break;
            case R.id.group_info_tv:
                showDialog(getString(R.string.delete_chat_record), 0);
                break;
            case R.id.group_info_ll:
                if (!"3".equals(found)) {
                    toast("只有群主和管理员可修改");
                } else {
                    intent.setClass(this, AliasActivity.class);
                    intent.putExtra("groupListModel ", groupListModel);
                    intent.putExtra("name", "0");
                    startActivity(intent);
                }
                break;
            case R.id.group_info_ll2:
                if (!found.equals("3")) {
                    toast("只有群主和管理员可修改");

                } else {
                    intents.setClass(this, AliasActivity.class);
                    intents.putExtra("groupListModel ", groupListModel);
                    intents.putExtra("name", "2");
                    startActivity(intents);
                }


                break;

        }


    }

    /**
     * @param msg  弹框信息
     * @param type 0 清空聊天记录  1 删除并退出
     */
    private void showDialog(String msg, int type) {
        new MessageDialog.Builder(this)
                // 标题可以不用填写
                .setTitle("提示")
                // 内容必须要填写
                .setMessage(msg)
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        if (type == 0) {

                        } else if (type == 1) {
                            Delete();
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    void Delete() {
        map.clear();
        map.put("Method", "Outgroup");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Gcard", Gcard);
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
                        AliasModel aliasModel = gson.fromJson(GetDate(response.body()), AliasModel.class);
                        toast(aliasModel.getMsg());
                        Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                        startActivity(intent);
                        GroupList();

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

    void search() {
        map.clear();
        map.put("Method", "SearchUser");
        map.put("SearchKey", SearchKey);
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        OkGo.<String>get(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            toast(CheckDate(response.body()).getMsg());
                            return;
                        }
                        AddFriendModel addFriendModel = gson.fromJson(GetDate(response.body()), AddFriendModel.class);
                        Intent intent = new Intent();
                        if (addFriendModel.getFsate().equals("1")) {
                            intent.setClass(getActivity(), FrienProfileActivity.class);
                        } else {
                            intent.setClass(getActivity(), UserInformationActivity.class);
                        }
                        intent.putExtra("addFriendModel", addFriendModel);
                        intent.putExtra("friendDetailsModel", friendDetailsModel);
                        startActivity(intent);

                    }

                });

    }
}