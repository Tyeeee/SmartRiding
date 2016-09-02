package com.yjt.app.constant;

public class Constant {

    //bugly
    public static final String BUGLY_APP_ID = "900018257";

    public static final String START = "start";
    public static final String END = "end";

    public static final int SIMULATED_NAVIGATION_SPEED = 100;
    public static final int LATLNG_ZOOM = 15;

    public static final String Kilometer = "\u516c\u91cc";// "公里";
    public static final String Meter = "\u7c73";// "米";
    public static final String ByFoot = "\u6b65\u884c";// "步行";
    public static final String To = "\u53bb\u5f80";// "去往";
    public static final String Station = "\u8f66\u7ad9";// "车站";
    public static final String TargetPlace = "\u76ee\u7684\u5730";// "目的地";
    public static final String StartPlace = "\u51fa\u53d1\u5730";// "出发地";
    public static final String About = "\u5927\u7ea6";// "大约";
    public static final String Direction = "\u65b9\u5411";// "方向";

    public static final String GetOn = "\u4e0a\u8f66";// "上车";
    public static final String GetOff = "\u4e0b\u8f66";// "下车";
    public static final String Zhan = "\u7ad9";// "站";

    public static final String cross = "\u4ea4\u53c9\u8def\u53e3"; // 交叉路口
    public static final String type = "\u7c7b\u522b"; // 类别
    public static final String address = "\u5730\u5740"; // 地址
    public static final String PrevStep = "\u4e0a\u4e00\u6b65";
    public static final String NextStep = "\u4e0b\u4e00\u6b65";
    public static final String Gong = "\u516c\u4ea4";
    public static final String ByBus = "\u4e58\u8f66";
    public static final String Arrive = "\u5230\u8FBE";// 到达

    public static final String DAY_NIGHT_MODE = "daynightmode";
    public static final String DEVIATION = "deviationrecalculation";
    public static final String JAM = "jamrecalculation";
    public static final String TRAFFIC = "trafficbroadcast";
    public static final String CAMERA = "camerabroadcast";
    public static final String SCREEN = "screenon";
    public static final String THEME = "theme";
    public static final String ISEMULATOR = "isemulator";


    public static final String ACTIVITYINDEX = "activityindex";

    public static final int SIMPLEHUDNAVIE = 0;
    public static final int EMULATORNAVI = 1;
    public static final int SIMPLEGPSNAVI = 2;
    public static final int SIMPLEROUTENAVI = 3;

    public static final int COLOR_DEFAULT = 0x9999;
    public static final int SIZE_DEFAULT = 0x9999;

    public static final int DRAWABLE_TOP = 0x0001;
    public static final int DRAWABLE_LEFT = 0x0002;
    public static final int DRAWABLE_RIGHT = 0x0003;
    public static final int DRAWABLE_BOTTOM = 0x0004;

    public static class ItemPosition {
        public static final int HOME = 0;
        public static final int DEVICE = 1;
        public static final int MESSAGE = 2;
        public static final int SETTING = 3;
        public static final int SEARCH_DEVICE = 0;
        public static final int GENERAL_SETTING = 1;
        public static final int CHECK_UPDATE = 2;
        public static final int CLEAR_DATA = 3;
        public static final int BREAK_LINK = 4;
        public static final int ABOUT_DEVICE = 5;
    }

    public static class ItemType {
        public static final int HEADER_VIEW = 0x1001;
        public static final int CONTENT_VIEW = 0x1002;
        public static final int FOOTER_VIEW = 0x1003;
        public static final int OTHER_VIEW = 0x1004;
    }

    public static class Page {
        public static final int HOME = 0x2001;
        public static final int DEVICE = 0x2002;
        public static final int MESSAGE = 0x2003;
        public static final int SETTING = 0x2004;
    }

    public static class PointType {
        public static final int START = 0x300001;
        public static final int PASS = 0x300002;
        public static final int END = 0x300003;
    }

    public static class Common {
        public static final int RESULT_CODE = 0x4002;
    }

    public static class RequestCode {
        public static final int POINT = 0x4001;
    }


    public static class Message {
        public static final int LOCATION_SUCCESS = 0x5001;
        public static final int LOCATION_FAILED = 0x5002;
    }

    public static class Map {
        public static final long LOCATION_MINIMUM_TIME_INTERVAL = 1000;
        public static final long LOCATION_TIME_OUT = 10000;
        public static final long GEOCODE_SEARCH_SUCCESS = 1000;
        public static final String ROAD_UNIMPEDED = "畅通";
        public static final String ROAD_CRAWL = "缓行";
        public static final String ROAD_JAM = "拥堵";
        public static final String ROAD_SERIOUS_JAM = "严重拥堵";
        public static final String MOVE_STATUS1 = "右转";
        public static final String MOVE_STATUS2 = "左转";
        public static final String MOVE_STATUS3 = "直行";
        public static final String MOVE_STATUS4 = "减速行驶";
        public static final String MOVE_STATUS5 = "向右前方行驶";
        public static final String MOVE_STATUS5_1 = "向右前方";
        public static final String MOVE_STATUS6 = "靠右";
        public static final String MOVE_STATUS7 = "向左前方行驶";
        public static final String MOVE_STATUS7_1 = "向左前方";
        public static final String MOVE_STATUS8 = "靠左";
        public static final String MOVE_STATUS9 = "向左后方行驶";
        public static final String MOVE_STATUS9_1 = "向左后方";
        public static final String MOVE_STATUS10 = "左转调头";
        public static final String MOVE_STATUS11 = "向右后方行驶";
        public static final String MOVE_STATUS11_1 = "向右后方";
        public static final String MOVE_STATUS12 = "通过人行横道";
        public static final String MOVE_STATUS13 = "通过地下通道";
        public static final String MOVE_STATUS14 = "通过过街天桥";
    }

}
