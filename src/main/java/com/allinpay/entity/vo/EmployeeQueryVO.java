package com.allinpay.entity.vo;

import lombok.Data;

/**
 * author: tanguang
 * data: 2020/7/16
 **/
@Data
public class EmployeeQueryVO {
    private String tlCustId;

    private String branchId;

    private String branchName;

    private String employeeId;

    private String employeeName;

    private String status;

    private Integer pageNum;

    private Integer pageSize;
}
