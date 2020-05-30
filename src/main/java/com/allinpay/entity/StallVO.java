package com.allinpay.entity;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020-05-30
 **/
@Data
public class StallVO {
    private Integer areaId;
    private String area;
    private String stall;
    private String status;
    private String rentStatus;
    private String rentStartTime;
    private String rentEndTime;
}
