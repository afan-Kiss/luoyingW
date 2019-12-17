package com.hjq.demo.mine_chenmo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.hjq.base.BaseDialog;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mine_chenmo.chatui.util.LogUtil;
import com.hjq.demo.mine_chenmo.chatui.util.PictureFileUtil;
import com.hjq.demo.model.DynamicUpload;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.ui.dialog.AddressDialog;
import com.hjq.demo.util.ApiURLUtils;
import com.hjq.image.ImageLoader;
import com.hjq.widget.layout.SettingBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends MyActivity {
    @BindView(R.id.stb_address)
    SettingBar stb_address;
    @BindView(R.id.stb_set_name)
    SettingBar stb_set_name;
    @BindView(R.id.stb_geqian)
    SettingBar stb_geqian;
    @BindView(R.id.stb_phone)
    SettingBar stb_phone;
    @BindView(R.id.img_head_icon)
    ImageView img_head_icon;

    String mProvince;
    String mCity;
    String mArea;
    String imgurl;

    public static final int REQUEST_CODE_IMAGE = 0000;

    @Override

    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        imgurl = UserManager.getUser().getHead_img();
        ImageLoader.with(UserInfoActivity.this)
                .load(imgurl)
                .into(img_head_icon);
        stb_set_name.setRightText(UserManager.getUser().getNickname());
        stb_phone.setRightText(UserManager.getUser().getPhone_number());
        stb_geqian.setRightText(UserManager.getUser().getSignature());
        stb_address.setRightText(mProvince + " " + mCity + " " + mArea);
        mProvince = UserManager.getUser().getRegion_country();
        mCity = UserManager.getUser().getRegion_province();
        mArea = UserManager.getUser().getRegion_city();
        stb_address.setRightText(mProvince + mCity + mArea);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Updatemember();
    }

    @OnClick({R.id.stb_set_name, R.id.stb_geqian, R.id.stb_address, R.id.img_head_icon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stb_set_name:
                Intent intent = new Intent();
                intent.setClass(this, SetTxtActivity.class);
                intent.putExtra("txt", stb_set_name.getRightText());
                intent.putExtra("type", "昵称");
                startActivityForResult(intent, 1001);
                break;
            case R.id.stb_geqian:
                Intent intent2 = new Intent();
                intent2.setClass(this, SetTxtActivity.class);
                intent2.putExtra("txt", stb_geqian.getRightText());
                intent2.putExtra("type", "个签");
                startActivityForResult(intent2, 1001);
                break;
            case R.id.img_head_icon:
                PictureFileUtil.openGalleryPic(UserInfoActivity.this, NewChatActivity.REQUEST_CODE_IMAGE);
                break;
            case R.id.stb_address:
                new AddressDialog.Builder(this)
                        .setTitle(getString(R.string.address_title))
                        .setListener(new AddressDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                mProvince = province;
                                mCity = city;
                                mArea = area;
                                stb_address.setRightText(mProvince + mCity + mArea);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }


    /**
     * 修改用户信息
     */
    private void Updatemember() {
        showLoading();
        map.clear();
        map.put("Method", "Updatemember");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Username", UserManager.getUser().getUsername());
        map.put("Nickname", stb_set_name.getRightText().toString());//昵称
        map.put("Head_img", imgurl);//头像
        map.put("Gender", "2");//性别
        map.put("Region_country", mProvince);
        map.put("Region_province", mCity);
        map.put("Region_city", mArea);
        map.put("Signature", stb_geqian.getRightText().toString());//个性签名

        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        if (CheckDate(response.body()).getState() != 1) {
                            MessageDialog(CheckDate(response.body()).getMsg());
                            return;
                        }
                        UserManager.getUser().setHead_img(imgurl);
                        UserManager.getUser().setNickname(stb_set_name.getRightText().toString());
                        UserManager.getUser().setSignature(stb_geqian.getRightText().toString());
                        UserManager.getUser().setRegion_country(mProvince);
                        UserManager.getUser().setRegion_province(mCity);
                        UserManager.getUser().setRegion_city(mArea);
                        toast("修改成功");
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


    // 接受搜索界面返回的搜索关键字,用来进行网络请求数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001&&data!=null) {
            String type = data.getStringExtra("type");
            if (type.equals("昵称")) {
                stb_set_name.setRightText(data.getStringExtra("settxt"));
            } else if (type.equals("个签")) {
                stb_geqian.setRightText(data.getStringExtra("settxt"));
            }
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        LogUtil.d("获取图片路径成功:" + media.getPath());
                        uploadpicture(media.getPath());
                    }
                    break;
            }
        }
    }


    /**
     * 上传图片
     *
     * @param
     */
    public void uploadpicture(String path) {
        Map<String, String> map = new HashMap<>();
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
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        if (CheckDate(response.body()).getState() != 1) {
                            return;
                        }
                        DynamicUpload data = gson.fromJson(GetDate(response.body()), DynamicUpload.class);
                        imgurl = data.getImg();
                        ImageLoader.with(UserInfoActivity.this)
                                .load(imgurl)
                                .into(img_head_icon);
                    }
                });
    }

}
