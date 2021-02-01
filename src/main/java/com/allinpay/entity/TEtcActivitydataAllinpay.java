package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收银宝渠道的报表数据(TEtcActivitydataAllinpay)实体类
 *
 * @author makejava
 * @since 2021-01-19 13:20:40
 */
@Data
@TableName("T_ETC_ACTIVITYDATA_ALLINPAY")
public class TEtcActivitydataAllinpay implements Serializable {
    private static final long serialVersionUID = 570939213676874685L;
    /**
     * 主键
     */
    @TableId(value = "AC_ALLIN_ID",type = IdType.AUTO)
    private Integer acAllinId;
    /**
     * 交易号
     */
    @NotBlank
    private String acPayNum;
    /**
     * 交易号（补充）
     */
    @NotBlank
    private String acPayNumex;
    /**
     * 活动编号
     */
    @NotBlank
    private String acBatchId;
    /**
     * 商户号
     */
    @NotBlank
    private String acMerchantId;
    /**
     * 门店名称
     */
    @NotBlank
    private String acMerchantName;
    /**
     * 终端号
     */
    @NotBlank
    private String acEquimentId;
    /**
     * 发卡行
     */
    @NotBlank
    private String acBankName;
    /**
     * 账号类型
     */
    @NotBlank
    private String acAccountType;
    /**
     * 账号
     */
    @NotBlank
    private String acAccountNum;
    /**
     * 交易类型
     */
    @NotBlank
    private String acExchangeType;
    /**
     * 交易状态
     */
    @NotBlank
    private String acExchangeStatues;
    /**
     * 交易初始金额
     */
    @NotNull
    private BigDecimal acExchangeSourcenum;
    /**
     * 交易实付金额
     */
    @NotNull
    private BigDecimal acExchangeRealnum;
    /**
     * 交易优惠金额
     */
    @NotNull
    private BigDecimal acDiscountNum;
    /**
     * 合作方出资额
     */
    @NotNull
    private BigDecimal acOrganPaynum;
    /**
     * 商户出资额
     */
    @NotNull
    private BigDecimal acMerchantPaynum;
    /**
     * 合作方id
     */
    private String acCooporganId;
    /**
     * 创建时间
     */
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;



}