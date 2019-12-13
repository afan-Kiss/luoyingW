package com.hjq.demo.guowenbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;

import butterknife.BindView;

public class WebViewActivity extends MyActivity {
    @BindView(R.id.WebView)
    WebView WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        WebView.loadUrl("http://www.hao123.com");
        WebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
//返回true
                return true;
            }
        });
    }


    @Override
    protected void initData() {

    }
}
