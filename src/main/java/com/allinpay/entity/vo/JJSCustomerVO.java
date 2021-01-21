package com.allinpay.entity.vo;

import lombok.Data;

/**
 * author: tanguang
 * data: 2021/1/12
 **/
@Data
public class JJSCustomerVO {
    private String hldName;
    private String cerNum;
    private String acctNum;
    private String tradeAmount;
    private String result;
    private String remark;

    public JJSCustomerVO(String hldName, String cerNum, String acctNum, String tradeAmount) {
        this.hldName = hldName;
        this.cerNum = cerNum;
        this.acctNum = acctNum;
        this.tradeAmount = tradeAmount;
    }

    public JJSCustomerVO(String hldName, String cerNum, String acctNum, String tradeAmount, String result, String remark) {
        this.hldName = hldName;
        this.cerNum = cerNum;
        this.acctNum = acctNum;
        this.tradeAmount = tradeAmount;
        this.result = result;
        this.remark = remark;
    }
}
