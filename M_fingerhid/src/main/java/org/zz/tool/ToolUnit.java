package org.zz.tool;

import android.os.*;
import java.util.*;
import java.io.*;

public class ToolUnit
{
    public static String getSDCardPath() {
        File sdDir = null;
        final boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return null;
    }
    
    public static Boolean isExist(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
    
    public static Boolean AddDirectory(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return true;
    }
    
    public static void GetSubFolders(final String strMainFolder, final List<String> strSubFolders) {
        final File[] files = new File(strMainFolder).listFiles();
        for (int i = 0; i < files.length; ++i) {
            final File f = files[i];
            if (f.isDirectory()) {
                strSubFolders.add(f.getPath());
            }
        }
    }
    
    public static void GetSubFiles(final String Path, final String Extension, final List<String> strSubFiles) {
        final File[] files = new File(Path).listFiles();
        for (int i = 0; i < files.length; ++i) {
            final File f = files[i];
            if (f.isFile() && f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                strSubFiles.add(f.getPath());
            }
        }
    }
    
    public static int SaveData(final String filepath, final byte[] buffer, final int size) {
        final File f = new File(filepath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        try {
            fos.write(buffer, 0, size);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fos.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return -2;
        }
        return 0;
    }
    
    public static long getFileSizes(final String filepath) {
        final File f = new File(filepath);
        long s = 0L;
        if (f.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                s = fis.available();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            return s;
        }
        return -1L;
    }
    
    public static byte[] ReadData(final String filepath) {
        final File f = new File(filepath);
        if (!f.exists()) {
            return null;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            final int buf_size = 1024;
            final byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                bos.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
    
    public static boolean deleteFile(final String sPath) {
        boolean flag = false;
        final File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    public static void AppendFile(final String fileName, final String content, final int iPos) {
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            if (iPos == 0) {
                fileLength = 0L;
            }
            else if (iPos > 0) {
                fileLength = iPos;
            }
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
        }
        catch (IOException e) {
            e.printStackTrace();
            if (randomFile != null) {
                try {
                    randomFile.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public static void AppendFile_bk(final String fileName, final String content, final int iPos) {
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            if (iPos == 0) {
                fileLength = 0L;
            }
            else if (iPos > 0) {
                fileLength = iPos;
            }
            randomFile.seek(fileLength);
            final int szlen = content.length();
            byte[] buffer = new byte[szlen];
            buffer = content.getBytes();
            randomFile.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            if (randomFile != null) {
                try {
                    randomFile.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
