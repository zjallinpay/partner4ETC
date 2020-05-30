package com.allinpay.entity;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020-05-26
 **/
@Data
public class RentOrderVO {
    private String orderNo;
    private String payTime;
    private String amount;
    private String tenantName;
    private String tenantPhone;
    private String stallName;
    private String rentTime;
    /**
     * 是否可续租 0 可续租 1 不可续租
     */
    private String continueRent;
}
