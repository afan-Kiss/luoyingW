package com.hjq.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.CollectEntity;
import com.hjq.demo.model.CollectListEntity;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.adapter.CollectionAdapter;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.widget.SpaceItemDecoration;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.starrtc.demo.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收藏
 */
public final class CollectionActivity extends MyActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    @BindView(R.id.swiper)
    SwipeRefreshLayout swiper;
    private CollectionAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvCollection.addItemDecoration(new SpaceItemDecoration(0, 16, false));
        rvCollection.setLayoutManager(manager);
        mAdapter = new CollectionAdapter(getActivity(), new ArrayList<>());
        rvCollection.setAdapter(mAdapter);
        getContent();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        mAdapter.onItemClickListener(new CollectionAdapter.setOnItemClick() {

            @Override
            public void onItemClick(View view, CollectionAdapter.ViewHolder viewHolder, int position, List<CollectListEntity> entityList) {

            }

            @Override
            public void onDeleteMessageClick(View view, CollectionAdapter.ViewHolder viewHolder, int position, List<CollectListEntity> entityList) {
                mAdapter.closeDelete(viewHolder);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void getContent() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            return;
        }
        map.clear();
        map.put("Method", "Ucollection");
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
                        CollectEntity entity = gson.fromJson(GetDate(response.body()), CollectEntity.class);
                        mAdapter.setData(entity.all_array);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast(R.string.error_network);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    private void reloadData() {
        getContent();
        swiper.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}