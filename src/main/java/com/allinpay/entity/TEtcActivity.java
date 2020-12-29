package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (TEtcActivity)实体类
 *
 * @author makejava
 * @since 2020-12-17 14:39:18
 */
@Data
@TableName("T_ETC_ACTIVITY")
@NoArgsConstructor
public class TEtcActivity implements Serializable {
    private static final long serialVersionUID = -47030637250391570L;
    /**
     * 活动id主键
     */
    @TableId(value = "ACTIVITY_ID",type = IdType.AUTO)
    private Integer activityId;
    /**
     * 活动名称
     */
    @NotBlank
    @TableField("ACTIVITY_NAME")
    private String activityName;
    /**
     * 表示参与活动的渠道 枚举类型值：收银宝活动、微信原生活动、支付宝原生活动、云闪付原生活动、其他类型活动
     */
    @NotBlank
    @TableField("ACTIVITY_CHNNAL")
    private String activityChnnal;
    /**
     * 活动批次号
     */
    @NotBlank
    @TableField("ACTIVITY_BATCHNO")
    private String activityBatchno;
    /**
     * 枚举类型值：代金券、随机立减、折扣、满减。
     */
    @NotBlank
    @TableField("DISCOUNT_TYPE")
    private String discountType;
    /**
     * 枚举类型值：免充值、预充值（垫资）、预充值（不垫资）
     */
    @NotBlank
    @TableField("FUND_TYPE")
    private String fundType;
    /**
     * 活动主办人
     */
    @NotBlank
    @TableField("ACTIVITY_MASTER")
    private String activityMaster;
    /**
     * 补充活动中涉及的其余附件材料（例签署的协议附件材料） 多个地址间以 ; 分割 默认为"无"
     */
    @TableField("ACTIVITY_FILE")
    private String activityFile;
    /**
     * 活动开始时间
     */
    @NonNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("START_TIME")
    private Date startTime;
    /**
     * 活动结束时间
     */
    @NonNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("END_TIME")
    private Date endTime;
    /**
     * 可用开始时间
     */
    @TableField("ABLESTART_TIME")
    private String ablestartTime;
    /**
     * 可用结束时间
     */
    @TableField("ABLEEND_TIME")
    private String ableendTime;
    /**
     * 可用的周
     */
    @TableField("ABLE_WEEK")
    private String ableWeek;
    /**
     * 发卡行限制
     */
    @TableField("BANK_LIMIT")
    private String bankLimit;
    /**
     * 活动说明
     */
    @TableField("ACTIVITY_REMARK")
    private String activityRemark;
    /**
     * 合作机构
     * */
    @TableField("COOP_ORGAN")
    private String coopOrgan;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @TableField(exist = false)
    private String activityStatus;

    public String getActivityStatus() {
        if (null==startTime||endTime==null||System.currentTimeMillis()<startTime.getTime()){
            return "未开始";
        }

        if (startTime.getTime()<System.currentTimeMillis()&&System.currentTimeMillis()<endTime.getTime()){
            return "活动中";
        }
        return "活动结束";
    }
}