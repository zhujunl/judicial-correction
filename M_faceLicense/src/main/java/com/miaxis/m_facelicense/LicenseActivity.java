//
//package com.miaxis.m_facelicense;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.wsm.hidlib.HIDManager;
//import com.wsm.hidlib.callback.HIDDataListener;
//import com.wsm.hidlib.callback.HIDOpenListener;
//import com.wsm.hidlib.constant.ConnectCostant;
//import com.wsm.hidlib.constant.FormatConstant;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class LicenseActivity extends AppCompatActivity {
//    final String TAG="LicenseActivity";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_license);
//
//
//        HIDManager.getInstance().enableLog(true);
//        HIDManager.getInstance().openHID(this, new HIDOpenListener() {
//            @Override
//            public void openSuccess(int i) {
//                Log.d(TAG," openSuccess");
//            }
//
//            @Override
//            public void openError(int openErrorStatus) {
//                if (openErrorStatus == ConnectCostant.USB_DISCONNECT) {
//                    //USB断开
//                    Log.e(TAG," 断开USB");
//                }
//                if (openErrorStatus == ConnectCostant.COMMUNICATION_CLOSE) {
//                    //服务销毁
//                    Log.e(TAG," 服务销毁");
//                }
//            }
//        }, new HIDDataListener() {
//            @Override
//            public void onDataReceived(byte b, String dataMessage) {
//                if (!TextUtils.isEmpty(dataMessage)) {
//                    String result = dataMessage;
//                    Log.d(TAG, "onDataReceived: length:" + dataMessage.length() + "   content:" + result);
//                }
//            }
//
//            @Override
//            public void onOriginalDataReceived(byte b, byte[] bytes, int length) {
//                Log.d(TAG, "onOriginalDataReceived: " + length);
//            }
//        });
//        HIDManager.getInstance().setFormat(FormatConstant.FORMAT_GBK);
//    }
//}