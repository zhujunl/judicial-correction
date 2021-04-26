package com.sdt;

import android.app.Activity;


/**
 * 二代证示例
 */
public class MainActivity extends Activity {

//    Thread t2;
//
//    Common common; //common对象，存储一些需要的参数
//
//    /*民族列表*/
//    String[] nation = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
//            "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳",
//            "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "克尔克孜", "土",
//            "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米",
//            "塔吉克", "怒", "乌兹别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔",
//            "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺"
//    };
//
//    public boolean findloop = true;
//    Sdtapi sdta;
//
//    Handler MyHandler = new Handler() { //消息处理函数，处理应用程序内部的消息传递
//
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) //结束循环扫描
//                setstop((String) msg.obj);
//            else if (msg.what == 2) //开始循环扫描找卡
//            {
//                setallunclick((String) msg.obj);
//                new Thread() {
//                    public void run() {
//                        try {
//                            sleep(2500);
//                            if (t2 != null && t2.isAlive())
//                                findloop = false;
//                            com.miaxis.idcardreader.MainActivity.this.finish();
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//            } else if (msg.what == 3)//usb设备已经重新授权，应用重新启动
//            {
//                com.miaxis.idcardreader.MainActivity.this.recreate();
//            }
//        }
//
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        common = new Common();
//
//
//        try {
//            sdta = new Sdtapi(this);
//
//        } catch (Exception e1) {//捕获异常，
//
//
//            if (e1.getCause() == null) //USB设备异常或无连接，应用程序即将关闭。
//            {
//
//                new Thread() {
//                    public void run() {
//                        Message msg = new Message();
//                        msg.what = 2;
//                        msg.obj = "USB设备异常或无连接，应用程序即将关闭。";
//                        MyHandler.sendMessage(msg);
//                    }
//                }.start();
//            } else //USB设备未授权，需要确认授权
//            {
//                ViewRe.setGravity(0);
//                ViewRe.setTextSize(30);
//
//                setallunclick("USB设备未授权，弹出请求授权窗口后，请点击\"确定\"继续");
//
//            }
//
//        }
//
//
//        IntentFilter filter = new IntentFilter();//意图过滤器
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);//USB设备拔出
//        filter.addAction(common.ACTION_USB_PERMISSION);//自定义的USB设备请求授权
//        registerReceiver(mUsbReceiver, filter);
//
//        //获取模块状态
//        btgetsamstatus.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                int ret;
//                ret = sdta.SDT_GetSAMStatus();
//                String show;
//                if (ret == 0x90)
//                    show = "模块状态良好";
//                else
//                    show = "模块状态错误:" + String.format("0x%02x", ret);
//
//                ViewRe.setText(show);
//
//            }
//
//        });
//
//
//        //获取模块编码SAMID即安全模块编号
//        btgetsamid.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                char[] puSAMID = new char[36];
//                int ret = sdta.SDT_GetSAMIDToStr(puSAMID);
//
//                if (ret == 0x90)
//                    ViewRe.setText(puSAMID, 0, puSAMID.length);
//                else {
//                    String show = "错误:" + String.format("0x%02x", ret);
//                    ViewRe.setText(show);
//
//                }
//            }
//        });
//
//
//        //读身份证号码
//        btReadIdNum.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                int ret;
//
//                sdta.SDT_StartFindIDCard();//寻找身份证
//                sdta.SDT_SelectIDCard();//选取身份证
//
//
//                IdCardMsg msg = new IdCardMsg();//身份证信息对象，存储身份证上的文字信息
//                ret = ReadBaseMsgToStr(msg);
//
//                String show;
//                if (ret == 0x90)
//                    show = "身份证号码:" + '\n' + String.valueOf(msg.id_num) + '\n';
//                else
//                    show = "读身份证号码失败:" + String.format("0x%02x", ret);
//
//                ViewRe.setText(show);
//
//            }
//        });
//
//
//        //读基本信息
//        btReadBaseMsg.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                int ret;
//                String show = "";
//
//                sdta.SDT_StartFindIDCard();//寻找身份证
//                sdta.SDT_SelectIDCard();//选取身份证
//
//                IdCardMsg msg = new IdCardMsg();//身份证信息对象，存储身份证上的文字信息
//
//                ret = ReadBaseMsgToStr(msg);
//
//                if (ret == 0x90) {
//                    show = "姓名:" + msg.name + '\n'
//                            + "性别:" + msg.sex + '\n'
//                            + "民族:" + msg.nation_str + "族" + '\n'
//                            + "出生日期:" + msg.birth_year + "-" + msg.birth_month + "-" + msg.birth_day + '\n'
//                            + "住址:" + msg.address + '\n'
//                            + "身份证号码:" + msg.id_num + '\n'
//                            + "签发机关:" + msg.sign_office + '\n'
//                            + "有效期起始日期:" + msg.useful_s_date_year + "-" + msg.useful_s_date_month + "-" + msg.useful_s_date_day + '\n'
//                            + "有效期截止日期:" + msg.useful_e_date_year + "-" + msg.useful_e_date_month + "-" + msg.useful_e_date_day + '\n';
//
//                } else
//                    show = "读基本信息失败:" + String.format("0x%02x", ret);
//
//                ViewRe.setText(show);
//            }//end onclick()
//
//        });
//
//        //读基本信息
//        btReadBaseMsg1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//
//                sdta.SDT_StartFindIDCard();//寻找身份证
//                sdta.SDT_SelectIDCard();//选取身份证
//
//
//                byte[] pucPHMsg = new byte[1024];//头像
//                int[] puiPHMsgLen = new int[1];
//                byte[] pucFpMsg = new byte[1024];//两个指纹
//                int[] puiFpMsgLen = new int[1];
//
//                int ret = ReadBaseMsg(pucPHMsg, puiPHMsgLen, pucFpMsg, puiFpMsgLen);
//
//                if (ret == 0x90) {
//                    int[] puiCHMsgLen = new int[1];
//                    byte[] pucCHMsg = new byte[256];
//                    try {
//                        char[] pucCHMsgStr = new char[128];
//                        DecodeByte(pucCHMsg, pucCHMsgStr);//将读取的身份证中的信息字节，解码
//                        byte[] bmp = new byte[38862];
//                        Bitmap bitmap = GetImage(pucPHMsg, bmp);
//                        if (bitmap != null) {
//                            ViewRe1.setImageBitmap(bitmap);
//                            ViewRe.setText("获取头像成功");
//                        } else {
//                            ViewRe.setText("获取头像失败:图像解析失败");
//                            ViewRe1.setImageResource(R.mipmap.ic_launcher);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ViewRe.setText("获取头像失败:" + e.getMessage());
//                        ViewRe1.setImageResource(R.mipmap.ic_launcher);
//                    }
//                } else {
//                    ViewRe.setText("获取头像失败:" + ret);
//                    ViewRe1.setImageResource(R.mipmap.ic_launcher);
//                }
//            }//end onclick()
//
//        });
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mUsbReceiver);
//    }
//
//    //广播接收器
//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            //USB设备拔出广播
//            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
//                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                String deviceName = device.getDeviceName();
//                if (device != null && device.equals(deviceName)) {
//                    Message msg = new Message();
//                    msg.what = 2;
//                    msg.obj = "USB设备拔出，应用程序即将关闭。";
//                    MyHandler.sendMessage(msg);
//
//                }
//
//            } else if (common.ACTION_USB_PERMISSION.equals(action)) {//USB设备未授权，从SDTAPI中发出的广播
//                Message msg = new Message();
//                msg.what = 3;
//                msg.obj = "USB设备无权限";
//                MyHandler.sendMessage(msg);
//            }
//        }
//    };
//
//
//    //字节解码函数
//    void DecodeByte(byte[] msg, char[] msg_str) throws Exception {
//        byte[] newmsg = new byte[msg.length + 2];
//        newmsg[0] = (byte) 0xff;
//        newmsg[1] = (byte) 0xfe;
//        System.arraycopy(msg, 0, newmsg, 2, msg.length);
//        String s = new String(newmsg, StandardCharsets.UTF_16);
//        for (int i = 0; i < s.toCharArray().length; i++) {
//            msg_str[i] = s.toCharArray()[i];
//        }
//    }
//
//    //读取身份证中的文字信息（可阅读格式的）
//    public int ReadBaseMsgToStr(IdCardMsg msg) {
//        int ret;
//
//        int[] puiCHMsgLen = new int[1];
//        int[] puiPHMsgLen = new int[1];
//
//        byte[] pucCHMsg = new byte[256];
//        byte[] pucPHMsg = new byte[1024];
//
//
//        //sdtapi中标准接口，输出字节格式的信息。
//        ret = sdta.SDT_ReadBaseMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen);
//
//        if (ret == 0x90) {
//            try {
//                char[] pucCHMsgStr = new char[128];
//                DecodeByte(pucCHMsg, pucCHMsgStr);//将读取的身份证中的信息字节，解码成可阅读的文字
//                PareseItem(pucCHMsgStr, msg); //将信息解析到msg中
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return ret;
//    }
//
//
//    //读取身份证中的头像信息
//    public int ReadBaseMsg(byte[] pucPHMsg, int[] puiPHMsgLen, byte[] pucFpMsg, int[] puiFpMsgLen) {
//        int ret;
//        int[] puiCHMsgLen = new int[1];
//        byte[] pucCHMsg = new byte[256];
//
//        //sdtapi中标准接口，输出字节格式的信息。
//        return sdta.SDT_ReadBaseFPMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen, pucFpMsg, puiFpMsgLen);
//    }
//
//    /****************************************************************************
//     功  能：居民身份证(港澳台居民居住证)- 图片
//     参  数：cardInfo  - 输入，卡信息
//     bBMPFile  - 输出，解码后BMP图片数据，38862字节
//     返  回：ERRCODE_SUCCESS(0)	成功
//     其他				失败
//     *****************************************************************************/
//    public Bitmap GetImage(byte[] cardInfo, byte[] bBMPFile) {
//        byte[] tmp = new byte[1024];
//        System.arraycopy(cardInfo, 0, tmp, 0, tmp.length);
//        int mPhotoSize = 38862;
//        if (bBMPFile.length < mPhotoSize)
//            return null;
//        int miaxis_wlt2BgrData = Wlt2BmpCall.miaxis_Wlt2BgrData(tmp, bBMPFile);
//        Log.e("保存身份证图像", "Wlt2Bgr:" + miaxis_wlt2BgrData);
//        if (miaxis_wlt2BgrData == 0) {
//            return Wlt2BmpCall.miaxis_Bgr2Bitmap(102, 126, bBMPFile);
//        }
//        return null;
//    }
//
//    //分段信息提取
//    void PareseItem(char[] pucCHMsgStr, IdCardMsg msg) {
//        msg.name = String.copyValueOf(pucCHMsgStr, 0, 15);
//        String sex_code = String.copyValueOf(pucCHMsgStr, 15, 1);
//
//        if (sex_code.equals("1"))
//            msg.sex = "男";
//        else if (sex_code.equals("2"))
//            msg.sex = "女";
//        else if (sex_code.equals("0"))
//            msg.sex = "未知";
//        else if (sex_code.equals("9"))
//            msg.sex = "未说明";
//
//        String nation_code = String.copyValueOf(pucCHMsgStr, 16, 2);
//        msg.nation_str = nation[Integer.valueOf(nation_code) - 1];
//
//
//        msg.birth_year = String.copyValueOf(pucCHMsgStr, 18, 4);
//        msg.birth_month = String.copyValueOf(pucCHMsgStr, 22, 2);
//        msg.birth_day = String.copyValueOf(pucCHMsgStr, 24, 2);
//        msg.address = String.copyValueOf(pucCHMsgStr, 26, 35);
//        msg.id_num = String.copyValueOf(pucCHMsgStr, 61, 18);
//        msg.sign_office = String.copyValueOf(pucCHMsgStr, 79, 15);
//
//        msg.useful_s_date_year = String.copyValueOf(pucCHMsgStr, 94, 4);
//        msg.useful_s_date_month = String.copyValueOf(pucCHMsgStr, 98, 2);
//        msg.useful_s_date_day = String.copyValueOf(pucCHMsgStr, 100, 2);
//
//        msg.useful_e_date_year = String.copyValueOf(pucCHMsgStr, 102, 4);
//        msg.useful_e_date_month = String.copyValueOf(pucCHMsgStr, 106, 2);
//        msg.useful_e_date_day = String.copyValueOf(pucCHMsgStr, 108, 2);
//
//    }


}