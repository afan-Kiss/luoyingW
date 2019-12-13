package com.hjq.demo.daerxiansheng.Adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.daerxiansheng.Entity.ChatContactEntity;
import com.hjq.demo.util.TimeUtils;
import com.hjq.demo.util.Utils;
import com.hjq.image.ImageLoader;
import com.hp.hpl.sparta.Text;

import java.text.ParseException;
import java.util.List;
import java.util.Timer;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-23.
 * 简述: <详细的聊天记录item显示聊天内容>
 */
public class SearchMessageDetailAdapter extends BaseRecyclerViewAdapter<SearchMessageDetailAdapter.ViewHolder, ChatContactEntity> {
    private String searchContent;

    public SearchMessageDetailAdapter(Context context, List<ChatContactEntity> data) {
        super(context, data);
    }

    @Override
    int getResource(int viewType) {
        return R.layout.item_search_message_detail;
    }

    @Override
    ViewHolder getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    int getRealItemViewType(int position) {
        return position;
    }

    @Override
    void bindView(ViewHolder holder, int position) {
        ChatContactEntity entity = getData(position);
        if (entity != null) {
            ImageLoader.with(mRootContext).load(entity.imageUrl).circle(10).into(holder.imageview_header);
            if (!TextUtils.isEmpty(entity.name)) {
                holder.textview_name.setText(entity.name);
            } else {
                holder.textview_name.setText(entity.phone);
            }
            if (!TextUtils.isEmpty(searchContent)) {
                Log.d("搜索字符在第几位", entity.content.length() + "字符下标:" + entity.content.indexOf(searchContent));
            }
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(mRootContext.getResources().getColor(R.color.yellow_FD9644));
            SpannableString spannableString = new SpannableString(entity.content);
            if(searchContent.length()<=1){
                spannableString.setSpan(colorSpan, entity.content.indexOf(searchContent), entity.content.indexOf(searchContent)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                spannableString.setSpan(colorSpan, entity.content.indexOf(searchContent), searchContent.length()+entity.content.indexOf(searchContent), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (!TextUtils.isEmpty(entity.time)) {
                try {
                    if (TimeUtils.IsToday(TimeUtils.getTimeString(Long.valueOf(entity.time)))) {
                        holder.textview_date.setText(TimeUtils.getTimeString(Long.valueOf(entity.time), "HH:mm"));
                    } else if (!TimeUtils.IsYesterday(TimeUtils.getTimeString(Long.valueOf(entity.time)))) {
                        holder.textview_date.setText("昨天");
                    } else if (TimeUtils.isThisWeek(Long.valueOf(entity.time))) {
                        holder.textview_date.setText(TimeUtils.dateToWeek(TimeUtils.getTimeString(Long.valueOf(entity.time), "yyyy-MM-dd")));
                    } else {
                        holder.textview_date.setText(TimeUtils.getTimeString(Long.valueOf(entity.time)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(entity.content)) {
                holder.textview_content.setText(spannableString);
            }
        }

    }

    public void setSerachContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview_header;
        TextView textview_date, textview_name, textview_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_header = itemView.findViewById(R.id.imageview_header);
            textview_date = itemView.findViewById(R.id.textview_date);
            textview_name = itemView.findViewById(R.id.textview_name);
            textview_content = itemView.findViewById(R.id.textview_content);
        }
    }

    public interface onCliclckListener {
        void onItemClick(View v, int position, ChatContactEntity entity, List<ChatContactEntity> list);
    }
}
