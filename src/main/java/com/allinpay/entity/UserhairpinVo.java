package com.allinpay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * 用户发卡信息查询参数实体类
 *
 * @author xuming
 * @date 2019-07-02
 */

@Setter
@Getter
@ToString
public class UserhairpinVo extends OrgQueryVo {

    /**
     * 用户标识
     */
    private String authId;

    /**
     * 用户名称
     */
    private String authName;

    /**
     * 流水号
     */
    private String orderNo;

    /**
     * 签约结果
     */
    private String signsStatus;


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
    private String userPartnerId;

    /**
     * 车牌号
     *
     */
    private String carNo;
}
