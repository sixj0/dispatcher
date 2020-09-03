package com.sixj.dispatcher.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应code枚举
 * @author sixiaojie
 * @date 2020-09-03-17:40
 */
@AllArgsConstructor
@Getter
public enum BaseRespStatusEnum {

    /**
     * 200：操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 10000+：通用异常code
     */
    SYSTEM_ERROR(10000, "系统异常"),
    PARAM_ERROR(10001, "请求参数错误"),
    BIZ_ERROR(10002, "业务异常"),
    SYSTEM_BUSY_ERROR(10003, "系统繁忙，请稍后重试"),
    ;

    /**
     * 响应code
     */
    private int status;
    /**
     * 响应message
     */
    private String message;

}
