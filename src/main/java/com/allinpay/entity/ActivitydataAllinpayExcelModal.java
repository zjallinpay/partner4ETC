package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class ActivitydataAllinpayExcelModal extends BaseRowModel implements Serializable {


    /**
     * 交易号
     */
    @ExcelProperty(value="交易号",index=0)
    private String acPayNum;
    /**
     * 交易号（补充）
     */
    @ExcelProperty(value="交易号（补充)",index=1)
    private String acPayNumex;
    /**
     * 活动编号
     */
    @ExcelProperty(value="活动编号",index=2)
    private String acBatchId;
    /**
     * 商户号
     */
    @ExcelProperty(value="商户号",index=3)
    private String acMerchantId;
    /**
     * 门店名称
     */
    @ExcelProperty(value="门店名称",index=4)
    private String acMerchantName;
    /**
     * 终端号
     */
    @ExcelProperty(value="终端号",index=5)
    private String acEquimentId;
    /**
     * 发卡行
     */
    @ExcelProperty(value="发卡行",index=6)
    private String acBankName;


    /**
     * 账号类型
     */
    @ExcelProperty(value="账号类型",index=7)
    private String acAccountType;
    /**
     * 账号
     */
    @ExcelProperty(value="账号",index=8)
    private String acAccountNum;

    /**
     * 创建时间
     */
    @ExcelProperty(value="账号",index=9)
    private Date createTime;
    /**
     * 交易类型
     */
    @ExcelProperty(value="交易类型",index=10)
    private String acExchangeType;
    /**
     * 交易状态
     */
    @ExcelProperty(value="交易状态",index=11)
    private String acExchangeStatues;
    /**
     * 交易初始金额
     */
    @ExcelProperty(value="交易初始金额",index=12)
    private BigDecimal acExchangeSourcenum;
    /**
     * 交易实付金额
     */
    @ExcelProperty(value="交易实付金额",index=13)
    private BigDecimal acExchangeRealnum;
    /**
     * 交易优惠金额
     */
    @ExcelProperty(value="交易优惠金额",index=14)
    private BigDecimal acDiscountNum;
    /**
     * 合作方出资额
     */
    @ExcelProperty(value="合作方出资额",index=15)
    private BigDecimal acOrganPaynum;
    /**
     * 商户出资额
     */
    @ExcelProperty(value="商户出资额",index=16)
    private BigDecimal acMerchantPaynum;
    /**
     * 合作方id
     */
    @ExcelProperty(value="合作方id",index=17)
    private String acCooporganId;

}
