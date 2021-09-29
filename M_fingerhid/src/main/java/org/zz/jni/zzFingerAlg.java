package org.zz.jni;

public class zzFingerAlg
{
    public native int mxGetVersion(final byte[] p0);
    
    public native int mxGetTzBase64(final byte[] p0, final byte[] p1);
    
    public native int mxGetMBBase64(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public native int mxFingerMatchBase64(final byte[] p0, final byte[] p1, final int p2);
    
    public native int mxGetTz256(final byte[] p0, final byte[] p1);
    
    public native int mxGetMB256(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public native int mxFingerMatch256(final byte[] p0, final byte[] p1, final int p2);
    
    public native int mxGetTz512(final byte[] p0, final byte[] p1);
    
    public native int mxGetMB512(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public native int mxFingerMatch512(final byte[] p0, final byte[] p1, final int p2);
    
    public native int mxGetTz512ASCII(final byte[] p0, final byte[] p1);
    
    public native int mxGetMB512ASCII(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public native int mxFingerMatch512ASCII(final byte[] p0, final byte[] p1, final int p2);
    
    public native int mxGetTz520(final byte[] p0, final byte[] p1);
    
    public native int mxGetMB520(final byte[] p0, final byte[] p1, final byte[] p2, final byte[] p3);
    
    public native int mxFingerMatch520(final byte[] p0, final byte[] p1, final int p2);
    
    public native int mxGetTzBase64FromISO(final byte[] p0, final byte[] p1);
    
    public native int mxImgToIso(final byte[] p0, final int p1, final int p2, final int p3, final byte[] p4);
    
    public native int mxGetTz512FromISO(final byte[] p0, final byte[] p1);
    
    public native int mxGetTz512From256X360(final byte[] p0, final byte[] p1);
    
    public native int mxGetTz512ASCIIFrom256X360(final byte[] p0, final byte[] p1);
}
