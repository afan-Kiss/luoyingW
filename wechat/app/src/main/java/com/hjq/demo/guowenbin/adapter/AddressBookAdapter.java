package com.hjq.demo.guowenbin.adapter;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.FriendListModel;
import com.hjq.image.ImageLoader;

import java.util.List;

import androidx.annotation.Nullable;

import butterknife.BindView;


public class AddressBookAdapter extends BaseMultiItemQuickAdapter<FriendListModel.AllArrayBean, BaseViewHolder> {
    @BindView(R.id.imageview_header)
    ImageView imageview_header;
    @BindView(R.id.textview_username)
    TextView textview_username;


    Context contexts;
    List<FriendListModel.AllArrayBean> friendListModelList;

    public AddressBookAdapter(@Nullable List<FriendListModel.AllArrayBean> data, Context context) {
        super(data);
        addItemType(1, R.layout.item_index);
        addItemType(2, R.layout.address_book_item);
        friendListModelList = data;
        contexts = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, FriendListModel.AllArrayBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                helper.addOnClickListener(R.id.add_new_friends);
                helper.addOnClickListener(R.id.add_group);
                helper.addOnClickListener(R.id.virtual_app);

                break;
            case 2:
                helper.setText(R.id.textview_username, item.getNickname());
                imageview_header = helper.getView(R.id.imageview_header);
                ImageLoader.with(contexts).load(item.getHead_img()).into(imageview_header);
                helper.setText(R.id.tv_letter, item.getSortLetters());
                if (item.getLetter()) {
                    helper.setGone(R.id.tv_letter, item.getLetter());
                } else {
                    helper.setGone(R.id.tv_letter, item.getLetter());
                }
                helper.addOnClickListener(R.id.ll_layouts);
                break;
            default:
                break;
        }


    }
}

