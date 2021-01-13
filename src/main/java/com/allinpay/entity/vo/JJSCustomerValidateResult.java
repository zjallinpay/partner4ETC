package com.allinpay.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * author: tanguang
 * data: 2021/1/13
 **/
@Data
public class JJSCustomerValidateResult {
    private String batchNo;
    private List<JJSCustomerVO> list;
}
