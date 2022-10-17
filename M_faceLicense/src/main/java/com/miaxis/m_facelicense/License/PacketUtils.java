package com.miaxis.m_facelicense.License;

public class PacketUtils {
    public PacketUtils() {
    }

    public static byte[] makeHeader(byte[] body, int BIOType, int fun) {
        byte[] dataLength = int2bytes(body.length + 12);
        byte[] bfun = int2bytes(fun);
        byte[] bioType = int2bytes(BIOType);
        byte[] btss = byteMerger(dataLength, bioType);
        return byteMerger(btss, bfun);
    }

    public static byte[] makeHeader(byte[] body, int fun) {
        byte[] dataLength = int2bytes(body.length + 12);
        byte[] bfun = int2bytes(fun);
        return byteMerger(dataLength, bfun);
    }

    public static byte[] int2bytes(int num) {
        byte[] result = new byte[]{(byte) (num >>> 24 & 255), (byte) (num >>> 16 & 255), (byte) (num >>> 8 & 255), (byte) (num >>> 0 & 255)};
        return result;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] makeField(byte[] value) {
        return byteMerger(int2bytes(value.length), value);
    }

    public static byte[] makePacket(byte[] body, int BIOType, int fun) {
        byte[] data = byteMerger(makeHeader(body, BIOType, fun), body);
        return data;
    }

    public static byte[] makePacket(byte[] body, int fun) {
        byte[] data = byteMerger(makeHeader(body, fun), body);
        return data;
    }
}
