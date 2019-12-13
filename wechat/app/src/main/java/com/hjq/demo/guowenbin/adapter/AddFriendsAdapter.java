package com.hjq.demo.guowenbin.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.model.AddFriendListModel;
import com.hjq.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AddFriendsAdapter extends BaseQuickAdapter<AddFriendListModel.NewArrayBean, BaseViewHolder> {
    @BindView(R.id.add_friend_head_iv)
    ImageView add_friend_head_iv;
    @BindView(R.id.add_friend_agree)
    TextView add_friend_agree;

    Context contexst;
    List<AddFriendListModel.NewArrayBean> addFriendListModels;

    public AddFriendsAdapter(int layoutResId, @Nullable List<AddFriendListModel.NewArrayBean> data, Context context) {
        super(layoutResId, data);
        contexst = context;
        addFriendListModels = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendListModel.NewArrayBean item) {
        add_friend_head_iv = helper.getView(R.id.add_friend_head_iv);
        ImageLoader.with(contexst).load(item.getHead_img()).into(add_friend_head_iv);
        helper.setText(R.id.add_friend_name, item.getNickname());
        helper.setText(R.id.friend_Verify_friends, item.getMsg());
        helper.addOnClickListener(R.id.add_friend_agree);

    }
}
