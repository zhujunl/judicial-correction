package com.fingerprint;

public class zzFingerAlgID
{
    public static native int mxGetVersion(final byte[] p0);
    
    public static native int mxGetTz512(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTz512FromISO(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTz512From152X200(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTz512From256X304(final byte[] p0, final byte[] p1);
    
    public static native int mxGetMB512(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public static native int mxFingerMatch512(final byte[] p0, final byte[] p1, final int p2);
    
    public static native int mxFingerUsersMatch512(final byte[] p0, final int p1, final byte[] p2, final int p3);
    
    public static native int mxGetTzBase64(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTzBase64FromISO(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTzBase64From152X200(final byte[] p0, final byte[] p1);
    
    public static native int mxGetTzBase64From256X304(final byte[] p0, final byte[] p1);
    
    public static native int mxGetMBBase64(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public static native int mxFingerMatchBase64(final byte[] p0, final byte[] p1, final int p2);
    
    public static native int mxFingerUsersMatchBase64(final byte[] p0, final int p1, final byte[] p2, final int p3);
    
    public static native int mxIDFingerMatch512Score(final byte[] p0, final byte[] p1, final int[] p2);
    
    static {
        System.loadLibrary("mxFingerAlgId");
    }
}
