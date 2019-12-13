package com.hjq.demo.daerxiansheng.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.Adapter.ChatMessageAdapter;
import com.hjq.demo.daerxiansheng.Adapter.ContactAdapter;
import com.hjq.demo.daerxiansheng.Adapter.SearchMessageDetailAdapter;
import com.hjq.demo.daerxiansheng.Entity.ChatContactEntity;
import com.hjq.demo.daerxiansheng.Entity.UserListEntity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.SPUtils;
import com.hjq.demo.util.Utils;
import com.hjq.image.ImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MoreSearchActivity extends MyActivity {
    @BindView(R.id.imageview_cancle)
    ImageView imageview_cancle;
    @BindView(R.id.edittext_search)
    EditText edittext_search;
    @BindView(R.id.textview_cancle)
    TextView textview_cancle;
    @BindView(R.id.recyclerview_content)
    RecyclerView recyclerview_content;
    @BindView(R.id.imageview_back)
    ImageView imageview_back;
    @BindView(R.id.relativelayout_title)
    RelativeLayout relativelayout_title;
    @BindView(R.id.imageview_search)
    ImageView imageview_search;
    @BindView(R.id.textview_searchcontent)
    TextView textview_searchcontent;

    //    private ContactAdapter infoAdapter;
    private SearchMessageDetailAdapter adapter;

    private int selectType;//1点击的是更多消息 2 点击的是更多联系人

    private List<ChatContactEntity> frendsMessageEntityList, chatEntityList;//获取聊天消息内容
    private List<UserListEntity> listEntities;
    private String searchContent;
    private int footerHeight;

    View headerView;
    TextView textview_headercontent;
    ImageView imageview_headercontent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_search;
    }

    @Override
    protected void initView() {

        selectType = getIntent().getIntExtra("selectType", 0);
        searchContent = getIntent().getStringExtra("searchContent");
        headerView = getLayoutInflater().inflate(R.layout.view_more_search, null);
        textview_headercontent = headerView.findViewById(R.id.textview_content);
        imageview_headercontent = headerView.findViewById(R.id.imageview_header);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, (float) 74.5));
        headerView.setLayoutParams(params);
        LinearLayoutManager messageManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerview_content.setLayoutManager(messageManager);

        imageview_back.setOnClickListener(v -> finish());
        if (!TextUtils.isEmpty(searchContent)) {
            edittext_search.setText(searchContent);
            edittext_search.requestFocus();//获取焦点 光标出现
            edittext_search.setSelection(searchContent.length());//将光标移至文字末尾
        }
        adapter = new SearchMessageDetailAdapter(this, null);
        recyclerview_content.setAdapter(adapter);
        footerHeight = Utils.dp2px(this, (float) 80);
        if (selectType == 1) {
            frendsMessageEntityList = getIntent().getParcelableArrayListExtra("listEntity");
            if (frendsMessageEntityList.get(0) != null) {
                if (!TextUtils.isEmpty(frendsMessageEntityList.get(0).name)) {
                    textview_headercontent.setText(String.format(getString(R.string.chatrecord), frendsMessageEntityList.get(0).name));
                    textview_searchcontent.setText(frendsMessageEntityList.get(0).name);
                } else {
                    textview_headercontent.setText(String.format(getString(R.string.chatrecord), frendsMessageEntityList.get(0).phone));
                    textview_searchcontent.setText(frendsMessageEntityList.get(0).phone);
                }
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, (float) (frendsMessageEntityList.size() * 76)) + footerHeight);
                linearParams.setMargins(0, Utils.dp2px(this, (float) 9.5), 0, 0);
                recyclerview_content.setLayoutParams(linearParams);
                ImageLoader.with(this).load(frendsMessageEntityList.get(0).imageUrl).circle(10).into(imageview_headercontent);
            }
            setRecyclerViewHeight();
            toast(frendsMessageEntityList.size() + "条消息");
            adapter.setData(frendsMessageEntityList);
        } else {
            chatEntityList = getIntent().getParcelableArrayListExtra("listEntity");
            if (chatEntityList.get(0) != null) {
                if (!TextUtils.isEmpty(chatEntityList.get(0).name)) {
                    textview_headercontent.setText(String.format(getString(R.string.chatrecord), frendsMessageEntityList.get(0).name));
                    textview_searchcontent.setText(chatEntityList.get(0).name);
                } else {
                    textview_headercontent.setText(String.format(getString(R.string.chatrecord), frendsMessageEntityList.get(0).phone));
                    textview_searchcontent.setText(chatEntityList.get(0).phone);
                }
                ImageLoader.with(this).load(chatEntityList.get(0).imageUrl).circle(10).into(imageview_headercontent);
            }
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, (float) (chatEntityList.size() * 76)) + footerHeight);
            linearParams.setMargins(0, Utils.dp2px(this, (float) 9.5), 0, 0);
            recyclerview_content.setLayoutParams(linearParams);
            adapter.setHeaderView(headerView);
            adapter.setData(chatEntityList);
            toast(chatEntityList.size() + "条消息");
        }
        adapter.setSerachContent(searchContent);
//        adapter.setOnItemClickListener(new ChatMessageAdapter.onItemClick() {
//            @Override
//            public void onUserClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
//
//            }
//
//            @Override
//            public void onItemClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
//
//            }
//
//
//            @Override
//            public void onRootViewClick() {
//
//            }
//
//            @Override
//            public void onItemClick(View view, int position, ChatContactEntity data) {
//                toast("点击了聊天消息item");
//            }
//        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(edittext_search.getText().toString())) {
                    imageview_cancle.setVisibility(View.VISIBLE);
                } else {
                    imageview_cancle.setVisibility(View.GONE);
                }
            }
        };
        imageview_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = edittext_search.getSelectionStart();
                Editable editable = edittext_search.getText();
                editable.delete(index - 1, index);
            }
        });
        edittext_search.addTextChangedListener(textWatcher);
        textview_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeybord(edittext_search, MoreSearchActivity.this);

            }
        });
        edittext_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!TextUtils.isEmpty(edittext_search.getText().toString())) {
                        Utils.closeKeybord(edittext_search, MoreSearchActivity.this);
                        toast("搜索");
                        search();
                        searchDB();
                    } else {
                        toast("请输入搜索内容");
                    }

                }
                return false;
            }
        });


    }

    void setRecyclerViewHeight() {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, * Utils.dp2px(MoreSearchActivity.this, 63) + Utils.dp2px(this, 38));
//        params.setMargins(0, com.shehuan.niv.Utils.dp2px(MoreSearchActivity.this, (float) 9.5), 0, 0);
//        recyclerview_content.setLayoutParams(params);
        adapter.setHeaderView(headerView);
    }

    void searchDB() {
//        frendsMessageEntityList = DBHelper.queryLoveDesc("18402995814");
        if (frendsMessageEntityList != null && frendsMessageEntityList.size() > 0) {
            toast("有数据库消息");
            setRecyclerViewHeight();
//            messageAdapter.setData(frendsMessageEntityList);
        }
    }

    void search() {
        showLoading();
        map.clear();
        map.put("Method", "SearchUser");
        map.put("SearchKey", edittext_search.getText().toString());
        map.put("Sinkey", SPUtils.getString("ket", "123"));
        map.put("Username", SPUtils.getString("Username", ""));
        OkGo.<String>get(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        if (CheckDate(response.body()).getState() != 1) {
//                            toast(CheckDate(response.body()).getMsg());
//                            return;
//                        }
                        UserListEntity entity = gson.fromJson(GetDate(response.body()), UserListEntity.class);
                        listEntities = new ArrayList<>();
                        listEntities.add(entity);
//                            entity.all_array = new ArrayList<>();
//                        head_img=用户头像
//                        fsate=是否好友(1好友 2单面好友 3不是好友)
//                        nickname=用户昵称
//                        username=帐号
//                        gender=性别(1无性别 2男性 3女性 4多性)
//                        signature=个性签名
//                        region_country=国家
//                        region_province=省份
//                        region_city=城市
//                        card=用户唯一标识
                        UserListEntity item = new UserListEntity();
                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
                        item.card = "1";
                        item.fsate = 2;
                        item.nickname = "时代厄尔asdfa";
                        item.username = "18402995814";
                        item.gender = 3;
                        item.signature = "asdfasdfasdlaksgdkajhsgdkjhafg";
                        item.region_province = "上海市";
                        item.region_country = "中国";
                        listEntities.add(item);
                        item = new UserListEntity();
                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
                        item.card = "1";
                        item.fsate = 2;
                        item.nickname = "sdlgiu时代法律框架和水果";
                        item.username = "18402995814";
                        item.gender = 3;
                        item.signature = "asdfasdfasdlaksgdkajhsgdkjhafg";
                        item.region_province = "上海市";
                        item.region_country = "中国";
                        listEntities.add(item);
                        item = new UserListEntity();
                        item.head_img = "https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg";
                        item.card = "1";
                        item.fsate = 1;
                        item.nickname = "时代厄尔";
                        item.username = "18402995814";
                        item.gender = 2;
                        item.signature = "拉开建设的广发卡就是短发开始就韩国进口韩国";
                        item.region_province = "上海市";
                        item.region_country = "中国";
                        listEntities.add(item);
                        adapter.setHeaderView(headerView);
//                        infoAdapter.setData(listEntities);

//                        if (entity.all_array != null && entity.all_array.size() > 0) {
//
//                        } else {
//
//
//                        }
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
