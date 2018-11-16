package util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by jinfei on 2016/5/3.
 */
public class DensityUtil {
        private DensityUtil() {
            throw new UnsupportedOperationException("cannot be instantiated");
        }



        public static int dp2px(Context context, float dpVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                    .getDisplayMetrics());
        }



        public static int sp2px(Context context, float spVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources()
                    .getDisplayMetrics());
        }


        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        public static int px2dp(Context context, float pxVal) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxVal / scale + 0.5f);
        }


        public static float px2sp(Context context, float pxVal) {
            return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
        }


    public static int dip2px(Context context, int i) {
        return i;
    }
}

