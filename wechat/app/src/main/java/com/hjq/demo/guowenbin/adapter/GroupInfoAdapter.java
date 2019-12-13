package com.hjq.demo.guowenbin.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.GroupListModel;
import com.hjq.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupInfoAdapter extends BaseQuickAdapter<GroupListModel.AllArrayBean.UserListBean, BaseViewHolder> {
    @BindView(R.id.group_info_head_iv)
    ImageView group_iv;
    Context contexts;
    List<GroupListModel.AllArrayBean.UserListBean> mdata = new ArrayList<>();


    public GroupInfoAdapter(int layoutResId, @Nullable List<GroupListModel.AllArrayBean.UserListBean> data, Context context) {
        super(layoutResId, data);
        this.mdata = data;
        contexts = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListModel.AllArrayBean.UserListBean item) {
        group_iv = helper.getView(R.id.group_info_head_iv);
        ImageLoader.with(contexts).load(item.getHead_img()).into(group_iv);
        helper.setText(R.id.group_info_head_tv, item.getNickname());

    }
}

