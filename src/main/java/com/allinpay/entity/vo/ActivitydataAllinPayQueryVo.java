package com.allinpay.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收银宝渠道数据查询
 * */
@Data
public class ActivitydataAllinPayQueryVo {


    /**
     * 批次id
     */
    private String acBatchId;


    /**
     * 消耗时间开始
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTimeStart;


    /**
     * 消耗时间结束
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTimeEnd;


    /**
     * 商户号
     */
    private String acMerchantId;
    /**
     * 门店名称
     */
    private String acMerchantName;
    /**
     * 交易状态
     */
    private String acExchangeStatues;

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
