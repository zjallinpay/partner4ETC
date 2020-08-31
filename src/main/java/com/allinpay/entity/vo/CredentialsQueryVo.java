package com.allinpay.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 电子凭证查询请求参数
 */
@Data
public class CredentialsQueryVo {

    /**
     * 通联商户号
     */
    private String tlCustId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 员工名
     */
    private String employeeName;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 所属分公司
     */
    private String branchName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
    /**
     * 交易类型
     */
    private String tradeType;

    private Integer pageNum;

    private Integer pageSize;

}
