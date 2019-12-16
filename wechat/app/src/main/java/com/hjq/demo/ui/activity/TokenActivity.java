package com.hjq.demo.ui.activity;


import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;

import com.hjq.demo.mine_chenmo.fragment.testTokenFragment;


import butterknife.BindView;

/**
 * 令牌
 */
public final class TokenActivity extends MyActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_token;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new testTokenFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();

//        //展示令牌列
//        showTokenList();

    }



    @Override
    protected void initData() {

        //初始化数据,取 [过往] 记录的密钥开始生成令牌链表AuthList.


    }




}