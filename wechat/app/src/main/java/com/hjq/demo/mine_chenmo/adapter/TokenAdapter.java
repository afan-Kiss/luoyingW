package com.hjq.demo.mine_chenmo.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.widget.CircleProgress;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * @Author 陈末
 * @Time 2019-11-14 21:38
 * @Title 令牌列表
 * @desc
 */
public final class TokenAdapter extends MyRecyclerViewAdapter<TokenEntity> {


    public TokenAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.textview_tokenaccount)
        TextView textview_tokenaccount;
        @BindView(R.id.textview_code)
        TextView textview_code;
        @BindView(R.id.circle_progress_bar)
        CircleProgress circle_progress_bar;

        ViewHolder() {
            super(R.layout.item_token);
        }

        @Override
        public void onBindView(int position) {
            textview_tokenaccount.setText(getItem(position).name);
            textview_code.setText(getItem(position).code);
            CountDownTimer timer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                textview_time.setText(millisUntilFinished / 1000 + "");
                    Log.i("token-----", "millisUntilFinished = " + millisUntilFinished);
                    circle_progress_bar.setValue(millisUntilFinished / 1000);
                    Log.i("token-----", "onTick");

                }

                @Override
                public void onFinish() {
                    circle_progress_bar.setValue(0);
                    Log.i("token-----", "onFinish");
//                    String key = GoogleAuthenticator.generateSecretKey();
//                    TokenEntity entity = new TokenEntity();
//                    entity.code = GoogleAuthenticator.check_code(key, System.currentTimeMillis());
//                    entity.name = getItem(position).name;
//                    entity.key = key;
//                    entity.card = UserManager.getUser().getCard();
//                    DBHelper.deleteToken(getItem(position).id);
//                    int num1 = DBHelper.queryToken().size();
//                    Log.i("token-----", "删除了数据以后的数据库长度" + num1);
//                    DBHelper.insertToken(entity);
//                    int num2 = DBHelper.queryToken().size();
//                    Log.i("token-----", "添加了数据以后的数据库长度" + num2);
//
//                    EventArgs<Integer> eventArgs = new EventArgs<>();
//                    eventArgs.setEventType(EventType.Event_Token_Update);
//                    eventArgs.setObject(position);
//                    EventBus.getDefault().post(eventArgs);

                }
            };
            timer.start();
        }

    }
}