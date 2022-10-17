package com.miaxis.m_facelicense.License;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/21 0021.
 */

public class FileUtil {

    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String readLine;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void writeFile(File file, String content, boolean isAdd) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, isAdd));
            bw.write(content);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param path 文件夹路径
     * @return true 存在  false 不存在
     * @author chen.gs
     * @category 判断文件夹是否存在, 如果不存在则创建文件夹
     */
    public static Boolean AddDirectory(String path) {
        File file = new File(path);
        boolean mkdir = false;
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            mkdir = parentFile.mkdir();
        }
        if (!file.exists()) {
            mkdir = file.mkdir();
        }
        return mkdir;
    }


}
