package com.miaxis.judicialcorrection.id.readIdCard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.bean.IdCard;
import com.miaxis.judicialcorrection.bean.IdCardMsg;
import com.sdt.Common;
import com.sdt.Sdtapi;
import com.zz.jni.Wlt2BmpCall;

import java.nio.charset.StandardCharsets;

/**
 * @author Tank
 * @date 2021/4/25 9:33 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ReadIdCardManager {

    private Sdtapi mSdtApi;

    private ReadIdCardManager() {
    }

    public static ReadIdCardManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ReadIdCardManager instance = new ReadIdCardManager();
    }

    /**
     * ================================ 静态内部类单例 ================================
     **/

    //    public interface ReadIdCardCallback {
    //
    //        void readIdCardCallback(int code, String message, IdCardMsg idCardMsg, Bitmap bitmap);
    //
    //    }
    public boolean init(Activity activity) {
        try {
            mSdtApi = new Sdtapi(activity);
            IntentFilter filter = new IntentFilter();//意图过滤器
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);//USB设备拔出
            filter.addAction(Common.ACTION_USB_PERMISSION);//自定义的USB设备请求授权
            activity.registerReceiver(mUsbReceiver, filter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void free(Activity activity) {
        try {
            activity.unregisterReceiver(mUsbReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApiResult<IdCard> read() {
        ApiResult<IdCard> result = new ApiResult<>();
        mSdtApi.SDT_StartFindIDCard();//寻找身份证
        mSdtApi.SDT_SelectIDCard();//选取身份证
        byte[] pucPHMsg = new byte[1024];//头像
        int[] puiPHMsgLen = new int[1];
        byte[] pucFpMsg = new byte[1024];//两个指纹
        int[] puiFpMsgLen = new int[1];
        int ret = ReadBaseMsg(pucPHMsg, puiPHMsgLen, pucFpMsg, puiFpMsgLen);
        if (ret == 0x90) {
            byte[] pucCHMsg = new byte[256];
            try {
                char[] pucCHMsgStr = new char[128];
                DecodeByte(pucCHMsg, pucCHMsgStr);//将读取的身份证中的信息字节，解码
                IdCardMsg msg = new IdCardMsg();//身份证信息对象，存储身份证上的文字信息
                ret = ReadBaseMsgToStr(msg);
                if (ret == 0x90) {
                    IdCard idCard = new IdCard();
                    idCard.idCardMsg = msg;
                    byte[] bmp = new byte[38862];
                    Bitmap bitmap = GetImage(pucPHMsg, bmp);
                    if (bitmap != null) {
                        idCard.face = bitmap;
                        result.code = 0;
                    } else {
                        result.code = 1;
                    }
                    result.setData(idCard);
                } else {
                    result.code = -4;
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.code = -2;
                return result;
            }
        } else {
            result.code = -1;
            result.msg = "没有身份证";
            return result;
        }
    }

    //广播接收器
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(context, "action:" + action, Toast.LENGTH_SHORT).show();
            //USB设备拔出广播
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                String deviceName = device.getDeviceName();
                int deviceId = device.getDeviceId();
                int productId = device.getProductId();
            } else if (Common.ACTION_USB_PERMISSION.equals(action)) {//USB设备未授权，从SDTAPI中发出的广播
                //                Message msg = new Message();
                //                msg.what = 3;
                //                msg.obj = "USB设备无权限";
                //                MyHandler.sendMessage(msg);
            }
        }
    };

    //读取身份证中的文字信息（可阅读格式的）
    public int ReadBaseMsgToStr(IdCardMsg msg) {
        int[] puiCHMsgLen = new int[1];
        int[] puiPHMsgLen = new int[1];
        byte[] pucCHMsg = new byte[256];
        byte[] pucPHMsg = new byte[1024];
        //sdtapi中标准接口，输出字节格式的信息。
        int ret = mSdtApi.SDT_ReadBaseMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen);
        if (ret == 0x90) {
            try {
                char[] pucCHMsgStr = new char[128];
                DecodeByte(pucCHMsg, pucCHMsgStr);//将读取的身份证中的信息字节，解码成可阅读的文字
                PareseItem(pucCHMsgStr, msg); //将信息解析到msg中
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    //字节解码函数
    void DecodeByte(byte[] msg, char[] msg_str) throws Exception {
        byte[] newmsg = new byte[msg.length + 2];
        newmsg[0] = (byte) 0xff;
        newmsg[1] = (byte) 0xfe;
        System.arraycopy(msg, 0, newmsg, 2, msg.length);
        String s = new String(newmsg, StandardCharsets.UTF_16);
        for (int i = 0; i < s.toCharArray().length; i++) {
            msg_str[i] = s.toCharArray()[i];
        }
    }

    //读取身份证中的头像信息
    public int ReadBaseMsg(byte[] pucPHMsg, int[] puiPHMsgLen, byte[] pucFpMsg, int[] puiFpMsgLen) {
        int[] puiCHMsgLen = new int[1];
        byte[] pucCHMsg = new byte[256];
        //sdtapi中标准接口，输出字节格式的信息。
        return mSdtApi.SDT_ReadBaseFPMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen, pucFpMsg, puiFpMsgLen);
    }

    /****************************************************************************
     功  能：居民身份证(港澳台居民居住证)- 图片
     参  数：cardInfo  - 输入，卡信息
     bBMPFile  - 输出，解码后BMP图片数据，38862字节
     返  回：ERRCODE_SUCCESS(0)	成功
     其他				失败
     *****************************************************************************/
    public Bitmap GetImage(byte[] cardInfo, byte[] bBMPFile) {
        byte[] tmp = new byte[1024];
        System.arraycopy(cardInfo, 0, tmp, 0, tmp.length);
        int mPhotoSize = 38862;
        if (bBMPFile.length < mPhotoSize)
            return null;
        int miaxis_wlt2BgrData = Wlt2BmpCall.miaxis_Wlt2BgrData(tmp, bBMPFile);
        if (miaxis_wlt2BgrData == 0) {
            return Wlt2BmpCall.miaxis_Bgr2Bitmap(102, 126, bBMPFile);
        }
        return null;
    }

    //分段信息提取
    void PareseItem(char[] pucCHMsgStr, IdCardMsg msg) {
        msg.name = String.copyValueOf(pucCHMsgStr, 0, 15);
        String sex_code = String.copyValueOf(pucCHMsgStr, 15, 1);

        switch (sex_code) {
            case "1":
                msg.sex = "男";
                break;
            case "2":
                msg.sex = "女";
                break;
            case "9":
                msg.sex = "未说明";
                break;
            case "0":
            default:
                msg.sex = "未知";
        }

        String nation_code = String.copyValueOf(pucCHMsgStr, 16, 2);
        msg.nation_str = nation[Integer.parseInt(nation_code) - 1];
        msg.birth_year = String.copyValueOf(pucCHMsgStr, 18, 4);
        msg.birth_month = String.copyValueOf(pucCHMsgStr, 22, 2);
        msg.birth_day = String.copyValueOf(pucCHMsgStr, 24, 2);
        msg.address = String.copyValueOf(pucCHMsgStr, 26, 35);
        msg.id_num = String.copyValueOf(pucCHMsgStr, 61, 18);
        msg.sign_office = String.copyValueOf(pucCHMsgStr, 79, 15);
        msg.useful_s_date_year = String.copyValueOf(pucCHMsgStr, 94, 4);
        msg.useful_s_date_month = String.copyValueOf(pucCHMsgStr, 98, 2);
        msg.useful_s_date_day = String.copyValueOf(pucCHMsgStr, 100, 2);
        msg.useful_e_date_year = String.copyValueOf(pucCHMsgStr, 102, 4);
        msg.useful_e_date_month = String.copyValueOf(pucCHMsgStr, 106, 2);
        msg.useful_e_date_day = String.copyValueOf(pucCHMsgStr, 108, 2);
    }

    /*民族列表*/
    private final String[] nation = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
            "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳",
            "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "克尔克孜", "土",
            "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米",
            "塔吉克", "怒", "乌兹别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔",
            "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺"
    };
}
