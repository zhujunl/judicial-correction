package com.miaxis.judicialcorrection.face.callback;


import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

/**
 * @author Tank
 * @date 2021/4/29 10:33 AM
 * @des
 * @updateAuthor
 * @updateDes
 */

public interface VerifyCallback {

    /**
     * 身份核验回调
     */
    void onVerify(ZZResponse<VerifyInfo> response);

}
