package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 物业统计
 *
 * */
@Data
public class PropertyStatsModal extends BaseRowModel implements Serializable {
    //序号
    @ExcelProperty(value="序号",index=0)
    private String sort;
    //批次号
    @ExcelProperty(value="活动名称",index=1)
    private String acName;

    //地区
    @ExcelProperty(value="地区",index=2)
    private String area;

    //门店
    @ExcelProperty(value="小区",index=3)
    private String merName;

    //活跃用户数(唯一银行流水号数)
    @ExcelProperty(value="活跃用户数",index=4)
    private String activeUserCount;
    //活跃用户数(唯一银行流水号数)
    @ExcelProperty(value="微信优惠金额",index=5)
    private String wechatDiscountAmount;
    @ExcelProperty(value="银行卡优惠金额",index=6)
    private String bnakDiscountAmount;
    @ExcelProperty(value="微信缴费金额",index=7)
    private String wechatAmount;

    @ExcelProperty(value="银行卡缴费金额",index=8)
    private String bankAmount;
    //核销数
    @ExcelIgnore
    private String checkNum;
    //优惠金额
    @ExcelIgnore
    private BigDecimal discountNum;

    //商户号
    @ExcelIgnore
    private String acMerchantId;





}
