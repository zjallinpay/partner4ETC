package com.allinpay.entity;

import com.allinpay.core.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 信息查询封装参数基本实体类
 *
 * @author xuming
 * @date 2019-07-02
 */
@Setter
@Getter
@ToString
public class OrgQueryVo extends BaseEntity {

    /**
     * 机构编号
     */
    private String partnerId;
    /**
     * 机构名称
     */
    private String partnerName;
    /**
     * 机构类型
     */
    private String partnerType;
    /**
     * 营业执照
     */
    private String businessLicenceNo;
    /**
     * 机构层级
     */
    private Integer rank;
    /**
     * 推广人
     */
    private String saler;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 法人证件号
     */
    private String legalId;
    /**
     * 法人身份证号
     */
    private String legalPhone;
    /**
     * 机构联系人
     */
    private String contactor;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 父机构编号
     */
    private String parentId;
    /**
     * 审核状态 审核中、审核通过、审核未通过
     */
    private Integer status;

    /**
     * 封装多状态的sb
     */
    private String sbstatus;
    /**
     * 查询创建时间起始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeStart;

    /**
     * 查询创建时间止
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeEnd;

    /**
     * 查询更新时间起
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTimeStart;

    /**
     * 查询更新时间止
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTimeEnd;

    /**
     * 操作人
     */
    private String sysUser;

    /**
     * 服务请求路径
     */
    private String url;
}
