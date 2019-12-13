package com.hjq.demo.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.BarHide;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.other.AppConfig;
import com.hjq.demo.util.PreKeys;
import com.hjq.demo.util.PrefUtils;
import com.hjq.demo.util.RxSPTool;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import butterknife.BindView;

/**
 * desc   : 闪屏界面
 */
public final class SplashActivity extends MyActivity
        implements OnPermission, Animation.AnimationListener {

    private static final int ANIM_TIME = 1000;

    @BindView(R.id.iv_splash_bg)
    View mImageView;
    @BindView(R.id.iv_splash_icon)
    View mIconView;
    @BindView(R.id.iv_splash_name)
    View mNameView;

    @BindView(R.id.tv_splash_debug)
    View mDebugView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        // 初始化动画
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(ANIM_TIME * 2);
        aa.setAnimationListener(this);
        mImageView.startAnimation(aa);

//        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        sa.setDuration(ANIM_TIME);
//        mIconView.startAnimation(sa);
//
//        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ra.setDuration(ANIM_TIME);
//        mNameView.startAnimation(ra);

        // 设置状态栏和导航栏参数
        getStatusBarConfig()
                // 有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fullScreen(true)
                // 隐藏状态栏
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                // 透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentNavigationBar()
                .init();
    }

    @Override
    protected void initData() {
        if (AppConfig.isDebug()) {
            mDebugView.setVisibility(View.VISIBLE);
        } else {
            mDebugView.setVisibility(View.INVISIBLE);
        }
    }

    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.RECORD_AUDIO,
                        Permission.READ_PHONE_STATE)
                .request(this);
    }

    /**
     * {@link OnPermission}
     */

    @Override
    public void hasPermission(List<String> granted, boolean isAll) {
//        startActivityFinish(LoginActivity.class);
        if (RxSPTool.getContent(this, "isStart").trim().equals("")) {
            startActivity(WelcomeActivity.class);
        } else {
            if (!TextUtils.isEmpty(PrefUtils.getString(this, PreKeys.USERNAME,""))){
                startActivityFinish(HomeActivity.class);
            }else {
                startActivityFinish(LoginActivity.class);
            }

        }

        overridePendingTransition(0, 0);
    }

    @Override
    public void noPermission(List<String> denied, boolean quick) {
        if (quick) {
            toast(R.string.common_permission_fail);
            XXPermissions.gotoPermissionSettings(SplashActivity.this, true);
        } else {
            toast(R.string.common_permission_hint);
            postDelayed(this::requestPermission, 1000);
        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (XXPermissions.isHasPermission(SplashActivity.this, Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.RECORD_AUDIO, Permission.CAMERA
        )) {
            hasPermission(null, true);
        } else {
            requestPermission();
        }
    }

    /**
     * {@link Animation.AnimationListener}
     */

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        requestPermission();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}