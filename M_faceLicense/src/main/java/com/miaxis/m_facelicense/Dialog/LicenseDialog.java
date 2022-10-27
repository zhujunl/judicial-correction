package com.miaxis.m_facelicense.Dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.gson.Gson;
import com.miaxis.m_facelicense.License.LicenseManager;
import com.miaxis.m_facelicense.R;
import com.miaxis.m_facelicense.bean.UserBean;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * @author ZJL
 * @date 2022/10/17 10:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LicenseDialog extends AlertDialog {
    final String TAG = "LicenseDialog";
    Context context;
    boolean exit;
    TextView exiTxt;
    ScanListener scanListener;

    public LicenseDialog(@NonNull Context context, boolean exit,ScanListener scanListener) {
        super(context);
        this.context = context;
        this.exit = exit;
        this.scanListener=scanListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_license);
        setCancelable(false);
        Log.d(TAG, "onCreat");
        exiTxt = findViewById(R.id.dialog_exit);
        exiTxt.setOnClickListener(v -> {
            if (exit) {
                System.exit(0);
            } else {
                dismiss();
            }
        });
    }

    private StringBuffer sb = new StringBuffer();

    boolean isScaning = false;
    int len = 0;
    int oldLen = 0;

    //二维码扫码
    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT
                    || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                return super.dispatchKeyEvent(event);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            char unicodeChar = (char) event.getUnicodeChar();
            sb.append(unicodeChar);
            len++;
            startScan();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void startScan() {
        if (isScaning) {
            return;
        }
        isScaning = true;
        timerScanCal();
    }

    private void timerScanCal() {
        Log.d(TAG, "timerScanCal");
        oldLen = len;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(50);
                if (oldLen != len) {
                    timerScanCal();
                    return;
                }
                isScaning = false;
                if (sb.length() > 0) {
                    String str = sb.toString();
                    Log.d(TAG, "扫码:" + str);
                    String[] split = str.split("\\.");
                    try {
                        UserBean userBean = new Gson().fromJson(split[0], UserBean.class);
                        Log.d(TAG, userBean.toString());
                        String license = LicenseManager.getInstance().threadGetLicense(context, userBean.account, userBean.password);
                        if (TextUtils.isEmpty(license)) {
                            Log.d(TAG, "授权成功");
                            dismiss();
                        } else {
                            Log.e(TAG, "授权失败");
                            scanListener.ScanResult("授权失败:"+license);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        scanListener.ScanResult("授权失败:"+e.getMessage());
                    } finally {
                        SystemClock.sleep(1000);
                        sb.setLength(0);
                    }
                }
            }
        }).start();
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface ScanListener{
        void ScanResult(String result);
    }
}
