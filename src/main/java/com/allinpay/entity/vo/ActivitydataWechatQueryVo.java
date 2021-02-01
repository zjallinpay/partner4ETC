package com.allinpay.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 微信渠道数据查询
 * */
@Data
public class ActivitydataWechatQueryVo {


    /**
     * 批次id
     */
    private String acBatchId;

    /**
     * 交易类型
     */
    private String acExchangeType;

    /**
     * 消耗时间开始
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date acPaytimeStart;


    /**
     * 消耗时间结束
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date acPaytimeEnd;


    /**
     * 消耗商户号
     */
    private String acPaymerchantId;

    private Integer pageNo;

    private Integer pageSize;

    public Integer getPageNo() {
        if (pageNo==null){
            return 1;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize==null){
            return 10;
        }
        return pageSize;
    }
}
