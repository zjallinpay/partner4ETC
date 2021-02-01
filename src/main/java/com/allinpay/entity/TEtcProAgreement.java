package com.allinpay.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目和协议的关系(TEtcProAgreement)实体类
 *
 * @author makejava
 * @since 2021-01-28 17:35:26
 */
@Data
@NoArgsConstructor
public class TEtcProAgreement implements Serializable {
    private static final long serialVersionUID = 942111747265746623L;
    /**
     * 主键
     */
    private Integer proAgId;
    /**
     * 项目id
     */
    private Integer proId;
    /**
     * 协议id
     */
    private Integer argId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modiifyTime;



}