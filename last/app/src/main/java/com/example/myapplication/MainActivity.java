package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected int getContentViewId() {
        return R.layout.activity_main;
    }
    protected View mLayoutView;//总布局
    private List<Fragment> list;
    private String[] text={"路线查询","站点查询","公交查询"};
    private MyAdapter adapter;
    private int[] images={R.drawable.icon_route,R.drawable.icon_busstation,R.drawable.icon_bus};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cart:
                ShotShareUtil.shotShare(this);
                Toast.makeText(MainActivity.this,"分享",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myClickshareljy(View v) {
        Toast.makeText(getApplicationContext(), "你点击了分享的按钮", Toast.LENGTH_LONG).show();
        ShotShareUtil.shotShare(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewId() != 0) {
            mLayoutView = LayoutInflater.from(this).inflate(getContentViewId(), null);
            setContentView(mLayoutView);

        }
        setContentView(R.layout.activity_main);
        //设置TabLayout并初始化。
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab1=tabLayout.newTab().setCustomView(setView(R.drawable.w_sun,"晴天"));
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2=tabLayout.newTab().setCustomView(setView(R.drawable.w_cloud,"多云"));
        tabLayout.addTab(tab2);
        TabLayout.Tab tab3=tabLayout.newTab().setCustomView(setView(R.drawable.ic_directions_bus_grey600_18dp,"大雨"));
        tabLayout.addTab(tab3);
        //页面，数据源
        list = new ArrayList<>();
        list.add(new TabFragment_01());
        list.add(new TabFragment_02());
        list.add(new TabFragment_03());
        //设置ViewPager。
        ViewPager viewPager = findViewById(R.id.view_pager);
        adapter =new MyAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);

        for(int i=0;i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab=tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        initPhotoError();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private View setView(int drawableTd,String tabText){
        View view = View.inflate(this,R.layout.tab_view,null);
        ImageView iv = view.findViewById(R.id.iv);
        TextView tv = view.findViewById(R.id.tv);
        iv.setImageResource(drawableTd);
        tv.setText(tabText);
        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {
        private Context context;

        public MyAdapter(@NonNull FragmentManager fm,Context context) {
            super(fm);
            this.context=context;
        }



        @Override
        public int getCount() {
            return list.size();
        }

        public View getTabView(int position){
            View view= LayoutInflater.from(context).inflate(R.layout.tab_view,null);
            ImageView iv = view.findViewById(R.id.iv);
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(text[position]);
            iv.setImageResource(images[position]);
            return view;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return   list.get(position);
        }
    }
}

