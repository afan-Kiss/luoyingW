package com.hjq.demo.daerxiansheng.Adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
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
 * 简述: <聊天消息adapter>
 */
public class ChatMessageAdapter extends BaseRecyclerViewAdapter<ChatMessageAdapter.ViewHolder, ChatContactEntity> {
    private onItemClick mOnItemClick;
    private boolean isVisibility;//是否显示信息栏//true显示信息栏 false不显示
    private String searchContent;

    public ChatMessageAdapter(Context context, List<ChatContactEntity> data) {
        super(context, data);
    }

    @Override
    int getResource(int viewType) {
        return R.layout.item_user;
    }

    @Override
    public View getView(int resourceId, ViewGroup viewGroup, boolean attachToParent) {
        return super.getView(R.layout.item_user, viewGroup, false);
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
        holder.relativelayout_root.setVisibility(View.VISIBLE);
        ChatContactEntity entity = getData(position);
        if (position == mData.size() - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }
        if (entity != null) {
            if (!isVisibility) {
                holder.textview_info.setVisibility(View.GONE);
            } else {
                holder.textview_info.setVisibility(View.VISIBLE);
                if (entity.messageCount > 1) {
                    holder.textview_info.setText(String.format(mRootContext.getString(R.string.chatMessageCount), entity.messageCount));
                } else {
                    if (!TextUtils.isEmpty(entity.content)) {
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(mRootContext.getResources().getColor(R.color.yellow_FD9644));
                        SpannableString spannableString = new SpannableString(entity.content);
                        if (searchContent.length() <= 1) {
                            spannableString.setSpan(colorSpan, entity.content.indexOf(searchContent), entity.content.indexOf(searchContent)+ 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }else{
//                            spannableString.setSpan(colorSpan, entity.content.indexOf(searchContent), searchContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(colorSpan, entity.content.indexOf(searchContent), searchContent.length()+entity.content.indexOf(searchContent), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        holder.textview_info.setText(spannableString);
                    }

                }
            }
            if (!TextUtils.isEmpty(entity.name)) {
                holder.textview_name.setText(entity.name);
            } else if (!TextUtils.isEmpty(entity.phone)) {
                holder.textview_name.setText(entity.phone);
            }
            if (!TextUtils.isEmpty(entity.imageUrl)) {
                ImageLoader.with(mRootContext).load(entity.imageUrl).circle(10).into(holder.imageview_header);
            }
        }
        holder.imageview_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onUserClick(v, position, entity, mData);
                }
            }
        });
        holder.relativelayout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClick(v, position, entity, mData);
                }
            }
        });

    }

    public void setSerachContent(String searchContent) {
        this.searchContent = searchContent;
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
        void onUserClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list);

        void onItemClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list);

    }
}
