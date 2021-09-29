package org.zz.protocol;

import android.os.*;

public class MXLog
{
    public static boolean LOG_MSG;
    public static final int SHOW_MSG = 255;
    public static final int CLEAR_MSG = 256;
    private static Handler m_fHandler;
    
    public static void SetHandler(final Handler fHandler) {
        MXLog.m_fHandler = fHandler;
    }
    
    public static void SendMsg(final String obj) {
        if (!MXLog.LOG_MSG) {
            return;
        }
        final Message message = new Message();
        message.what = 255;
        message.obj = obj;
        message.arg1 = 0;
        if (MXLog.m_fHandler != null) {
            MXLog.m_fHandler.sendMessage(message);
        }
    }
    
    public static void ClearMsg() {
        if (!MXLog.LOG_MSG) {
            return;
        }
        final Message message = new Message();
        message.what = 256;
        message.obj = null;
        message.arg1 = 0;
        if (MXLog.m_fHandler != null) {
            MXLog.m_fHandler.sendMessage(message);
        }
    }
    
    static {
        MXLog.LOG_MSG = false;
        MXLog.m_fHandler = null;
    }
}
