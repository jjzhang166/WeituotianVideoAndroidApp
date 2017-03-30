package com.weituotian.video;

/**
 * Created by wing on 2015/10/22.
 * 存放一些运行中需要的全局常量,如服务器的地址等
 */
public interface GlobalConstant {

    public final static int REQUEST_CODE_LOGIN = 1;
    public final static int REQUEST_CODE_BROSWER = 2;
    public final static int REQUEST_CODE_REG = 3;

    int HOME_FRAGMENT = 0;
    int HISTORY_FRAGMENT = 1;
    String SERVER_URL = "http://182.254.156.215:8080/";
    String TX_URL = "http://182.254.156.215:8080/";
    String VIDEO_H264 = "video_3.json";
    String VIDEO_MPG = "video_1.json";
    String VIDEO_MPG_BIG = "video_2.json";
    String VIDEO_CODE_MATERIAL = "123456";
    String VIDEO_CODE_ANIM = "654321";
    String IMAGE_URL = "image";

    String BASE_URL = "";
    String LOGIN_URL = "";
}
