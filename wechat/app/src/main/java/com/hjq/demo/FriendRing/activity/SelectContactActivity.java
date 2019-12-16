package com.hjq.demo.FriendRing.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.FriendRing.adapter.SelectContactAdapter;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
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

/**
 * @author GF
 * @des 选择联系人
 * @date 2019/11/18
 */
public class SelectContactActivity extends MyActivity {

    @BindView(R.id.titlebar)
    TitleBar mTitlebar;
    @BindView(R.id.et_sreach)
    EditText et_sreach;
    @BindView(R.id.rl_sreach)
    RelativeLayout mRlSreach;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;


    //Adapter
    private SelectContactAdapter mAdapter;
    //
    private List<FriendListModel.AllArrayBean> mList = new ArrayList<>();
    private List<FriendListModel.AllArrayBean> arrayList = new ArrayList<>();
    //
    private List<String> idList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_contact;
    }

    @Override
    protected void initView() {
        mTitlebar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                if (idList.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < idList.size(); i++) {
                        sb.append(idList.get(i) + "|");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("ids", sb.toString().substring(0, sb.toString().length() - 1));
                    setResult(200,intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
        initRecyclView();
        et_sreach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList.clear();
                String trim = et_sreach.getText().toString().trim();
                for (int j = 0; j < mList.size(); j++) {
                    if (mList.get(j).getNickname().contains(trim)) {
                        arrayList.add(mList.get(i));
                    }
                }
                //if (mList !=null){
                mAdapter.setNewData(arrayList);
                //}

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initRecyclView() {
        mAdapter = new SelectContactAdapter(R.layout.item_contact, mList, this);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendListModel.AllArrayBean bean = (FriendListModel.AllArrayBean) adapter.getData().get(position);
            if (view.getId() == R.id.cb_select) {
                if (idList.contains(bean.getUser_id())) {
                    if (idList.size() > 0) {
                        for (int i = 0; i < idList.size(); i++) {
                            if (idList.get(i).equals(bean.getUser_id())) {
                                idList.remove(i);
                                break;
                            }

                        }
                    }
                } else {
                    idList.add(bean.getUser_id());
                }
            }
        });
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
                        FriendListModel friendListModel = gson.fromJson(GetDate(response.body()), FriendListModel.class);
                        Tezheng.Features_friends = friendListModel.getFeatures_friends();
                        mList = friendListModel.getAll_array();
                        mAdapter.setNewData(friendListModel.getAll_array());
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
