package com.allinpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目(TEtcProject)实体类
 *
 * @author makejava
 * @since 2021-01-19 13:22:35
 */
@Data
@TableName("T_ETC_PROJECT")
@NoArgsConstructor
public class TEtcProject implements Serializable {
    private static final long serialVersionUID = 865333086468405585L;
    /**
     * 项目主键
     */
    @TableId(value = "PRO_ID",type = IdType.AUTO)
    private Integer proId;
    /**
     * 项目名称
     */
    @NotBlank
    private String proName;
    /**
     * 合作机构名称
     */
    @NotBlank
    private String coopOragnname;
    /**
     * 合作机构id
     */
    private Integer coopOrganid;

    /**
     * 项目部门
     * */
    @NotBlank
    private String proDepartment;
    /**
     * 项目起始日期一般根据签署协议上的日期进行录入 格式为：yyyy-MM-dd
     */
    @NotBlank
    @JsonFormat(pattern="yyyy-MM-dd")
    private String proStarttime;
    /**
     * 项目结束日期 格式为：yyyy-MM-dd
     */
    @NotBlank
    @JsonFormat(pattern="yyyy-MM-dd")
    private String proEndtime;
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