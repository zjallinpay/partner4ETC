package com.allinpay.entity;

import lombok.Data;

/**
 *  电子凭证查询返回类
 */
@Data
public class CredentialsQueryMapping {

    /**
     * 交易流水号
     */
    private String  trxId;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付状态
     */
    private String payStatus;
    /**
     * PAY_AMOUNT
     */
    private String payAmount;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 通联商户号
     */
    private String tlCustId;

    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 分公司编号
     */
    private String branchId;
    /**
     * 分公司名称
     */
    private String branchName;
    /**
     * 员工编号
     */
    private String employeeId;
    /**
     * 员工名字
     */
    private String employeeName;
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 客户手机号
     */
    private String custPhone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 插入时间
     */
    private String insertTime;
    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新日期
     */
    private String updateDate;
    /**
     * 终端编号
     */
    private String termNo;
    /**
     * 终端流水号
     */
    private String traceNo;

    /**
     * 借贷标志,刷卡消费(00-借记卡,01-存折,02-信用卡,03-准贷记卡,04-预付费卡,05-境外卡,99- 其他);扫码支付(
     * 02-信用卡
     * 其他为借记卡)
     */
    private String acctType;
    /**
     * 交易帐号
     */
    private String acct;
    /**
     * 手续费
     */
    private String fee;
}
