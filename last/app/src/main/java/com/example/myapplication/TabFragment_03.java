package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.AMapException;

import java.util.List;

public class TabFragment_03 extends Fragment implements BusLineSearch.OnBusLineSearchListener,BusStationSearch.OnBusStationSearchListener, onClick,View.OnClickListener, setOnClickListener {

    public TabFragment_03() {
    }
    private View view;
    @Nullable

    Button searchByName;
    EditText searchName;
    private List<BusLineItem> lineItems = null;// 公交线路搜索返回的busline
    private List<BusStationItem> stationItems=null;
    private BusLineResult busLineResult;// 公交线路搜索返回的结果
    private BusStationResult busStationResult;
    private BusLineQuery busLineQuery;// 公交线路查询的查询类
    private BusStationQuery busStationQuery;
    private BusStationSearch busStationSearch;
    private ProgressDialog progDialog = null;// 进度框
    private BusLineSearch busLineSearch;// 公交线路列表查询
    private int currentpage = 0;// 公交搜索当前页，第一页从0开始
    String citycode="021";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.third_tab,container,false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchByName=(Button)getActivity().findViewById(R.id.searchByName);
        searchByName.setOnClickListener(this);
        searchName=(EditText)getActivity().findViewById(R.id.Buslinename);
    }


    public void showResultList(List<BusLineItem> busLineItems) {
        BusLineDialog busLineDialog = new BusLineDialog(getActivity(), busLineItems);
        busLineDialog.onListItemClicklistener(new OnListItemlistener() {
            @Override
            public void onListItemClick(BusLineDialog dialog,
                                        final BusLineItem item) {
                showProgressDialog();

                String lineId = item.getBusLineId();// 得到当前点击item公交线路id
                busLineQuery = new BusLineQuery(lineId, BusLineQuery.SearchType.BY_LINE_ID,
                        "021");// 第一个参数表示公交线路id，第二个参数表示公交线路id查询，第三个参数表示所在城市名或者城市区号
                BusLineSearch busLineSearch = new BusLineSearch(
                        getActivity(), busLineQuery);
                busLineSearch.setOnBusLineSearchListener(TabFragment_03.this);
                busLineSearch.searchBusLineAsyn();// 异步查询公交线路id
            }
        });
        busLineDialog.show();

    }
//    public void showStationList(List<BusStationItem> busStationItems){
//        BusStationDialog busStationDialog =new BusStationDialog(getActivity(),busStationItems);
//        busStationDialog.show();
//    }

    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索:\n");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    public void searchLine() {
        currentpage = 0;// 第一页默认从0开始
        showProgressDialog();
        String search = searchName.getText().toString().trim();
        if ("".equals(search)) {
            search = "641";
            searchName.setText(search);
        }
        busLineQuery = new BusLineQuery(search, BusLineQuery.SearchType.BY_LINE_NAME,
                "021");// 第一个参数表示公交线路名，第二个参数表示公交线路查询，第三个参数表示所在城市名或者城市区号
        busLineQuery.setPageSize(10);// 设置每页返回多少条数据
        busLineQuery.setPageNumber(currentpage);// 设置查询第几页，第一页从0开始算起
        busLineSearch = new BusLineSearch(getActivity(), busLineQuery);// 设置条件
        busLineSearch.setOnBusLineSearchListener(this);// 设置查询结果的监听
        busLineSearch.searchBusLineAsyn();// 异步查询公交线路名称
        // 公交站点搜索事例
        /*
         * BusStationQuery query = new BusStationQuery(search,cityCode);
         * query.setPageSize(10); query.setPageNumber(currentpage);
         * BusStationSearch busStationSearch = new BusStationSearch(this,query);
         * busStationSearch.setOnBusStationSearchListener(this);
         * busStationSearch.searchBusStationAsyn();
         */
    }
//    public void searchStation(){
//        currentpage=0;
//        showProgressDialog();;
//        String search=searchName.get
//    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void onBusStationSearched(BusStationResult busStationResult, int i) {

    }

 interface OnListItemlistener {
        void onListItemClick(BusLineDialog dialog, BusLineItem item);
//        public void onListItemClick(BusStationDialog dialog,BusStationItem item);
    }

    private class BusLineDialog extends Dialog implements View.OnClickListener {

        private List<BusLineItem> busLineItems;
        private BusLineAdapter busLineAdapter;
        private Button preButton, nextButton;
        private ListView listView;
        protected OnListItemlistener onListItemlistener;

        public BusLineDialog(Context context, int theme) {
            super(context, theme);
        }

        public void onListItemClicklistener(
                OnListItemlistener onListItemlistener) {
            this.onListItemlistener = onListItemlistener;

        }

        public BusLineDialog(Context context, List<BusLineItem> busLineItems) {
            this(context, android.R.style.Theme_NoTitleBar);
            this.busLineItems = busLineItems;
            busLineAdapter = new BusLineAdapter(context, busLineItems);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            WindowManager windowManager = getActivity().getWindowManager();
//            DisplayMetrics d = new DisplayMetrics(); // get the window's width and height
//            windowManager.getDefaultDisplay()
//            WindowManager.LayoutParams p = getWindow().getAttributes(); //get the current dialog attributes
//            p.height = (int) getWindowManager().get; // set height as the window's 0.9
//            p.width = (int) (d.getWidth() * 0.75); //  set width as the window's 0.75
//        getWindow().setAttributes(p);
            setContentView(R.layout.busline_dialog);
            preButton = (Button) findViewById(R.id.preButton);
            nextButton = (Button) findViewById(R.id.nextButton);
            listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(busLineAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    onListItemlistener.onListItemClick(BusLineDialog.this,
                            busLineItems.get(arg2));
                    dismiss();

                }
            });
            preButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            if (currentpage <= 0) {
                preButton.setEnabled(false);
            }
            if (currentpage >= busLineResult.getPageCount() - 1) {
                nextButton.setEnabled(false);
            }

        }

        @Override
        public void onClick(View v) {
            this.dismiss();
            if (v.equals(preButton)) {
                currentpage--;
            } else if (v.equals(nextButton)) {
                currentpage++;
            }
            showProgressDialog();
            busLineQuery.setPageNumber(currentpage);// 设置公交查询第几页
            busLineSearch.setOnBusLineSearchListener(TabFragment_03.this);
            busLineSearch.searchBusLineAsyn();// 异步查询公交线路名称
        }
    }

//    class BusStationDialog extends Dialog implements View.OnClickListener {
//
//        private List<BusStationItem> busStationItems;
//        private BusStationAdapter busStationAdapter;
//        private Button preButton, nextButton;
//        private ListView listView;
//        protected OnListItemlistener onListItemlistener;
//
//        public BusStationDialog(Context context, int theme) {
//            super(context, theme);
//        }
//
//        public void onListItemClicklistener(
//                OnListItemlistener onListItemlistener) {
//            this.onListItemlistener = onListItemlistener;
//
//        }
//
//        public BusStationDialog(Context context, List<BusStationItem> busStationItems) {
//            this(context, android.R.style.Theme_NoTitleBar);
//            this.busStationItems = busStationItems;
//            busStationAdapter = new BusStationAdapter(context, busStationItems);
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
////            WindowManager windowManager = getActivity().getWindowManager();
////            DisplayMetrics d = new DisplayMetrics(); // get the window's width and height
////            windowManager.getDefaultDisplay()
////            WindowManager.LayoutParams p = getWindow().getAttributes(); //get the current dialog attributes
////            p.height = (int) getWindowManager().get; // set height as the window's 0.9
////            p.width = (int) (d.getWidth() * 0.75); //  set width as the window's 0.75
////        getWindow().setAttributes(p);
//            setContentView(R.layout.busline_dialog);
//            preButton = (Button) findViewById(R.id.preButton);
//            nextButton = (Button) findViewById(R.id.nextButton);
//            listView = (ListView) findViewById(R.id.listview);
//            listView.setAdapter(busStationAdapter);
////            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////                @Override
////                public void onItemClick(AdapterView<?> arg0, View arg1,
////                                        int arg2, long arg3) {
//////                    onListItemlistener.onListItemClick(BusStationDialog.this,
//////                            busStationItems.get(arg2));
////                    dismiss();
////
////                }
////            });
//            preButton.setOnClickListener(this);
//            nextButton.setOnClickListener(this);
//            if (currentpage <= 0) {
//                preButton.setEnabled(false);
//            }
//            if (currentpage >= busStationItems.size() - 1) {
//                nextButton.setEnabled(false);
//            }
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            this.dismiss();
//            if (v.equals(preButton)) {
//                currentpage--;
//            } else if (v.equals(nextButton)) {
//                currentpage++;
//            }
//            showProgressDialog();
//           busStationQuery.setPageNumber(currentpage);// 设置公交查询第几页
//           busStationSearch.setOnBusStationSearchListener(TabFragment_03.this);
//           busStationSearch.searchBusStationAsyn();// 异步查询公交线路名称
//        }
//    }


    @Override
    public void onClick(View v) {
        searchLine();
    }
    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0
                            && result.getBusLines() != null
                            && result.getBusLines().size() > 0) {
                        busLineResult = result;
                        lineItems = result.getBusLines();
                        System.out.println(lineItems.get(0).getBusStations());
                        showResultList(lineItems);
                    }
                } else if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_ID) {
//                    lineItems = result.getBusLines();
//                    System.out.println(lineItems.get(0));
//                    System.out.println(lineItems.get(0).getBusCompany());
//                    stationItems=lineItems.get(0).getBusStations();
//                    System.out.println(stationItems.size());
//
//                    showStationList(stationItems);

//                    aMap.clear();// 清理地图上的marker
//                    busLineResult = result;
//                    lineItems = busLineResult.getBusLines();
//                    BusLineOverlay busLineOverlay = new BusLineOverlay(this,
//                            aMap, lineItems.get(0));
//                    busLineOverlay.removeFromMap();
//                    busLineOverlay.addToMap();
//                    busLineOverlay.zoomToSpan();
                }
            } else {

//                ToastUtil.show(BuslineActivity.this, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(BuslineActivity.this, rCode);
        }

    }

}
