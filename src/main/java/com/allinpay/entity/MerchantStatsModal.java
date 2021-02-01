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
 * 商圈统计
 *
 * */
@Data
public class MerchantStatsModal extends BaseRowModel implements Serializable {
    //序号
    @ExcelProperty(value="序号",index=0)
    private String sort;
    //批次号
    @ExcelProperty(value="批次号",index=1)
    private String acBatchId;
    //地区
    @ExcelProperty(value="地区",index=2)
    private String area;
    //活动商圈
    @ExcelProperty(value="活动商圈",index=3)
    private String tradingArea;
    //门店
    @ExcelProperty(value="门店",index=4)
    private String merName;
    //商户号
    @ExcelIgnore
    private String acMerchantId;

    //满减
    @ExcelIgnore
    private BigDecimal discountNum;

    //核销数
    @ExcelIgnore
    private Integer checkNum;
    //满减/核销数
    @ExcelProperty(value="满减/核销数",index=5)
    private String discountAndCheckText;

    //优惠金额（万元）
    @ExcelProperty(value="优惠金额（万元）",index=6)
    private BigDecimal discountAmount;
    //设备号
    @ExcelIgnore
    private String equipId;

    public String getDiscountAndCheckText() {
        if (StringUtils.isNotBlank(discountAndCheckText)){
            return discountAndCheckText;
        }
        return discountNum+"/"+checkNum;
    }
}
