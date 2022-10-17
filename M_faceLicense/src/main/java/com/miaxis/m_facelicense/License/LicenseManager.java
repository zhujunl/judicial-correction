package com.miaxis.m_facelicense.License;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import jni.MXLicenseAPI;

/**
 * @author ZJL
 * @date 2022/10/17 11:14
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LicenseManager {
    private static LicenseManager instance;

    public static String USER_NAME = "MIAXIS_20190802";
    public static String USER_PWD = "fbcbefbba07b4b70141cf3f1f128b804";
    String strIP = "183.129.171.153";
    int iPort = 1902;
    public static final String FACE_MAIN_PATH = Environment.getExternalStorageDirectory() + File.separator + "miaxis" + File.separator + "FaceId_ST";
    int m_iAlgTypeIndex = 0;
    private MXLicenseAPI m_licenseApi;
    public static final String LICENCE_NAME = "st_lic.txt";
    public static final String LICENCE_NAME_1N = "st_lic_1n.txt";
    String TAG = "LicenseManager";


    public static LicenseManager getInstance() {
        synchronized (LicenseManager.class) {
            if (instance == null) {
                instance = new LicenseManager();
            }
        }
        return instance;
    }

    public void init() {
        m_licenseApi = new MXLicenseAPI();
    }

    public String threadGetLicense(Context context, String name, String pwd) {
        try {
            Log.e(TAG, "OnClickGetLicense");
            int iTimeout = 5 * 1000;
            String reData = mxGetLicense(context, strIP, iPort, iTimeout, name, pwd, 1001);
            JSONObject o = stringToJson(reData);
            if (reData != null && o != null) {
                String code = o.getString("code");
                if (TextUtils.equals("200", code)) {
                    String licStr = o.getString("data");
                    writeLic(licStr);
                    Log.d(TAG, licStr);
                    return "";
                } else {
                    String msg = "失败";
                    try {
                        String errMsg = o.getString("message");
                        if (!TextUtils.isEmpty(errMsg)) {
                            Log.e(TAG, errMsg);
                            msg = errMsg;
                        } else {
                            Log.e(TAG, "Error");
                        }
                    } catch (JSONException ex) {
                        String strInfo = "失败";
                        Log.e(TAG, strInfo + " " + ex.getMessage());
                        msg = strInfo + " " + ex.getMessage();
                    }
                    return msg;
                }
            } else {
                Log.e(TAG, "授权失败！未知错误");
                return "授权失败！未知错误";
            }
        } catch (JSONException ex) {
            Log.e(TAG, "JSONException=" + ex.getMessage());
            return "JSONException=" + ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String mxGetLicense(Context context, String strIP, int iPort, int iTimeout, String strUserID, String strPWD, int iAlgType) {
        int nRet = 0;
        String strHardwareId = m_licenseApi.GenHardwareId(context);
        if (strHardwareId == null) {
            JSONObject o1 = new JSONObject();
            try {
                String strInfo = "Get hardware code error";
                o1.put("code", "-1");
                o1.put("message", strInfo + ",nRet=" + nRet);
                o1.put("data", "");
            } catch (JSONException var18) {
                var18.printStackTrace();
            }

            String reStr = o1.toString();
            return reStr;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date(System.currentTimeMillis());
            String strDate = simpleDateFormat.format(date);
            AuthorClient client = new AuthorClient(strIP, iPort, iTimeout);

            //保存申请信息
            String strFileName = TAG + strDate + ".dat";
            try {
                writeComputeInfo(strFileName, strHardwareId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String strResult = client.faceAuthor(iAlgType, strDate, strUserID, strPWD, strHardwareId);
                Log.e(TAG, "strResult=" + strResult);
                return strResult;
            } catch (Exception var19) {
                Exception e = var19;
                JSONObject o1 = new JSONObject();

                try {
                    o1.put("code", "-2");
                    o1.put("message", e.getMessage());
                    o1.put("data", "");
                } catch (JSONException var17) {
                    var17.printStackTrace();
                    return null;
                }

                String reStr = o1.toString();
                return reStr;
            }
        }
    }

    private void writeLic(String licStr) throws Exception {
        Log.e("MIAXIS", "writeLic");
        FileUtil.AddDirectory(FACE_MAIN_PATH);
        if (licStr != null && licStr.length() > 10
                && !licStr.startsWith("EEROR:")
                && !licStr.startsWith("error:")
                && !licStr.startsWith("err:")) {
            String strLicenceName = LICENCE_NAME;
            if (m_iAlgTypeIndex == 0) {
                strLicenceName = LICENCE_NAME;
            } else {
                strLicenceName = LICENCE_NAME_1N;
            }
            File licFile = new File(FACE_MAIN_PATH, strLicenceName);
            if (!licFile.exists()) {
                if (!licFile.createNewFile()) {
                    Log.e("MIAXIS", "创建授权文件失败");
                    throw new Exception("创建授权文件失败！");
                }
            }
            FileUtil.writeFile(licFile, licStr, false);
        }
    }

    private void writeComputeInfo(String strFileNamne, String computeInfo) throws Exception {
        Log.e(TAG, "writeComputeInfo");
        FileUtil.AddDirectory(FACE_MAIN_PATH);
        File licFile = new File(FACE_MAIN_PATH, strFileNamne);
        if (!licFile.exists()) {
            if (!licFile.createNewFile()) {
                Log.e(TAG, "创建文件失败");
                throw new Exception("创建文件失败！");
            }
            FileUtil.writeFile(licFile, computeInfo, false);
        }
    }

    private JSONObject stringToJson(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        }
    }

    public String VerifyLicense(Context context) {
        Log.e(TAG, "OnClickVerifyLicense");
        String strLic = readLicence();
        if (strLic == null) {
            Log.e(TAG, "readLicence失败");
            return "读取授权文件失败";
        }

        int nRet = m_licenseApi.VerifyLicense(context, "mxFaceAPI", strLic);
        if (nRet == 0) {
            Log.e(TAG, "VerifyLicense,nRet=" + nRet);
            return "验证通过";
        } else {
            String strInfo = "Verify no pass";
            Log.e(TAG, "VerifyLicense,nRet=" + nRet);
            return "验证不通过,nRet=" + nRet;
        }
    }

    private String readLicence() {
        String strLicenceName = LICENCE_NAME;
        if (m_iAlgTypeIndex == 0) {
            strLicenceName = LICENCE_NAME;
        } else {
            strLicenceName = LICENCE_NAME_1N;
        }
        File lic = new File(FACE_MAIN_PATH, strLicenceName);
        if (!lic.exists()) {
            return null; //本地无授权码
        } else {
            String sLicence = FileUtil.readFile(lic);
            if (TextUtils.isEmpty(sLicence)) {
                return null; //本地授权码为空
            } else {
                return sLicence;//本地已有授权码
            }
        }
    }
}
