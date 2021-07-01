package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

public class TabFragment_01 extends Fragment implements GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener, RouteSearch.OnRouteSearchListener {
    public TabFragment_01() {
    }
    EditText ed_start;
    EditText ed_end;
    Button btn;
    Button btn2;
    private View view;
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch_start;
    private GeocodeSearch geocoderSearch_end;
    private RouteSearch mRouteSearch;
    private final int ROUTE_TYPE_BUS = 1;
    private LatLonPoint[] latLonPoints=new LatLonPoint[100];
    private int position=0;
    private String mCurrentCityName = "上海";
    private BusRouteResult mBusRouteResult;
    private ListView mBusResultList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.first_tab,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ed_start=getActivity().findViewById(R.id.startposition);
        ed_end=getActivity().findViewById(R.id.endposition);
        btn=getActivity().findViewById(R.id.searchBystartandend);
        btn2=getActivity().findViewById(R.id.searchRoute);
        geocoderSearch_start = new GeocodeSearch(getActivity());
        geocoderSearch_end = new GeocodeSearch(getActivity());
        geocoderSearch_start.setOnGeocodeSearchListener(this);
        geocoderSearch_end.setOnGeocodeSearchListener(this);
        btn.setOnClickListener(this);
//        latLonPoints[position]=new LatLonPoint(0,0);
//        latLonPoints[position+1]=new LatLonPoint(0,0);

        progDialog = new ProgressDialog(getActivity());

        btn2.setOnClickListener(this);
        mBusResultList=getActivity().findViewById(R.id.list_view);
        mRouteSearch=new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {


        dismissDialog();
        if (result != null && result.getGeocodeAddressList() != null
                && result.getGeocodeAddressList().size() > 0) {
            GeocodeAddress address_start = result.getGeocodeAddressList().get(0);
//                GeocodeAddress address_end = result.getGeocodeAddressList().get(0);
            latLonPoints[position]=address_start.getLatLonPoint();

            System.out.println("经纬度值:" + latLonPoints[position] );
            position++;
//                System.out.println("经纬度值:" + address_end.getLatLonPoint() );
        } else {
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                latLonPoints[position-1], latLonPoints[position-2]);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 响应地理编码按钮
             */
            case R.id.searchBystartandend:
//                Toast.makeText(getActivity(),ed_start.getText().toString(),Toast.LENGTH_LONG).show();
                getLatlon(ed_start.getText().toString(),ed_end.getText().toString());
                break;
            case R.id.searchRoute:
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
            default:
                break;
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name_start,final String name_end) {
        showDialog();

        GeocodeQuery query_start = new GeocodeQuery(name_start, "021");
        GeocodeQuery query_end = new GeocodeQuery(name_end, "021");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch_start.getFromLocationNameAsyn(query_start);
        geocoderSearch_end.getFromLocationNameAsyn(query_end);// 设置同步地理编码请求
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }
    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }



    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    System.out.println(result);
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(getActivity(), mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                }
            } else {
            }
        } else {
        }

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    //    private Button searchByName=view.findViewById(R.id.searchBystartandend);
//    private EditText startPositon=view.findViewById(R.id.startposition);
//    private EditText endPosition=view.findViewById(R.id.endposition);


}
