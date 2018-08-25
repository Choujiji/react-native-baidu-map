package org.lovebing.reactnative.baidumap;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.ArrayList;

/**
 * Created by mac on 2018/8/25.
 */

public class BaiduCustomRouteSearch implements OnGetRoutePlanResultListener {
    private RoutePlanSearch mSearch = null;

    /** 路线规划接口 */
    public interface OnMyGetRoutePlanResultListener {
        /** 自行车路线规划回调 */
        public void onGetBikingRouteResult(Boolean success, BikingRouteResult result);
        /** 步行路线规划回调 */
        public void onGetWalkingRouteResult(Boolean success, WalkingRouteResult result);
        /** 公交路线规划回调 */
        public void onGetTransitRouteResult(Boolean success, TransitRouteResult result);
        /** 驾车路线规划回调 */
        public void onGetDrivingRouteResult(Boolean success, DrivingRouteResult result);
    }
    private  OnMyGetRoutePlanResultListener listerer = null;
    public void  setOnMyGetRoutePlanResultListener(OnMyGetRoutePlanResultListener listerer) {
        this.listerer = listerer;
    }

    /** 自行车模式 */
    public static final int BikingRoute = 0;
    /** 驾车模式 */
    public static final int DrivingRoute = 1;
    /** 公交模式 */
    public static final int TransitRoute = 2;
    /** 步行模式 */
    public static final int WalkingRoute = 3;

    /** 初始化路线规划对象 */
    public BaiduCustomRouteSearch() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    /**
     *
     * @param start     起始点
     * @param end       终点
     * @param passBy    经过点（只在driving模式下使用，其余传null）
     * @param mode     模式
     */
    public void SearchProcess(LatLng start, LatLng end, ArrayList<LatLng> passBy, int mode) {
        ArrayList<PlanNode> passByNodes = new ArrayList<PlanNode>();

        // 设置起始点终点信息
        PlanNode startNode = PlanNode.withLocation(start);
        PlanNode endNode = PlanNode.withLocation(end);
        if (passBy != null) {
            // 配置经过点信息
            for (int i = 0; i < passBy.size(); i++) {
                PlanNode passNode = PlanNode.withLocation(passBy.get(i));
                passByNodes.add(passNode);
            }
        }
        // 根据路线模式处理
        switch (mode) {
            case BikingRoute: {
                BikingRoutePlanOption option = new BikingRoutePlanOption()
                        .from(startNode)
                        .to(endNode);
                mSearch.bikingSearch(option);
                break;
            }
            case DrivingRoute: {
                DrivingRoutePlanOption option = new DrivingRoutePlanOption()
                        .from(startNode)
                        .to(endNode);
                mSearch.drivingSearch(option);
                break;
            }
            case TransitRoute: {
                TransitRoutePlanOption option = new TransitRoutePlanOption()
                        .from(startNode)
                        .to(endNode);
                mSearch.transitSearch(option);
                break;
            }
            case WalkingRoute: {
                WalkingRoutePlanOption option = new WalkingRoutePlanOption()
                        .from(startNode)
                        .to(endNode);
                mSearch.walkingSearch(option);
                break;
            }
            default:
                break;

        }
    }

    // 实现接口回调（result即为路线结果）

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if ((bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR)
                && bikingRouteResult != null) {
            // 成功
            if (this.listerer == null) {
                return;
            }
            this.listerer.onGetBikingRouteResult(true, bikingRouteResult);
            return;
        }
        // 失败
        this.listerer.onGetBikingRouteResult(false, null);
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if ((drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR)
                && drivingRouteResult != null) {
            // 成功
            if (this.listerer == null) {
                return;
            }
            this.listerer.onGetDrivingRouteResult(true, drivingRouteResult);
            return;
        }
        // 失败
        this.listerer.onGetDrivingRouteResult(false, null);
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        if ((transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR)
                && transitRouteResult != null) {
            // 成功
            if (this.listerer == null) {
                return;
            }
            this.listerer.onGetTransitRouteResult(true, transitRouteResult);
            return;
        }
        // 失败
        this.listerer.onGetTransitRouteResult(false, null);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if ((walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR)
                && walkingRouteResult != null) {
            // 成功
            if (this.listerer == null) {
                return;
            }
            this.listerer.onGetWalkingRouteResult(true, walkingRouteResult);
            return;
        }
        // 失败
        this.listerer.onGetWalkingRouteResult(false, null);
    }
}
