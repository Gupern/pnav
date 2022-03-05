package com.gupern.pnav.common.baseInterface;

/**
 * @author: Gupern
 * @date: 2022/3/6 1:54
 * @description: 全局异常处理 的 错误信息接口
 */
public interface BaseErrorInfoInterface {
    /** 错误码*/
    String getResultCode();

    /** 错误描述*/
    String getResultMsg();
}

