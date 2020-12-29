package com.allinpay.entity.vo;

import lombok.Data;
/**
 * 条件查询商户
 *
 * */
@Data
public class MerchantQueryVo {

    private String merName;

    private String belongIndustry;

    private String brandName;

    private String tradingArea;

    private String area;

    private String isAllinpaymer;

    private String expandChannl;

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