package com.miaxis.judicialcorrection.id.callback;


import com.miaxis.judicialcorrection.id.bean.IdCard;

/**
 * @author Tank
 * @date 2021/4/29 10:33 AM
 * @des
 * @updateAuthor
 * @updateDes
 */

public interface ReadIdCardCallback {

    /**
     * 读身份证回调
     */
    void onIdCardRead(IdCard result);

}
