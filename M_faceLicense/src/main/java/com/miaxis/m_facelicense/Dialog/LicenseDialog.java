package com.miaxis.m_facelicense.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miaxis.m_facelicense.License.LicenseManager;
import com.miaxis.m_facelicense.R;
import com.miaxis.m_facelicense.bean.UserBean;
import com.wsm.hidlib.HIDManager;
import com.wsm.hidlib.callback.HIDDataListener;
import com.wsm.hidlib.callback.HIDOpenListener;
import com.wsm.hidlib.constant.ConnectCostant;
import com.wsm.hidlib.constant.FormatConstant;

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

    public LicenseDialog(@NonNull Context context, boolean exit) {
        super(context);
        this.context = context;
        this.exit = exit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_license);
        setCancelable(false);
        Log.d(TAG, "onCreat");
        HIDManager.getInstance().enableLog(true);
        HIDManager.getInstance().openHID(context, new HIDOpenListener() {
            @Override
            public void openSuccess(int i) {
                Log.d(TAG, " openSuccess");
            }

            @Override
            public void openError(int openErrorStatus) {
                if (openErrorStatus == ConnectCostant.USB_DISCONNECT) {
                    //USB断开
                    Log.e(TAG, " 断开USB");
                }
                if (openErrorStatus == ConnectCostant.COMMUNICATION_CLOSE) {
                    //服务销毁
                    Log.e(TAG, " 服务销毁");
                }
            }
        }, new HIDDataListener() {
            @Override
            public void onDataReceived(byte b, String dataMessage) {
                if (!TextUtils.isEmpty(dataMessage)) {
                    String result = dataMessage;
                    Log.d(TAG, result);

                    try {
                        UserBean userBean = new Gson().fromJson(result, UserBean.class);
                        Log.d(TAG, userBean.toString());
                        String license = LicenseManager.getInstance().threadGetLicense(context, userBean.account, userBean.password);
                        if (TextUtils.isEmpty(license)) {
                            Log.d(TAG, "授权成功");
                            dismiss();
                        } else {
                            Log.e(TAG, "授权失败");
                            Toast.makeText(context, license, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onOriginalDataReceived(byte b, byte[] bytes, int length) {
                Log.d(TAG, "onOriginalDataReceived: " + length);
            }
        });
        HIDManager.getInstance().setFormat(FormatConstant.FORMAT_GBK);
        exiTxt = findViewById(R.id.dialog_exit);
        exiTxt.setOnClickListener(v -> {
            if (exit) {
                System.exit(0);
            } else {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        HIDManager.getInstance().closeHID();
    }
}
