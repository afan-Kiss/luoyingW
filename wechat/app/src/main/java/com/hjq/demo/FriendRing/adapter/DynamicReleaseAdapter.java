package com.hjq.demo.FriendRing.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.model.DynamicImageBean;
import com.hjq.demo.R;
import com.hjq.image.ImageLoader;

import java.util.List;

/**
 * @author GF
 * @des 动态图片
 * @date 2019/11/15
 */
public class DynamicReleaseAdapter extends BaseMultiItemQuickAdapter<DynamicImageBean,BaseViewHolder> {

    private Context mContext;

    public DynamicReleaseAdapter(List<DynamicImageBean> data,Context context) {
        super(data);
        addItemType(1, R.layout.item_dynamic_image_add);
        addItemType(2, R.layout.item_dynamic_image);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicImageBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                helper.addOnClickListener(R.id.iv_image_add);
                break;
            case 2:
                ImageView view = helper.getView(R.id.iv_image);
                ImageLoader.with(mContext)
                        .load(item.getPath())
                        //.override(108,108)
                        .into(view);
                helper.addOnClickListener(R.id.iv_image);

                break;
        }
    }
}
