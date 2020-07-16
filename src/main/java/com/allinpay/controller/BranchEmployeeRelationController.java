package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.BranchEmployeeMapping;
import com.allinpay.entity.vo.EmployeeQueryVO;
import com.allinpay.service.IBranchEmployeeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 杭叉员工信息配置
 * author: tanguang
 * data: 2020/7/16
 **/
@RestController
@RequestMapping("/manage/employee")
public class BranchEmployeeRelationController {
    @Autowired
    private IBranchEmployeeMappingService branchEmployeeMappingService;

    @RequestMapping("/list")
    public ResponseData getList(EmployeeQueryVO queryVO) {
        PageVO<BranchEmployeeMapping> pageVO = branchEmployeeMappingService.selectByCondition(queryVO);
        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(BranchEmployeeMapping employeeMapping) {
        branchEmployeeMappingService.addEmployee(employeeMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/edit")
    public ResponseData edit(BranchEmployeeMapping employeeMapping) {
        branchEmployeeMappingService.editEmployee(employeeMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/status")
    public ResponseData changeStatus(BranchEmployeeMapping employeeMapping) {
        branchEmployeeMappingService.changeStatus(employeeMapping);
        return ResponseData.success().setData(null);
    }
}
