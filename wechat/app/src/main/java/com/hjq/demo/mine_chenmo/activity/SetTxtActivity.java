package com.hjq.demo.mine_chenmo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.widget.layout.SettingBar;

import butterknife.BindView;

public class SetTxtActivity extends MyActivity {
    @BindView(R.id.et_txt)
    EditText et_txt;

    String type = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_txt;
    }

    @Override
    protected void initView() {
        et_txt.setText(getIntent().getStringExtra("txt"));
        type=getIntent().getStringExtra("type");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Intent intent = new Intent();
        intent.putExtra("settxt", et_txt.getText().toString());
        intent.putExtra("type",type);
        setResult(1001, intent);
        Log.i("chuishen", "onRightClick: "+type);
        finish();
    }


}
