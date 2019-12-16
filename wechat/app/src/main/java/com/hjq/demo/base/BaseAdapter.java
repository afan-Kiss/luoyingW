package com.hjq.demo.base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder,T> extends RecyclerView.Adapter<VH> {
    protected List<T> mData;
    protected Context mRootContext;
    protected LayoutInflater mLayoutInflater;
    protected OnClickListener<T> mClickListener;
    protected final int ITEMTYPE_HEADER = -999;
    protected final int ITEMTYPE_FOOTER = -888;

//    protected RoundedCornersTransformation mRoundedCornersTransformation;

    protected View mHeaderView, mFooterView;

    public BaseAdapter(Context context, List<T> data) {
        this.mRootContext = context;
        this.mData = data;

        //获取屏幕分辨率和绝对宽高
        DisplayMetrics metrics = new DisplayMetrics();

        this.mLayoutInflater = LayoutInflater.from(this.mRootContext);
//        this.mViewHelper = ViewHelper_.getInstance_(this.mRootContext);
    }

    public View getView(@LayoutRes int resouceId) {
        return getView(resouceId, null);
    }

    public View getView(@LayoutRes int resouceId, ViewGroup viewGroup) {
        return getView(resouceId, viewGroup, viewGroup != null);
    }
    public View getView(@LayoutRes int resourceId, ViewGroup viewGroup, boolean attachToParent) {
        View result = null;

        if (mLayoutInflater != null) {
            result = mLayoutInflater.inflate(resourceId, viewGroup, attachToParent);
        }

        return result;
    }

    protected abstract int getResource(int viewType);

    protected abstract VH getViewHolder(View view, int viewType);

    protected abstract int getRealItemViewType(int position);

    protected abstract void bindView(@NonNull VH holder, int position);

    @Override
    public int getItemViewType(int position) {
        if (this.mHeaderView != null && position == 0) {
            return ITEMTYPE_HEADER;
        }
        if (this.mFooterView != null && this.mData != null && position == this.mData.size() + (this.mHeaderView == null ? 0 : 1)) {
            return ITEMTYPE_FOOTER;
        }
        int realPostion = this.mHeaderView == null ? position : position - 1;
        return getRealItemViewType(realPostion);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEMTYPE_HEADER) {
            return (VH) new HeaderViewHolder(this.mHeaderView);
        }
        if (viewType == ITEMTYPE_FOOTER) {
            return (VH) new FooterViewHolder(this.mFooterView);
        }

        int resourceId = getResource(viewType);
        View view = getView(resourceId, parent, false);
        VH viewHolder = getViewHolder(view, viewType);
        if (viewHolder == null) {
            viewHolder = (VH) new DefaultViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (holder instanceof BaseAdapter.HeaderViewHolder || holder instanceof BaseAdapter.FooterViewHolder) {
            return;
        }
        int realPosition = position - (this.mHeaderView == null ? 0 : 1);
        bindView(holder, realPosition);
    }

    public void setHeaderView(View view) {
        this.mHeaderView = view;
        notifyDataSetChanged();
    }

    public void setFooterView(View view) {
        this.mFooterView = view;
        notifyDataSetChanged();
    }

    public void setHeaderAndFooterView(View headerView, View footerView) {
        setHeaderView(headerView);
        setFooterView(footerView);
    }
    @Override
    public int getItemCount() {
        return this.mData == null ? 0 : (this.mData.size() + (this.mHeaderView == null ? 0 : 1) + (this.mFooterView == null ? 0 : 1));
    }

    public void setData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public T getData(int position) {
        if (this.mData != null && this.mData.size() > position) {
            return this.mData.get(position);
        }
        return null;
    }

    public void setClickListener(OnClickListener<T> mClickListener) {
        this.mClickListener = mClickListener;
    }

//    protected RoundedCornersTransformation getRoundedCornersTransformation(int radius, int margin, RoundedCornersTransformation.CornerType cornerType) {
//        if (mRoundedCornersTransformation == null) {
//            mRoundedCornersTransformation = new RoundedCornersTransformation(radius, margin, cornerType);
//        }
//        return mRoundedCornersTransformation;
//    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        public Context mContext;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView = new View(mContext);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

    public interface OnClickListener<T> {
        void onRootViewClick();

        void onItemClick(View view, int position, T data,List<T> listData);
    }
}
