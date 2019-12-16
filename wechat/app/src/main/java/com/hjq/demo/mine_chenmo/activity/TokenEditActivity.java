package com.hjq.demo.mine_chenmo.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseRecyclerViewAdapter;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.mine_chenmo.adapter.TokenEditAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author 陈末
 * @Time 2019-11-14 22:18
 * @Title 令牌编辑
 * @desc
 */
public class TokenEditActivity extends MyActivity implements BaseRecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.rc_token_edit)
    RecyclerView rc_token_edit;
    @BindView(R.id.tv_delete)
    TextView tv_delete;

    TokenEditAdapter tokenEditAdapter;
    private List<TokenEntity> entity;
    List<Long> tokenId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_token_edit;
    }

    @Override
    protected void initView() {
        tokenId = new ArrayList<>();
        tokenEditAdapter = new TokenEditAdapter(getActivity());
        rc_token_edit.setLayoutManager(new LinearLayoutManager(getActivity()));
        tokenEditAdapter.setOnItemClickListener(this);
        rc_token_edit.setAdapter(tokenEditAdapter);
        entity = DBHelper.queryToken();
        if (entity != null && entity.size() > 0) {
            tokenEditAdapter.setData(entity);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        finish();
    }

    @OnClick({R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                if (tokenId != null && tokenId.size() > 0) {
                    for (Long item : tokenId) {
                        DBHelper.deleteToken(item);
                    }
                    entity = DBHelper.queryToken();
                    tokenEditAdapter.setData(entity);
                    toast("删除成功");
                } else {
                    toast("请选择需要删除的令牌");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        if (entity != null && entity.size() > 0) {
            if (entity.get(position) != null) {
                entity.get(position).isSelect = !entity.get(position).isSelect;
                if (entity.get(position).isSelect) {
                    tokenId.add(entity.get(position).id);
                } else {
                    tokenId.remove(entity.get(position).id);
                }
            }
        }
        tokenEditAdapter.notifyDataSetChanged();
    }
}
