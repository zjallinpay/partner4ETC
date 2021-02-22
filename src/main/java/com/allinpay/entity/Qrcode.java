package com.allinpay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Qrcode {
    private String mchtId;
    private String mchtName;
    private String appId;
    private String appKey;
    private String orgId;
    private Integer pageNum;
    private String status;
    private String partnerModel;
    private String updateTime;
    private String signType;
    private Integer pageSize;
}
