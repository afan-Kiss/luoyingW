package com.hjq.demo.guowenbin.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.AliasModel;
import com.hjq.demo.model.FriendDetailsModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.widget.view.ClearEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * 修改备注
 */

public class AliasActivity extends MyActivity {
    @BindView(R.id.alias_edit)
    ClearEditText alias_edit;
    FriendDetailsModel friendDetailsModel;
    private String name;
    @BindView(R.id.alias_tv)
    TextView alias_tv;
    @BindView(R.id.TitleBar)
    TitleBar titleBarl;
    private String Gcard, getGroup_notic, getGroup_name, getGroup_notics, getGroup_names, card;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_alias;
    }

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        getGroup_name = getIntent().getStringExtra("getGroup_name");
        getGroup_notic = getIntent().getStringExtra("getGroup_notic");
        Gcard = getIntent().getStringExtra("cardss");
        Log.i("chuishen77", "initView: " + getIntent().getStringExtra("Gcard"));
        if (name.equals("1")) {
            try {
                friendDetailsModel = getIntent().getParcelableExtra("friendDetailsModel");
                alias_edit.setText(friendDetailsModel.getRemarkname());
                card = friendDetailsModel.getCard();
            } catch (Exception e) {

            }


        }

        if (name.equals("0")) {
            alias_tv.setText("群聊名称");

            titleBarl.setTitle("修改群名称");
            alias_edit.setText(getGroup_name);


        }


        if (name.equals("2")) {
            getGroup_names = getIntent().getStringExtra("getGroup_names");
            getGroup_notics = getIntent().getStringExtra("getGroup_notics");
            alias_tv.setText("修改群公告");
            titleBarl.setTitle("群公告");
            alias_edit.setText(getGroup_notics);


        }
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        name = getIntent().getStringExtra("name");
        if (name.equals("0")) {
            Modifygroupname();
        }

        if (name.equals("1"))


            Alises();
        if (name.equals("2")) {
            Modifygroupnames();
        }
        super.onRightClick(v);
    }

    /**
     * 添加/更新/删除好友备注
     */
    void Alises() {
        map.clear();
        map.put("Method", "Alias");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Card", card);
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Remarks ", alias_edit.getText().toString());
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
                        finish();


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
     * 修改群组信息
     */
    void Modifygroupname() {
        map.clear();
        map.put("Method", "Modgroup");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Gcard", Gcard);
        map.put("Groupname", alias_edit.getText().toString());
        map.put("Gnotic", getGroup_notic);
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
                        finish();

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
     * 修改群组信息
     */
    void Modifygroupnames() {
        map.clear();
        map.put("Method", "Modgroup");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Gcard", Gcard);
        map.put("Groupname", getGroup_names);
        map.put("Gnotic", alias_edit.getText().toString());
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
                        finish();
                        alias_edit.setText(getGroup_notic);
                        Log.i("cssssss", "123: " + GetDate(response.body()));
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