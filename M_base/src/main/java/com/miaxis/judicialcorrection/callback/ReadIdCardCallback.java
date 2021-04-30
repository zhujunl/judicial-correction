package com.miaxis.judicialcorrection.callback;


import com.miaxis.judicialcorrection.bean.IdCard;

/**
 * @author Tank
 * @date 2021/4/29 10:33 AM
 * @des
 * @updateAuthor
 * @updateDes
 */

public interface ReadIdCardCallback {

    void onIdCardRead(IdCard result);

}
