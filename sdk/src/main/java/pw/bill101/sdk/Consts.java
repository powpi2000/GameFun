package pw.bill101.sdk;

import java.io.File;

/**
 * Created by powpi2000 on 2017/2/9.
 */

public class Consts {
    public static final boolean DEBUG = true;

    //当前登录的用户名
    public static String CUR_USERNAME = "";
    //登录后默认密码（只用于显示）
    public static final String LOGINED_DEFAULT_PASSWORD = "PJSDKPWD";
    public static final File CACHE_FOLDER = new File(ExternalStorage.getAllStorageLocations()
            .get(ExternalStorage.SD_CARD), "GAMEFUN");//缓存目录
    static {
        CACHE_FOLDER.mkdirs();
    }

    public static class Cache {
        public static final String UDID = "udid";
        //登录的用户名，用分号隔开
        public static final String USERNAME_LIST = "username_list";

        //存储用户token（根据用户登录名）
        public static final String TOKEN_NAME = "token_%1$s";
    }

}
