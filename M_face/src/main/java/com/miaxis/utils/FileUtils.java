package com.miaxis.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author Tank
 * @date 2021/5/11 13:29
 * @des
 * @updateAuthor
 * @updateDes
 */
public class FileUtils {

    public static File createFileParent(Context context) {
        try {
            File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile();
            if (path != null && !path.exists()) {
                boolean mkdirs = path.mkdirs();
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
