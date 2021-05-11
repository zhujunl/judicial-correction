package com.miaxis.judicialcorrection.base.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * 尺寸
 */
public class DensityUtils {

    public static Point getScreenWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);//不能省略,必须有
        return outSize;
    }
}
