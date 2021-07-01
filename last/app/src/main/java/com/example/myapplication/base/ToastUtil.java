package com.example.myapplication.base;

import android.content.Context;
import android.widget.Toast;

/**
 * Title:
 * Description:
 * <p>
 * Created by pei
 * Date: 2018/2/9
 */
public class ToastUtil {

    /**短吐司**/
    public static void shortShow(Context context, String msg){
        if(StringUtil.isNotEmpty(msg)){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
        }
    }
}
