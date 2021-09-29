package org.zz.jni;

public class mxImgRepair
{
    public native int VerificationKeyData(final int p0, final byte[] p1);
    
    public native int FP_UPEKReapir(final byte[] p0, final int p1, final int p2);
    
    public native int FP_OPTICReapir(final byte[] p0, final int p1, final int p2);
    
    public native int Scale(final byte[] p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final byte[] p7, final int p8, final int p9);
    
    public native int CalcFingerArea256X360(final byte[] p0, final int p1, final int p2);
    
    static {
        System.loadLibrary("mxImgRepair");
    }
}
