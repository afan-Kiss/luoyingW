package com.hjq.demo.ui.activity;

import com.hjq.demo.FriendRing.FriendRingFragment;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;

/**
 * 朋友圈
 */
public final class FriendCircleActivity extends MyActivity {

    private String mUserName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_frind_circle;
    }

    @Override
    protected void initView() {
        mUserName = getIntent().getStringExtra("userName");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new FriendRingFragment(mUserName))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    @Override
    protected void initData() {

    }

}