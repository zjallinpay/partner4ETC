package com.allinpay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户发卡信息查询返回信息实体类
 *
 * @author xuming
 * @date 2019-07-02
 */
@Setter
@Getter
@ToString
public class UserhairpinBack {

    /**
     * 机构编号
     */
    private String partnerId;

    /**
     * 机构名称
     */
    private String partnername;
//
//    /**
//     * 接口版本号
//     */
//    private String versionPort;

    /**
     * 请求流水号
     */
    private String orderNo;

    /**
     * 请求时间
     */
    private String reqtime;

    /**
     * 用户标识
     */
    private String authId;

    /**
     * 用户名称
     */
    private String authName;

    /**
     * 发行状态
     */
    private String issuestatus;

    /**
     * 发行结果
     */
    private String issuemsg;

//    /**
//     * 随机字符串
//     */
//    private String randomsStr;
//
//    /**
//     * 签名
//     */
//    private String signName;

//    /**
//     * 签约时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private String signdate;

    /**
     * 机构请求手机号
     */
    private String phone;

    /**
     * 车牌号
     */
    private String carno;

    /**
     * 绑定手机号
     */
    private String realphone;

    /**
     * 车主身份证
     */
    private String id;

    /**
     * 车主姓名
     */
    private String realname;

    /**
     * 请求完成时间
     */
    private String finishtime;

    /**
     *  配送方式，1-快递，2-自提
     */
    private String deliverymethod;

    /**
     *  车牌颜色
     */
    private String licensecolor;

    /**
     *  绑定银行卡号
     */
    private String cardno;

    /**
     * 推荐人
     */
    private String referer;

    /**
     * 银行网点
     */
    private String serviceaddress;
}
