package com.allinpay.entity;

import lombok.Data;

@Data
public class MerchantConfigMapping {
    private Integer kid;

    private String merchantId;

    private String merchantName;

    private String activityId;

    private String status;

    private String insertTime;

    private String updateTime;

    private Integer pageNum;

    private Integer pageSize;
}