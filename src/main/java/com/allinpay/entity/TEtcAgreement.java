package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 协议(TEtcAgreement)实体类
 *
 * @author makejava
 * @since 2021-01-19 13:21:41
 */
@Data
@TableName("T_ETC_AGREEMENT")
@NoArgsConstructor
public class TEtcAgreement implements Serializable {
    private static final long serialVersionUID = 473794418812601562L;
    /**
     * 协议id
     */
    @TableId(value = "ARG_ID",type = IdType.AUTO)
    private Integer argId;
    /**
     * 协议名称
     */
    @NotBlank
    private String argName;
    /**
     * 合作机构类型
     */
    @NotBlank
    private String coopOrgtype;
    /**
     * 添加日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改日期
     */
    private Date modifyTime;
    /**
     * 协议附件文件名
     */
    private String argFile;



}