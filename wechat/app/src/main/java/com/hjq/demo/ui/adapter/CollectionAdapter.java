package com.hjq.demo.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.hjq.demo.R;
import com.hjq.demo.model.CollectListEntity;
import com.hjq.demo.util.TimeUtils;
import com.hjq.image.ImageLoader;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionAdapter extends RecyclerSwipeAdapter<CollectionAdapter.ViewHolder> {

    private Context mContext;
    private setOnItemClick mSetOnItemClick;
    private List<CollectListEntity> mList;

    public CollectionAdapter(Context context, List<CollectListEntity> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder classViewHolder, int i) {
        ViewHolder viewHolder = classViewHolder;
        CollectListEntity entity = mList.get(i);
        viewHolder.swippelayoutContent.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swippelayoutContent.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swippelayoutContent.findViewById(R.id.tv_collect_delete));
        if (entity != null) {
            viewHolder.tvCollectAddress.setText(entity.getNickname());
            if (!TextUtils.isEmpty(entity.getCtype())) {
                // 0=文本 1=图片
                if ("0".equals(entity.getCtype())) {
                    viewHolder.ivCollect.setVisibility(View.GONE);
                    viewHolder.tvCollectContent.setVisibility(View.VISIBLE);
                    viewHolder.tvCollectContent.setText(entity.getCval());
                } else if ("1".equals(entity.getCtype())) {
                    viewHolder.ivCollect.setVisibility(View.VISIBLE);
                    viewHolder.tvCollectContent.setVisibility(View.GONE);
                    ImageLoader.with(mContext).load(entity.getCval()).circle(10).into(viewHolder.ivCollect);
                }
            }
            if (!TextUtils.isEmpty(entity.getCtime())) {
                try {
                    if (!TimeUtils.IsToday(TimeUtils.getTimeString(Long.valueOf(entity.getCtime())))) {
                        viewHolder.tvCollectTime.setText(TimeUtils.stampToDate(entity.getCtime(), "HH:mm"));
                    } else if (!TimeUtils.IsYesterday(TimeUtils.getTimeString(Long.valueOf(entity.getCtime())))) {
                        viewHolder.tvCollectTime.setText("昨天");
                    } else if (TimeUtils.isThisWeek(Long.valueOf(entity.getCtime()))) {
                        viewHolder.tvCollectTime.setText(TimeUtils.dateToWeek(TimeUtils.getTimeString(Long.valueOf(entity.getCtime()), "yyyy-MM-dd")));
                    } else {
                        viewHolder.tvCollectTime.setText(TimeUtils.stampToDate(entity.getCtime(), "HH:mm"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            viewHolder.ivCollect.setOnClickListener(v -> {
                if (mSetOnItemClick != null) {
                    mSetOnItemClick.onDeleteMessageClick(v, viewHolder, i, mList);
                }
            });
            viewHolder.llCollect.setOnClickListener(v -> {
                if (mSetOnItemClick != null) {
                    mSetOnItemClick.onItemClick(v, viewHolder, i, mList);
                }
            });
        }
        mItemManger.bindView(viewHolder.itemView, i);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<CollectListEntity> entity) {
        mList.clear();
        Collections.reverse(entity);
        mList.addAll(entity);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != mList && mList.size() > 0) ? mList.size() : 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swippelayout_content;
    }

    public void closeDelete(ViewHolder viewHolder) {
        viewHolder.swippelayoutContent.close();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_collect_delete)
        TextView tvCollectDelete;
        @BindView(R.id.tv_collect_content)
        TextView tvCollectContent;
        @BindView(R.id.iv_collect)
        ImageView ivCollect;
        @BindView(R.id.tv_collect_address)
        TextView tvCollectAddress;
        @BindView(R.id.tv_collect_time)
        TextView tvCollectTime;
        @BindView(R.id.swippelayout_content)
        SwipeLayout swippelayoutContent;
        @BindView(R.id.ll_collect)
        LinearLayout llCollect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void onItemClickListener(setOnItemClick mSetOnItemClick) {
        this.mSetOnItemClick = mSetOnItemClick;
    }

    public interface setOnItemClick {
        void onItemClick(View view, ViewHolder viewHolder, int position, List<CollectListEntity> entityList);

        void onDeleteMessageClick(View view, ViewHolder viewHolder, int position, List<CollectListEntity> entityList);

    }
}
