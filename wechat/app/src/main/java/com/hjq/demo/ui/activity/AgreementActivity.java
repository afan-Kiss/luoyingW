package com.hjq.demo.ui.activity;

import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;

import butterknife.BindView;

import static com.hjq.demo.api.API.AGREEMENT;

/**
 * @author Cinpe
 */


public final class AgreementActivity extends MyActivity {

    @BindView(R.id.web_agreement)
    WebView agreement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initView() {
        initWebView();
        agreement.loadUrl(AGREEMENT);
//        agreement.loadDataWithBaseURL(AGREEMENT,"","text/html","utf-8",null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        agreement.onResume();
    }

    @Override
    protected void onPause() {
        agreement.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        agreement.destroy();
        super.onDestroy();
    }

    /**
     * WebView初始化
     */
    private void initWebView(){
        WebSettings webSettings=agreement.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(250);//设置文本的缩放倍数，默认为 100

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        webSettings.setDefaultFontSize(24);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        // 5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //其他操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setGeolocationEnabled(true);//允许网页执行定位操作
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");//设置User-Agent


        //不允许访问本地文件（不影响assets和resources资源的加载）
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        agreement.setWebChromeClient(new WebChromeClient());
        agreement.setWebViewClient(new WebViewClient());
    }

}