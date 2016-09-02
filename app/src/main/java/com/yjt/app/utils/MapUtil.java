package com.yjt.app.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Regex;

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
        final String  space  = "&nbsp;";
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
            return dis + Constant.Kilometer;
        }

        if (lenMeter > 1000) {
            float         dis  = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String        dstr = fnum.format(dis);
            return dstr + Constant.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + Constant.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + Constant.Meter;
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

    /**
     * long类型时间格式化
     */
    public String convertToTime(long time) {
        SimpleDateFormat df   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date             date = new Date(time);
        return df.format(date);
    }

    public String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour    = second / 3600;
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

    public void showMapException(Activity activity, final int resultCode) {
        switch (resultCode) {
            case AMapException.CODE_AMAP_SIGNATURE_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SIGNATURE_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_INVALID_USER_KEY:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_INVALID_USER_KEY, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_NOT_AVAILBALE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_NOT_AVAILBALE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_DAILY_QUERY_OVER_LIMIT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_DAILY_QUERY_OVER_LIMIT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ACCESS_TOO_FREQUENT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ACCESS_TOO_FREQUENT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_INVALID_USER_IP:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_INVALID_USER_IP, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_INVALID_USER_DOMAIN:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_INVALID_USER_DOMAIN, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_INVALID_USER_SCODE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_INVALID_USER_SCODE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_USERKEY_PLAT_NOMATCH:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_USERKEY_PLAT_NOMATCH, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_IP_QUERY_OVER_LIMIT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_IP_QUERY_OVER_LIMIT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_NOT_SUPPORT_HTTPS:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_NOT_SUPPORT_HTTPS, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_INSUFFICIENT_PRIVILEGES:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_INSUFFICIENT_PRIVILEGES, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_USER_KEY_RECYCLED:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_USER_KEY_RECYCLED, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ENGINE_RESPONSE_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ENGINE_RESPONSE_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ENGINE_RESPONSE_DATA_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ENGINE_CONNECT_TIMEOUT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ENGINE_CONNECT_TIMEOUT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ENGINE_RETURN_TIMEOUT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ENGINE_RETURN_TIMEOUT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_INVALID_PARAMS:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_INVALID_PARAMS, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_MISSING_REQUIRED_PARAMS:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_ILLEGAL_REQUEST:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_ILLEGAL_REQUEST, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_UNKNOWN_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_UNKNOWN_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_ERRORCODE_MISSSING:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_ERRORCODE_MISSSING, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_ERROR_PROTOCOL:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_ERROR_PROTOCOL, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_URL_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_URL_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_NETWORK_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_NETWORK_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_UNKNOWN_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_UNKNOWN_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_INVALID_PARAMETER:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_INVALID_PARAMETER, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_IO_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_IO_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_NULLPOINT_EXCEPTION:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_TABLEID_NOT_EXIST:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ID_NOT_EXIST:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ID_NOT_EXIST, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SERVICE_MAINTENANCE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SERVICE_MAINTENANCE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ENGINE_TABLEID_NOT_EXIST:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_NEARBY_INVALID_USERID:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_NEARBY_INVALID_USERID, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_NEARBY_KEY_NOT_BIND:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_NEARBY_KEY_NOT_BIND, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
            case AMapException.CODE_AMAP_CLIENT_USERID_ILLEGAL:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_USERID_ILLEGAL, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_NEARBY_NULL_RESULT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOAD_TOO_FREQUENT:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_CLIENT_UPLOAD_LOCATION_ERROR:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ROUTE_OUT_OF_SERVICE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ROUTE_OUT_OF_SERVICE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ROUTE_NO_ROADS_NEARBY:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ROUTE_NO_ROADS_NEARBY, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_ROUTE_FAIL:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_ROUTE_FAIL, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_OVER_DIRECTION_RANGE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_OVER_DIRECTION_RANGE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SHARE_LICENSE_IS_EXPIRED:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            case AMapException.CODE_AMAP_SHARE_FAILURE:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), AMapException.AMAP_SHARE_FAILURE, activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
            default:
                ViewUtil.getInstance().showAlertDialog(activity, activity.getString(R.string.error_prompt), activity.getString(R.string.search_failed)
                        + Regex.LEFT_PARENTHESIS.getRegext()
                        + resultCode
                        + Regex.RIGHT_PARENTHESIS.getRegext(), activity.getString(R.string.enter), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null, null);
                break;
        }
    }
}
