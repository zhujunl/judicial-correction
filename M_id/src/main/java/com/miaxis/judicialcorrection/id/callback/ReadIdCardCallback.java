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

    void onIdCardRead(IdCard result);

}
