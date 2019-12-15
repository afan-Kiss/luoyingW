package com.hjq.demo.FriendRing;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.demo.FriendRing.activity.DynamicReleaseActivity;
import com.hjq.demo.FriendRing.adapter.DynamicAdapter;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.model.DynamicBean;
import com.hjq.demo.model.DynamicUcomment;
import com.hjq.demo.model.DynamicUlikeBean;
import com.hjq.demo.model.DynamicUpload;
import com.hjq.demo.session.Tezheng;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.PhotoActivity;
import com.hjq.demo.ui.dialog.MenuDialog;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.TimeUtils;
import com.hjq.image.ImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.kevin.floatingeditor.EditorCallback;
import cn.kevin.floatingeditor.EditorHolder;
import cn.kevin.floatingeditor.FloatEditorActivity;

/**
 * @author GF
 * @des 朋友圈Fagment
 * @date 2019/11/14
 */
public class FriendRingFragment extends MyLazyFragment {

    private static final String TAG = "FriendRingFragment";

    @BindView(R.id.rv_dynamic)
    RecyclerView mRvDynamic;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout mSrlRefresh;
    @BindView(R.id.bodyLayout)
    RelativeLayout bodyLayout;
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    //    @BindView(R.id.scrollview)
//    NestedScrollView scrollview;
    //Adapter
    DynamicAdapter mDynamicAdapter;
    DynamicBean.AllArrayBean mDynamicBean;
    //
    int page = 1;
    private String mUserName;
    private List<DynamicBean.AllArrayBean> mDynamicBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    public FriendRingFragment(String userName) {
        this.mUserName = userName;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friendring;
    }

    @Override
    protected void initView() {
        mDynamicBean = new DynamicBean.AllArrayBean();
        mDynamicBean.setItemType(1);
        mDynamicBean.setUser_name(UserManager.getUser().getNickname());
        mDynamicBean.setUser_img(UserManager.getUser().getHead_img());
        mDynamicBean.setCircle_img(UserManager.getUser().getCircle_img());
        mDynamicBeanList.add(mDynamicBean);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        initScrollVIew();
        initRecycleView(linearLayoutManager);
    }

    private void initScrollVIew() {
    }


    private void initRecycleView(LinearLayoutManager linearLayoutManager) {
        mRvDynamic.setLayoutManager(linearLayoutManager);
        mRvDynamic.addItemDecoration(new DivItemDecoration(2, true));
        mRvDynamic.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if(dy > 100){
//                    titlebar.setVisibility(View.VISIBLE);
//                }else {
//                    titlebar.setVisibility(View.GONE);
//                }


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ImageLoader.startslide(getActivity());
                } else {
                    ImageLoader.stopslide(getActivity());
                }


                Log.i(TAG, "onScrollStateChanged: " + recyclerView.getY());


                int pos = linearLayoutManager.findFirstVisibleItemPosition();
                int lastPos = linearLayoutManager.findLastVisibleItemPosition();
                View nowView = linearLayoutManager.findViewByPosition(pos);
                Log.i(TAG, "onScrollStateChanged: " + nowView.getY());

            }
        });
        mDynamicAdapter = new DynamicAdapter(mDynamicBeanList, getActivity());
        mRvDynamic.setAdapter(mDynamicAdapter);
        mSrlRefresh.setOnRefreshListener(refreshLayout -> {
            mSrlRefresh.setEnableRefresh(true);
            dynamicList(true, 1, mUserName);
        });
        mSrlRefresh.setOnLoadMoreListener(refreshLayout -> {
            page++;
            mSrlRefresh.setEnableLoadMore(true);
            dynamicList(false, page, mUserName);
        });
        mDynamicAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DynamicBean.AllArrayBean bean = (DynamicBean.AllArrayBean) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_dynamic_add:
                    Intent intent = new Intent(getActivity(), DynamicReleaseActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.head_layout:
                    replace();
                    break;
                case R.id.iv_share:
                    new MessageDialog.Builder(getActivity())
                            .setMessage("确定要转发此朋友圈吗")
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    fcircle(bean.getFid());
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {

                                }
                            }).show();
                    break;
                case R.id.tv_like:
                    Rlike(bean.getFid());
                    break;
                case R.id.iv_comment:
                    FloatEditorActivity.openEditor(getActivity(), editorCallback2,
                            new EditorHolder(R.layout.layout_editview,
                                    0, R.id.sendIv, R.id.circleEt, bean.getFid()));
                    break;
                case R.id.iv_delete:
                    deleteDynamic(bean.getFid(), position);
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    protected void initData() {
        dynamicList(true, 1, mUserName);
    }


    /**
     * 动态数据
     *
     * @param isRefresh
     * @param page
     */
    public void dynamicList(boolean isRefresh, int page, String userName) {
        Log.d("page", page + "");
        map.clear();
        map.put("Method", "Circle");
//        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Username", userName);
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Page", String.valueOf(page));
        map.put("PageSize", "10");
        map.put("Otime", TimeUtils.getCurTimeMills());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        Log.d("列表", GetDate(response.body()));
                        DynamicBean dynamicBean = gson.fromJson(GetDate(response.body()), DynamicBean.class);
                        Tezheng.Features_news = dynamicBean.getFeatures_circle();
                        if (isRefresh) {
                            mDynamicBeanList.clear();
                            mDynamicBeanList.add(mDynamicBean);
                            mDynamicBeanList.addAll(dynamicBean.getAll_array());
                        } else {
                            mDynamicBeanList.addAll(dynamicBean.getAll_array());
                        }
                        Ulike();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                        if (isRefresh) {
                            mSrlRefresh.finishRefresh();
                        } else {
                            mSrlRefresh.finishLoadMore();
                        }
                    }
                });
    }


    /**
     * 更换背景图
     */
    public void replace() {
        // 底部选择框
        new MenuDialog.Builder(getActivity())
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setList("更换封面图")
                .setListener(new MenuDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, Object o) {
                        PhotoActivity.start(getAttachActivity(), 1, new PhotoActivity.OnPhotoSelectListener() {
                            @Override
                            public void onSelect(List<String> data) {
                                if (data != null && data.size() > 0) {
                                    try {
                                        uploadpicture(data.get(0));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {

                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
                .show();
    }


    /**
     * 更换朋友圈 背景图
     *
     * @param imagePath
     */
    public void dynamicReplace(String imagePath) {
        map.clear();
        map.put("Method", "Imgcircle");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Imgurl", imagePath);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        Log.d("图片地址:", imagePath);
                        mDynamicBeanList.get(0).setCircle_img(imagePath);
                        UserManager.getUser().setCircle_img(imagePath);
                        mDynamicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }


    /**
     * 删除朋友圈
     *
     * @param fid
     * @param position
     */
    public void deleteDynamic(String fid, int position) {
        map.clear();
        showLoading();
        map.put("Method", "Dcircle");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Fid", fid);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        mDynamicBeanList.remove(position);
                        mDynamicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    /**
     * 评论列表
     */
    public void Ucomment() {
        map.clear();
        map.put("Method", "Ucomment");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        DynamicUcomment dynamicBean = gson.fromJson(GetDate(response.body()), DynamicUcomment.class);
                        Log.d("评论", GetDate(response.body()));
                        Tezheng.Comment = dynamicBean.getFeatures_comment();
                        mDynamicAdapter.setUcomment(dynamicBean.getAll_array());
                        //mDynamicAdapter.setNewData(mDynamicBeanList);
                        mDynamicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }


    /**
     * 点赞列表
     */
    public void Ulike() {
        map.clear();
        map.put("Method", "Ulike");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        DynamicUlikeBean dynamicBean = gson.fromJson(GetDate(response.body()), DynamicUlikeBean.class);
                        Tezheng.Flike = dynamicBean.getFeatures_like();
                        mDynamicAdapter.setUlike(dynamicBean.getAll_array());
                        Log.d("点赞", GetDate(response.body()));
                        Ucomment();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }


    /**
     * 点赞
     *
     * @param fid
     */
    private void Rlike(String fid) {
        map.clear();
        map.put("Method", "Rlike");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Fid", fid);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        GetDate(response.body());
                        dynamicList(true, 1, mUserName);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }


    /**
     * 转发成功
     *
     * @param fid
     */
    private void fcircle(String fid) {
        map.clear();
        map.put("Method", "Fcircle");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Fid", fid);
        map.put("Jurisdiction", "0");
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        GetDate(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    /**
     * 评论
     *
     * @param content
     * @param fid
     */
    public void comment(String content, String fid) {
        map.clear();
        map.put("Method", "Rcomment");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Fid", fid);
        map.put("textval", content);
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        GetDate(response.body());
                        dynamicList(true, 1, mUserName);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param
     */
    public void uploadpicture(String path) throws IOException {
        map.clear();
        map.put("Method", "Upload");
        map.put("Username", UserManager.getUser().getPhone_number());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        fileList.add(file);
        OkGo.<String>post(API.BASE_API)
                .addFileParams("File", fileList)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        DynamicUpload data = gson.fromJson(GetDate(response.body()), DynamicUpload.class);
                        Log.d("上传", data.getImg());
                        dynamicReplace(data.getImg());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast("服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }

    EditorCallback editorCallback2 = new EditorCallback() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onSubmit(String content, String fid) {
            comment(content, fid);
        }

        @Override
        public void onAttached(final ViewGroup rootView) {

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 200) {
                dynamicList(true, 1, mUserName);
            }
        }
    }

    @Override
    protected void onRestart() {
        dynamicList(false, 1, mUserName);
        super.onRestart();
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        startActivity(DynamicReleaseActivity.class);
    }

    public static FriendRingFragment newInstance(String userName) {
        return new FriendRingFragment(userName);
    }
}
