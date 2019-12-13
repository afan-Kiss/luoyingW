package com.hjq.demo.FriendRing.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.FriendListModel;
import com.hjq.image.ImageLoader;

import java.util.List;

/**
 * @author GF
 * @des 选择好友
 * @date 2019/11/18
 */
public class SelectContactAdapter extends BaseQuickAdapter<FriendListModel.AllArrayBean,BaseViewHolder>{

    private Context mContext;

    public SelectContactAdapter(int layoutResId, List<FriendListModel.AllArrayBean> data,Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListModel.AllArrayBean item) {
        ImageView view = helper.getView(R.id.iv_head);
        ImageLoader.with(mContext)
                .load(item.getHead_img())
                .into(view);
        helper.setText(R.id.tv_nickname,item.getNickname());
        helper.addOnClickListener(R.id.cb_select);
    }
}
