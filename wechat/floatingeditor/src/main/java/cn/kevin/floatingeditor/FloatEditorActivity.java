package cn.kevin.floatingeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建日期：2017/9/13.
 *
 * @author kevin
 */

public class FloatEditorActivity extends Activity implements View.OnClickListener {
    public static final String KEY_EDITOR_HOLDER = "editor_holder";
    public static final String KEY_EDITOR_CHECKER = "editor_checker";
    private View cancel;
    private View submit;
    private EditText etContent;
    private static EditorCallback mEditorCallback;
    private EditorHolder holder;
    private InputCheckRule checkRule;
    private boolean isClicked;
    public static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        holder = (EditorHolder) getIntent().getSerializableExtra(KEY_EDITOR_HOLDER);
        checkRule = (InputCheckRule) getIntent().getSerializableExtra(KEY_EDITOR_CHECKER);
        if (holder == null) {
            throw new RuntimeException("EditorHolder params not found!");
        }
        setContentView(holder.layoutResId);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mEditorCallback.onAttached((ViewGroup) getWindow().getDecorView());

        initView();
        setEvent();
    }

    private void initView() {
        cancel = findViewById(holder.cancelViewId);
        submit = findViewById(holder.submitViewId);
        etContent = (EditText) findViewById(holder.editTextId);
    }

    private void setEvent() {
        if (cancel != null)
            cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder) {
        openEditor(context, editorCallback, holder, null);
        mContext = context;
    }

    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule) {
        mContext = context;
        Intent intent = new Intent(context, FloatEditorActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(KEY_EDITOR_HOLDER, holder);
        intent.putExtra(KEY_EDITOR_CHECKER, checkRule);
        mEditorCallback = editorCallback;
        context.startActivity(intent);
    }

    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule) {
        openEditor(context, editorCallback, DefaultEditorHolder.createDefaultHolder(), checkRule);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == holder.cancelViewId) {
            mEditorCallback.onCancel();
        } else if (id == holder.submitViewId) {
            mEditorCallback.onSubmit(etContent.getText().toString(), holder.fid);
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(submit.getWindowToken(), 0);
        }
        isClicked = true;
        finish();
    }

    private boolean illegal() {
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < checkRule.minLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_min_warn, checkRule.minLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (content.length() > checkRule.maxLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_max_warn, checkRule.maxLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!TextUtils.isEmpty(checkRule.regxRule)) {
            Pattern pattern = Pattern.compile(checkRule.regxRule);
            Matcher matcher = pattern.matcher(content);
            if (!matcher.matches()) {
                Toast.makeText(this, getString(checkRule.regxWarn), Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isClicked) {
            mEditorCallback.onCancel();
        }
        mEditorCallback = null;
    }
}
