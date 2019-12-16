package com.hjq.demo.guowenbin.Fragment;


import android.view.View;

import androidx.fragment.app.Fragment;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends MyLazyFragment<HomeActivity> {


    public static FriendFragment newInstance() {
        return new FriendFragment();
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


}
