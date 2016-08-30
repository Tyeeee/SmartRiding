package com.yjt.app.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.LogUtil;


public class MapActivity extends BaseActivity implements View.OnClickListener, AMapNaviListener, AMapNaviViewListener, GeocodeSearch.OnGeocodeSearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void permissionRequestIntent() {

    }

    @Override
    protected void permissionRequestResult() {

    }

    @Override
    protected void endOperation() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInitNaviFailure() {
        LogUtil.print("------onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("------onInitNaviSuccess");
    }

    @Override
    public void onStartNavi(int i) {
        LogUtil.print("------onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        LogUtil.print("------onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        LogUtil.print("------onLocationChange");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        LogUtil.print("------onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        LogUtil.print("------onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        LogUtil.print("------onArriveDestination");
    }

    @Override
    public void onCalculateRouteSuccess() {
        LogUtil.print("------onCalculateRouteSuccess");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        LogUtil.print("------onCalculateRouteFailure");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        LogUtil.print("------onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        LogUtil.print("------onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        LogUtil.print("------onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        LogUtil.print("------onGpsOpenStatus");
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        LogUtil.print("------onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        LogUtil.print("------onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        LogUtil.print("------showCross");
    }

    @Override
    public void hideCross() {
        LogUtil.print("------hideCross");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        LogUtil.print("------showLaneInfo");
    }

    @Override
    public void hideLaneInfo() {
        LogUtil.print("------hideLaneInfo");
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {
        LogUtil.print("------onCalculateMultipleRoutesSuccess");
    }

    @Override
    public void notifyParallelRoad(int i) {
        LogUtil.print("------notifyParallelRoad");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        LogUtil.print("------updateAimlessModeStatistics");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        LogUtil.print("------updateAimlessModeCongestionInfo");
    }

    @Override
    public void onNaviSetting() {
        LogUtil.print("------onNaviSetting");
    }

    @Override
    public void onNaviCancel() {
        LogUtil.print("------onNaviCancel");
    }

    @Override
    public boolean onNaviBackClick() {
        LogUtil.print("------onNaviBackClick");
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {
        LogUtil.print("------onNaviMapMode");
    }

    @Override
    public void onNaviTurnClick() {
        LogUtil.print("------onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {
        LogUtil.print("------onNextRoadClick");
    }

    @Override
    public void onScanViewButtonClick() {
        LogUtil.print("------onScanViewButtonClick");
    }

    @Override
    public void onLockMap(boolean b) {
        LogUtil.print("------onLockMap");
    }

    @Override
    public void onNaviViewLoaded() {
        LogUtil.print("------onNaviViewLoaded");
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtil.print("------onRegeocodeSearched");
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtil.print("------onGeocodeSearched");
    }
}
