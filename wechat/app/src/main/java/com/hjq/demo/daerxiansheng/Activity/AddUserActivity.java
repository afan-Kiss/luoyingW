package com.hjq.demo.daerxiansheng.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.guowenbin.activity.FrienProfileActivity;
import com.hjq.demo.guowenbin.activity.UserInformationActivity;
import com.hjq.demo.model.AddFriendModel;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.TimeUtils;
import com.hjq.demo.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

public class AddUserActivity extends MyActivity {

    @BindView(R.id.edittext_search)
    EditText edittext_search;
    @BindView(R.id.recyclerview_content)
    RecyclerView recyclerview_content;
    AddFriendModel addFriendModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_user;
    }

    @Override
    protected void initView() {
        edittext_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Utils.closeKeybord(edittext_search, AddUserActivity.this);
                    search();
                }
                return false;
            }
        });
    }


    void search() {
        showLoading();
        map.clear();
        map.put("Method", "SearchUser");
        map.put("SearchKey", edittext_search.getText().toString());
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
                        startActivity(intent);
                        Log.i("addFriendModel", "onSuccess: " + GetDate(response.body()));
//                        for (int i = 0; i <= 10; i++) {
//                            insertMessage();
//                        }

//                        UserListEntity userListEntity = gson.fromJson(GetDate(response.body()), UserListEntity.class);
////                        if (userListEntity.all_array != null && userListEntity.all_array.size() > 0) {
////
////                        }
//                        List<UserListEntity> listEntities = new ArrayList<>();
//                        UserListEntity entity = new UserListEntity();
////                            entity.all_array = new ArrayList<>();
////                        head_img=用户头像
////                        fsate=是否好友(1好友 2单面好友 3不是好友)
////                        nickname=用户昵称
////                        username=帐号
////                        gender=性别(1无性别 2男性 3女性 4多性)
////                        signature=个性签名
////                        region_country=国家
////                        region_province=省份
////                        region_city=城市
////                        card=用户唯一标识
//                        UserListEntity item = new UserListEntity();
//                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
//                        item.card = "1";
//                        item.fsate = 2;
//                        item.nickname = "时代厄尔asdfa";
//                        item.username = "18402995814";
//                        item.gender = 3;
//                        item.signature = "asdfasdfasdlaksgdkajhsgdkjhafg";
//                        item.region_province = "上海市";
//                        item.region_country = "中国";
//                        listEntities.add(item);
//                        item = new UserListEntity();
//                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
//                        item.card = "1";
//                        item.fsate = 2;
//                        item.nickname = "sdlgiu时代法律框架和水果";
//                        item.username = "18402995814";
//                        item.gender = 3;
//                        item.signature = "asdfasdfasdlaksgdkajhsgdkjhafg";
//                        item.region_province = "上海市";
//                        item.region_country = "中国";
//                        listEntities.add(item);
//                        item = new UserListEntity();
//                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
//                        item.card = "1";
//                        item.fsate = 1;
//                        item.nickname = "时代厄尔";
//                        item.username = "18402995814";
//                        item.gender = 2;
//                        item.signature = "拉开建设的广发卡就是短发开始就韩国进口韩国";
//                        item.region_province = "上海市";
//                        item.region_country = "中国";
//                        listEntities.add(item);
////                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, entity.all_array.size() * com.shehuan.niv.Utils.dp2px(SearchActivity.this, 63)+com.shehuan.niv.Utils.dp2px(SearchActivity.this, 81));
////                        params.setMargins(0, com.shehuan.niv.Utils.dp2px(SearchActivity.this, (float) 9.5), 0, 0);
////                        recyclerview_message.setLayoutParams(params);
////                        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, entity.all_array.size() * com.shehuan.niv.Utils.dp2px(SearchActivity.this, 63)+com.shehuan.niv.Utils.dp2px(SearchActivity.this, 81));
////                        params.setMargins(0, com.shehuan.niv.Utils.dp2px(SearchActivity.this, (float) 9.5), 0, 0);
////                        recyclerview_userinfo.setLayoutParams(params);
////                        infoAdapter.setFooterView(message_footerView);
////                        infoAdapter.setHeaderView(message_headerView);
////                        userAdapter.setHeaderView(userinfo_headerView);
////                        userAdapter.setFooterView(userinfo_footerView);
////                        userAdapter.setData(entity.all_array);
////                        infoAdapter.setData(entity.all_array);
////                        UserListEntity entity = gson.fromJson(GetDate(response.body()), UserListEntity.class);
////                        if (entity.all_array != null && entity.all_array.size() > 0) {
////
////                        } else {
////
////
////                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });

    }


    @Override
    protected void initData() {

    }
}
