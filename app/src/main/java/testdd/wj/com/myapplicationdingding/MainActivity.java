package testdd.wj.com.myapplicationdingding;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import testdd.wj.com.myapplicationdingding.SharePreferences.MaintenanceSharePreferences;
import util.DensityUtil;

public class MainActivity extends AppCompatActivity {
    String pak = "com.alibaba.android.rimet";
    private Button mButton;
    private LinearLayout mLogLayout;
    private Button mPeizhi;
    private Button kaiqu;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    AccessibilitySampleService mAccessibilitySampleService;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TextView textView = new TextView(MainActivity.this);
            Bundle bundle;
            switch (msg.what) {
                case 1:
                    bundle = msg.getData();
                    textView.setText(bundle.getString("AlarmReceiver"));
                    mLogLayout.addView(textView);
                    break;
                case 2:

                    bundle = msg.getData();
                    long zt = bundle.getLong("dataLong");
                    String dateString = bundle.getString("AlarmReceiver");
                    Intent intent = getPackageManager().getLaunchIntentForPackage(TestApplication.pak);

//                    Intent intent = new Intent(Intent.ACTION_VIEW); //跳到钉钉打卡上班的页面
//                    intent.setData(Uri.parse(TestApplication.SCHEME_DING_DING));

                    if (intent != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AlarmManager manager = (AlarmManager) TestApplication.mContext.getSystemService(TestApplication.mContext.ALARM_SERVICE);
                        long triggerAtTime = SystemClock.elapsedRealtime() + zt;
                        Date date = new Date(System.currentTimeMillis() + zt);
//                    textView.setText("时间-------:"+dateString);
                        textView.setText("启动时间-------:" + simpleDateFormat.format(date));
                        mLogLayout.addView(textView);
                        if (intent != null) {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            return;
                        }
//
                        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                        manager.set(AlarmManager.RTC, System.currentTimeMillis() + zt, pi);



                    }
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.b1);
        mLogLayout = findViewById(R.id.log_layout);
        mPeizhi = findViewById(R.id.peizhi);
        TestApplication.setmHandler(mHandler);
        kaiqu = findViewById(R.id.kaiqu);
        StringBuffer buffer = new StringBuffer();
        TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectLeft);
        //考勤
        buffer.append("6px转成dp"+ DensityUtil.px2dp(MainActivity.this,6));
        buffer.append("\n954px转成dp"+ DensityUtil.px2dp(MainActivity.this,954));
        buffer.append("\n273px转成dp"+ DensityUtil.px2dp(MainActivity.this,273));
        buffer.append("\n1176px转成dp"+ DensityUtil.px2dp(MainActivity.this,1176));
        //上班
        buffer.append("\n-------\n357px转成dp"+ DensityUtil.px2dp(MainActivity.this,357));
        buffer.append("\n597px转成dp"+ DensityUtil.px2dp(MainActivity.this,597));
        buffer.append("\n723px转成dp"+ DensityUtil.px2dp(MainActivity.this,723));
        buffer.append("\n966px转成dp"+ DensityUtil.px2dp(MainActivity.this,966));
//[357,930][723,1299]
        //下班
        buffer.append("\n-------\n357px转成dp"+ DensityUtil.px2dp(MainActivity.this,357));
        buffer.append("\n930px转成dp"+ DensityUtil.px2dp(MainActivity.this,930));
        buffer.append("\n723px转成dp"+ DensityUtil.px2dp(MainActivity.this,723));
        buffer.append("\n1299px转成dp"+ DensityUtil.px2dp(MainActivity.this,1299));
        //工作 [576,2201][864,2412]
        buffer.append("\n-------\n576px转成dp"+ DensityUtil.px2dp(MainActivity.this,575));
        buffer.append("\n2201px转成dp"+ DensityUtil.px2dp(MainActivity.this,2200));
        buffer.append("\n865px转成dp"+ DensityUtil.px2dp(MainActivity.this,865));
        buffer.append("\n2412px转成dp"+ DensityUtil.px2dp(MainActivity.this,2410));
        TextView textView1 = new TextView(MainActivity.this);
        textView1.setText(buffer.toString());
        mLogLayout.addView(textView1);



//        Settings.Secure.putString(TestApplication.mContext.getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,"testdd.wj.com.myapplicationdingding/testdd.wj.com.myapplicationdingding.AccessibilitySampleService");
//        Settings.Secure.putInt(TestApplication.mContext.getContentResolver(),Settings.Secure.ACCESSIBILITY_ENABLED,1);
        mButton.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.android.test");
                intent.setPackage("testdd.wj.com.myapplicationdingding");
                sendBroadcast(intent);
            }
        });

        mPeizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryFilterAppInfo()){
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText("钉钉打卡已经安装...继续检查辅助程序有没有开启");
                    mLogLayout.addView(textView);
                    if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(MainActivity.this,
                            AccessibilitySampleService.class.getName())) {// 判断服务是否开启
                        TextView textView1 = new TextView(MainActivity.this);
                        textView1.setText("辅助程序没有开启...请在 设置-->辅助功能-->无障碍 中开启"+getString(R.string.accessibility_tip));
                        mLogLayout.addView(textView1);

                    }

                };
            }
        });
        kaiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAccessibilitySettingHelper.jumpToSettingPage(MainActivity.this);// 跳转到开启页面
            }
        });


    }

    /**
     * 检查钉钉是否安装 真-安装 假-没有安装
     * @return
     */
    private boolean queryFilterAppInfo() {
        PackageManager pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        List<ApplicationInfo> applicationInfos = new ArrayList<>();

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages = new HashSet();
        for (ResolveInfo resolveInfo : resolveinfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }

        for (ApplicationInfo app : appInfos) {
//            if((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)//通过flag排除系统应用，会将电话、短信也排除掉
//            {
//                applicationInfos.add(app);
//            }
//            if(app.uid > 10000){//通过uid排除系统应用，在一些手机上效果不好
//                applicationInfos.add(app);
//            }
            if (allowPackages.contains(app.packageName)) {
                if (app.packageName!=null && !app.packageName.isEmpty()){
                    if (app.packageName.equals(TestApplication.pak)){
                        return true;
                    }
                }
                Log.i("APP:", "包名称:" + app.packageName);
                applicationInfos.add(app);
            }
        }
        return false;
    }


    public boolean getInspectInstallAPP(Context context) { //检查安装的APP 有没有钉钉软件 有返回真,没有假
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        /**
         * packageName：包名。
         versionCode：版本号
         versionName：版本名。
         firstInstallTime：首次安装时间。
         lastUpdateTime：最后一次覆盖安装时间。
         */
        for (PackageInfo p : packageInfos) {
            if (p != null) {
                if (p.packageName.equals(TestApplication.pak)){
                    return true;
                }
                Date date = new Date(p.firstInstallTime);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.i("获取APP包名:", "" + p.packageName);
                Log.i("获取APP版本号:", "" + p.versionCode);
                Log.i("获取APP版本名:", "" + p.versionName);
                Log.i("获取APP首次安装时间:", "" + sd.format(date));
                Log.i("获取APP最后一次覆盖安装时间:", "" + p.lastUpdateTime);
            }

        }
        return false;
    }
}
