
package jni;

public class MXLicenseAPI {

    //加载动态库
    static {
        System.loadLibrary("MXLicenseAPI");
    }

    /****************************************************************************
     功  能：	获取版本
     参  数：
     返	回：	版本信息
     *****************************************************************************/
    public native String getVersion();

    /****************************************************************************
     功	能：生成申请数据
     参	数：context       - 输入，上下文
     返 回：申请数据
     ****************************************************************************/
    public native String GenHardwareId(Object context);

    /****************************************************************************
     功	能：验证授权数据
     参	数：context       - 输入，上下文
            szAlgTypeName   - [in]	人脸比对算法mxFaceAPI
                                    人脸搜索算法mxFaceSearchAPI
                                    指纹比对算法mxFingerAPI
                                    指纹搜索算法mxFingerSearchAPI
     szLicenseData	- [in]授权数据
     返	回：0-成功，其他-失败
     ****************************************************************************/
    public native int VerifyLicense(Object context,String szAlgTypeName,String szLicenseData);

}
