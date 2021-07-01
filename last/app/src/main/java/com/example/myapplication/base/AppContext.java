package com.example.myapplication.base;

import android.app.Application;

/**
 * 在 mainfast 文件中注册
 * <application
 *   android:name="com.example.textexception.AppContext"
 *   android:allowBackup="true"
 *   .....
 *   ></application>
 * 
 * @author pei
 */
public class AppContext extends Application {

	private static AppContext instance;

	public static synchronized AppContext getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
//		//bugly调试,第三个参数为SDK调试模式开关,建议在测试阶段建议设置成true，发布时设置为false
//		CrashReport.initCrashReport(getApplicationContext(), Constents.BUGLY_APP_ID, BuildConfig.DEBUG);


	}

}
