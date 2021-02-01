package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 报表导出记录(TEtcStatsrecord)实体类
 *
 * @author makejava
 * @since 2021-01-19 13:24:21
 */
@Data
@TableName("T_ETC_STATSRECORD")
@NoArgsConstructor
public class TEtcStatsrecord implements Serializable {
    private static final long serialVersionUID = 144678760104934955L;
    /**
     * 记录id
     */
    @TableId(value = "RE_ID",type = IdType.AUTO)
    private Integer reId;
    /**
     * 开始时间
     */
    @NonNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date reStartTime;
    /**
     * 结束时间
     */
    @NonNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date reEndTime;

    /**
     * 活动批次号
     */
    @NotBlank
    private String acBatchId;
    /**
     * 活动渠道
     */
    private String activityChnnal;
    /**
     * 活动类型 商圈、物业
     */
    private String activityType;


    /**
     * 生成时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;




}