package com.sixj.dispatcher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sixj.dispatcher.common.LiteRestResponse;
import com.sixj.dispatcher.entity.Dispatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sixiaojie
 * @date 2020-09-02-11:24
 */
public interface DispatcherService extends IService<Dispatcher> {

    /**
     * 新增请求
     * @param dispatcher
     * @return
     */
    Integer insertRequest(Dispatcher dispatcher);

    /**
     * 编辑请求
     * @param dispatcher
     * @return
     */
    Boolean updateRequest(Dispatcher dispatcher);


    /**
     * 执行请求转发
     * @param dispatcherCode
     * @param dispatcherUrl
     * @param request
     * @return
     */
    LiteRestResponse execute(Integer dispatcherCode, String dispatcherUrl, HttpServletRequest request);

}
