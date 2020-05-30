package com.allinpay.entity;

import lombok.Data;


@Data
public class RentOrder {
    private String orderNo;

    private String amount;

    private String payStatus;

    private String payType;

    private String payTime;

    private String tenantName;

    private String tenantPhone;

    private Integer areaId;

    private String area;

    private String stall;

    private String rentStartTime;

    private String rentEndTime;

    private String trxId;

    private String channelTrxId;

    private String openId;

    private String insertTime;

    private String updateTime;

    private String period;

    private String continueRent;
}