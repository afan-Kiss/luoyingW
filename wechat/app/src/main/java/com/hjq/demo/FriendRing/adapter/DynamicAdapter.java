package com.hjq.demo.FriendRing.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.DynamicBean;
import com.hjq.demo.model.DynamicImageBean;
import com.hjq.demo.model.DynamicUcomment;
import com.hjq.demo.model.DynamicUlikeBean;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.ImageActivity;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.demo.util.TimeUtils;
import com.hjq.demo.widget.CommentListView;
import com.hjq.demo.widget.ExpandTextView;
import com.hjq.demo.widget.MultiImageView;
import com.hjq.demo.widget.PraiseListView;
import com.hjq.image.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author GF
 * @des 朋友圈Adapter
 * @date 2019/11/14
 */
public class DynamicAdapter extends BaseMultiItemQuickAdapter<DynamicBean.AllArrayBean, BaseViewHolder> {

    private Context mContext;
    private List<DynamicUlikeBean.AllArrayBean> ulikeList = new ArrayList<>();
    private List<DynamicUcomment.AllArrayBean> uCommentList = new ArrayList<>();

    public DynamicAdapter(List<DynamicBean.AllArrayBean> data, Context context) {
        super(data);
        mContext = context;
        addItemType(1, R.layout.item_dynamica_head);
        addItemType(2, R.layout.item_dynamic);
    }


    /**
     * 重写 onCreateViewHolder 返回  ViewStubHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        if (viewType == 1) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamica_head, parent, false);
            viewHolder = new BaseViewHolder(headView);
        } else {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, parent, false);
            viewHolder = new ViewStubHolder(headView);
        }
        return viewHolder;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void convert(BaseViewHolder helper, DynamicBean.AllArrayBean item) {
        ImageView imageView;
        switch (helper.getItemViewType()) {
            case 1:
                RelativeLayout circle_img = helper.getView(R.id.head_layout);
                ImageLoader.loadImage(circle_img, item.getCircle_img(), mContext);
                //
                imageView = helper.getView(R.id.iv_avatar);
                ImageLoader.with(mContext)
                        .load(item.getUser_img())
                        .into(imageView);
                helper.setText(R.id.tv_nickname, item.getUser_name());
                helper.addOnClickListener(R.id.head_layout);
                break;
            case 2:
                //图像
                imageView = helper.getView(R.id.iv_avatar);
                ImageLoader.with(mContext)
                        .load(item.getUser_img())
                        .into(imageView);
                helper.setText(R.id.tv_nickname, item.getUser_name());
                //时间
                Long time = (System.currentTimeMillis() / 1000) - Long.valueOf(item.getRelease_time());
                if (time > 0) {
                    if (time < 3600) {
                        if (time <= 60) {
                            helper.setText(R.id.tv_time, "刚刚");
                        } else {
                            helper.setText(R.id.tv_time, String.valueOf(time / 60) + "分钟前");
                        }
                    } else if (3600 < time && time <= 86400) {
                        helper.setText(R.id.tv_time, String.valueOf(time / 3600) + "小时前");
                    } else if (86400 < time && time <= 2592000) {
                        helper.setText(R.id.tv_time, String.valueOf(time / 86400) + "天前");
                    } else {
                        String result = TimeUtils.getTimeString(Long.valueOf(item.getRelease_time()), "MM-dd");
                        int index = result.indexOf("-");
                        helper.setText(R.id.tv_time, result.substring(0, index) + "月" + result.substring(index + 1, result.length()) + "日");
                    }
                }
                //内容 图片
                ArrayList<String> arrayList = new ArrayList();
                List<DynamicImageBean> beans = new ArrayList<>();
                String result[] = item.getRelease_val().split("\\|");
                List<String> resultList = Arrays.asList(result);
                if (resultList != null && resultList.size() > 0) {
                    for (int i = 0; i < resultList.size(); i++) {
                        String content = resultList.get(i);
                        if (!TextUtils.isEmpty(content)) {
                            if (content.contains("http")) {
                                DynamicImageBean bean = new DynamicImageBean();
                                bean.setPath(content);
                                beans.add(bean);
                                arrayList.add(content);
                            } else {
                                ExpandTextView contentTv = helper.getView(R.id.contentTv);
                                contentTv.setText(RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(content))));
                            }
                        }
                    }
                }
                //图片
                ViewStubHolder holder = (ViewStubHolder) helper;
                holder.multiImageView.setList(beans);
                if (arrayList.size() > 0) {
                    holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ImageActivity.start(mContext, arrayList, position);
                        }
                    });
                }
                //点赞
                List<DynamicUlikeBean.AllArrayBean> uLike = new ArrayList<>();
                if (ulikeList.size() > 0) {
                    for (int i = 0; i < ulikeList.size(); i++) {
                        String fid = item.getFid();
                        if (ulikeList.get(i) != null) {
                            if (ulikeList.get(i).getFid().equals(fid)) {
                                uLike.add(ulikeList.get(i));
                            }
                        }
                    }
                }
                PraiseListView praiseListView = helper.getView(R.id.praiseListView);
                praiseListView.setDatas(uLike);
                //评论
                List<DynamicUcomment.AllArrayBean> uComment = new ArrayList<>();
                if (uCommentList.size() > 0) {
                    for (int i = 0; i < uCommentList.size(); i++) {
                        String fid = item.getFid();
                        if (uCommentList.get(i) != null) {
                            if (uCommentList.get(i).getFid().equals(fid)) {
                                uComment.add(uCommentList.get(i));
                            }
                        }
                    }
                }
                CommentListView commentListView = helper.getView(R.id.commentList);
                commentListView.setDatas(uComment);
                if (uComment.size() == 0 && uLike.size() == 0) {
                    helper.setGone(R.id.ll_comment, false);
                } else if (uComment.size() > 0 && uLike.size() > 0) {
                    helper.setGone(R.id.ll_comment, true);
                    helper.setGone(R.id.praiseListView, true);
                    helper.setGone(R.id.lin_dig, true);
                    helper.setGone(R.id.commentList, true);
                } else {
                    helper.setGone(R.id.ll_comment, true);
                    helper.setGone(R.id.lin_dig, false);
                    if (uLike.size() == 0) {
                        helper.setGone(R.id.praiseListView, false);
                    } else {
                        helper.setGone(R.id.praiseListView, true);
                    }
                    if (uComment.size() == 0) {
                        helper.setGone(R.id.commentList, false);
                    } else {
                        helper.setGone(R.id.commentList, true);
                    }
                }
                //删除隐藏
                if (UserManager.getUser() == null || UserManager.getUser().getCard() == null){
                    Toast.makeText(mContext,"User().getCard()为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (item.getCard().equals(com.hjq.demo.session.UserManager.getUser().getCard())) {
                    helper.setGone(R.id.iv_delete, true);
                } else {
                    helper.setGone(R.id.iv_delete, false);
                }
                //点赞隐藏
                boolean isShowLike = false;
                if (ulikeList.size() > 0) {
                    for (int i = 0; i < ulikeList.size(); i++) {
                        String fid = item.getFid();
                        if (ulikeList.get(i) != null) {
                            if (ulikeList.get(i).getFid().equals(fid) && ulikeList.get(i).getCard().equals(UserManager.getUser().getCard())) {
                                isShowLike = true;
                            }
                        }
                    }
                }
                if (isShowLike) {
                    helper.setGone(R.id.tv_like, false);
                } else {
                    helper.setGone(R.id.tv_like, true);
                }
                helper.addOnClickListener(R.id.iv_share);
                helper.addOnClickListener(R.id.tv_like);
                helper.addOnClickListener(R.id.iv_comment);
                helper.addOnClickListener(R.id.iv_delete);
                break;
        }
    }

    public void setUlike(List<DynamicUlikeBean.AllArrayBean> beanList) {
        ulikeList = beanList;
    }

    public void setUcomment(List<DynamicUcomment.AllArrayBean> beanList) {
        uCommentList = beanList;
    }


}
