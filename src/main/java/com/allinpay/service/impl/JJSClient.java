package com.allinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.SecretManager;
import com.allinpay.core.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * author: tanguang
 * data: 2021/1/12
 **/
@Component
@Slf4j
public class JJSClient {
    private static final String MD5_KEY = "1234567890";
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 统一入口
     */
    public Map<String, String> execute(Map<String, String> bizMap, String reqUrl) {
        //构建头信息
        paddingHeadInfo(bizMap);
        //请求签名
        bizMap.put("sign", SecretManager.sign(bizMap, MD5_KEY));

        //请求
        Map<String, Object> response = restTemplate.postForObject(reqUrl, bizMap, Map.class);
        Map<String, String> responseMap = JSON.parseObject(JSON.toJSONString(response), new TypeReference<Map<String, String>>() {
        }, Feature.OrderedField);
        log.info("jjs response：{}", JSON.toJSONString(responseMap));

        //响应处理
        if (!SecretManager.validSign(responseMap, MD5_KEY)) {
            throw new AllinpayException(BizEnums.SIGN_CHECK_ERROR.getCode(),
                    BizEnums.SIGN_CHECK_ERROR.getMsg());
        }

        if (!(CommonConstant.RESPONSE_SUCCESS_0000.equals(responseMap.get("code")))) {
            throw new AllinpayException(responseMap.get("code"), responseMap.get("msg"));
        }
        return responseMap;
    }

    private void paddingHeadInfo(Map<String, String> bizMap) {
        bizMap.put("instId", ShiroUtils.getUserEntity().getUsername());
        bizMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
    }
}
