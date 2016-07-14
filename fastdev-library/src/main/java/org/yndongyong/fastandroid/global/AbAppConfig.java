package org.yndongyong.fastandroid.global;

/**
 * Created by Dong on 2016/5/11.
 */
public class AbAppConfig {

    public static String DOWNLOAD_ROOT_DIR = "fastandroid";
    public static String DOWNLOAD_IMAGE_DIR = "images";
    public static String DOWNLOAD_FILE_DIR = "files";
    public static String CACHE_DIR = "caches";
    public static String DB_DIR = "db";
    public static long DISK_CACHE_EXPIRES_TIME = 60000000L;
    public static int MAX_CACHE_SIZE_INBYTES = 10485760;
    public static int MAX_DISK_USAGE_INBYTES = 20971520;


    //设置设计稿的尺寸
    public static int UI_WIDTH = 1280;
    public static int UI_HEIGHT = 720;
    public static float UI_DENSITY = 2;

    public static String CONNECT_EXCEPTION = "无法连接到网络";

    public static String UNKNOWN_HOST_EXCEPTION = "连接远程地址失败";

    public static String SOCKET_EXCEPTION = "网络连接出错，请重试";

    public static String SOCKET_TIMEOUT_EXCEPTION = "连接超时，请重试";

    public static String NULL_POINTER_EXCEPTION = "抱歉，远程服务出错了";

    public static String NULL_MESSAGE_EXCEPTION = "抱歉，程序出错了";

    public static String CLIENT_PROTOCOL_EXCEPTION = "Http请求参数错误";

    public static String MISSING_PARAMETERS = "参数没有包含足够的值";

    public static String REMOTE_SERVICE_EXCEPTION = "抱歉，远程服务出错了500";

    public static String NOT_FOUND_EXCEPTION = "请求的资源无效404";
    public static String REQUEST_ERROR_EXCEPTION = "请求的参数无效400";

    public static String FORBIDDEN_EXCEPTION = "没有权限访问资源";
    public static String UNTREATED_EXCEPTION = "未处理的异常";

    public static String ERRORDATA = "数据错误";
}
