package com.yjt.app.ui.widget;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.TMC;

import java.util.ArrayList;
import java.util.List;

public class CustomOverlay {

    private AMap mAMap;
    private LatLng mStartPoint;
    private LatLng mEndPoint;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private DrivePath mPath;
    private List<LatLonPoint> mPassPoints;
    private List<Marker> mPassMarkers = new ArrayList<Marker>();
    private boolean isPassMarkerVisible = true;
    private List<TMC> mTmcs;
    private PolylineOptions mOptions;
    private boolean isColor = true;
    private boolean isNodeIconVisible = true;
    private List<LatLng> mLatLngPaths;
    protected List<Marker> stationMarkers = new ArrayList<Marker>();
    protected List<Polyline> allPolyLines = new ArrayList<Polyline>();

}
