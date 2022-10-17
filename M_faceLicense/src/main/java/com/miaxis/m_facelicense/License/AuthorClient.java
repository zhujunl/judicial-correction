package com.miaxis.m_facelicense.License;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthorClient {
    static final int TYPE_AUTHOR = 1;
    static final int FUN_AUTHOR = 1001;
    private String host;
    private Integer post;
    private Integer timeOut;

    public AuthorClient(String host, Integer post, Integer timeOut) {
        this.host = host;
        this.post = post;
        this.timeOut = timeOut;
    }

    public String faceAuthor(int iAlgType, String currentDate, String account, String authorKey, String deviceInfo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if ("".equals(this.host)) {
            throw new Exception("请先配置地址");
        } else if ("".equals(this.post)) {
            throw new Exception("请先配置端口");
        } else if (!this.isValidDate(currentDate)) {
            throw new Exception("请输入日期的正确格式:yyyymmdd");
        } else if (Integer.parseInt(sdf.format(new Date())) != Integer.parseInt(currentDate)) {
            throw new Exception("必须为当前时间");
        } else if ("".equals(account)) {
            throw new Exception("请输入公司帐号");
        } else if ("".equals(authorKey)) {
            throw new Exception("请输入授权密钥");
        } else if ("".equals(deviceInfo)) {
            throw new Exception("请输入硬件信息");
        } else {
            InetSocketAddress addr = new InetSocketAddress(this.host, this.post);
            Socket socket = new Socket();

            try {
                socket.setSoTimeout(this.timeOut);
                socket.connect(addr);
            } catch (SocketException var16) {
                throw new Exception("网络异常");
            }

            OutputStream out = socket.getOutputStream();
            byte[] accountbody = PacketUtils.byteMerger(PacketUtils.makeField(currentDate.getBytes("utf-8")),
                    PacketUtils.makeField(account.getBytes("utf-8")));
            byte[] passwordbody = PacketUtils.byteMerger(accountbody, PacketUtils.makeField(authorKey.getBytes("utf-8")));
            byte[] devicebody = PacketUtils.byteMerger(passwordbody, PacketUtils.makeField(deviceInfo.getBytes("utf-8")));
            out.write(PacketUtils.makePacket(devicebody, iAlgType));
            InputStream in = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String info = br.readLine();
            br.close();
            isr.close();
            in.close();
            out.close();
            socket.close();
            return info;
        }
    }

    private boolean isValidDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        try {
            Date date = formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception var4) {
            return false;
        }
    }
}
