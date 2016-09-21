package com.yjt.app.utils;

import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.enums.PathPlanningErrCode;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.ui.dialog.PromptDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapUtil {

    private static MapUtil mMapUtil;

    private MapUtil() {
        // cannot be instantiated
    }

    public static synchronized MapUtil getInstance() {
        if (mMapUtil == null) {
            mMapUtil = new MapUtil();
        }
        return mMapUtil;
    }

    public static void releaseInstance() {
        if (mMapUtil != null) {
            mMapUtil = null;
        }
    }

    public String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public String makeHtmlNewLine() {
        return "<br />";
    }

    public String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + Constant.Map.Kilometer;
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + Constant.Map.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + Constant.Map.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + Constant.Map.Meter;
    }

    public boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    public NaviLatLng parseCoordinate(String text) {
        try {
            return new NaviLatLng(Double.parseDouble(text.split(",")[0]), Double.parseDouble(text.split(",")[1]));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * long类型时间格式化
     */
    public String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    public int getDriveActionID(String actionName) {
        switch (actionName) {
            case Constant.Map.MOVE_STATUS1:
                return R.mipmap.dir1;
            case Constant.Map.MOVE_STATUS2:
                return R.mipmap.dir2;
            case Constant.Map.MOVE_STATUS3:
                return R.mipmap.dir3;
            case Constant.Map.MOVE_STATUS4:
                return R.mipmap.dir4;
            case Constant.Map.MOVE_STATUS5:
            case Constant.Map.MOVE_STATUS6:
                return R.mipmap.dir5;
            case Constant.Map.MOVE_STATUS7:
            case Constant.Map.MOVE_STATUS8:
                return R.mipmap.dir6;
            case Constant.Map.MOVE_STATUS9:
            case Constant.Map.MOVE_STATUS10:
                return R.mipmap.dir7;
            case Constant.Map.MOVE_STATUS11:
                return R.mipmap.dir8;
            default:
                return R.mipmap.dir3;
        }
    }

    public int getWalkActionID(String actionName) {
        switch (actionName) {
            case Constant.Map.MOVE_STATUS1:
                return R.mipmap.dir1;
            case Constant.Map.MOVE_STATUS2:
                return R.mipmap.dir2;
            case Constant.Map.MOVE_STATUS3:
                return R.mipmap.dir3;
            case Constant.Map.MOVE_STATUS4:
                return R.mipmap.dir4;
            case Constant.Map.MOVE_STATUS5_1:
            case Constant.Map.MOVE_STATUS6:
                return R.mipmap.dir5;
            case Constant.Map.MOVE_STATUS7_1:
            case Constant.Map.MOVE_STATUS8:
                return R.mipmap.dir6;
            case Constant.Map.MOVE_STATUS9_1:
                return R.mipmap.dir7;
            case Constant.Map.MOVE_STATUS11_1:
                return R.mipmap.dir8;
            case Constant.Map.MOVE_STATUS12:
                return R.mipmap.dir9;
            case Constant.Map.MOVE_STATUS13:
                return R.mipmap.dir10;
            case Constant.Map.MOVE_STATUS14:
                return R.mipmap.dir11;
            default:
                return R.mipmap.dir3;
        }
    }

    public void showMapError(FragmentActivity activity, final int resultCode) {
        switch (resultCode) {
            case AMapException.CODE_AMAP_SIGNATURE_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SIGNATURE_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_INVALID_USER_KEY:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_INVALID_USER_KEY)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_NOT_AVAILBALE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_NOT_AVAILBALE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_DAILY_QUERY_OVER_LIMIT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_DAILY_QUERY_OVER_LIMIT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ACCESS_TOO_FREQUENT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ACCESS_TOO_FREQUENT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_INVALID_USER_IP:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_INVALID_USER_IP)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_INVALID_USER_DOMAIN:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_INVALID_USER_DOMAIN)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_INVALID_USER_SCODE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_INVALID_USER_SCODE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_USERKEY_PLAT_NOMATCH:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_USERKEY_PLAT_NOMATCH)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_IP_QUERY_OVER_LIMIT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_IP_QUERY_OVER_LIMIT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_NOT_SUPPORT_HTTPS:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_NOT_SUPPORT_HTTPS)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_INSUFFICIENT_PRIVILEGES:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_INSUFFICIENT_PRIVILEGES)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_USER_KEY_RECYCLED:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_USER_KEY_RECYCLED)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ENGINE_RESPONSE_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ENGINE_RESPONSE_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ENGINE_RESPONSE_DATA_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ENGINE_CONNECT_TIMEOUT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ENGINE_CONNECT_TIMEOUT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ENGINE_RETURN_TIMEOUT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ENGINE_RETURN_TIMEOUT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_INVALID_PARAMS:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_INVALID_PARAMS)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_MISSING_REQUIRED_PARAMS:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_ILLEGAL_REQUEST:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_ILLEGAL_REQUEST)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_UNKNOWN_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_UNKNOWN_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_ERRORCODE_MISSSING:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_ERRORCODE_MISSSING)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_ERROR_PROTOCOL:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_ERROR_PROTOCOL)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_URL_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_URL_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_NETWORK_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_NETWORK_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_UNKNOWN_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_UNKNOWN_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_INVALID_PARAMETER:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_INVALID_PARAMETER)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_IO_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_IO_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_NULLPOINT_EXCEPTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_TABLEID_NOT_EXIST:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ID_NOT_EXIST:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ID_NOT_EXIST)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SERVICE_MAINTENANCE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SERVICE_MAINTENANCE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ENGINE_TABLEID_NOT_EXIST:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_NEARBY_INVALID_USERID:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_NEARBY_INVALID_USERID)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_NEARBY_KEY_NOT_BIND:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_NEARBY_KEY_NOT_BIND)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_USERID_ILLEGAL:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_USERID_ILLEGAL)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_NEARBY_NULL_RESULT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOAD_TOO_FREQUENT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOAD_LOCATION_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ROUTE_OUT_OF_SERVICE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ROUTE_OUT_OF_SERVICE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ROUTE_NO_ROADS_NEARBY:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ROUTE_NO_ROADS_NEARBY)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_ROUTE_FAIL:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_ROUTE_FAIL)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_OVER_DIRECTION_RANGE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_OVER_DIRECTION_RANGE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SHARE_LICENSE_IS_EXPIRED:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case AMapException.CODE_AMAP_SHARE_FAILURE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(AMapException.AMAP_SHARE_FAILURE)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_CONNECTION:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan1))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_ENDPOINT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan2))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_NOROADFORENDPOINT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan3))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_NOROADFORSTARTPOINT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan4))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_NOROADFORWAYPOINT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan5))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_PROTOCOL:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan6))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.ERROR_STARTPOINT:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan7))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.INSUFFICIENT_PRIVILEGES:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan8))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.INVALID_PARAMS:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan9))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.INVALID_USER_KEY:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan10))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.OVER_QUOTA:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan11))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.SERVICE_NOT_EXIST:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan12))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.SERVICE_RESPONSE_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan13))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.SUCCESS_ROUTE:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan14))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            case PathPlanningErrCode.UNKNOWN_ERROR:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan15))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
            default:
                PromptDialog.createBuilder(activity.getSupportFragmentManager())
                        .setTitle(activity.getString(R.string.error_prompt))
                        .setPrompt(activity.getString(R.string.error_prompt_path_plan1))
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_ERROR)
                        .setCancelableOnTouchOutside(false)
                        .show();
                break;
        }
    }
}
