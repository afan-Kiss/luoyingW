package com.hjq.demo.guowenbin.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.FriendListModel;
import com.hjq.image.ImageLoader;

import java.util.List;

import butterknife.BindView;

public class VirtualAdApter extends BaseQuickAdapter<FriendListModel.AllArrayBean, BaseViewHolder> {
    @BindView(R.id.imageview_header)
    ImageView imageview_header;
    @BindView(R.id.textview_username)
    TextView textview_username;


    Context contexts;
    List<FriendListModel.AllArrayBean> friendListModelList;

    public VirtualAdApter(int layoutResId, @Nullable List<FriendListModel.AllArrayBean> data, Context context) {
        super(layoutResId, data);
        friendListModelList = data;
        contexts = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListModel.AllArrayBean item) {
        helper.setText(R.id.textview_username, item.getNickname());
        imageview_header = helper.getView(R.id.imageview_header);
        ImageLoader.with(contexts).load(item.getHead_img()).into(imageview_header);

    }
}
