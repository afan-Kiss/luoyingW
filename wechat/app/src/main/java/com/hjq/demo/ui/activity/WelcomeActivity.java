package com.hjq.demo.ui.activity;

import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.util.RxSPTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class WelcomeActivity extends MyActivity {
    @BindView(R.id.viewpager_content)
    ViewPager viewpager_content;
    int page=0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        List<Integer> integers = new ArrayList<>();
        integers.add(R.drawable.splash1);
        integers.add(R.drawable.splash2);
        integers.add(R.drawable.splash3);
        viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);
        PagerAdapter adapter = new ViewAdapter(integers);
        viewpager_content.setAdapter(adapter);
        viewpager_content.setPageTransformer(true, new DepthPageTransformer());
        viewpager_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=position;
//                if (position == 2) {
////                    startActivityFinish(LoginActivity.class);
//                    startActivity(LoginActivity.class);
//                    overridePendingTransition(0, 0);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        RxSPTool.putContent(this, "isStart", "ok");
    }


    @OnClick({R.id.btn_tiaoguo, R.id.btn_tiyan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tiaoguo:
                startActivityFinish(LoginActivity.class);
                break;
            case R.id.btn_tiyan:
             if(page==2){
                 startActivityFinish(LoginActivity.class);
             }
                break;
            default:
                break;
        }
    }



    class ViewAdapter extends PagerAdapter {
        private List<Integer> datas;
        private View view;

        public ViewAdapter(List<Integer> list) {
            datas = list;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            view = View.inflate(WelcomeActivity.this, R.layout.item_splash, null);
            ImageView imageView_content = view.findViewById(R.id.imageview_content);
//            ImageLoader.with(SplashActivity.this).load(datas.get(position)).circle(10).into(imageView_content);
//            imageView_content.setImageDrawable(datas.get(position));
            imageView_content.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView_content.setImageResource(datas.get(position));
//            View view=datas.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(view);
        }
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
