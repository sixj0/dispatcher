package com.sixj.dispatcher.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sixj.dispatcher.common.BaseException;
import com.sixj.dispatcher.common.BaseRespStatusEnum;
import com.sixj.dispatcher.common.LiteRestResponse;
import com.sixj.dispatcher.entity.Dispatcher;
import com.sixj.dispatcher.mapper.DispatcherMapper;
import com.sixj.dispatcher.service.DispatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author sixiaojie
 * @date 2020-09-02-11:25
 */
@Service
@Slf4j
public class DispatcherServiceImpl extends ServiceImpl<DispatcherMapper, Dispatcher> implements DispatcherService {

    @Override
    public Integer insertRequest(Dispatcher dispatcher) {
        this.save(dispatcher);
        return dispatcher.getCode();
    }

    @Override
    public Boolean updateRequest(Dispatcher dispatcher) {
        UpdateWrapper<Dispatcher> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("code",dispatcher.getCode());
        return this.update(dispatcher,updateWrapper);
    }


    @Override
    public LiteRestResponse execute(Integer dispatcherCode, String dispatcherUrl, HttpServletRequest request) {
        // 根据编码查询请求转发配置信息
        Dispatcher dispatcher = selectDispatcher(dispatcherCode, dispatcherUrl);

        // header
        String requestHeaders = dispatcher.getRequestHeaders();
        Map<String, String> headerMap = getHeaderMap(requestHeaders, request);

        // 执行请求
        String result = executeRequest(dispatcher, request, headerMap);

        // 处理返回值
        LiteRestResponse response = createResponse(dispatcher, result);
        return response;
    }

    /**
     * 查询请求转发的配置信息
     * @param dispatcherCode
     * @param dispatcherUrl
     * @return
     */
    private Dispatcher selectDispatcher(Integer dispatcherCode,String dispatcherUrl){
        QueryWrapper<Dispatcher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",dispatcherCode);
        Dispatcher dispatcher = this.getOne(queryWrapper);
        if(Objects.isNull(dispatcher)){
            throw new BaseException("编码不存在");
        }
        // 使用自定义的请求地址
        if(!StringUtils.isEmpty(dispatcherUrl)){
            dispatcher.setRequestUrl(dispatcherUrl);
        }
        return dispatcher;
    }


    /**
     * 获取请求头信息
     * @param requestHeaders
     * @param request
     * @return
     */
    private Map<String, String> getHeaderMap(String requestHeaders,HttpServletRequest request){
        Set<String> headerNameList = new HashSet<>();
        if(!StringUtils.isEmpty(requestHeaders.trim())){
            List<String> split = StrUtil.split(requestHeaders, ',', true, true);
            headerNameList.addAll(split);
        }
        // 默认Content-Type
        headerNameList.add("Content-Type");
        Map<String, String> headerMap = new HashMap<>(2);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            if(headerNameList.contains(headerName)){
                headerMap.put(headerName,request.getHeader(headerName));
            }
        }
        return headerMap;
    }


    /**
     * 执行请求
     * @param dispatcher
     * @param request
     * @return
     */
    private String executeRequest(Dispatcher dispatcher, HttpServletRequest request, Map<String, String> headerMap){
        // 请求地址
        String requestUrl = dispatcher.getRequestUrl();
        // 请求方式
        String requestMethod = dispatcher.getRequestMethod();

        HttpRequest httpRequest = null;

        // 请求超时时间10s
        int timeout = 10*1000;

        // post请求
        String postMethod = "post";
        String json = "json";
        if(postMethod.equals(requestMethod.toLowerCase())){
            if (request.getContentType() != null && request.getContentType().contains(json)) {
                // json
                String jsonData = getJsonData(request);
                httpRequest = HttpRequest.post(requestUrl).body(jsonData);
            } else {
                // formData
                Map<String, Object> formData = getFormData(request);
                httpRequest = HttpRequest.post(requestUrl).form(formData);
            }
        }
        // get请求
        String getMethod = "get";
        if(getMethod.equals(requestMethod.toLowerCase())){
            Map<String, Object> formData = getFormData(request);
            httpRequest = HttpRequest.get(requestUrl).form(formData);
        }
        if(httpRequest == null){
            throw new BaseException("请求转发只支持post与get请求");
        }
        String result;
        try {
            result = httpRequest.timeout(timeout)
                    .headerMap(headerMap, true)
                    .execute()
                    .body();
        }catch (Exception e){
            throw new BaseException("接口请求异常:"+e.getMessage()+",接口地址："+requestUrl);
        }

        return result;
    }

    /**
     * 处理返回值数据结构
     * @param dispatcher
     * @param result
     * @return
     */
    private LiteRestResponse createResponse(Dispatcher dispatcher,String result){
        String dataName = dispatcher.getData();
        String statusName = dispatcher.getStatus();
        String messageName = dispatcher.getMessage();
        String statusOk = dispatcher.getStatusOk();

        boolean isJsonObj = JSONUtil.isJsonObj(result);
        if(!isJsonObj){
            throw new BaseException("无法解析返回结果,因为不是json对象");
        }
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result);
        String data = jsonObject.getStr(dataName);
        String status = jsonObject.getStr(statusName);
        String message = jsonObject.getStr(messageName);
        if(statusOk.equals(status)){
            return LiteRestResponse.success(data);
        }else {
            return LiteRestResponse.fail(BaseRespStatusEnum.SYSTEM_ERROR.getStatus(),message);
        }
    }

    /**
     * 获取json数据
     * @param request
     * @return
     */
    private String getJsonData(HttpServletRequest request){
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(request.getInputStream(), request.getCharacterEncoding());
            BufferedReader reader = new BufferedReader(is);
            //将json数据放到String中
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取formData数据
     * @param request
     * @return
     */
    private Map<String,Object> getFormData(HttpServletRequest request){
        Map<String, Object> formData = new HashMap<>(2);
        //获取前台传来的参数
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            // 剔除请求转发接口本身的参数
            if("dispatcherCode".equals(name) || "dispatcherUrl".equals(name)){
                continue;
            }
            formData.put(name,request.getParameter(name));
        }
        return formData;
    }



}
