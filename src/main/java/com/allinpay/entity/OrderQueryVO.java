package com.allinpay.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeEndTime;
}
