package com.allinpay.entity;

import lombok.Data;

@Data
public class CJMOrder {
    private String orderNo;
    private String trxId;
    private String srcTrxId;
    private String terminalNo;
    private String payStatus;
    private String erpStatus;
    private String payAmount;
    private String payTime;
    private String payType;
    private String tlCustId;
    private String bizJson;
    private String extInfo;
    private String insertTime;
    private String updateTime;
    private String fee;
    private String notifyJson;
    private String userId;
    private String activityId;
    private String discountAmount;
    private String sourceAmount;
}