package com.sixj.dispatcher.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应格式
 * @author sixiaojie
 * @date 2020-09-03-17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiteRestResponse<T> {

    private T data;
    private boolean rel;
    private int status;
    private String message;

    /**
     * 成功返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> LiteRestResponse<T> success(T data) {
        return new LiteRestResponse<>(
                data, true, BaseRespStatusEnum.SUCCESS.getStatus(), BaseRespStatusEnum.SUCCESS.getMessage());
    }

    /**
     * 成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> LiteRestResponse<T> success() {
        return success(null);
    }

    /**
     * 错误返回
     *
     * @param
     * @return
     */
    public static LiteRestResponse fail(BaseRespStatusEnum respStatusEnum) {
        return fail(respStatusEnum.getStatus(), respStatusEnum.getMessage());
    }

    /**
     * 错误返回
     *
     * @param status  错误码
     * @param message 错误信息
     * @return
     */
    public static LiteRestResponse fail(int status, String message) {
        return new LiteRestResponse<>(null, false, status, message);
    }

}
