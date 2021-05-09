package com.miaxis.judicialcorrection.leave;

import com.miaxis.judicialcorrection.base.api.vo.Leave;
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
    void onCancel(@NotNull VerifyInfo verifyInfo,@NotNull Leave.ListBean listBean);

    /**
     * 查询进度
     */
    void onQueryProgress(@NotNull VerifyInfo verifyInfo,@NotNull Leave.ListBean listBean);

}
