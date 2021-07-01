package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.base.AppContext;
import com.example.myapplication.base.LogUtil;
import com.example.myapplication.base.StringUtil;
import com.example.myapplication.base.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShotShareUtil {

    /**截屏分享，供外部调用**/
    public static void shotShare(Context context){
        //截屏
        String path=screenShot(context);
        //分享
        if(StringUtil.isNotEmpty(path)){
            ShareImage(context,path);
        }
    }

    /**获取截屏**/
    private static String screenShot(Context context){
        String imagePath=null;
        Bitmap bitmap= snapShotWithoutStatusBar(context);
        if(bitmap!=null){
            try {
                // 图片文件路径
                imagePath = getDiskCachePath()+"share.png";
                LogUtil.i("====imagePath====" + imagePath);
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                return imagePath;
            } catch (Exception e) {
                LogUtil.e("====screenshot:error====" + e.getMessage());
            }
        }
        return null;
    }

    /**分享**/
    private static void ShareImage(Context context, String imagePath){
        if (imagePath != null){
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            File file = new File(imagePath);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));// 分享的内容
            intent.setType("image/*");// 分享发送的数据类型
            Intent chooser = Intent.createChooser(intent, "Share screen shot");
            if(intent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(chooser);
            }
        } else {
            ToastUtil.shortShow(context,"先截屏，再分享");
        }
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     **/
    private static Bitmap snapShotWithoutStatusBar(Context context) {
        View view = ((Activity) context).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) AppContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        display.getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }


    /**获取缓存路径(内存不足时数据会消失，应用删除的时候，数据会被清理掉)**/
    private static String getDiskCachePath() {
        String cachePath = null;
        //手机内部缓存路径
        //------/data/user/0/package_name/cache/
        String innerPath = AppContext.getInstance().getCacheDir().getAbsolutePath() + File.separator;
        if (isSdcardExist()) {
            File cacheFile = AppContext.getInstance().getExternalCacheDir();
            if (cacheFile != null) {
                //sdcard上缓存路径
                //--------/storage/emulated/0/Android/data/package_name/cache/
                String ousidePath = cacheFile.getAbsolutePath() + File.separator;
                if (usefulFilePath(ousidePath)) {
                    cachePath = ousidePath;
                } else {
                    //若此路径下无法执行读写,则是android4.4系统为防止外置sdk存储混乱，不好管理，禁用了外置sdk的读写
                    cachePath = innerPath;
                }
            } else {
                //AppContext.getInstance().getExternalCacheDir()可能为空，原因是sdcard死锁，需要重启手机
                cachePath = innerPath;
            }
        }else{
            cachePath = innerPath;
        }
        return cachePath;
    }

    private static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**判断文件路径的有效性**/
    private static boolean usefulFilePath(String path) {
        if (StringUtil.isNotEmpty(path)) {
            String tempPath = path + "demo.txt";
            File file = new File(tempPath);
            if (file.exists()) {
                return true;
            } else {
                try {
                    if (file.createNewFile()) {
                        file.delete();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
