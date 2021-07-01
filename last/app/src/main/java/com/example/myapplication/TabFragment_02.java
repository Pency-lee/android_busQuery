package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.AMapException;

import java.util.ArrayList;
import java.util.List;

public class TabFragment_02 extends Fragment implements BusStationSearch.OnBusStationSearchListener, onClick,View.OnClickListener, setOnClickListener {

    public TabFragment_02() {
    }

    private View view;
    @Nullable

    Button searchByName;
    EditText searchName;
    private AMap aMap;
    private MapView mapView;
    private BusStationResult busStationResult;
    private int currentpage = 0;// 公交搜索当前页，第一页从0开始
    private ProgressDialog progDialog = null;// 进度框
    private Spinner selectCity;// 选择城市下拉列表
    private String[] itemCitys = { "北京-010", "郑州-0371", "上海-021" };
    private String cityCode = "021";// 城市区号
    private BusStationQuery busLineQuery;// 公交线路查询的查询类
    private BusStationSearch busLineSearch;// 公交线路列表查询

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.second_tab,container,false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchByName=(Button)getActivity().findViewById(R.id.stationsearch);
        searchByName.setOnClickListener(this);
        searchName=(EditText)getActivity().findViewById(R.id.stationname);
    }


    public void showResultList(List<BusStationItem> busStationItems) {
        BusStationDialog busStationDialog =new BusStationDialog(getActivity(),busStationItems);
        busStationDialog.onListItemClicklistener(new OnListItemlistener() {
            @Override
            public void onListItemClick(BusStationDialog dialog,
                                        final BusStationItem item) {
                List<BusLineItem> busLineItems=item.getBusLineItems();
                showLineList(busLineItems);
            }
        });
        busStationDialog.show();
    }

    public void showLineList(List<BusLineItem> busLineItems){
        BusLineDialog busLineDialog=new BusLineDialog(getActivity(),busLineItems);
        busLineDialog.show();
    }
//    public void showStationList(List<BusStationItem> busStationItems){
//        TabFragment_03.BusStationDialog busStationDialog =new TabFragment_02.BusStationDialog(getActivity(),busStationItems);
//        busStationDialog.show();
//    }

    interface OnListItemlistener {
        void onListItemClick(BusStationDialog dialog, BusStationItem item);
    }

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
        // currentpage = 0;// 第一页默认从0开始
        showProgressDialog();
        String search = searchName.getText().toString().trim();
        if ("".equals(search)) {
            search = "望京";
            searchName.setText(search);
        }
        busLineQuery = new BusStationQuery(search, "021");// 第一个参数表示公交线路名，第二个参数表示公交线路查询，第三个参数表示所在城市名或者城市区号
//		busLineQuery.setPageSize(10);// 设置每页返回多少条数据
        // busLineQuery.setPageNumber(currentpage);// 设置查询第几页，第一页从0开始算起
        busLineSearch = new BusStationSearch(getActivity(), busLineQuery);// 设置条件
        busLineSearch.setOnBusStationSearchListener(this);// 设置查询结果的监听

        busLineSearch.setQuery(new BusStationQuery(search,cityCode));
        busLineSearch.searchBusStationAsyn();
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




//    interface OnListItemlistener {
//        public void onListItemClick(TabFragment_02.BusStationDialog dialog, BusLineItem item);
////        public void onListItemClick(BusStationDialog dialog,BusStationItem item);
//    }

//    class BusLineDialog extends Dialog implements View.OnClickListener {
//
//        private List<BusLineItem> busLineItems;
//        private BusStationAdapter busLineAdapter;
//        private Button preButton, nextButton;
//        private ListView listView;
//        protected TabFragment_03.OnListItemlistener onListItemlistener;
//
//        public BusLineDialog(Context context, int theme) {
//            super(context, theme);
//        }
//
//        public void onListItemClicklistener(
//                TabFragment_03.OnListItemlistener onListItemlistener) {
//            this.onListItemlistener = onListItemlistener;
//
//        }
//
//        public BusLineDialog(Context context, List<BusLineItem> busLineItems) {
//            this(context, android.R.style.Theme_NoTitleBar);
//            this.busLineItems = busLineItems;
//            busLineAdapter = new BusLineAdapter(context, busLineItems);
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
//            listView.setAdapter(busLineAdapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1,
//                                        int arg2, long arg3) {
//                    onListItemlistener.onListItemClick(TabFragment_02.BusLineDialog.this,
//                            busLineItems.get(arg2));
//                    dismiss();
//
//                }
//            });
//            preButton.setOnClickListener(this);
//            nextButton.setOnClickListener(this);
//            if (currentpage <= 0) {
//                preButton.setEnabled(false);
//            }
//            if (currentpage >= busStationResult.getPageCount() - 1) {
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
//            busLineQuery.setPageNumber(currentpage);// 设置公交查询第几页
//            busLineSearch.setOnBusStationSearchListener(TabFragment_02.this);
//            busLineSearch.searchBusStationAsyn();// 异步查询公交线路名称
//        }
//    }

    private class BusStationDialog extends Dialog implements View.OnClickListener {

        private List<BusStationItem> busStationItems;
        private BusStationAdapter busStationAdapter;
        private Button preButton, nextButton;
        private ListView listView;
        protected OnListItemlistener onListItemlistener;

        public BusStationDialog(Context context, int theme) {
            super(context, theme);
        }


        public void onListItemClicklistener(OnListItemlistener onListItemlistener) {
            this.onListItemlistener = onListItemlistener;
        }

        public BusStationDialog(Context context, List<BusStationItem> busStationItems) {
            this(context, android.R.style.Theme_NoTitleBar);
            this.busStationItems = busStationItems;
            busStationAdapter = new BusStationAdapter(context, busStationItems);
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
            listView.setAdapter(busStationAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    onListItemlistener.onListItemClick(BusStationDialog.this,
                            busStationItems.get(arg2));
                    dismiss();

                }
            });
            preButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            if (currentpage <= 0) {
                preButton.setEnabled(false);
            }
            if (currentpage >= busStationItems.size() - 1) {
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
            busLineSearch.setOnBusStationSearchListener(TabFragment_02.this);
            busLineSearch.searchBusStationAsyn();// 异步查询公交线路名称
        }
    }



    private class BusLineDialog extends Dialog  {

        private List<BusLineItem> busLineItems;
        private BusLineAdapter busLineAdapter;
        private Button preButton, nextButton;
        private ListView listView1;

        public BusLineDialog(Context context, int theme) {
            super(context, theme);
        }


        public BusLineDialog(Context context, List<BusLineItem> busLineItems) {
            this(context, android.R.style.Theme_NoTitleBar);
            this.busLineItems = busLineItems;
            busLineAdapter = new BusLineAdapter(context, this.busLineItems);
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
            listView1 = (ListView) findViewById(R.id.listview);
            listView1.setAdapter(busLineAdapter);


        }

    }


    @Override
    public void onClick(View v) {
        searchLine();
    }
//    @Override
//    public void onBusLineSearched(BusLineResult result, int rCode) {
//        dissmissProgressDialog();
//        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
//            if (result != null && result.getQuery() != null
//                    && result.getQuery().equals(busLineQuery)) {
//                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
//                    if (result.getPageCount() > 0
//                            && result.getBusLines() != null
//                            && result.getBusLines().size() > 0) {
//                        busLineResult = result;
//                        lineItems = result.getBusLines();
//                        System.out.println(lineItems.get(0).getBusStations());
//                        showResultList(lineItems);
//                    }
//                } else if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_ID) {
//                }
//            } else {
//            }
//        } else {
//        }
//
//    }
    @Override
    public void onBusStationSearched(BusStationResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPageCount() > 0
                    && result.getBusStations() != null
                    && result.getBusStations().size() > 0) {
                List<BusStationItem> item = (List<BusStationItem>) result
                        .getBusStations();
                System.out.println("成功");
                System.out.println(item.get(0).getBusLineItems());
                showResultList(item);
//                StringBuffer buf = new StringBuffer();
//                for (int i = 0; i < item.size(); i++) {
//                    buf.append(" station: ").append(i).append(" name: ")
//                            .append(item.get(i).getBusStationName());
//                    Log.d("LG", "stationName:"
//                            + item.get(i).getBusStationName() + "stationpos:"
//                            + item.get(i).getLatLonPoint().toString());
//                }
//                String text = buf.toString();
//                Toast.makeText(BusStationActivity.this, text,
//                        Toast.LENGTH_SHORT).show();
            } else {

            }
        } else  {

        }
    }
}
