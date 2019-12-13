package com.hjq.demo.FriendRing.adapter;

import android.view.View;
import android.view.ViewStub;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.widget.MultiImageView;

/**
 * @author GF
 * @des 继承 BaseViewHolder  防止 viewStub 父布局 多次填充
 * @date 2019/11/16
 */
public class ViewStubHolder extends BaseViewHolder {

    MultiImageView multiImageView;

    public ViewStubHolder(View view) {
        super(view);
        initSubView(view.findViewById(R.id.viewStub));
    }

    public void initSubView(ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView =  subView.findViewById(R.id.multiImagView);
        if(multiImageView!=null){
            this.multiImageView = multiImageView;
        }
    }
}
