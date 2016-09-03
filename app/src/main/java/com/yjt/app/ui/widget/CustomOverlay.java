package com.yjt.app.ui.widget;

import android.graphics.Color;
import android.text.TextUtils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.TMC;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomOverlay {

    private AMap              mAMap;
    private LatLng            mStartPoint;
    private LatLng            mEndPoint;
    private Marker            mStartMarker;
    private Marker            mEndMarker;
    private DrivePath         mDrivePath;
    private List<LatLonPoint> mPassPoints;
    private List<Marker> mPassMarkers        = new ArrayList<Marker>();
    private boolean      isPassMarkerVisible = true;
    private List<TMC>       mTmcs;
    private PolylineOptions mOptions;
    private boolean isColor = true;
    private float        mRouteWidth;
    private boolean      isNodeIconVisible;
    private List<LatLng> mLatLngPaths;
    private List<Marker>   mStationMarkers = new ArrayList<Marker>();
    private List<Polyline> mPolyLines      = new ArrayList<Polyline>();

    public CustomOverlay(AMap aMap, DrivePath path, LatLonPoint startPoint, LatLonPoint endPoint, List<LatLonPoint> passPoints) {
        this.mAMap = aMap;
        this.mDrivePath = path;
        this.mStartPoint = MapUtil.getInstance().convertToLatLng(startPoint);
        this.mEndPoint = MapUtil.getInstance().convertToLatLng(endPoint);
        this.mPassPoints = passPoints;
    }

    public void setColor(boolean color) {
        isColor = color;
    }

    public void setRouteWidth(float routeWidth) {
        this.mRouteWidth = routeWidth;
    }

    public void setPassMarkerVisible(boolean passMarkerVisible) {
        isPassMarkerVisible = passMarkerVisible;
    }

    public void setNodeIconVisible(boolean nodeIconVisible) {
        isNodeIconVisible = nodeIconVisible;
    }

    public void addRouteToMap() {
        mOptions = new PolylineOptions();
        mOptions.color(Color.BLUE).width(mRouteWidth);
        if (mAMap != null && mDrivePath != null && mRouteWidth != 0) {
            mLatLngPaths = new ArrayList<>();
            mTmcs = new ArrayList<>();
            mOptions.add(mStartPoint);
            for (DriveStep step : mDrivePath.getSteps()) {
                List<LatLonPoint> points = step.getPolyline();
                mTmcs.addAll(step.getTMCs());
//                addStationMarkers(step, MapUtil.getInstance().convertToLatLng(points.get(0)));
                for (LatLonPoint point : points) {
                    mOptions.add(MapUtil.getInstance().convertToLatLng(point));
                    mLatLngPaths.add(MapUtil.getInstance().convertToLatLng(point));
                }
            }
            mOptions.add(mEndPoint);
            if (mStartMarker != null) {
                mStartMarker.remove();
                mStartMarker = null;
            }
            if (mEndMarker != null) {
                mEndMarker.remove();
                mEndMarker = null;
            }
            addStartAndEndPointMarker();
            addPassPointMarker();
            if (!isColor) {
                updatePathColor();
            } else {
                addPolyline(mOptions);
            }
        } else {
            return;
        }
    }

    public void removeMarkerAndLine() {
        if (mStartMarker != null) {
            mStartMarker.remove();
        }
        if (mEndMarker != null) {
            mEndMarker.remove();
        }
        if (mStationMarkers != null && mStationMarkers.size() > 0) {
            for (Marker stationMarker : mStationMarkers) {
                stationMarker.remove();
            }
            mStationMarkers.clear();
        }
        if (mPolyLines != null && mPolyLines.size() > 0) {
            for (Polyline line : mPolyLines) {
                line.remove();
            }
            mPolyLines.clear();
        }
        if (mPassMarkers != null && mPassMarkers.size() > 0) {
            for (Marker passMarker : mPassMarkers) {
                passMarker.remove();
            }
            mPassMarkers.clear();
        }
    }

    private void addStartAndEndPointMarker() {
        mStartMarker = mAMap.addMarker((new MarkerOptions())
                                               .position(mStartPoint)
                                               .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start))
                                               .title("\u8D77\u70B9"));
        mEndMarker = mAMap.addMarker((new MarkerOptions())
                                             .position(mEndPoint)
                                             .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_end))
                                             .title("\u8D77\u70B9"));
    }

    private void addPassPointMarker() {
        if (this.mPassPoints != null && this.mPassPoints.size() > 0) {
            LatLonPoint latLonPoint;
            for (int i = 0; i < this.mPassPoints.size(); i++) {
                latLonPoint = this.mPassPoints.get(i);
                if (latLonPoint != null) {
                    mPassMarkers.add(mAMap.addMarker(new MarkerOptions()
                                                             .position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()))
                                                             .visible(isPassMarkerVisible)
                                                             .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pass))
                                                             .title("\u9014\u7ECF\u70B9")));
                }
            }
        }
    }

    private void addStationMarkers(DriveStep driveStep, LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng)
                .title("\u65B9\u5411:" + driveStep.getAction()
                               + "\n\u9053\u8DEF:" + driveStep.getRoad())
                .snippet(driveStep.getInstruction()).visible(isNodeIconVisible)
                .anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car));
        if (options != null) {
            Marker marker = mAMap.addMarker(options);
            if (marker != null) {
                mStationMarkers.add(marker);
            }
        } else {
            return;
        }
    }

    private void updatePathColor() {
        if (mAMap != null && mLatLngPaths != null && mLatLngPaths.size() > 0 && mTmcs != null && mTmcs.size() > 0) {
            int          j           = 0;
            LatLng       startLatLng = mLatLngPaths.get(0);
            LatLng       endLatLng   = null;
            double       segment     = 0.0;
            TMC          tmc         = null;
            List<LatLng> temp        = new ArrayList<>();
            addPolyline(new PolylineOptions().add(mStartPoint, startLatLng).setDottedLine(true));
            addPolyline(new PolylineOptions().add(mLatLngPaths.get(mLatLngPaths.size() - 1), endLatLng).setDottedLine(true));
            for (int i = 0; i < mLatLngPaths.size() && j < mTmcs.size(); i++) {
                tmc = mTmcs.get(j);
                endLatLng = mLatLngPaths.get(i);
                segment += AMapUtils.calculateLineDistance(startLatLng, endLatLng);
                if (segment > tmc.getDistance() + 1) {
                    double result = (AMapUtils.calculateLineDistance(startLatLng, endLatLng)
                            - (segment - tmc.getDistance()))
                            / AMapUtils.calculateLineDistance(startLatLng, endLatLng);
                    LatLng middleLatLng = new LatLng((endLatLng.latitude - startLatLng.latitude) * result + startLatLng.latitude
                            , (endLatLng.latitude - startLatLng.latitude) * result + startLatLng.latitude);
                    temp.add(middleLatLng);
                    startLatLng = middleLatLng;
                    i--;
                } else {
                    temp.add(endLatLng);
                    startLatLng = endLatLng;
                }
                if (segment >= tmc.getDistance() || i == mLatLngPaths.size() - 1) {
                    if (j == mTmcs.size() - 1 && i < mLatLngPaths.size() - 1) {
                        for (i++; i < mLatLngPaths.size(); i++) {
                            temp.add(mLatLngPaths.get(i));
                        }
                    }
                    j++;
                    if (TextUtils.equals(tmc.getStatus(), Constant.Map.ROAD_UNIMPEDED)) {
                        addPolyline(new PolylineOptions().addAll(temp).width(mRouteWidth).color(Color.GREEN));
                    } else if (TextUtils.equals(tmc.getStatus(), Constant.Map.ROAD_CRAWL)) {
                        addPolyline(new PolylineOptions().addAll(temp).width(mRouteWidth).color(Color.YELLOW));
                    } else if (TextUtils.equals(tmc.getStatus(), Constant.Map.ROAD_JAM)) {
                        addPolyline(new PolylineOptions().addAll(temp).width(mRouteWidth).color(Color.MAGENTA));
                    } else if (TextUtils.equals(tmc.getStatus(), Constant.Map.ROAD_SERIOUS_JAM)) {
                        addPolyline(new PolylineOptions().addAll(temp).width(mRouteWidth).color(Color.RED));
                    } else {
                        addPolyline(new PolylineOptions().addAll(temp).width(mRouteWidth).color(Color.CYAN));
                    }
                    temp.clear();
                    temp.add(startLatLng);
                    segment = 0;
                }
                if (i == mLatLngPaths.size() - 1) {
                    addPolyline(new PolylineOptions().add(endLatLng, mEndPoint).setDottedLine(true));
                }
            }
        } else {
            return;
        }
    }

    private void addPolyline(PolylineOptions options) {
        if (options != null) {
            Polyline line = mAMap.addPolyline(options);
            if (line != null) {
                mPolyLines.add(line);
            }
        } else {
            return;
        }
    }

    public void zoomToSpan() {
        if (mStartPoint != null && mAMap != null) {
            LatLngBounds.Builder builder = LatLngBounds.builder();
            builder.include(new LatLng(mStartPoint.latitude, mStartPoint.longitude));
            builder.include(new LatLng(mEndPoint.latitude, mEndPoint.longitude));
            if (mPassPoints != null && mPassPoints.size() > 0) {
                for (LatLonPoint point : mPassPoints) {
                    builder.include(new LatLng(point.getLatitude(), point.getLongitude()));
                }
            }
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
        }
    }
}
