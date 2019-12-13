package com.hjq.demo.daerxiansheng.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.daerxiansheng.Entity.MessageEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.guowenbin.adapter.AddFriendsAdapter;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.TimeUtils;
import com.hjq.image.ImageLoader;
import com.shehuan.niv.Utils;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangrenwei on 2019-11-15 23:49.
 */
public class MessageAdapter extends RecyclerSwipeAdapter<MessageAdapter.ViewHolder> {
    private View convertView;
    private Context context;
    private List<MessageListEntity> entityList;
    private setOnItemClick mSetOnItemClick;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(convertView);
    }

    public List<MessageListEntity> getEntityList() {
        return entityList;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MessageListEntity entity = entityList.get(i);
        viewHolder.swippelayout_content.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swippelayout_content.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swippelayout_content.findViewById(R.id.textview_delete));
        viewHolder.imageview_shield.setVisibility(View.GONE);
        if (entity != null) {
            //好友
            if (!TextUtils.isEmpty(entity.rtype)) {
                if (entity.rtype.equals("1")) {
                    if (!TextUtils.isEmpty(entity.head_img)) {
                        ImageLoader.with(context).load(entity.head_img).circle(10).into(viewHolder.imageview_header);
//                    ImageLoader.with(context).load("https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg").circle(5).into(viewHolder.imageview_header);
                    }

                    if (!TextUtils.isEmpty(entity.nickname)) {
                        viewHolder.textview_username.setText(entity.nickname);
                    }
                } else if (entity.rtype.equals("2")) {
                    if (!TextUtils.isEmpty(entity.group_img)) {
                        ImageLoader.with(context).load(entity.group_img).circle(10).into(viewHolder.imageview_header);
                    }
                    if (!TextUtils.isEmpty(entity.group_name)) {
                        viewHolder.textview_username.setText(entity.group_name);
                    }
                }
            }
            FrameLayout.LayoutParams params = null;
            if (entity.messCount > 0) {
                viewHolder.textview_tipscount.setVisibility(View.VISIBLE);
                if (entity.messCount < 99) {
                    viewHolder.textview_tipscount.setBackgroundResource(R.drawable.shape_fillredf66447_bordernone_radius100);
                    viewHolder.textview_tipscount.setText(String.valueOf(entity.messCount));
                    params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dp2px(context, 18));
                    params.gravity = Gravity.END | Gravity.TOP;
                    viewHolder.textview_tipscount.setLayoutParams(params);
                } else {
                    viewHolder.textview_tipscount.setBackgroundResource(R.drawable.shape_fillredf66447_bordernone_radius_9);
                    viewHolder.textview_tipscount.setText("···");
                    params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dp2px(context, 18));
                    params.gravity = Gravity.END | Gravity.TOP;
                    params.setMargins(Utils.dp2px(context, 6), 0, Utils.dp2px(context, 6), 0);
                    viewHolder.textview_tipscount.setLayoutParams(params);

                }
            } else {
                viewHolder.textview_tipscount.setVisibility(View.GONE);
            }
            if (entity.disturb.equals("1")) {
                viewHolder.imageview_shield.setVisibility(View.VISIBLE);
            }
            if (entity.rclass.equals("1")) {
                viewHolder.textview_content.setText(entity.rval);
            } else if (entity.rclass.equals("2")) {
                viewHolder.textview_content.setText("[图片]");
            } else if (entity.rclass.equals("3")) {
                viewHolder.textview_content.setText("[音频]");
            }
            if (!TextUtils.isEmpty(entity.rtime)) {
                try {
                    if (!TimeUtils.IsToday(TimeUtils.getTimeString(Long.valueOf(entity.rtime)))) {
                        viewHolder.textview_time.setText(TimeUtils.stampToDate(entity.rtime, "HH:mm"));
                    } else if (!TimeUtils.IsYesterday(TimeUtils.getTimeString(Long.valueOf(entity.rtime)))) {
                        viewHolder.textview_time.setText("昨天");
                    } else if (TimeUtils.isThisWeek(Long.valueOf(entity.rtime))) {
                        viewHolder.textview_time.setText(TimeUtils.dateToWeek(TimeUtils.getTimeString(Long.valueOf(entity.rtime), "yyyy-MM-dd")));
                    } else {
                        viewHolder.textview_time.setText(TimeUtils.stampToDate(entity.rtime, "HH:mm"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            viewHolder.framelayout_header.setOnClickListener(v -> {
                if (entity.rtype.equals("1") || entity.rtype.equals("2")) {
                    if (mSetOnItemClick != null) {
                        mSetOnItemClick.onHeaderUserClick(v, i, entity, entityList);
                    }
                }
            });
            viewHolder.textview_delete.setOnClickListener(v -> {
                if (mSetOnItemClick != null) {
                    mSetOnItemClick.onDeleteMessageClick(v, viewHolder, i, entity, entityList);
                }
            });
            viewHolder.relativelayout_root.setOnClickListener(v -> {
                if (mSetOnItemClick != null) {
                    mSetOnItemClick.onItemClick(v, viewHolder, i, entity, entityList);
                }
            });
        }
        mItemManger.bindView(viewHolder.itemView, i);
    }

    public void closeDelete(ViewHolder viewHolder) {
        viewHolder.swippelayout_content.close();

    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swippelayout_content;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_time, textview_content, textview_username, textview_tipscount, textview_delete;
        SwipeLayout swippelayout_content;
        ImageView imageview_header, imageview_shield;
        FrameLayout framelayout_header;
        RelativeLayout relativelayout_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_tipscount = itemView.findViewById(R.id.textview_tipscount);
            textview_time = itemView.findViewById(R.id.textview_time);
            textview_username = itemView.findViewById(R.id.textview_username);
            textview_content = itemView.findViewById(R.id.textview_content);
            swippelayout_content = itemView.findViewById(R.id.swippelayout_content);
            imageview_header = itemView.findViewById(R.id.imageview_header);
            framelayout_header = itemView.findViewById(R.id.framelayout_header);
            textview_delete = itemView.findViewById(R.id.textview_delete);
            imageview_shield = itemView.findViewById(R.id.imageview_shield);
            relativelayout_root = itemView.findViewById(R.id.relativelayout_root);

        }
    }

    public MessageAdapter(Context context, List<MessageListEntity> entity) {
        this.context = context;
        this.entityList = entity;
    }

    public void setData(List<MessageListEntity> entity) {
        Collections.reverse(entity);
        this.entityList = entity;
        notifyDataSetChanged();
    }

    //
//    @Override
//    public int getItemCount() {
//        return super.getItemCount();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        convertView = LayoutInflater.from(mRootContext).inflate(R.layout.item_message,parent,false);
//        return new ViewHolder(convertView);
//    }
//
//    public class ViewHolder extends MyRecyclerViewAdapter.ViewHolder {
//
//        @BindView(R.id.textview_time)
//        TextView textview_time;
//        @BindView(R.id.textview_content)
//        TextView textview_content;
//        @BindView(R.id.textview_username)
//        TextView textview_username;
//        @BindView(R.id.imageview_header)
//        ImageView imageview_header;
//        @BindView(R.id.framelayout_header)
//        FrameLayout framelayout_header;
//        @BindView(R.id.textview_tipscount)
//        TextView textview_tipscount;
//        @BindView(R.id.swippelayout_content)
//        SwipeLayout swippelayout_content;
//
//        public ViewHolder(View view){
//            super(view);
//
//        }
//
//
//        @Override
//        public void onBindView(int position) {
////            convertView.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    switch (event.getAction()){
////                        case MotionEvent.ACTION_UP:
////
////                            //获得ViewHolder
////                            ViewHolder viewHolder = (ViewHolder) v.getTag();
////
////                            //获得HorizontalScrollView滑动的水平方向值.
////                            int scrollX = viewHolder.horizontalscrollview_root.getScrollX();
////
////                            //获得操作区域的长度
//////                int actionA = viewHolder.button.getWidth();
//////                int actionB = viewHolder.buttonTest.getWidth();
//////                int actionW = actionA+actionB;
////                            int actionW = viewHolder.action.getWidth();
////                            /**
////                             *  注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
////                             ** 如果水平方向的移动值<操作区域的长度的一半,就复原
////                             ** 否则的话显示操作区域
////                             */
////                            if (scrollX < actionW / 3){
////                                viewHolder.horizontalscrollview_root.smoothScrollTo(0, 0);
////                            }else{
////                                viewHolder.horizontalscrollview_root.smoothScrollTo(actionW, 0);
////                            }
////                            return true;
////                    }
////                    return false;
////                }
////            });
//            swippelayout_content.setShowMode(SwipeLayout.ShowMode.PullOut);
//            swippelayout_content.addDrag(SwipeLayout.DragEdge.Right, swippelayout_content.findViewById(R.id.textviewTitle));
//            MessageEntity entity = getItem(position);
//            if (entity != null) {
//                if (!TextUtils.isEmpty(entity.imageUrl)) {
//                    ImageLoader.with(getContext()).load("https://imgs.foxfour.cn/2019%2F07%2F19%2F62b1e201907191310099355.jpg").circle(5).into(imageview_header);
//                }
//                if (!TextUtils.isEmpty(entity.content)) {
//                    textview_content.setText(entity.content);
//                }
//                if (!TextUtils.isEmpty(entity.time)) {
//                    textview_time.setText(entity.time);
//                }
//                if (!TextUtils.isEmpty(entity.username)) {
//                    textview_username.setText(entity.username);
//                }
//                if (entity.count > 0) {
//                    textview_tipscount.setText(entity.count + "");
//                    if (entity.count <= 9) {
//                        //圆型展示
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dp2px(getContext(), 57), Utils.dp2px(getContext(), 57));
//                        params.addRule(RelativeLayout.CENTER_VERTICAL);
//                        params.setMargins(Utils.dp2px(getContext(), 16), 0, Utils.dp2px(getContext(), 16), 0);
//                        framelayout_header.setLayoutParams(params);
//                        FrameLayout.LayoutParams framelayoutParams = new FrameLayout.LayoutParams(Utils.dp2px(getContext(), 18), Utils.dp2px(getContext(), 18));
//                        framelayoutParams.setMargins(0, Utils.dp2px(getContext(), 5), 0, 0);
//                        framelayoutParams.gravity = Gravity.END;
//                        textview_tipscount.setLayoutParams(framelayoutParams);
//                        textview_tipscount.setGravity(Gravity.CENTER);
//                    } else {
//                        //非圆形展示
//                    }
//                } else {
//                    textview_tipscount.setVisibility(View.GONE);
//                }
//            }
//        }
//    }
    public void onItemClickListener(setOnItemClick mSetOnItemClick) {
        this.mSetOnItemClick = mSetOnItemClick;
    }

    public interface setOnItemClick {
        void onItemClick(View view, ViewHolder viewHolder, int position, MessageListEntity entity, List<MessageListEntity> entityList);

        void onDeleteMessageClick(View view, MessageAdapter.ViewHolder viewHolder, int position, MessageListEntity entity, List<MessageListEntity> entityList);

        void onHeaderUserClick(View view, int position, MessageListEntity entity, List<MessageListEntity> entityList);
    }

}
