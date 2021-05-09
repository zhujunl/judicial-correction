package com.miaxis.judicialcorrection;

/**
 * @author Tank
 * @date 2021/5/7 14:11
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface LiveListener {

    /**
     * 请假申请
     */
    void onApply();

    /**
     * 销假
     */
    void onCancel();

    /**
     * 查询进度
     */
    void onQueryProgress();

}
