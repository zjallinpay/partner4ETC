package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动信息Excel
 *
 * @author makejava
 * @since 2020-12-17 13:56:27
 */
@Data
@NoArgsConstructor
public class ActivityExcelModal extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 活动名称
     */
    @ExcelProperty(value="活动名称",index=0)
    private String activityName;
    /**
     * 表示参与活动的渠道 枚举类型值：收银宝活动、微信原生活动、支付宝原生活动、云闪付原生活动、其他类型活动
     */
    @ExcelProperty(value="活动渠道",index=1)
    private String activityChnnal;
    /**
     * 活动批次号
     */
    @ExcelProperty(value="活动批次号",index=2)
    private String activityBatchno;
    /**
     * 枚举类型值：代金券、随机立减、折扣、满减。
     */
    @ExcelProperty(value="优惠类型",index=3)
    private String discountType;
    /**
     * 枚举类型值：免充值、预充值（垫资）、预充值（不垫资）
     */
    @ExcelProperty(value="资金模式",index=6)
    private String fundType;
    /**
     * 活动主办人
     */
    @ExcelProperty(value="活动拓展人",index=7)
    private String activityMaster;

    /**
     * 活动开始时间
     */
    @ExcelProperty(value="活动开始时间",index=4)
    private Date startTime;
    /**
     * 活动结束时间
     */
    @ExcelProperty(value="活动结束时间",index=5)
    private Date endTime;
    /**
     * 可用开始时间
     */
    @ExcelProperty(value="活动可用时间段开始时间",index=9)
    private String ablestartTime;
    /**
     * 可用结束时间
     */
    @ExcelProperty(value="活动可用时间段结束时间",index=10)
    private String ableendTime;
    /**
     * 可用的周
     */
    @ExcelProperty(value="活动可用日期周",index=8)
    private String ableWeek;
    /**
     * 发卡行限制
     */
    @ExcelProperty(value="发卡行限制",index=11)
    private String bankLimit;
    /**
     * 活动说明
     */
    @ExcelProperty(value="活动说明",index=12)
    private String activityRemark;

    /**
     * 活动类型 商圈、物业
     */
    @ExcelProperty(value="活动类型",index=13)
    private String activityType;
}