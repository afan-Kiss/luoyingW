package com.hjq.demo.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.base.BaseLazyFragment;
import com.hjq.base.BuildConfig;
import com.hjq.demo.R;
import com.hjq.demo.appsys.EventArgs;
import com.hjq.demo.model.ResponseBody;
import com.hjq.demo.other.EventBusManager;
import com.hjq.demo.other.StatusManager;
import com.hjq.demo.ui.dialog.MessageDialog;
import com.hjq.demo.util.RxEncryptTool;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目中 Fragment 懒加载基类
 */
public abstract class MyLazyFragment<A extends MyActivity>
        extends BaseLazyFragment<A> implements OnTitleBarListener {

    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;
    /**
     * ButterKnife 注解
     */
    private Unbinder mButterKnife;

    protected EventBus eventBus;

    /**
     * 获取标题栏 id
     */
    protected int getTitleId() {
        return 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mButterKnife = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    protected void initFragment() {
        if (getTitleId() > 0) {
            // 勤快模式
            View view = findViewById(getTitleId());
            if (view instanceof TitleBar) {
                mTitleBar = (TitleBar) view;
            }
        } else if (getTitleId() == 0 && getView() instanceof ViewGroup) {
            // 懒人模式
            mTitleBar = MyActivity.findTitleBar((ViewGroup) getView());
        }
        if (mTitleBar != null) {
            mTitleBar.setOnTitleBarListener(this);
        }

        initImmersion();
        super.initFragment();
        EventBusManager.register(this);
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersion() {

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            statusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleId() > 0) {
                ImmersionBar.setTitleBar(this, findViewById(getTitleId()));
            } else if (mTitleBar != null) {
                ImmersionBar.setTitleBar(this, mTitleBar);
            }
        }
    }

    /**
     * 是否在Fragment使用沉浸式
     */
    public boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    protected ImmersionBar getStatusBarConfig() {
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式
     */
    private ImmersionBar statusBarConfig() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(statusBarDarkFont())
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true);
        return mImmersionBar;
    }

    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isStatusBarEnabled() && isLazyLoad()) {
            // 重新初始化状态栏
            statusBarConfig().init();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mButterKnife != null) {
            mButterKnife.unbind();
        }
        EventBusManager.unregister(this);
    }

    /**
     * 设置标题栏的标题
     */
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    public void setTitle(CharSequence title) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        } else {
            // 如果没有标题栏对象就直接设置给绑定的 Activity
            getAttachActivity().setTitle(title);
        }
    }

    @Nullable
    public TitleBar getTitleBar() {
        if (getTitleId() > 0 && findViewById(getTitleId()) instanceof TitleBar) {
            return findViewById(getTitleId());
        }
        return null;
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * 打印日志
     */
    public void log(Object object) {
        if (BuildConfig.DEBUG) {
            Log.v(getClass().getSimpleName(), object != null ? object.toString() : "null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengClient.onResume(this);
    }

    @Override
    public void onPause() {
        UmengClient.onPause(this);
        super.onPause();
    }

    /**
     * {@link OnTitleBarListener}
     */

    /**
     * TitleBar 左边的View被点击了
     */
    @Override
    public void onLeftClick(View v) {
    }

    /**
     * TitleBar 中间的View被点击了
     */
    @Override
    public void onTitleClick(View v) {
    }

    /**
     * TitleBar 右边的View被点击了
     */
    @Override
    public void onRightClick(View v) {
    }

    private final StatusManager mStatusManager = new StatusManager();

    /**
     * 显示加载中
     */
    public void showLoading() {
        mStatusManager.showLoading(getAttachActivity());
    }

    public void showLoading(@StringRes int id) {
        mStatusManager.showLoading(getAttachActivity(), getString(id));
    }

    public void showLoading(CharSequence text) {
        mStatusManager.showLoading(getAttachActivity(), text);
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        mStatusManager.showComplete();
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        mStatusManager.showEmpty(getView());
    }

    /**
     * 显示错误提示
     */
    public void showError() {
        mStatusManager.showError(getView());
    }

    /**
     * 显示自定义提示
     */
    public void showLayout(@DrawableRes int drawableId, @StringRes int stringId) {
        mStatusManager.showLayout(getView(), drawableId, stringId);
    }

    public void showLayout(Drawable drawable, CharSequence hint) {
        mStatusManager.showLayout(getView(), drawable, hint);
    }

    public Gson gson = new Gson();

    public Map<String, String> map = new HashMap<>();


    public ResponseBody CheckDate(String date) {


        ResponseBody responseBody = gson.fromJson(RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date))), ResponseBody.class);

        return responseBody;
    }

    public String GetDate(String date) {

        return RxEncryptTool.unicodeDecode(new String(RxEncryptTool.base64Decode(date)));
    }

    public void MessageDialog(String msg) {
        new MessageDialog.Builder(getActivity())
                // 标题可以不用填写
                .setTitle("提示")
                // 内容必须要填写
                .setMessage(msg)
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(null)
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EventArgs eventArgs) {
    }

    @Subscribe
    public void onEvent(EventArgs eventArgs) {
        onEventBus(eventArgs);

    }


}