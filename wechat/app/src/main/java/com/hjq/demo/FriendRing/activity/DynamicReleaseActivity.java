package com.hjq.demo.FriendRing.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.FriendRing.adapter.DynamicReleaseAdapter;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.model.DynamicImageBean;
import com.hjq.demo.model.DynamicUpload;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.activity.ImageActivity;
import com.hjq.demo.ui.activity.PhotoActivity;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author GF
 * @des 动态发布
 * @date 2019/11/15
 */
public class DynamicReleaseActivity extends MyActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.tv_dynamic_content)
    EditText mTvDynamicContent;
    @BindView(R.id.rv_dynamic_image)
    RecyclerView mRvDynamicImage;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.rl_dynamic_pression)
    RelativeLayout mRlDynamicPression;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    private DynamicReleaseAdapter mAdapter;
    private List<DynamicImageBean> beanList = new ArrayList<>();
    private List<String> imagePath = new ArrayList<>();
    private String ids;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_release;
    }

    @Override
    protected void initView() {
        initTitle();
        initRecycleView();
    }

    private void initTitle() {
        mTitle.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                if (TextUtils.isEmpty(mTvDynamicContent.getText().toString().trim()) && beanList.size() == 1) {
                    ToastUtils.show("您还没有输入内容,不能进行发布!");
                } else {
                    if (beanList.size() > 1) {
                        imagePath.clear();
                        if (beanList.size() == 9) {
                            if (TextUtils.isEmpty(beanList.get(8).getPath())) {
                                beanList.remove(8);

                            }
                        } else {
                            beanList.remove(beanList.size() - 1);
                        }
                        for (int i = 0; i < beanList.size(); i++) {
                            if (!TextUtils.isEmpty(beanList.get(i).getPath())) {
                                Log.d("Image:", i + "-" + beanList.size());
                                try {
                                    uploadpicture(beanList.get(i).getPath(), i == (beanList.size() - 1));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        Relcircle();
                    }
                }

            }
        });
        mRlDynamicPression.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectContactActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    private void initRecycleView() {
        DynamicImageBean bean = new DynamicImageBean();
        bean.setItemType(1);
        beanList.add(bean);
        mRvDynamicImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new DynamicReleaseAdapter(beanList, this);
        mRvDynamicImage.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_image_add:
                    PhotoActivity.start(this, 9, new PhotoActivity.OnPhotoSelectListener() {
                        @Override
                        public void onSelect(List<String> data) {
                            if (data != null && data.size() > 0) {
                                beanList.remove(beanList.size() - 1);
                                for (int i = 0; i < data.size(); i++) {
                                    DynamicImageBean bean = new DynamicImageBean();
                                    bean.setPath(data.get(i));
                                    bean.setItemType(2);
                                    beanList.add(bean);
                                }
                                if (beanList.size() < 9) {
                                    DynamicImageBean bean = new DynamicImageBean();
                                    bean.setItemType(1);
                                    beanList.add(bean);
                                }
                                mAdapter.setNewData(beanList);
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    break;
                case R.id.iv_image:
                    ArrayList<String> arrayList = new ArrayList();
                    for (int i = 0; i < beanList.size() - 1; i++) {
                        arrayList.add(beanList.get(i).getPath());
                    }
                    ImageActivity.start(this, arrayList, position);
                    break;
                default:
                    break;
            }
        });
    }


    public void Relcircle() {
        showLoading();
        map.clear();
        map.put("Method", "Relcircle");
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        if (!TextUtils.isEmpty(mTvDynamicContent.getText().toString().trim())) {
            map.put("Textval", RxEncryptTool.base64Encode2(mTvDynamicContent.getText().toString().trim()));
        }
        if (imagePath.size() > 0) {
            StringBuffer path = new StringBuffer();
            for (int i = 0; i < imagePath.size(); i++) {
                path.append(imagePath.get(i) + "|");
            }
            map.put("Imgarray", path.toString().substring(0, path.toString().length() - 1));
            Log.d("图片:", path.toString());
        }
        if (TextUtils.isEmpty(ids)) {
            map.put("Jurisdiction", "0");
        } else {
            map.put("Jurisdiction", ids);
        }
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        Log.d("发布", GetDate(response.body()));
                        ToastUtils.show("发布了成功!");
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast(R.string.error_network);
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
    public void uploadpicture(String path, boolean isRealse) throws IOException {
        map.clear();
        map.put("Method", "Upload");
        map.put("Username", UserManager.getUser().getUsername());
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
                        Log.d("上传" + isRealse, GetDate(response.body()));
                        DynamicUpload data = gson.fromJson(GetDate(response.body()), DynamicUpload.class);
                        imagePath.add(data.getImg());
                        if (imagePath.size() == beanList.size()) {
                            Relcircle();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast(R.string.error_network);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showComplete();
                    }
                });
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 200) {
                if (data != null) {
                    String ids = data.getStringExtra("ids");
                    mTvContent.setText("谁可以看:部分人可见");
                    this.ids = ids;
                }
            }
        }
    }

}
