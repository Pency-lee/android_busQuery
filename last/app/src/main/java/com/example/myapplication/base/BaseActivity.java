package com.example.myapplication.base;//package com.example.myapplication.base;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
///**
// * Title:
// * Description:
// * <p>
// * Created by pei
// * Date: 2018/2/9
// */
//public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
//
//    protected View mLayoutView;//总布局
//    protected Activity mContext;
//    private Unbinder mUnbinder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mContext = this;
//
//        if (getContentViewId() != 0) {
//            mLayoutView = LayoutInflater.from(mContext).inflate(getContentViewId(), null);
//            setContentView(mLayoutView);
//
//            mUnbinder = ButterKnife.bind(this);
//        }
//
//        initData();
//        setListener();
//        initPhotoError();
//    }
//
//    private void initPhotoError(){
//        // android 7.0系统解决拍照的问题
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
//    }
//
//    @Override
//    public void onClick(View v) {
//        DoubleClickUtil.shakeClick(v);
//    }
//
//    @Override
//    protected void onDestroy() {
//        mUnbinder.unbind();
//        super.onDestroy();
//    }
//
//    protected abstract int getContentViewId();
//    protected abstract void initData();
//    protected abstract void setListener();
//
//
//
//}
