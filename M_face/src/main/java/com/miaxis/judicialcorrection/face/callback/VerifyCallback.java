package com.miaxis.judicialcorrection.face.callback;

import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

/**
 * @author Tank
 * @date 2021/5/7 10:27
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
