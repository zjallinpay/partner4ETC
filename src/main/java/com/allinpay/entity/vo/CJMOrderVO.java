package com.allinpay.entity.vo;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020/11/9
 **/
@Data
public class CJMOrderVO {
    private String orderNo;
    private String trxId;
    private String terminalNo;
    private String merchantId;
    private String merchantName;
    private String payStatus;
    private String payAmount;
    private String discountAmount;
    private String sourceAmount;
    private String payTime;
    private String payType;
    private String extInfo;
}
