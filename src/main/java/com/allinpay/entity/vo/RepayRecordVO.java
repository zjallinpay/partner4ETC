package com.allinpay.entity.vo;

import lombok.Data;

/**
 * author: tanguang
 * data: 2021/1/14
 **/
@Data
public class RepayRecordVO {
    private String batchNo;
    private String traceNum;
    private String transDate;
    private String hldName;
    private String cerNum;
    private Integer acctNum;
    private Integer tradeAmount;
    private Integer result;
    private Integer remark;
}
