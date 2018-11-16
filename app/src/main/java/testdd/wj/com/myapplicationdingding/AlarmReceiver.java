package testdd.wj.com.myapplicationdingding;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jie on 2018/9/25.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String action = intent.getAction();
        Log.i("AlarmReceiver",""+action);
        Intent intent1=new Intent(TestApplication.mContext,LongRunningService.class);
        intent1.putExtra("action",action);
        if(action!=null && action.equals("com.android.test")){ //测试广播
            Message message = new Message();
            message.what=1;
            Bundle bundle = new Bundle();
            bundle.putString("AlarmReceiver","测试广播--时间是:"+simpleDateFormat.format(new Date()));
            message.setData(bundle);
            fasongxiaoxi(message);
            //你想要实现的代码
            Message message1 = new Message();;
            int zt =1000;
            Bundle bundle1 = new Bundle();
            bundle1.putString("AlarmReceiver","计划时间:"+simpleDateFormat.format(new Date()));
            bundle1.putLong("dataLong",zt);
            message1.what=2;
            message1.setData(bundle1);
            fasongxiaoxi(message1);
        }else {
            Log.i("AlarmReceiver", "广播来了.......");
            Log.i("AlarmReceiver", "当前时间： " + new Date().
                    toString());
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("AlarmReceiver", "广播来了--时间是:" + simpleDateFormat.format(new Date()));
            message.setData(bundle);
            fasongxiaoxi(message);
                /*
                int zt = (int) (Math.random() * 1000 * 60);

                Date d;
                try {
                    String s = simpleDateFormat.format(new Date());
                    s = s.substring(0, 11) + "08:59:00";
                    d = simpleDateFormat.parse(s); //上班时间
                    Log.i("AlarmReceiver", "上班时间:" + s);
                    long dTemp = d.getTime();
//                Date dd = simpleDateFormat.parse(new Date().
//                        toString());
                    long ddTemp = new Date().getTime();
                    boolean f = true;
                    Message message1 = new Message();
                    ;
                    while (f) {
                        if (dTemp - (ddTemp + zt) < 0) {
                            Date date = new Date(ddTemp + zt);
//                        Log.i("MainActivity:","生成的暂停时间："+zt);
                            Log.i("MainActivity:", "计划时间不对:" + simpleDateFormat.format(date));
                            zt = (int) (Math.random() * 1000 * 60);


                        } else {
                            Date date = new Date(ddTemp + zt);
                            Log.i("MainActivity:", "计划时间正确:" + simpleDateFormat.format(date));
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("AlarmReceiver", "计划时间:" + simpleDateFormat.format(date));
                            bundle1.putLong("dataLong", zt);
                            message1.what = 2;
                            message1.setData(bundle1);
                            f = false;
                        }
                    }
                    fasongxiaoxi(message1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i("AlarmReceiver", "暂停时间： " + zt);
                */

        }


        TestApplication.mContext.startService(intent1);
    }
    public void fasongxiaoxi(Message message){
        if (TestApplication.mHandler!=null){
            TestApplication.mHandler.handleMessage(message);
        }

    }
}
