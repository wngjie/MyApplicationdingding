package testdd.wj.com.myapplicationdingding;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;

import java.net.URLEncoder;

import testdd.wj.com.myapplicationdingding.SharePreferences.MaintenanceSharePreferences;

/**
 * Created by jie on 2018/9/25.
 */
public class TestApplication extends Application {
    public static final String pak="com.alibaba.android.rimet";
    public static final String SCHEME_DING_DING="dingtalk://dingtalkclient/page/link?url=" +
            URLEncoder.encode("https://attend.dingtalk.com/attend/index.html");
    public static Handler mHandler;
    public static Context mContext;
    private static SharedPreferences mSharePreferences; //全局SharePerferences pileInfo文件
    public static void setmHandler(Handler handler){
        mHandler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext==null){
            mContext = getApplicationContext();
        }
        if (mSharePreferences==null){
            getmSharePreferences();
        }
        MaintenanceSharePreferences.init();
    }
    public static SharedPreferences getmSharePreferences(){
        if (mSharePreferences==null){
            mSharePreferences = mContext.getSharedPreferences("data", MODE_PRIVATE); //初始化存储文档
        }
        return mSharePreferences;
    }
}
