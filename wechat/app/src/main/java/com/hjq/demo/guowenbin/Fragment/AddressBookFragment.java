package com.hjq.demo.guowenbin.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.daerxiansheng.Activity.AddUserActivity;
import com.hjq.demo.daerxiansheng.Activity.SearchActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.guowenbin.activity.AddNewFriendActivity;
import com.hjq.demo.guowenbin.activity.FrienProfileActivity;
import com.hjq.demo.guowenbin.activity.GroupChatActivity;
import com.hjq.demo.guowenbin.activity.VirtualActivity;
import com.hjq.demo.guowenbin.adapter.AddressBookAdapter;
import com.hjq.demo.model.FriendListModel;
import com.hjq.demo.session.Tezheng;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.widget.WaveSideBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.starrtc.demo.utils.NetworkUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * 通讯录
 */
public class AddressBookFragment extends MyLazyFragment<HomeActivity> implements BaseQuickAdapter.OnItemChildClickListener, WaveSideBar.OnSelectIndexItemListener {

    @BindView(R.id.sidebar)
    WaveSideBar mSidebar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.TitleBar)
    TitleBar TitleBar;
    @BindView(R.id.address_book_ll)
    LinearLayout mAddressBookLl;
    @BindView(R.id.swiper)
    SwipeRefreshLayout mSwipview;

    private List<FriendListModel.AllArrayBean> mDateList = new ArrayList<>();
    private FriendListModel.AllArrayBean mBean = new FriendListModel.AllArrayBean();
    private AddressBookAdapter addressBookAdapter;
    private LinearLayoutManager layoutManager;

    Intent intent;

    public static AddressBookFragment newInstance() {
        return new AddressBookFragment();
        // Required empty public constructor
    }

    FriendListModel friendListModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_book;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initView() {
        //布局一
        mBean.setItemtype(1);
        //
        mSidebar.setOnSelectIndexItemListener(this::onSelectIndexItem);
        addressBookAdapter = new AddressBookAdapter(mDateList, getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        addressBookAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mSidebar.setOnSelectIndexItemListener(this);
        mRecyclerView.setAdapter(addressBookAdapter);
        Updatebuddylist();
        TitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                startActivity(AddUserActivity.class);
            }
        })
        ;
        mSwipview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });


        //
        mAddressBookLl.setOnClickListener(v -> {
            startActivity(SearchActivity.class);
        });
    }

    @Override
    protected void initData() {


    }

    void Updatebuddylist() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())){
            List<FrendsEntity> dataList = DBHelper.queryFrend();
            if (dataList != null && dataList.size()>0){
                List<FriendListModel.AllArrayBean> mNoNetDateList = new ArrayList<>();
                for (int i = 0; i <dataList.size() ; i++) {
                    FriendListModel.AllArrayBean bean = new FriendListModel.AllArrayBean();
                    FrendsEntity frendsEntity = dataList.get(i);
                    bean.setUser_id(frendsEntity.getUserid());
                    bean.setBlack(frendsEntity.getBlack()+"");
                    bean.setCard(frendsEntity.getCard());
                    bean.setDisturb(frendsEntity.getDisturb()+"");
                    bean.setUsername(frendsEntity.getUsername());
                    bean.setNickname(frendsEntity.getNickname());
                    bean.setHead_img(frendsEntity.getHead_img());
                    bean.setFsate(1+"");
                    mNoNetDateList.add(bean);
                }
                addressBookAdapter.setNewData(mNoNetDateList);
            }


            return;
        }
        map.clear();
        map.put("Method", "FriendList");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getPhone_number());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        Log.i("gwb1008", "onSuccess: " + GetDate(response.body()));
                        String a = GetDate(response.body());
                        friendListModel = gson.fromJson(GetDate(response.body()), FriendListModel.class);
                        Tezheng.Features_friends = friendListModel.getFeatures_friends();
                        try {
                            parsing();
                        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                            badHanyuPinyinOutputFormatCombination.printStackTrace();
                        }
                        if (friendListModel != null && friendListModel.getAll_array() != null && friendListModel.getAll_array().size() > 0) {
                            //将获取到的好友列表更新到数据库中
                            for (FriendListModel.AllArrayBean item : friendListModel.getAll_array()) {
                                //判断已经是好友才添加到数据库
                                if (item.getFsate().equals("1")) {
                                    FrendsEntity frendsEntity = new FrendsEntity();
                                    frendsEntity.userid = item.getUser_id();
                                    frendsEntity.black = Integer.parseInt(item.getBlack());
                                    frendsEntity.card = item.getCard();
                                    frendsEntity.disturb = Integer.parseInt(item.getDisturb());
                                    frendsEntity.fsate = 1;
                                    frendsEntity.username = item.getUsername();
                                    frendsEntity.nickname = item.getNickname();
                                    frendsEntity.head_img = item.getHead_img();
                                    frendsEntity.userCard = UserManager.getUser().getCard();
                                    //查询是否存在该用户 根据主键查询
                                    if (DBHelper.getUserEntityCard(item.getCard()) != null && DBHelper.getUserEntityCard(item.getCard()).size() > 0) {
                                        frendsEntity.id = DBHelper.getUserEntityCard(item.getCard()).get(0).id;
                                    }
                                    if (frendsEntity.id != null) {
                                        DBHelper.updateFrend(frendsEntity);
                                    } else {
                                        DBHelper.insertFrendReplace(frendsEntity);
                                    }
                                }

                            }
                        }

                        intent = new Intent();
                        intent.setClass(getActivity(), FrienProfileActivity.class);
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
    public void onSelectIndexItem(String index) {
        try {
            int position = getPositionForSection(friendListModel.getAll_array(), index.charAt(0));
            if (position != -1) {
                /**
                 * 直接到指定位置  +1 因为是双布局 顶部布局不算 所以+1
                 */
                layoutManager.scrollToPositionWithOffset(position + 1, 0);
                layoutManager.setStackFromEnd(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算
     *
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public void parsing() throws BadHanyuPinyinOutputFormatCombination {
        if (friendListModel.getAll_array().size() > 0) {
            for (int i = 0; i < friendListModel.getAll_array().size(); i++) {
                FriendListModel.AllArrayBean bean = friendListModel.getAll_array().get(i);
                String pinyin = getPingYin(bean.getNickname());
                String sortString = "";
                if (!TextUtils.isEmpty(pinyin)) {
                    sortString = pinyin.substring(0, 1).toUpperCase();
                }
                if (sortString.matches("[A-Z]")) {
                    friendListModel.getAll_array().get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    friendListModel.getAll_array().get(i).setSortLetters("#");
                }
            }
        }
        //排序
        Collections.sort(friendListModel.getAll_array(), new PinyinComparatorAdmin());
        //是否第一个
        for (int i = 0; i < friendListModel.getAll_array().size(); i++) {
            if (i == getPositionForSection(friendListModel.getAll_array(), friendListModel.getAll_array().get(i).getSortLetters().charAt(0))) {
                friendListModel.getAll_array().get(i).setLetter(true);
            } else {
                friendListModel.getAll_array().get(i).setLetter(false);
            }
        }
        //
        mDateList.clear();
        mDateList.add(mBean);
        mDateList.addAll(friendListModel.getAll_array());
        addressBookAdapter.setNewData(mDateList);
    }


    /**
     * 汉子转拼音
     *
     * @param chineseStr
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPingYin(String chineseStr) throws BadHanyuPinyinOutputFormatCombination {
        String zhongWenPinYin = "";
        char[] chars = chineseStr.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());
            if (pinYin != null)
                zhongWenPinYin += pinYin[0];
            else
                zhongWenPinYin += chars[i];
        }
        return zhongWenPinYin;
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        return format;
    }

    /**
     * 方法含义：将当前字母传入方法体中， 来获取当前字母在集合中第一次出现的位置position  如果等于当前item的position，UI字母栏
     * 显示，如果不是，UI字母栏隐藏
     *
     * @param section
     * @return 对应集合中第一个出现的字母
     */
    public static int getPositionForSection(List<FriendListModel.AllArrayBean> mList, int section) {
        for (int i = 0; i < mList.size(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        FriendListModel.AllArrayBean bean = (FriendListModel.AllArrayBean) adapter.getData().get(position);
        switch (view.getId()) {
            case R.id.add_new_friends:
                Intent intent = new Intent(getActivity(), AddNewFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.add_group:
                startActivity(GroupChatActivity.class);
                break;
            case R.id.virtual_app:
                Intent intent1 = new Intent(getActivity(), VirtualActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_layouts:
                this.intent.putExtra("uid", bean.getUser_id());
                this.intent.putExtra("card", bean.getCard());
                this.intent.putExtra("username", bean.getUsername());
                this.intent.putExtra("black", bean.getBlack());
                Log.i("123123213123123", "onItemChildClick: " + bean.getBlack());
                startActivity(this.intent);
                break;
        }
    }

    /**
     * 特殊符号
     */
    public static class PinyinComparatorAdmin implements Comparator<FriendListModel.AllArrayBean> {
        @Override
        public int compare(FriendListModel.AllArrayBean o1, FriendListModel.AllArrayBean o2) {
            if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Updatebuddylist();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        Updatebuddylist();
        super.onRestart();

    }



    private void reloadData(){
        Updatebuddylist();
        mSwipview.setRefreshing(false);
    }

}