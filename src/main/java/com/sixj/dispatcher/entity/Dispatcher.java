package com.sixj.dispatcher.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sixiaojie
 * @date 2020-09-02-11:13
 */
@Data
public class Dispatcher implements Serializable {

    /** 编号 */
    private Integer code;
    /** 描述 */
    private String description;
    /** 请求方式 */
    private String requestMethod;
    /** 请求地址 */
    private String requestUrl;
    /** 其他系统所需要的请求头，多个用逗号隔开 */
    private String requestHeaders;
    /** 返回值数据结构对应关系（data） */
    private String data;
    /** 返回值数据结构对应关系（status） */
    private String status;
    /** 成功状态码 */
    private String statusOk;
    /** 返回值数据结构对应关系（message） */
    private String message;

    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
    /** 是否删除 */
    private Boolean deleted;

}
