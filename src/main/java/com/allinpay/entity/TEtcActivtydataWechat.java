package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信渠道的报表数据(TEtcActivtydataWechat)实体类
 *
 * @author makejava
 * @since 2021-01-19 13:21:05
 */
@Data
@TableName("T_ETC_ACTIVTYDATA_WECHAT")
@NoArgsConstructor
@AllArgsConstructor
public class TEtcActivtydataWechat implements Serializable {
    private static final long serialVersionUID = 415658518990124204L;
    /**
     * 数据主键
     */
    @TableId(value = "AC_WE_ID",type = IdType.AUTO)
    private Integer acWeId;
    /**
     * 批次id
     */
    @NotBlank
    private String acBatchId;
    /**
     * 优惠id
     */
    @NotBlank
    private String acDiscountId;
    /**
     * 优惠类型
     */
    @NotBlank
    private String acDiscountType;
    /**
     * 优惠金额（元）
     */
    @NotNull
    private BigDecimal acDiscountNum;
    /**
     * 订单总金额（元）
     */
    @NotNull
    private BigDecimal acOrderAmount;
    /**
     * 交易类型
     */
    @NotBlank
    private String acExchangeType;
    /**
     * 支付单号
     */
    @NotBlank
    private String acPaymentId;
    /**
     * 消耗时间
     */
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date acPaytime;
    /**
     * 消耗商户号
     */
    private String acPaymerchantId;
    /**
     * 商户名称
     */
    private String acPaymerchantName;
    /**
     * 设备号
     */
    private String acEquimentId;
    /**
     * 银行流水号
     */
    @NotBlank
    private String acBankNum;

    private Date cteateTime;

    private Date modifyTime;

    public void setAcPaymentId(String acPaymentId) {
        if (StringUtils.isBlank(acPaymentId))
            return;

        this.acPaymentId = acPaymentId.replace("`","");
    }

    public void setAcBankNum(String acBankNum) {
        if (StringUtils.isBlank(acBankNum))
            return;

        this.acBankNum = acBankNum.replace("`","");
    }
}