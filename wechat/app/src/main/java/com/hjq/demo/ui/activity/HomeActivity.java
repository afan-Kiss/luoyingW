package com.hjq.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.demo.FriendRing.FriendRingFragment;
import com.hjq.demo.R;
import com.hjq.demo.api.API;
import com.hjq.demo.appsys.EventArgs;
import com.hjq.demo.appsys.EventType;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.daerxiansheng.Fragment.MessageFragment;
import com.hjq.demo.daerxiansheng.sql.DBHelper;
import com.hjq.demo.daerxiansheng.sql.FrendsMessageEntity;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;
import com.hjq.demo.guowenbin.Fragment.AddressBookFragment;
import com.hjq.demo.helper.ActivityStackManager;
import com.hjq.demo.helper.DoubleClickHelper;
import com.hjq.demo.mine_chenmo.fragment.MineFragment;
import com.hjq.demo.model.AcceptVideoModel;
import com.hjq.demo.other.KeyboardWatcher;
import com.hjq.demo.rong.RongListener;
import com.hjq.demo.rong.RongVoice;
import com.hjq.demo.session.Tezheng;
import com.hjq.demo.session.UserManager;
import com.hjq.demo.util.ApiURLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.starrtc.demo.api.MyStatus;
import com.starrtc.demo.demo.service.KeepLiveService;
import com.starrtc.demo.demo.voip.VoipAudioRingingActivity;
import com.starrtc.demo.demo.voip.VoipRingingActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import io.rong.callkit.RongCallCustomerHandlerListener;
import io.rong.callkit.RongCallKit;
import io.rong.callkit.RongCallProxy;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

/**
 * desc   : 主页界面
 */
public final class HomeActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener, OnTabSelectListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_home_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab)
    CommonTabLayout tab;


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    int[] mIconUnselectIds = {
            R.drawable.home_selected_message_off, R.drawable.home_address_book_selected_off, R.drawable.home_friends_off, R.drawable.home_my_off};
    // R.mipmap.icon_deposit_green,
    int[] mIconSelectIds = {
            R.drawable.home_selected_message, R.drawable.home_address_book_selected, R.drawable.home_friends_selected, R.drawable.home_my_selected};

    String[] mTitles = {"消息", "通讯录", "朋友圈", "我的"};

    private String Features_information;//消息列表特征码

    Intent service;

    Timer mTimer;

    /**
     * ViewPager 适配器
     */
    private BaseFragmentAdapter<MyLazyFragment> mPagerAdapter;

    private static final String TAG = "HomeActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        KeyboardWatcher.with(this)
                .setListener(this);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Heartbeat();
            }
        }, 1000, 2000);
        service = new Intent(this, KeepLiveService.class);
        service.putExtra("Username", UserManager.getUser().getPhone_number());
        startService(service);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        setTab();
        tab.hideMsg(0);
        new RongVoice(this).initRongRtcConnect(RongVoice.getToken());
        RongCallKit.setCustomerHandlerListener(new RongCallCustomerHandlerListener() {
            @Override
            public List<String> handleActivityResult(int i, int i1, Intent intent) {
                Log.i(TAG, "onCallMissed: ");
                return null;
            }

            @Override
            public void addMember(Context context, ArrayList<String> arrayList) {
                Log.i(TAG, "onCallMissed: ");

            }

            @Override
            public void onRemoteUserInvited(String s, RongCallCommon.CallMediaType callMediaType) {
                Log.i(TAG, "onCallMissed: ");

            }

            @Override
            public void onCallConnected(RongCallSession rongCallSession, SurfaceView surfaceView) {
                Log.i(TAG, "onCallMissed: ");

            }

            @Override
            public void onCallDisconnected(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {

                if (callDisconnectedReason.name().equals("REJECT")){
                    Log.i(TAG, "onCallMissed: ");
                }
                //rongCallSession.getSelfUserId()
                // rongCallSession.getTargetId()
                // rongCallSession.getMediaType().name()  VIDEO
                EventBus.getDefault().post("语音聊天"+rongCallSession.getMediaType().name()+" "+rongCallSession.getTargetId() +" " +rongCallSession.getMediaType().name() + " "+       ((rongCallSession.getEndTime() - rongCallSession.getStartTime())/1000));



            }

            @Override
            public void onCallMissed(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {

                Log.i(TAG, "onCallMissed: ");
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer = null;
        stopService(service);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        MessageFragment mMessageFragment = MessageFragment.newInstance();
        mMessageFragment.setmHandler(mHandler);
        mPagerAdapter.addFragment(mMessageFragment);
        mPagerAdapter.addFragment(AddressBookFragment.newInstance());
        mPagerAdapter.addFragment(FriendRingFragment.newInstance(UserManager.getUser().getPhone_number()));
        mPagerAdapter.addFragment(MineFragment.newInstance());

        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
    }

    /**
     * 填充Tab
     */
    private void setTab() {
        for (int i = 0; i < mTitles.length; i++) {
            final int finalI = i;
            mTabEntities.add(new CustomTabEntity() {

                @Override
                public String getTabTitle() {
                    return mTitles[finalI];
                }

                @Override
                public int getTabSelectedIcon() {
                    return mIconSelectIds[finalI];
                }

                @Override
                public int getTabUnselectedIcon() {
                    return mIconUnselectIds[finalI];
                }

            });
        }
        tab.setTabData(mTabEntities);
        tab.setOnTabSelectListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
        if (mPagerAdapter.getCurrentFragment().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 进行内存优化，销毁掉所有的界面
                    ActivityStackManager.getInstance().finishAllActivities();
                    // 销毁进程（请注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                    // System.exit(0);
                }
            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MessageFragment.newInstance();
                case 1:
                    return AddressBookFragment.newInstance();
                case 2:
                    return FriendRingFragment.newInstance(UserManager.getUser().getPhone_number());
                case 3:
                    return MineFragment.newInstance();
            }
            return null;
        }
    }


    @Override
    public void onEvent(EventArgs eventArgs) {
        if (eventArgs != null) {
            if (eventArgs.getEventType().equals(EventType.Event_ToMessage)) {
                mViewPager.setCurrentItem(0);
            }
            if (eventArgs.getEventType().equals(EventType.Event_MessageCode)) {
                Features_information = (String) eventArgs.getObject();
            }
        }
    }


    /**
     * 全局心跳
     */
    private void Heartbeat() {
        if (UserManager.getUser() == null || UserManager.getUser().getLoginkey() == null) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.clear();
//        Log.d("adskjfadsf", "更新消息特征码" + Features_information);
        map.put("Method", "Multitask");
        map.put("Sinkey", UserManager.getUser().getLoginkey());
        map.put("Features_friends", Tezheng.Features_friends);//好友列表特征码
        map.put("Features_group", Tezheng.Features_group);//群组列表特征码
        map.put("Features_information", Features_information);//消息列表特征码
        map.put("Features_news", Tezheng.Features_news);//朋友圈特征码
        map.put("Mycollection", Tezheng.Mycollection);//收藏特征码
        map.put("Comment", Tezheng.Comment);//评论征码
        map.put("Flike", Tezheng.Flike);//点赞征码
        map.put("Features_app", Tezheng.Features_app);//虚拟APP特征码
        map.put("Vson", "1.0");//版本号
        OkGo.<String>post(API.BASE_API)
                .params("Data", ApiURLUtils.GetDate(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.d("XinTiao", GetDate(response.body()));
                        //int status = CheckDate(response.body()).getState();
                        checkCode(GetDate(response.body()));
                    }

                });
    }

    /**
     * 1=强制退出登录；
     * 2=更新好友列表；
     * 3=更新群组列表；
     * 4=更新消息列表；
     * 5=更新朋友圈信息列表；
     * 6=更新朋友圈评论；
     * 7=更新朋友圈点赞；
     * 8=更新收藏；
     * 9=请求过于频繁；
     * 10=app版本过期；
     * 11=登录信息过期
     * 12=撤回消息
     * 13=有音视频通话
     * 14=更新虚拟APP列表
     * 15=有新评论
     * 16=双向撤回
     */
    private void checkCode(String body) {
        AcceptVideoModel acceptVideoModel = gson.fromJson(body, AcceptVideoModel.class);
        if (acceptVideoModel.getCode() != null) {
            String code[] = acceptVideoModel.getCode().split("\\|");
            List<String> list = Arrays.asList(code);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    switch (list.get(i)) {
                        case "1":
                            if (MyApplication.ChatActivityIsTop) {
                                EventBus.getDefault().post("你的账号在其他设备登录");
                                return;
                            }
                            if (!mDialogIsShow) {
                                mDialogIsShow = true;
                                showESCDialog("你的账号在其他设备登录");//强制退出
                            }
                            break;
                        case "13"://视频
//                            video(acceptVideoModel);
                            break;
                        case "15"://新的评论
                            break;
                        case "4":
                            //更新消息列表
                            EventBus.getDefault().post("更新消息");
                            break;
                        case "12":
                            //更新消息列表
                            EventBus.getDefault().post("撤回消息" + acceptVideoModel.getRid());

//                            撤回消息处理
                            撤回消息(acceptVideoModel.getRid());
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }


    private void 撤回消息(String rid) {
        // 撤回消息处理
        try {
//            new ChatAdapter(this, new ArrayList<>()).replaceReceviver(rid);
            List<FrendsMessageEntity> frendsMessageEntities = DBHelper.queryFrendMessage();

            for (FrendsMessageEntity frendsMessageEntity : frendsMessageEntities) {
                if (frendsMessageEntity.getMessage_id().equals(rid)) {
                    frendsMessageEntity.setContentType(1);
                    frendsMessageEntity.setContent("");
                    DBHelper.updateMessage(frendsMessageEntity);


                    updateHomeFragment(frendsMessageEntity);

                    break;
                }
            }


            Toast.makeText(this, "首页处理撤回消息", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "首页处理撤回消息 失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHomeFragment(FrendsMessageEntity frendsMessageEntity) {
        if (frendsMessageEntity == null) {
            Toast.makeText(this, "更新数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < DBHelper.getUserMessageList().size(); i++) {
            if (frendsMessageEntity.toUid.equals(DBHelper.getUserMessageList().get(i).card)) {
                MessageListEntity entity = DBHelper.getUserMessageList().get(i);
                entity.setRval("对方撤回了一条消息");
                entity.setRclass(1 + "");
                MyApplication.getDaoSession().getMessageListEntityDao().update(entity);
            }
        }
        EventBus.getDefault().post("更新消息");
    }


    /**
     * 视频处理
     *
     * @param acceptVideoModel
     */
    public void video(AcceptVideoModel acceptVideoModel) {
        Intent intent = new Intent();
        if (acceptVideoModel.getType().equals("2")) {
            intent.setClass(this, VoipRingingActivity.class);
        } else {
            intent.setClass(this, VoipAudioRingingActivity.class);
        }
        intent.putExtra("nickname", acceptVideoModel.getNickname());
        intent.putExtra("ip", acceptVideoModel.getIp());
        intent.putExtra("aid", acceptVideoModel.getAid());
        intent.putExtra("Username", UserManager.getUser().getPhone_number());
        intent.putExtra("Sinkey", UserManager.getUser().getLoginkey());
        intent.putExtra("head_img", acceptVideoModel.getHead_img());
        intent.putExtra("targetId", acceptVideoModel.getPhone());
        if (acceptVideoModel.getType().equals("2")) {
            if (!MyStatus.videoStatus) {
                MyStatus.videoStatus = true;
                startActivity(intent);
            }
        } else {
            if (!MyStatus.AudioStatus) {
                MyStatus.AudioStatus = true;
                startActivity(intent);
            }
        }
    }

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int num = msg.what;
            if (tab == null) return;
            if (num == 0) {
                tab.hideMsg(0);
            } else {
                tab.showMsg(0, num);
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        RongCallProxy.getInstance().setCallListener(new RongListener());

    }
}