package com.miaxis.judicialcorrection.base.common;

/**
 *
 * 访问网络中的状态
 * @author yawei
 * @data on 17-10-30  上午9:46
 * @org www.komlin.com
 * @email zyawei@live.com
 */

public enum Status {
    /**
     * 正在加载中
     */
    LOADING,
    /**
     * 加载失败
     *
     * 与{@link #SUCCESS}一定且仅回调一个,一次
     */
    ERROR,

    /**
     * 加载成功
     *
     * 与{@link #ERROR}一定且仅回调一个,一次
     */
    SUCCESS

}
