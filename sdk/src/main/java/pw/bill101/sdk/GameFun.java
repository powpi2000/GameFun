package pw.bill101.sdk;

import android.content.Context;
import android.content.Intent;

import pw.bill101.sdk.service.FloatViewService;

/**
 * Created by powpi2000 on 2017/2/9.
 */

public class GameFun {
    public static final String SDK_VERSION = "0.1";

    private static Context mContext;
    private static int mAppId;
    private static String mUDID;
    private static FloatViewService mFloatViewService;


    private static boolean mLogined;

    private static GameFun mGameFun;

    private GameFun() {
    }


    public static GameFun initialize(Context context, int appId) {
        if (mGameFun != null) {
            return mGameFun;
        }

        mContext = context;
        mAppId = appId;


        mGameFun = new GameFun();

        try {
            mContext.bindService(new Intent(mContext, FloatViewService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
        }

        //初始化UDID
        initUDID();



        //统计
        mGameFun.statSDK();

        return mGameFun;
    }

    /**
     * 登录泡椒
     */

    public static void doLogin(LoginListener listener) {
        if (mContext == null || mAppId == 0 || mGameFun == null) {
            return;
        }

        LoginDialog dialog = new LoginDialog(mContext, listener);
        dialog.show();
    }



    /**
     * 设置退出登录事件
     */
    public static void setLogoutListener(LogoutListener listener) {
        mLogoutListener = listener;
    }

    /**
     * 设置是否登录
     */
    public static void setLogined(boolean logined) {
        mLogined = logined;
    }

    /**
     *
     * @return
     */
    public static LogoutListener getLogoutListener() {
        return mLogoutListener;
    }


    /**
     * 显示悬浮图标
     */
    public static void showFloatingView() {
        if (mFloatViewService != null && mLogined) {
            mFloatViewService.showFloat();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public static void hideFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
        }
    }

    /**
     * 释放数据
     */
    public static void destroy() {
        //mPayListener = null;
        mLogined = false;
        try {
            mContext.unbindService(mServiceConnection);

            //if( mFloatViewService != null ) {
            //    mFloatViewService.destroyFloat();
            //}

            //mContext.unregisterReceiver(mHomeKeyEventReceiver);
        } catch (Exception e) {
        } finally {
            mFloatViewService = null;
            mGameFun = null;
        }
    }

    private static void initUDID() {
        String udid = AppCacheUtils.get(mContext).getString(Consts.Cache.UDID);
        if (TextUtils.isEmpty(udid) || udid.length() < 8 || udid.length() > 16) {
            udid = Utils.getUDID(mContext);
            AppCacheUtils.get(mContext).put(Consts.Cache.UDID, udid);
        }
        mUDID = udid;
    }

    public static int getAppId() {
        return mAppId;
    }

    public static String getUDID() {
        if (StringUtils.isEmpty(mUDID)) {
            initUDID();
        }
        return mUDID;
    }

    public static String getAppVersion() {
        if (mContext == null) {
            return "1.0";
        }
        return Utils.getVersionName(mContext);
    }

    /**
     * 获取渠道名称
     */
    public static String getChannel() {
        if (mContext == null) {
            return "paojiao";
        }
        return Utils.getChannelFromManifest(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 连接到Service
     */
    private final static ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatViewService = null;
        }
    };

    /**
     * 监听是否点击了home键将客户端推到后台
     */
    //private static BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
    //    String SYSTEM_REASON = "reason";
    //    String SYSTEM_HOME_KEY = "homekey";
    //    String SYSTEM_HOME_KEY_LONG = "recentapps";
    //
    //    @Override
    //    public void onReceive(Context context, Intent intent) {
    //        String action = intent.getAction();
    //        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
    //            String reason = intent.getStringExtra(SYSTEM_REASON);
    //            if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
    //                //表示按了home键,程序到了后台
    //                PJSDK.hideFloatingView();
    //            } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
    //                //表示长按home键,显示最近使用的程序列表
    //            }
    //        }
    //    }
    //};

    /**
     * SDK启动统计
     */
    private void statSDK() {
        Map<String, String> params = new HashMap<>();
        params.put("imei", Utils.getIMEI(mContext));
        //params.put("net_type", Utils.getNetType(mContext));
        params.put("mode", Build.MODEL);
        params.put("sdk", Build.VERSION.RELEASE);
        params.put("productVersion", SDK_VERSION);
        params.put("mac", Utils.getMac(mContext));
        params.put("product", "paojiao_sdk");
        params.put("cid", getChannel());
        HttpUtils.post(Api.STAT_URL, params, null);
    }



}
