package com.hjq.demo.mine_chenmo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;

import butterknife.BindView;

/**
 * @Author 陈末
 * @Time 2019-11-14 21:38
 * @Title 令牌列表
 * @desc
 */
public final class TokenEditAdapter extends MyRecyclerViewAdapter<TokenEntity> {


    public TokenEditAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }


    final class ViewHolder extends MyRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.img_isselect)
        ImageView img_isselect;
        @BindView(R.id.textview_name)
        TextView textview_name;

        ViewHolder() {
            super(R.layout.item_token_edit);
        }

        @Override
        public void onBindView(int position) {
            TokenEntity entity = getItem(position);

            if (entity != null) {
                if (entity.isSelect) {
                    img_isselect.setImageResource(R.mipmap.icon_token_edit_select);
                } else {
                    img_isselect.setImageResource(R.mipmap.icon_token_edit_noselect);
                }
                if (!TextUtils.isEmpty(entity.name)) {
                    textview_name.setText(entity.name);
                }

            }

        }
    }
}