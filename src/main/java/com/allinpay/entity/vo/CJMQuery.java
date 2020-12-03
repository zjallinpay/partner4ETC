package com.allinpay.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * author: tanguang
 * data: 2020/11/9
 **/
@Data
public class CJMQuery {
    private String merchantId;
    private String orderNo;
    private String payStatus;
    private Integer pageNum;
    private Integer pageSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeEndTime;
}
