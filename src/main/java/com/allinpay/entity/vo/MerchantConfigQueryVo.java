package com.allinpay.entity.vo;

import lombok.Data;

@Data
public class MerchantConfigQueryVo {
    private Integer kid;

    private String merchantId;

    private String merchantName;

    private String activityId;

    private String status;

    private String insertTime;

    private String updateTime;

    private Integer pageNum;

    private Integer pageSize;

    private String appId;

    private String secretKey;
}