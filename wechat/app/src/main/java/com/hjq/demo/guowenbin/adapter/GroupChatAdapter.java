package com.hjq.demo.guowenbin.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.model.GroupListModel;
import com.hjq.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * desc   : 群聊列表
 */
public final class GroupChatAdapter extends BaseQuickAdapter<GroupListModel.AllArrayBean, BaseViewHolder> {
    @BindView(R.id.group_iv)
    ImageView group_iv;
    Context contexts;
    List<GroupListModel.AllArrayBean> mdata = new ArrayList<>();

    public GroupChatAdapter(int layoutResId, @Nullable List<GroupListModel.AllArrayBean> data, Context context) {
        super(layoutResId, data);
        this.mdata = data;
        contexts = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListModel.AllArrayBean item) {
        helper.setText(R.id.group_name, item.getGroup_name());
        group_iv = helper.getView(R.id.group_iv);
        ImageLoader.with(contexts).load(item.getGroup_img()).into(group_iv);

    }
}