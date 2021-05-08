package com.miaxis.judicialcorrection.leave;

import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tank
 * @date 2021/5/7 14:11
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface LeaveListener {

    /**
     * 请假申请
     */
    void onApply(@NotNull VerifyInfo verifyInfo);

    /**
     * 销假
     */
    void onCancel();

    /**
     * 查询进度
     */
    void onQueryProgress();

}
