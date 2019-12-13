package com.hjq.demo.mine_chenmo.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;

import butterknife.BindView;

/**
 * desc   : 单聊
 */
public final class ChatAdapter extends MyRecyclerViewAdapter<String> {

    public ChatAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.tv_txt)
        TextView tv_txt;

        ViewHolder() {
            super(R.layout.item_copy);
        }

        @Override
        public void onBindView(int position) {
            tv_txt.setText(getItem(position));
        }

    }
}