package com.allinpay.entity;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020-05-30
 **/
@Data
public class OrderQueryVO {
    private String orderNo;
    private String tenantName;
    private String period;
    private Integer areaId;
    private String stall;
    private Integer pageNum;
    private Integer pageSize;
}
