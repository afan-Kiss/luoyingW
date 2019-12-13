package com.hjq.demo.mine_chenmo.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.googleauthenticator.DynamicToken;
import com.hjq.demo.googleauthenticator.GoogleAuthenticator;
import com.hjq.demo.session.UserManager;

import butterknife.BindView;

/**
 * @Author 陈末
 * @Time 2019-11-14 22:12
 * @Title 手动输入
 * @desc
 */
public class ManualinputActivity extends MyActivity {

    @BindView(R.id.edittext_account)
    EditText edittext_account;
    @BindView(R.id.edittext_password)
    EditText edittext_password;
    @BindView(R.id.titlebar)
    TitleBar titlebar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manualinput;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        boolean isNumber = true;
        try {
            Integer.parseInt(edittext_account.getText().toString());
        } catch (Exception ex) {
            isNumber = false;
        }
        if (isNumber) {
            toast("请在账号中输入数字英文组合");
            return;
        } else {
            isNumber = true;
        }
        try {
            Integer.parseInt(edittext_password.getText().toString());
        } catch (Exception ex) {
            isNumber = false;
        }
        if (isNumber) {
            toast("请在密钥中输入数字英文组合");
            return;
        }
        if (TextUtils.isEmpty(edittext_account.getText().toString())) {
            toast("请输入账户");
            return;
        }
        if (edittext_account.getText().toString().length() < 6) {
            toast("请最少输入6位数字或英文");
            return;
        }
        if (TextUtils.isEmpty(edittext_password.getText().toString())) {
            toast("请输入密钥");
            return;
        }
        if (edittext_password.getText().toString().length() < 6) {
            toast("请最少输入6位数字或英文");
            return;
        }
        String account = edittext_account.getText().toString();
        String password = edittext_password.getText().toString();
        insertToken(account, password);
        finish();
    }

    /**
     * 添加 令牌 到数据库
     * @param account
     * @param password
     */
    private void insertToken(String account, String password) {

        if(TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
            toast("用户名和密码不能为空");
            return;
        }
        String key = GoogleAuthenticator.genSecret(account, password);
        TokenEntity entity = new TokenEntity();
        entity.code = GoogleAuthenticator.check_code(key, System.currentTimeMillis());//这是啥?
        entity.name = account;
        entity.key = password;
        entity.card = UserManager.getUser().getCard();
        DBHelper.insertToken(entity);
    }

}
