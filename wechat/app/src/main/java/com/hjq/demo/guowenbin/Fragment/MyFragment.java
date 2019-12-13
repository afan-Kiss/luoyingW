package com.hjq.demo.guowenbin.Fragment;


import androidx.fragment.app.Fragment;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends MyLazyFragment<HomeActivity> {


    public static MyFragment newInstance() {
        return new MyFragment();

        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}
