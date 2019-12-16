package com.hjq.demo.mine_chenmo.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.TokenEntity;
import com.hjq.demo.googleauthenticator.DynamicToken;
import com.hjq.demo.widget.CircleProgress;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @Author 陈末
 * @Time 2019-11-14 21:38
 * @Title 令牌列表
 * @desc
 */
public final class testTokenAdapter extends MyRecyclerViewAdapter<Map<Byte, String>> {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * 计时器
     */
    private CountDownTimer timer;

    boolean isRun;

    /**
     * 上一次请求时的系统时间
     */
    Long systemTime;

    /**
     * 当前读秒
     */
    private int count;

    /**
     * 刷新间隔60秒
     */
    private int invertal = 60;


    public testTokenAdapter(Context context) {
        super(context);

        //初始化计时器
        initTimer();

        //初始化数据源
        flashData();

        //计时器开始跑.
        timer.start();


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
            textview_tokenaccount.setText(getItem(position).get((byte) 0));
            textview_code.setText(getItem(position).get((byte) 1));
            circle_progress_bar.setValue(count);


        }

    }


    /**
     * 刷新 数据源
     */
    public void flashData() {

        Log.i(TAG, "*********进入数据刷新");

        //刷新数据源???
        List<TokenEntity> tokenListEntity = DBHelper.queryToken();

        if (tokenListEntity == null) {
            Log.i(TAG, "DBHelper.queryToken()是空的");
        } else if (tokenListEntity.size() == 0) {
            Log.i(TAG, "DBHelper.queryToken()的长度是0");

        }

        if (tokenListEntity != null ) {

            //统一当前系统时间
            systemTime = new Date().getTime();

            if (this.getData() == null || tokenListEntity.size() != this.getData().size()) {
                //仅当数据 增,删时 重置链表.

                List<Map<Byte, String>> tempList = new ArrayList<>();

                for (TokenEntity entity :
                        tokenListEntity) {


                    //获取令牌
                    String Auth = key2Auth(entity.getKey(), systemTime);


                    //打印下是不是取错了
                    Log.i(TAG, "getCard" + entity.getCard());
                    Log.i(TAG, "getCode" + entity.getCode());
                    Log.i(TAG, "getName" + entity.getName());
                    Log.i(TAG, "getId" + entity.getId());
                    Log.i(TAG, "getKey" + entity.getKey());

                    Map<Byte, String> map = new HashMap();
                    map.put((byte) 0, entity.getName());
                    map.put((byte) 1, Auth);




                    tempList.add(map);
                }
                //刷新数据源
                this.setData(tempList);
            } else {
                //当数据 改动时 仅重新赋值.


                for (int i = 0; i < this.getData().size(); i++) {
                    this.getData().get(i).put((byte) 0, tokenListEntity.get(i).getName());

                    String Auth = key2Auth(tokenListEntity.get(i).getKey(), systemTime);
//                    String Auth = key2Auth("AYYE2WLPMW76JR2T", systemTime);

                    this.getData().get(i).put((byte) 1, Auth);

                }

                //刷新
                this.notifyDataSetChanged();
            }

        } else {
            Log.i(TAG, "没有数据源");
        }

    }


    /**
     * Key --> 令牌
     */
    private String key2Auth(String key, Long systemTime) {

        String Auth = null;

        try {
            //打印传入参数
            Log.i(TAG, "KEY =" + key);
            Log.i(TAG, "Time=" + systemTime);
            Auth = DynamicToken.getInstance().getDynamicCode(key, systemTime);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return Auth;
    }


    /**
     * 设置当前剩余读秒
     */
    private void setCount() {


        Long currentTime = System.currentTimeMillis();

//        //这可以注释掉
//        if((systemTime/60000)==(currentTime/60000)){
//            //是在当前60内,
//
//        }else {
//            //不在当前60内
//
//        }

        int tempCount = (60 - (int) (currentTime % 60000 / 1000));

        if (count != tempCount) {
            count = tempCount;
            Log.i(TAG, "**********打印倒计时:" + count);
            this.flashData();
        }

    }


    /**
     * 当activity初始化时 onBindView
     */
    public void onBindView(Context context) {
        if (context.equals(this.mRootContext)) {
            Log.i(TAG, "*********上下文相等");
        } else {
            Log.i(TAG, "*********上下文重置");
            this.mRootContext = context;
        }
    }


    /**
     * 当activity被销毁时,停止计时器
     */
    public void onDestory() {
    }

    /**
     * 初始化计时器
     */
    private void initTimer() {
        if (timer == null) {

            //初始化计时器
            timer = new CountDownTimer(Long.MAX_VALUE, 100) {

                @Override
                public void onTick(long l) {

                    //每过1秒,刷新读秒
                    Log.i(TAG, "每过1秒,刷新读秒");
                    setCount();

                }

                @Override
                public void onFinish() {

                    //计时器结束了? 怎么可能
                    Log.i(TAG, "[计时器不可思议的结束了]");
                }
            };
        }
    }
}