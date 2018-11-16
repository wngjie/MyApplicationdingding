package testdd.wj.com.myapplicationdingding.SharePreferences;

/**
 * Created by jie on 2018/1/17.
 */

import android.content.SharedPreferences;
import android.graphics.Rect;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import testdd.wj.com.myapplicationdingding.TestApplication;


/**
 * 用于存储公用信息
 */

public  class MaintenanceSharePreferences {
    /**
     * 存储在 全局SharePerferences pileInfo文件的key
     */
    public static String KEY_RECTMAP="RectMap";


    /**
     * 值
     */
    public static  String mRectMap;

    public static void init(){
        SharedPreferences mSharePreferences = TestApplication.getmSharePreferences();
        mRectMap = mSharePreferences.getString(KEY_RECTMAP,"");
    }


    public static boolean setVal(String key, Object obj){
        SharedPreferences mSharePreferences = TestApplication.getmSharePreferences();
        SharedPreferences.Editor editor = mSharePreferences.edit();
        try {
            if (obj instanceof Integer) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof String) {
                editor.putString(key, (String) obj);
            } else if (obj instanceof Float) {
                editor.putFloat(key, (Float) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else if (obj instanceof Set) {
                editor.putStringSet(key, (Set) obj);
            }else{
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                editor.putString(key, json);
            }
            editor.commit();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

}
