package com.hjq.demo.common;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.hjq.demo.BuildConfig;
import com.hjq.demo.greenDao.DaoMaster;
import com.hjq.demo.greenDao.DaoSession;
import com.hjq.demo.other.EventBusManager;
import com.hjq.demo.session.AManager;
import com.hjq.demo.ui.activity.CrashActivity;
import com.hjq.demo.ui.activity.HomeActivity;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;
import com.tencent.bugly.crashreport.CrashReport;
import com.zlw.main.recorderlib.RecordManager;

import org.greenrobot.greendao.database.Database;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.hjq.demo.rong.RongVoice.appkey;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目中的 Application 基类
 */
public final class MyApplication extends Application {

    private static Context mInstance;
    public static Application mApplication;
    private static DaoSession daoSession;
    public static boolean ChatActivityIsTop = false;
    public static String mCurrentChatCard;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mApplication = this;
        mInstance = this;
        initSDK(this);
        AManager.init(this);
        // note: DevOpenHelper is for dev only, use a OpenHelper subclass instead
        //初始化数据库
        //deleSQL();
        /**
         * 参数1： Application 实例
         * 参数2： 是否打印日志
         */
        RecordManager.getInstance().init(mApplication, false);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wechat.db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        initRongSdk(this);
    }

    private void initRongSdk(MyApplication myApplication) {
        RongIM.init(this,appkey);
        RongIMClient.init(this, appkey, false);
//        RongIM.init(this,"bmdehs6pbamks");
//        RCSCallClient.getInstance().init(this);




    }

    public void deleSQL() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wechat.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
        DaoMaster.createAllTables(daoMaster.getDatabase(), true);
    }

    //返回数据库操作对象
    public static DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 初始化一些第三方框架
     */
    public static void initSDK(Application application) {
        // 这个过程专门用于堆分析的 leak 金丝雀
        // 你不应该在这个过程中初始化你的应用程序
//        if (LeakCanary.isInAnalyzerProcess(application)) {
//            return;
//        }
        // 内存泄漏检测
        //LeakCanary.install(application);

        // 友盟统计、登录、分享 SDK
        UmengClient.init(application);

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(application);

        // 图片加载器
        ImageLoader.init(application);

        // EventBus 事件总线
        EventBusManager.init();

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, BuildConfig.BUGLY_ID, false);

        // Crash 捕捉界面
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(HomeActivity.class)
                // 错误的 Activity
                .errorActivity(CrashActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();
    }


    public static Context getInstance() {
        return mInstance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }


}