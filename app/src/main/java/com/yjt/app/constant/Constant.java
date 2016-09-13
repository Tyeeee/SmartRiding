package com.yjt.app.constant;

public class Constant {

    public static final String CHANNEL_NAME = "CHANNEL_NAME";
    public static final String BUGLY_APP_ID = "900018257";
    public static final String IFLY_APP_ID = "appid=57c7f443";
    public static final int PERMISSION_REQUEST_CODE = 0x00001;

    public static class View {
        public static final int COLOR_DEFAULT = 0x9999;
        public static final int SIZE_DEFAULT = 0x9999;
        public static final int RESOURCE_DEFAULT = 0x9999;
        public static final int DRAWABLE_TOP = 0x0001;
        public static final int DRAWABLE_LEFT = 0x0002;
        public static final int DRAWABLE_RIGHT = 0x0003;
        public static final int DRAWABLE_BOTTOM = 0x0004;
        public static final String CUSTOM_DIALOG = "custom_dialog";
        public static final String ROBOTO_REGULAR = "Roboto-Regular";
        public static final String ROBOTO_MEDIUM = "Roboto-Medium";
    }

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
        public static final int PASSWORD_MANAGEMENT = 0;
        public static final int DEVICE_LABEL = 1;
        public static final int USE_HELP = 2;
        public static final int FEEDBACK = 3;
        public static final int VERSION_UPDATE = 4;
        public static final int ABOUT_US = 5;
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
        public static final int POINT = 0x5001;
        public static final int DIALOG = 0x5002;
        public static final int DIALOG_EXIT = 0x5003;
        public static final int DIALOG_RADIO = 0x5004;
        public static final int DIALOG_DATE = 0x5005;
    }

    public static class Map {
        public static final String TTS_ROLE = "xiaoyan";
        public static final String TTS_SPEED = "50";
        public static final String TTS_VOLUME = "50";
        public static final String TTS_PITCH = "50";
        public static final String STREAM_TYPE = "50";
        public static final String KEY_REQUEST_FOCUS = "true";
        public static final String AUDIO_FORMAT = "wav";
        public static final String TTS_AUDIO_PATH = "/msc/tts.wav";

        public static final String JSON = "json";
        public static final String LANGUAGE = "zh_cn";
        public static final String LANGUAGE_TYPE = "mandarin";
        public static final String START_TIME_LINE = "5000";
        public static final String END_TIME_LINE = "1000";
        public static final String PUNCTUATION = "0";

        public static final String WS = "ws";
        public static final String CW = "cw";
        public static final String W = "w";

        public static final int SIMULATED_NAVIGATION_SPEED = 100;
        public static final int STEP_DISTANCE = 20;
        public static final String Kilometer = "\u516c\u91cc";// "公里";
        public static final String Meter = "\u7c73";// "米";
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
        public static final String MOVE_STATUS15 = "拍照";
        public static final String MOVE_STATUS16 = "车道";
        public static final String MOVE_STATUS17 = "并线";
    }

}
