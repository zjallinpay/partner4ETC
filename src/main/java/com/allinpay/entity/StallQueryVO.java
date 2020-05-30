package com.allinpay.entity;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020-05-30
 **/
@Data
public class StallQueryVO {
    private Integer areaId;
    private String stall;
    private String status;
    private Integer pageNum;
    private Integer pageSize;
}
