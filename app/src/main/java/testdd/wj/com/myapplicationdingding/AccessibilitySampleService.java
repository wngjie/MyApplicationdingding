package testdd.wj.com.myapplicationdingding;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testdd.wj.com.myapplicationdingding.SharePreferences.MaintenanceSharePreferences;
import util.DensityUtil;

/**
 * Created by jie on 2018/10/24.
 */
public class AccessibilitySampleService extends AccessibilityService {
    public String TAG="AccessibilitySampleService";
    String workResId = "com.alibaba.android.rimet:id/home_bottom_tab_button_work"; //工作按钮的ID
    String toolbar ="com.alibaba.android.rimet:id/toolbar"; //打卡上面的按钮;
    Map<String,Rect> controlRect=new HashMap<String,Rect>();
    private static final String       TEXT_FORCE_STOP = "强行停止";
    private static final String       TEXT_DETERMINE  = "确定";
    private static final CharSequence NAME_APP_DETAILS  = "com.android.settings.applications.InstalledAppDetailsTop";
    private static final CharSequence NAME_ALERT_DIALOG = "android.app.AlertDialog";
    public static final String pak="com.alibaba.android.rimet";
    public static final String settings="com.android.settings";
    public static final String thispak="testdd.wj.com.myapplicationdingding";
    public static  final  int SLEEPTIME=5000;


    public volatile int  pageState=-1; //0没有打开 1 首页 2 工作页面 3考勤页面 4打卡上班页面
    private Rect kaoqindakaRect;
    private Rect shangbandakaRect;
    private Rect xiabandakaRect;
    private Rect gongzuoRect;
    public volatile String mState="等待打卡";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public volatile int frequency=0;


    //getRootInActiveWindow(); 刷新节点信息
    /**
     * com.alibaba.android.rimet:id/home_bottom_tab_root 主页下的导航拦
     * com.alibaba.android.rimet:id/home_bottom_tab_button_work 工作按钮
     * com.alibaba.android.rimet:id/webview_frame 考勤打卡的页面
     */

    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG,"onServiceConnected()");
        TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectLeft);
        kaoqindakaRect = new Rect((int)TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectLeft),
                (int)TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectTop)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectRight)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.kaoqindakaRectBottom));
        shangbandakaRect = new Rect((int)TestApplication.mContext.getResources().getDimension(R.dimen.shangbandakaRectLeft),
                (int)TestApplication.mContext.getResources().getDimension(R.dimen.shangbandakaRectTop)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.shangbandakaRectRight)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.shangbandakaRecttBottom));
        xiabandakaRect = new Rect((int)TestApplication.mContext.getResources().getDimension(R.dimen.xiabandakaRectLeft),
                (int)TestApplication.mContext.getResources().getDimension(R.dimen.xiabandakaRectTop)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.xiabandakaRectRight)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.xiabandakaRectBottom));
        gongzuoRect = new Rect((int)TestApplication.mContext.getResources().getDimension(R.dimen.gongzuoRectLeft),
                (int)TestApplication.mContext.getResources().getDimension(R.dimen.gongzuoRectTop)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.gongzuoRectRight)
                ,(int)TestApplication.mContext.getResources().getDimension(R.dimen.gongzuoRectBottom));

//        AccessibilityServiceInfo info = getServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
////        info.packageNames = new String[]{pak,settings,thispak};
//        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
//        info.notificationTimeout = 3000;
//        setServiceInfo(info);
    }

    /**
     * ACTION_CLICK：模拟点击
     ACTION_SELECT：模拟选中
     ACTION_LONG_CLICK：模拟长按
     ACTION_SCROLL_FORWARD：模拟往前滚动
     * @param event
     */

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /**
         *
         TYPE_VIEW_CLICKED    // 当View被点击时发送此事件。
         TYPE_VIEW_LONG_CLICKED    // 当View被长按时发送此事件。
         TYPE_VIEW_FOCUSED    // 当View获取到焦点时发送此事件。
         TYPE_WINDOW_STATE_CHANGED    // 当Window发生变化时发送此事件。
         TYPE_VIEW_SCROLLED    // 当View滑动时发送此事件。
         TYPE_NOTIFICATION_STATE_CHANGED是通知栏事件，
         TYPE_WINDOW_STATE_CHANGED是窗口状态改变，
         TYPE_WINDOW_CONTENT_CHANGED是窗口内容改变
         */
//        if (event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED ){
//            Log.i(TAG,"事件源:"+event.getSource().toString());
//            Rect rect = new Rect();
//            event.getSource().getBoundsInScreen(rect);//获取点击事件源的范围
//            switch (controlRect.size()){
//                case 0:
//                    controlRect.put(key_gongzuo,rect);
//                break;
//                case 1:
//                    controlRect.put(key_kaoqindaka,rect);
//                    break;
//                case 2:
//                    controlRect.put(key_shangbandaka,rect);
//                    break;
//            }
//            MaintenanceSharePreferences.setVal(MaintenanceSharePreferences.KEY_RECTMAP,controlRect);
//        }
//        if (event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
        Log.i(TAG,"kaoqindakaRect.toSting"+kaoqindakaRect.toString());
        Log.i(TAG,"shangbandakaRect.toSting"+shangbandakaRect.toString());
        Log.i(TAG, "xiabandakaRect.toString"+xiabandakaRect.toString());
        task(getRootInActiveWindow());
//        String  nowPackageName = event.getPackageName().toString();
//        Log.i(TAG,nowPackageName);
//        if (nowPackageName==null){
//            return;
//        }
//        if (nowPackageName.equals(pak) ) {
//            Log.i(TAG, "进入监听钉钉");
//            task(getRootInActiveWindow());
//
//        }else if (nowPackageName.equals(settings)){
//            final CharSequence className = event.getClassName();
//            if (className.equals(NAME_APP_DETAILS)) { //模拟点击强行停止按钮
//                simulationClick(event, TEXT_FORCE_STOP);
////                    performGlobalAction(GLOBAL_ACTION_BACK);
//            }
//            if (className.equals(NAME_ALERT_DIALOG)) { //模拟点击弹窗的确认按钮
//                simulationClick(event, TEXT_DETERMINE);
//
//            }
//        }
//
//
//            }else  if (nowPackageName.equals(thispak) &&(mState.equals("重新打卡"))){
//                Log.i(TAG,mState);
//                Log.i(TAG,"监听自己App");
//                mState="等待打卡";
//                Intent intent = getPackageManager().getLaunchIntentForPackage(pak);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//            }
//        }
//        }


    }

    @Override
    public void onInterrupt() {

    }

    public boolean dfsnode(AccessibilityNodeInfo node , int num){

        if (node==null || node.getChildCount()==0){
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ;i < num ; i++){
            stringBuilder.append("__ ");    //父子节点之间的缩进
        }
        stringBuilder.append(node.toString());
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        Log.i(TAG,"控件属性---"+stringBuilder.toString());
        Log.i(TAG,"控件大小---"+rect.toShortString()+"--|--控件类型:"+node.getClassName()+"当前状态:"+mState);
        //[6,954][273,1176]
//        Rect(6, 954, 273, 1176);
//  && node.getClassName().equals("android.view.View")
        //576, 2605 - 864, 2792
        if (gongzuoRect.contains(rect)){
            Log.i(TAG,"找到节点--工作" +rect.toString());
            if(node.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                pageState=3;
                return true;
            }else {
                return false;
            }
        }
        if (kaoqindakaRect.contains(rect)){
            Log.i(TAG,"找到节点" +rect.toString());

            if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                Log.i(TAG,"节点点击成功考勤打卡-------"+rect.toString());
                pageState=3;
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            }else{
//                pageState=-1;
                return false;
            }

        }
        //[357,597][723,966]
        //[357,597][723,966]
        //[357,597][723,966]
//        Rect(357, 597 , 723, 966);
        //android.app.Dialog 这是打卡成功后弹出的 弹窗 [0,210][1080,1920] 这是位置


        //[357,930][723,1299]
        //下班打卡的位置
        //[120,522][960,1605]  这是打卡成功后
        Date date = new Date();
        String tem =  simpleDateFormat.format(date);

        if (!tem.substring(11,13).equals(TestApplication.mContext.getString(R.string.shangbanshijian))){
            //&& node.getClassName().equals("android.view.View")
            if (shangbandakaRect.contains(rect) ) {
                Log.i(TAG, "找到节点-" + rect.toString());
                if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    Log.i(TAG, "节点点击成功上班打卡-------" + rect.toString());
                    pageState = 0;
                    return true;
                } else {
//                pageState=-1;
                    return false;
                }

            }
        }else{
            //&& node.getClassName().equals("android.view.View")
            if (xiabandakaRect.contains(rect) ) {
                Log.i(TAG, "找到节点-" + rect.toString());
                if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    Log.i(TAG, "节点点击成功下班打卡-------" + rect.toString());
                    pageState = 0;
                    return true;
                } else {
//                pageState=-1;
                    return false;
                }
            }

        }
        for(int i = 0 ; i < node.getChildCount()  ; i++){      //遍历子节点
            dfsnode(node.getChild(i),num+1);
        }
        return false;
    }

    public void task(AccessibilityNodeInfo info) {
//        List<AccessibilityNodeInfo> list2 = info.findAccessibilityNodeInfosByViewId(toolbar);
//        if (list2!=null&&list2.size()>0){
//            pageState=3;
//        }
        Log.i(TAG, "方法进入时间：" + System.currentTimeMillis());   //打印
        Log.i(TAG, "方法进入状态：" + pageState);   //打印
        Log.i(TAG, "方法进入mState：" + mState);   //打印



//        getRootInActiveWindow();//刷新页面节点
        if (pageState == 3) {
            Log.i(TAG, "方法查找上班打卡页面：" + System.currentTimeMillis());   //打印
            try {

                Thread.sleep(30000);
                Log.i(TAG, "方法查找上班打卡页面：" + System.currentTimeMillis());   //打印
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dfsnode(getRootInActiveWindow(), 0)) {
                pageState = 0;
                mState = "打卡完毕";
                monitor();
                return;
            } else {
//                pageState = -1;
//                mState = "打卡异常";
            }
            showPackageDetail();
        }
        getRootInActiveWindow();//刷新页面节点
        if (pageState < 2) {
            Log.i(TAG, "方法查找工作页面：" + System.currentTimeMillis());   //打印
            if (dfsnode(getRootInActiveWindow(), 0)) {
                pageState = 3;
                return;
            } else {
//                pageState = -1;
//                mState = "打卡异常";
                showPackageDetail();
            }

        }
        getRootInActiveWindow();//刷新页面节点
        if (pageState < 2) {
            if (dfsnode(getRootInActiveWindow(), 0)){
                try {

                }catch (Exception e){
                    pageState = -1;
                    mState = "等待打卡";
                }


            };

    }
//        getRootInActiveWindow();//刷新页面节点

    }
    //https://blog.csdn.net/u012861467/article/details/81455619

    private void showPackageDetail(){
//        getRootInActiveWindow();
//        if (mState.equals("打卡完毕")|| mState.equals("打卡异常")) {
//            Intent intent = new Intent();
//            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            Uri uri = Uri.fromParts("package", pak, null);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(uri);
//            AccessibilitySampleService.this.startActivity(intent);
//        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void simulationClick(AccessibilityEvent event, String text){
        List<AccessibilityNodeInfo> nodeInfoList = event.getSource().findAccessibilityNodeInfosByText(text);
        for (AccessibilityNodeInfo node : nodeInfoList) {
            if (node.isClickable() && node.isEnabled()) {
                if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                    if (text.equals(TEXT_DETERMINE)){
                        mState = "等待打卡";
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                };
            }
        }
    }



    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "服务解绑了,释放资源操作...");
        mState="等待打卡";
        pageState=-1;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "服务停止了...");
        mState="等待打卡";
        pageState=-1;
        super.onDestroy();
    }


    /**
     * 系统桌面，点击菜单键时候，弹出的清理按键id
     */
    private static final String systemuiClearBtnId = "com.android.systemui:id/clearButton";
    /**
     * 点击系统菜单键，清理运存
     */
    private void clearMemory() {
        SystemClock.sleep(SLEEPTIME);
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId(systemuiClearBtnId);
            if (infos != null && infos.size() > 0) {
                AccessibilityNodeInfo clearBtn = infos.get(0);
                clearBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            nodeInfo.recycle();
        }
    }


    public void monitor() {
        SystemClock.sleep(SLEEPTIME);
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        SystemClock.sleep(SLEEPTIME);
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        clearMemory();
    }
}
