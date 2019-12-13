package com.hjq.demo.mine_chenmo.fragment;


import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseDialog;
import com.hjq.base.BaseDialogFragment;
import com.hjq.demo.R;
import com.hjq.demo.appsys.EventArgs;
import com.hjq.demo.appsys.EventType;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.mine_chenmo.activity.ManualinputActivity;
import com.hjq.demo.mine_chenmo.activity.TokenEditActivity;
import com.hjq.demo.mine_chenmo.adapter.testTokenAdapter;
import com.hjq.demo.ui.activity.ScanQrcodeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author 陈末
 * @Time 2019-11-14 21:11
 * @Title 令牌
 * @desc
 */
public class testTokenFragment extends MyLazyFragment {

    private final String TAG = this.getClass().getSimpleName();


    @BindView(R.id.rc_token_list)
    RecyclerView rc_token_list;

//        TokenAdapter tokenAdapter;
    testTokenAdapter tokenAdapter;
    private List<TokenEntity> tokenListEntity;

    public static testTokenFragment newInstance() {
        return new testTokenFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_token;
    }

    @Override
    protected void initView() {

        Log.i(TAG,"***********初始化View");
        //初始化适配器
        showTokenList();
        //初始化令牌列
        initAuthListView();
    }

    @Override
    public void onResume() {
        super.onResume();
//        tokenListEntity = DBHelper.queryToken();
//        if (tokenListEntity != null && tokenListEntity.size() >= 0) {
//            if (tokenAdapter != null) {
//                tokenAdapter.flashData();
//            }
//        }
    }


    @Override
    public void onEvent(EventArgs eventArgs) {
        super.onEvent(eventArgs);

        Log.i(TAG, "**********onEvent" + eventArgs.toString());


        if (eventArgs.getEventType().equals(EventType.Event_Token_Update)) {
            tokenListEntity = DBHelper.queryToken();
            if (tokenAdapter != null) {

                Log.i(TAG,"onEvent!");
                tokenAdapter.flashData();
            }
//            tokenAdapter.setItem((int) eventArgs.getObject(), tokenListEntity.get((int) eventArgs.getObject()));
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        startActivity(TokenEditActivity.class);
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        finish();
    }

    @OnClick({R.id.img_add_token})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_token:
                showTokenMenDialog();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化令牌列
     */
    private void showTokenList() {


        //创建适配器
        if (tokenAdapter == null) {
            tokenAdapter = new testTokenAdapter(getActivity());
            Log.i(TAG,"1.适配器初始化");
        }else {
            tokenAdapter.onBindView(getActivity());
        }

    }

    /**
     * 初始化令牌列
     */
    private void initAuthListView() {

        //载入适配器
        if(rc_token_list.getAdapter()==null){
            rc_token_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            rc_token_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            rc_token_list.setAdapter(tokenAdapter);
        }

    }


    /**
     * 显示 添加令牌 对话框
     */
    public void showTokenMenDialog() {
        BaseDialogFragment.Builder builder = new BaseDialogFragment.Builder(getActivity());

        builder.setContentView(R.layout.dialog_token_menu);
        builder.setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
        final BaseDialog baseDialog = builder.create();

        final LinearLayout ll_saoma = baseDialog.findViewById(R.id.ll_saoma);
        final LinearLayout ll_shoudong = baseDialog.findViewById(R.id.ll_shoudong);

//        baseDialog.getCurrentFocus();
        baseDialog.setCanceledOnTouchOutside(true);
        ll_saoma.setOnClickListener(v -> {
            startActivity(ScanQrcodeActivity.class);
            baseDialog.dismiss();
        });
        ll_shoudong.setOnClickListener(v -> {
            startActivity(ManualinputActivity.class);
            baseDialog.dismiss();
        });

        baseDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        DBHelper.deleteToken();
    }


}
