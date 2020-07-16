package com.allinpay.entity;

import lombok.Data;

@Data
public class BranchEmployeeMapping {
    private Integer kid;

    private String tlCustId;

    private String branchId;

    private String branchName;

    private String employeeId;

    private String employeeName;

    private String status;
}