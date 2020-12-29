package com.allinpay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * 合作银行obu激活查询参数实体类
 *
 * @author xuming
 * @date 2019-12-11
 */
@Setter
@Getter
@ToString
public class ActivationVo extends OrgQueryVo {

    /**
     * 用户标识
     */
    private String authId;

    /**
     * 用户名称
     */
    private String authName;

    /**
     * OBU序列号
     */
    private String obuId;

    /**
     * 激活状态
     */
    private String openIs;


    /**
     * 查询创建时间起始
     */
    private String queryTimeStart;

    /**
     * 查询创建时间止
     */
    private String queryTimeEnd;

    /**
     *  机构编号
     */
    private String partnerId;

    /**
     *  机构编号用户编号
     */
    private String userPartnerId;
}
