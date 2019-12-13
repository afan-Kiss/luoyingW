package com.hjq.demo.daerxiansheng.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.daerxiansheng.Entity.ChatContactEntity;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.image.ImageLoader;

import java.util.List;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-19.
 * 简述: <联系人群组adapter>
 */
public class ContactAdapter extends BaseRecyclerViewAdapter<ContactAdapter.ViewHolder, ChatContactEntity> {
    private boolean isVisibility;//是否显示信息栏//true显示信息栏 false不显示
    private onItemClick mOnItemClick;

    public ContactAdapter(Context context, List<ChatContactEntity> data) {
        super(context, data);
    }

    @Override
    int getResource(int viewType) {
        return R.layout.item_user;
    }

    @Override
    ViewHolder getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    int getRealItemViewType(int position) {
        return position;
    }

    public void setVisibilityInfo(boolean isVisibility) {
        this.isVisibility = isVisibility;
    }

    @Override
    void bindView(ViewHolder holder, int position) {
        if (position >= 3) {
            return;
        }
        ChatContactEntity entity = getData(position);
        if (position == mData.size() - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }
        if (entity != null) {
            if (!isVisibility) {
                holder.textview_info.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(entity.imageUrl)) {
                ImageLoader.with(mRootContext).load(entity.imageUrl).circle(10).into(holder.imageview_header);
            }
        }
        holder.imageview_header.setOnClickListener(v -> {
            if (mOnItemClick != null) {
                mOnItemClick.onUserClick(v, position, entity,mData);
            }
        });
        holder.relativelayout_root.setOnClickListener(v -> {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClick(v, position, entity,mData);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview_header;
        TextView textview_name, textview_info;
        View view_line;
        RelativeLayout relativelayout_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_header = itemView.findViewById(R.id.imageview_header);
            textview_name = itemView.findViewById(R.id.textview_name);
            textview_info = itemView.findViewById(R.id.textview_info);
            view_line = itemView.findViewById(R.id.view_line);
            relativelayout_root = itemView.findViewById(R.id.relativelayout_root);
        }
    }

    public void setOnItemClickListener(onItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public interface onItemClick extends OnClickListener<ChatContactEntity> {
        void onUserClick(View v, int position, ChatContactEntity entity,List<ChatContactEntity> list);
        void onItemClick(View v,int position, ChatContactEntity entity,List<ChatContactEntity> list);

    }
}
