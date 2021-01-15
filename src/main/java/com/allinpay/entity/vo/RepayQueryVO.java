package com.allinpay.entity.vo;

import lombok.Data;

/**
 * author: tanguang
 * data: 2021/1/14
 **/
@Data
public class RepayQueryVO {
    private String batchNo;
    private String hldName;
    private String tradeStartTime;
    private String tradeEndTime;
    private String status;
    private Integer pageNum;
    private Integer pageSize;
}
