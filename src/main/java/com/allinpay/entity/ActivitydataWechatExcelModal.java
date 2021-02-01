package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class ActivitydataWechatExcelModal extends BaseRowModel implements Serializable {

    @ExcelProperty(value="批次id",index=0)
    private String acBatchId;
    /**
     * 优惠id
     */
    @ExcelProperty(value="优惠id",index=1)
    private String acDiscountId;
    /**
     * 优惠类型
     */
    @ExcelProperty(value="优惠类型",index=2)
    private String acDiscountType;
    /**
     * 优惠金额（元）
     */
    @ExcelProperty(value="优惠金额（元）",index=3)
    private BigDecimal acDiscountNum;
    /**
     * 订单总金额（元）
     */
    @ExcelProperty(value="订单总金额（元）",index=4)
    private BigDecimal acOrderAmount;
    /**
     * 交易类型
     */
    @ExcelProperty(value="交易类型",index=5)
    private String acExchangeType;
    /**
     * 支付单号
     */
    @ExcelProperty(value="支付单号",index=6)
    private String acPaymentId;
    /**
     * 消耗时间
     */
    @ExcelProperty(value="消耗时间",index=7)
    private Date acPaytime;


    /**
     * 银行流水号
     */
    @ExcelProperty(value="银行流水号",index=8)
    private String acBankNum;


    /**
     * 商户名称
     */
    @ExcelProperty(value="商户名称",index=9)
    private String acPaymerchantName;

}
