package com.hjq.demo.daerxiansheng.Activity;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.hjq.demo.daerxiansheng.Entity.ChatContactEntity;
import com.hjq.demo.daerxiansheng.Entity.UserListEntity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsEntity;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.daerxiansheng.sql.GroupEntity;
import com.hjq.demo.daerxiansheng.sql.GroupMessageEntity;
import com.hjq.demo.guowenbin.activity.FrienProfileActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.SPUtils;
import com.hjq.demo.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends MyActivity {
    @BindView(R.id.imageview_cancle)
    ImageView imageview_cancle;
    @BindView(R.id.edittext_search)
    EditText edittext_search;
    @BindView(R.id.textview_cancle)
    TextView textview_cancle;
    @BindView(R.id.recyclerview_message)
    RecyclerView recyclerview_message;
    @BindView(R.id.recyclerview_userinfo)
    RecyclerView recyclerview_userinfo;
    @BindView(R.id.nestedscrollview_content)
    NestedScrollView nestedscrollview_content;
    @BindView(R.id.linearlayout_content)
    LinearLayout linearlayout_content;


    private ChatMessageAdapter messageAdapter, infoAdapter;
    private List<FrendsMessageEntity> contactListUserEntity, infoListUserEntity;//获取聊天消息内容
    private List<GroupMessageEntity> groupMessageEntities;//群组聊天消息
    private List<UserListEntity> listEntities;
    private List<ChatContactEntity> chatThreeList, contactThreeList, chatList, contactList;
    private List<List<ChatContactEntity>> listEntity;
    private boolean chatBackgroud, contactBackgroud;//是否有数据填充

    private int footerHeight;//footerview高度
    TextView userinfo_textview_headercontent, userinfo_textview_footercontent, message_textview_headercontent, message_textview_footercontent;
    RelativeLayout userinfo_relativelayout_root, message_relativelayout_root;
    View userinfo_headerView, userinfo_footerView, message_headerView, message_footerView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        linearlayout_content.setVisibility(View.GONE);
        background(true);
        initMessageHeaderView();
        initUserInfoHeaderView();
        message_footerView = getLayoutInflater().inflate(R.layout.view_footercontent, null);
        message_relativelayout_root = message_footerView.findViewById(R.id.relativelayout_root);
        message_textview_footercontent = message_footerView.findViewById(R.id.textview_content);
        message_textview_footercontent.setText("更多聊天消息");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, 43));
        message_footerView.setLayoutParams(params);

        userinfo_footerView = getLayoutInflater().inflate(R.layout.view_footercontent, null);
        userinfo_relativelayout_root = userinfo_footerView.findViewById(R.id.relativelayout_root);
        userinfo_textview_footercontent = userinfo_footerView.findViewById(R.id.textview_content);
        userinfo_textview_footercontent.setText("更多联系人");
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, 43));
        userinfo_footerView.setLayoutParams(params);
        message_footerView.setOnClickListener(v -> {
            toast("查看更多聊天消息");
            setMessageView(chatList.size(), false);
            messageAdapter.setData(chatList);
        });
        userinfo_footerView.setOnClickListener(v -> {
            toast("查看更多联系人");
            setUserView(contactList.size(), false);
            infoAdapter.setData(contactList);
        });
        initUserInfoHeaderView();
        initMessageHeaderView();
        imageview_cancle.setVisibility(View.GONE);

        LinearLayoutManager messageManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerview_message.setLayoutManager(messageManager);
        LinearLayoutManager userInfoManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerview_userinfo.setLayoutManager(userInfoManager);
        infoAdapter = new ChatMessageAdapter(this, null);
        recyclerview_userinfo.setAdapter(infoAdapter);
        messageAdapter = new ChatMessageAdapter(this, null);
        recyclerview_message.setAdapter(messageAdapter);
        infoAdapter.setOnItemClickListener(new ChatMessageAdapter.onItemClick() {
            @Override
            public void onUserClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
                toast("点击了联系人或群组头像");
            }

            @Override
            public void onItemClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
                Intent intent = new Intent(SearchActivity.this, FrienProfileActivity.class);
                intent.putExtra("card", entity.card);
                intent.putExtra("username", entity.phone);
                startActivity(intent);
            }

            @Override
            public void onRootViewClick() {

            }

            @Override
            public void onItemClick(View view, int position, ChatContactEntity data) {

            }
        });
        messageAdapter.setOnItemClickListener(new ChatMessageAdapter.onItemClick() {
            @Override
            public void onUserClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
                toast("点击了聊天消息头像");
            }

            @Override
            public void onRootViewClick() {

            }

            @Override
            public void onItemClick(View view, int position, ChatContactEntity data) {

            }


            @Override
            public void onItemClick(View view, int position, ChatContactEntity entity, List<ChatContactEntity> list) {
                Intent intent = new Intent(SearchActivity.this, MoreSearchActivity.class);
                intent.putExtra("searchContent", edittext_search.getText().toString());
                intent.putParcelableArrayListExtra("listEntity", (ArrayList<? extends Parcelable>) listEntity.get(position));
                intent.putExtra("selectType", 1);
                startActivity(intent);
            }
        });
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
        imageview_cancle.setOnClickListener(v -> {
            int index = edittext_search.getSelectionStart();
            Editable editable = edittext_search.getText();
            editable.delete(index - 1, index);
        });
        edittext_search.addTextChangedListener(textWatcher);
        textview_cancle.setOnClickListener(v -> {
            Utils.closeKeybord(edittext_search, SearchActivity.this);
            finish();
        });
        edittext_search.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!TextUtils.isEmpty(edittext_search.getText().toString())) {
                    Utils.closeKeybord(edittext_search, SearchActivity.this);
                    messageAdapter.setSerachContent(edittext_search.getText().toString());
                    listEntity = new ArrayList<>();
                    chatThreeList = new ArrayList<>();
                    contactThreeList = new ArrayList<>();
                    chatList = new ArrayList<>();
                    contactList = new ArrayList<>();
                    searchUser();
                    searchUserMessage();//查询符合条件的好友连天消息
                    if (chatBackgroud && contactBackgroud) {
                        background(true);
                    } else {
                        background(false);
                    }
                } else {
                    toast("请输入搜索内容");
                }

            }
            return false;
        });
    }

    //更多聊天信息头部view
    void initUserInfoHeaderView() {
        userinfo_headerView = getLayoutInflater().inflate(R.layout.view_headercontent, null);
        userinfo_textview_headercontent = userinfo_headerView.findViewById(R.id.textview_content);
        userinfo_textview_headercontent.setText("联系人/群组");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.shehuan.niv.Utils.dp2px(this, 38));
        userinfo_headerView.setLayoutParams(params);
    }

    //更多联系人头部view
    void initMessageHeaderView() {
        message_headerView = getLayoutInflater().inflate(R.layout.view_headercontent, null);
        message_textview_headercontent = message_headerView.findViewById(R.id.textview_content);
        message_textview_headercontent.setText("聊天消息");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.shehuan.niv.Utils.dp2px(this, 38));
        message_headerView.setLayoutParams(params);
    }


    /**
     * 查询好友聊天记录
     */
    void searchUserMessage() {
        List<List<FrendsMessageEntity>> contactListUserEntity = DBHelper.getFrendMessageListAsc(edittext_search.getText().toString());
        if (contactListUserEntity != null && contactListUserEntity.size() > 0) {

            for (int i = 0; i < contactListUserEntity.size(); i++) {
                chatList = new ArrayList<>();
                for (int j = 0; j < contactListUserEntity.get(i).size(); j++) {
                    ChatContactEntity entity = addMessageAdapterContent(
                            contactListUserEntity.get(i).get(j).toUserImage,
                            contactListUserEntity.get(i).get(j).card,
                            false,
                            contactListUserEntity.get(i).get(j).toUid
                            , contactListUserEntity.get(i).get(j).toUserName,
                            contactListUserEntity.get(i).get(j).UserName,
                            contactListUserEntity.get(i).size(), contactListUserEntity.get(i).get(j).content, contactListUserEntity.get(i).get(j).Time + "");
                    chatList.add(entity);
                }
                listEntity.add(chatList);
                ChatContactEntity entity = addMessageAdapterContent(
                        contactListUserEntity.get(i).get(0).toUserImage,
                        contactListUserEntity.get(i).get(0).card,
                        false,
                        contactListUserEntity.get(i).get(0).toUid
                        , contactListUserEntity.get(i).get(0).toUserName,
                        contactListUserEntity.get(i).get(0).UserName,
                        contactListUserEntity.get(i).size(), contactListUserEntity.get(i).get(0).content, contactListUserEntity.get(i).get(0).Time + "");

                if (chatThreeList.size() < 3) {
                    if (i < 3) {
                        chatThreeList.add(entity);
                    }
                }
            }
            setMessageView(chatThreeList.size(), true);

        }
        searchGroupMessage();//查询符合条件的群组聊天消息
    }

    /**
     * 查询群组消息
     */
    void searchGroupMessage() {
        List<List<GroupMessageEntity>> infoListUserEntity = DBHelper.getGroupMessageListAsc(edittext_search.getText().toString());
        if (infoListUserEntity != null && infoListUserEntity.size() > 0) {

            for (int i = 0; i < infoListUserEntity.size(); i++) {
                chatList = new ArrayList<>();
                for (int j = 0; j < infoListUserEntity.get(i).size(); j++) {
                    ChatContactEntity entity = addMessageAdapterContent(
                            infoListUserEntity.get(i).get(j).toUserImage,
                            infoListUserEntity.get(i).get(j).groupCard,
                            false,
                            infoListUserEntity.get(i).get(j).toUid
                            , infoListUserEntity.get(i).get(j).toUserName,
                            infoListUserEntity.get(i).get(j).UserName,
                            infoListUserEntity.get(i).size(), infoListUserEntity.get(i).get(j).content, infoListUserEntity.get(i).get(j).Time + "");
                    chatList.add(entity);
                }
                listEntity.add(chatList);
                ChatContactEntity entity = addMessageAdapterContent(infoListUserEntity.get(i).get(0).toUserImage,
                        infoListUserEntity.get(i).get(0).groupCard,
                        true,
                        infoListUserEntity.get(i).get(0).toUid,
                        infoListUserEntity.get(i).get(0).toUserName,
                        infoListUserEntity.get(i).get(0).UserName,
                        infoListUserEntity.get(i).size(), infoListUserEntity.get(i).get(0).content, infoListUserEntity.get(i).get(0).Time + "");
                if (chatThreeList.size() < 3) {
                    if (i < 3) {
                        chatThreeList.add(entity);
                    }
                }
                chatList.add(entity);
            }
            setMessageView(chatThreeList.size(), true);
            messageAdapter.setData(chatThreeList);
            chatBackgroud = false;
        } else {
            if (chatThreeList == null || chatThreeList.size() <= 0) {
                messageAdapter.setData(new ArrayList<>());
                messageAdapter.setFooterView(null);
                messageAdapter.setHeaderView(null);
                chatBackgroud = true;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                recyclerview_message.setLayoutParams(params);
            } else {
                setMessageView(chatThreeList.size(), true);
                messageAdapter.setData(chatThreeList);
                chatBackgroud = false;
            }
        }
    }

    /**
     * 设置聊天消息的视图
     *
     * @param size
     */
    void setMessageView(int size, boolean isfooter) {
        LinearLayout.LayoutParams params = null;
        if (size > 3) {
            if (isfooter) {
                footerHeight = Utils.dp2px(SearchActivity.this, 38) + Utils.dp2px(SearchActivity.this, 41);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3 * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            } else {
                footerHeight = Utils.dp2px(SearchActivity.this, 38);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            }
            messageAdapter.setFooterView(message_footerView);
        } else {
            footerHeight = Utils.dp2px(SearchActivity.this, 38);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            messageAdapter.setFooterView(null);
        }
        params.setMargins(0, Utils.dp2px(SearchActivity.this, (float) 9.5), 0, 0);
        recyclerview_message.setLayoutParams(params);
        messageAdapter.setVisibilityInfo(true);
        messageAdapter.setHeaderView(message_headerView);
        chatBackgroud = false;
    }

    /**
     * 给聊天消息集合添加数据准备刷新列表
     *
     * @param imageUrl  头像url
     * @param card      唯一标识
     * @param isContact 是否是群组
     * @param id        用户id
     * @param name      用户名称
     * @param userName  用户手机号
     * @param count     聊天内容条数
     */
    ChatContactEntity addMessageAdapterContent(String imageUrl, String card, boolean isContact, String id, String name, String userName, int count, String content, String time) {
        ChatContactEntity entity = new ChatContactEntity();
        entity.imageUrl = imageUrl;
        entity.card = card;
        entity.isContact = isContact;
        entity.id = id;
        entity.name = name;
        entity.phone = userName;
        entity.messageCount = count;
        entity.content = content;
        entity.time = time;
        return entity;
    }

    void searchUser() {
        List<FrendsEntity> entityList = DBHelper.getUserEntityName(edittext_search.getText().toString());
        if (entityList != null && entityList.size() > 0) {
//            int size = entityList.size() > 3 ? 3 : entityList.size();
            for (int i = 0; i < entityList.size(); i++) {
                ChatContactEntity entity = addMessageAdapterContent(
                        entityList.get(i).head_img,
                        entityList.get(i).card,
                        false,
                        entityList.get(i).userid
                        , entityList.get(i).nickname,
                        entityList.get(i).username,
                        0
                        , null, "");
                if (contactThreeList.size() < 3) {
                    if (i < 3) {
                        contactThreeList.add(entity);
                    }
                }
                contactList.add(entity);
            }
            setUserView(contactThreeList.size(), true);
        }
        searchUserGroup();
    }

    void searchUserGroup() {
        List<GroupEntity> entityList = DBHelper.getGroupEntity(edittext_search.getText().toString());
        if (entityList != null && entityList.size() > 0) {

            for (int i = 0; i < entityList.size(); i++) {
                ChatContactEntity entity = addMessageAdapterContent(
                        entityList.get(i).groupImg,
                        entityList.get(i).card,
                        false,
                        ""
                        , entityList.get(i).groupName,
                        "",
                        0,
                        null, "");
                if (contactThreeList.size() < 3) {
                    if (i < 3) {
                        contactThreeList.add(entity);
                    }
                }
                contactList.add(entity);
            }
            setUserView(contactThreeList.size(), true);
            infoAdapter.setData(contactThreeList);
            contactBackgroud = false;
        } else {
            if (contactThreeList == null || contactThreeList.size() <= 0) {
                infoAdapter.setData(new ArrayList<>());
                infoAdapter.setHeaderView(null);
                infoAdapter.setFooterView(null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                recyclerview_userinfo.setLayoutParams(params);
                contactBackgroud = true;
            } else {
                setUserView(contactThreeList.size(), true);
                infoAdapter.setData(contactThreeList);
                contactBackgroud = false;
            }
        }
    }

    void setUserView(int size, boolean isfooter) {
        LinearLayout.LayoutParams params = null;
        if (size > 3) {
            if (isfooter) {
                footerHeight = Utils.dp2px(SearchActivity.this, 38) + Utils.dp2px(SearchActivity.this, 41);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3 * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            } else {
                footerHeight = Utils.dp2px(SearchActivity.this, 38);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            }
            infoAdapter.setFooterView(userinfo_footerView);
        } else {
            footerHeight = Utils.dp2px(SearchActivity.this, 38);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size * Utils.dp2px(SearchActivity.this, 63) + footerHeight);
            infoAdapter.setFooterView(null);
        }
        params.setMargins(0, Utils.dp2px(SearchActivity.this, (float) 9.5), 0, 0);
        recyclerview_userinfo.setLayoutParams(params);
        infoAdapter.setVisibilityInfo(false);
        infoAdapter.setHeaderView(userinfo_headerView);
        contactBackgroud = false;

    }

    void background(boolean isWhite) {
        if (isWhite) {
            linearlayout_content.setVisibility(View.GONE);
            nestedscrollview_content.setBackgroundResource(R.color.white);
        } else {
            linearlayout_content.setVisibility(View.VISIBLE);
            nestedscrollview_content.setBackgroundResource(R.color.gray_EEEEEE);
        }
    }


    /**
     * 搜索用户/虚拟app
     */
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
                        if (CheckDate(response.body()).getState() != 1 || CheckDate(response.body()).getState() != 2) {
                            toast(CheckDate(response.body()).getMsg());
                            return;
                        }
                        UserListEntity entity = gson.fromJson(GetDate(response.body()), UserListEntity.class);
                        listEntities = new ArrayList<>();
                        listEntities.add(entity);
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
