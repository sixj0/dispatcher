package com.sixj.dispatcher.controller;

import com.sixj.dispatcher.common.LiteRestResponse;
import com.sixj.dispatcher.entity.Dispatcher;
import com.sixj.dispatcher.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sixiaojie
 * @date 2020-09-02-11:55
 */
@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {
    @Autowired
    private DispatcherService dispatcherService;

    /**
     * 新增请求
     * @param dispatcher
     * @return
     */
    @PostMapping("/insertRequest")
    public LiteRestResponse insertRequest(@RequestBody Dispatcher dispatcher){
        return LiteRestResponse.success(dispatcherService.insertRequest(dispatcher));
    }

    /**
     * 编辑请求
     * @param dispatcher
     * @return
     */
    @PostMapping("/updateRequest")
    public LiteRestResponse updateRequest(@RequestBody Dispatcher dispatcher){
        return LiteRestResponse.success(dispatcherService.updateRequest(dispatcher));
    }


    /**
     * 执行请求转发
     * @param dispatcherCode
     * @param dispatcherUrl
     * @param request
     * @return
     */
    @PostMapping("/execute")
    public LiteRestResponse execute(@RequestParam("dispatcherCode") Integer dispatcherCode,
                                    @RequestParam(value = "dispatcherUrl",required = false) String dispatcherUrl,
                                    HttpServletRequest request){
        return dispatcherService.execute(dispatcherCode,dispatcherUrl,request);
    }

}
