package com.hjq.demo.rong;//package com.hjq.demo.rong;
//
//import android.content.Context;
//
//import com.hjq.demo.session.UserManager;
//
//import java.util.Collections;
//import java.util.concurrent.Semaphore;
//
//import io.rong.imlib.RongIMClient;
//import io.rong.message.SightMessage;
//
///**
// * 融云视频 类
// */
//public class RongController {
//
//
//    private static RongController sAppClient;
//
//    public static void init(Context context) {
//        ResourcesHelper.init(context.getApplicationContext());
//        sAppClient = new RongController(context.getApplicationContext());
//    }
//
//    public static RongController getInstance() {
//        if (sAppClient == null) {
//            throw new RuntimeException("ChatClientManager is not initialized");
//        }
//        return sAppClient;
//    }
//
//    private static final String STORAGE_KEY_TOKEN = "SessionID";
//    private static final String STORAGE_KEY_USER_ID = "UserID";
//    private static final String STORAGE_KEY_DEVICE_ID = "DeviceID";
//
//    private Context mAppContext;
//    private RongIMClient mRongIMClient;
//    private AuthApi mAuthApi;
//    private List<OnConnectionStateChangeListener> mOnConnectionStateChangeListenerList;
//    private LoginExpiredListener mLoginExpiredListener;
//
//    private UserManager mUserManager;
//    private ChatManager mChatManager;
//    private ContactManager mContactManager;
//    private GroupManager mGroupManager;
//    private ConversationManager mConversationManager;
//    private DBHelper mDBHelper;
//    private StorageHelper mStorageHelper;
//
//    private String mUserID;
//    private String mToken;
//    private String mDeviceID;
//    private Semaphore mLoginLock;
//    private boolean isLogged;
//
//
//
//    private RongController(Context appContext) {
//        mAppContext = appContext.getApplicationContext();
//        mRongIMClient = RongIMClient.getInstance();
//        mOnConnectionStateChangeListenerList = Collections.synchronizedList(new LinkedList<OnConnectionStateChangeListener>());
//        mAuthApi = ApiHelper.getProxyInstance(AuthApi.class);
//        mLoginExpiredListener = new LoginExpiredListenerWrapper(null);
//        mLoginLock = new Semaphore(1);
//        mStorageHelper = new StorageHelper(mAppContext, mAppContext.getPackageName());
//        mUserManager = new UserManager(this);
//        mChatManager = new ChatManager(this);
//        mConversationManager = new ConversationManager(this);
//        mContactManager = new ContactManager(this);
//        mGroupManager = new GroupManager(this);
//
//        initIM();
//    }
//
//    private void initIM() {
//        RongIMClient.init(mAppContext);
//        try {
//            RongIMClient.registerMessageType(SightMessage.class);
//            RongIMClient.registerMessageType(ContactNotificationMessageEx.class);
//        } catch (AnnotationNotFoundException ignored) {
//        }
//        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
//            @Override
//            public boolean onReceived(Message message, int remainder) {
//                switch (message.getObjectName()) {
//                    case "RC:TxtMsg":
//                    case "RC:VcMsg":
//                    case "RC:ImgMsg":
//                    case "RC:LBSMsg":
//                    case "RC:FileMsg":
//                    case "Custom:VideoMsg":
//                        mChatManager.onReceiveChatMessage(message, remainder);
//                        break;
//                    case "Custom:ContactNtf"://该类型不会保存期起来
//                        mContactManager.onReceiveContactNotificationMessage((ContactNotificationMessageEx) message.getContent());
//                        break;
//                    case "RC:GrpNtf":
//                        mGroupManager.onReceiveGroupNotificationMessage((GroupNotificationMessage) message.getContent());
//                        break;
//                    default:
//                        LogUtil.e("Unknown Message ObjectName:" + message.getObjectName());
//                }
//                if (remainder == 0) {
//                    mContactManager.updateContactUnreadCount();
//                    mConversationManager.updateChatUnreadCount();
//                }
//                return true;
//            }
//        });
//        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
//            @Override
//            public void onChanged(ConnectionStatus connectionStatus) {
//                switch (connectionStatus) {
//                    case CONNECTED:
//                    case CONNECTING:
//                        for (OnConnectionStateChangeListener listener : mOnConnectionStateChangeListenerList) {
//                            listener.onConnected();
//                        }
//                        break;
//                    case NETWORK_UNAVAILABLE:
//                    case DISCONNECTED:
//                        for (OnConnectionStateChangeListener listener : mOnConnectionStateChangeListenerList) {
//                            listener.onDisconnected();
//                        }
//                        break;
//                    case KICKED_OFFLINE_BY_OTHER_CLIENT:
//                    case TOKEN_INCORRECT:
//                    case SERVER_INVALID:
//                    case CONN_USER_BLOCKED:
//                        mLoginExpiredListener.onLoginExpired();
//                        break;
//                }
//            }
//        });
//    }
//
//
//}
