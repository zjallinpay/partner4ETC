package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构(TEtcOragn)实体类
 *
 * @author makejava
 * @since 2021-01-19 10:34:06
 */

@Data
@TableName("T_ETC_ORAGN")
public class TEtcOragn implements Serializable {
    private static final long serialVersionUID = 792408433060019970L;
    /**
     * 主键
     */
    @TableId(value = "OG_ID",type = IdType.AUTO)
    private Integer ogId;
    /**
     * 机构名称
     */
    private String ogName;
    /**
     * 机构类型
     */
    private String ogType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;



}