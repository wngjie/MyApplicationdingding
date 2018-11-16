package testdd.wj.com.myapplicationdingding;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jie on 2018/9/25.
 */
public class LongRunningService extends Service {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override

    public IBinder onBind(Intent intent) {
        return null;

    }


    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AlarmReceiver", "开始启动钉钉时间：" + new Date().
                toString());
//                String pak=TestApplication.pak;
//                Intent intent1 = getPackageManager().getLaunchIntentForPackage(pak);
//                if (intent1 != null) {
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent1);
//                }


        int zt = (int) (Math.random() * 1000 * 60);

        Date d;
        String action="";
        if (intent.hasExtra("action")) {
            action = intent.getStringExtra("action");
        }
        if (!action.equals("com.android.test")) {
            try {
                String s = simpleDateFormat.format(new Date());
                if (s.substring(11, 13).equals(TestApplication.mContext.getString(R.string.shangbanshijian))) {
                    s = s.substring(0, 11) + TestApplication.mContext.getString(R.string.shangbanshijian) + ":59:00";
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
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

            String pak=TestApplication.pak;
                Intent intent1 = getPackageManager().getLaunchIntentForPackage(pak);
                if (intent1 != null) {
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 1000;   // 这是一分钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
//        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + zt, pi);

        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 判断某个APP是否已经运行
     * @param context
     * @param packageName
     * @return true 表示正在运行，false表示没有运行
     */


}