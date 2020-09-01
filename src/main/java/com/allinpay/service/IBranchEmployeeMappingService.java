package com.allinpay.service;


import com.allinpay.core.common.PageVO;
import com.allinpay.entity.BranchEmployeeMapping;
import com.allinpay.entity.CredentialsQueryMapping;
import com.allinpay.entity.vo.CredentialsQueryVo;
import com.allinpay.entity.vo.EmployeeQueryVO;

public interface IBranchEmployeeMappingService {

    PageVO<BranchEmployeeMapping> selectByCondition(EmployeeQueryVO queryVO);

    void addEmployee(BranchEmployeeMapping employeeMapping);

    void editEmployee(BranchEmployeeMapping employeeMapping);

    void changeStatus(BranchEmployeeMapping employeeMapping);

    PageVO<CredentialsQueryMapping> queryCredentials(CredentialsQueryVo credentialsQueryVo);

}
