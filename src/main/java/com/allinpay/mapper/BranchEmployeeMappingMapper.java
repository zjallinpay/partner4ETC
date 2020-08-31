package com.allinpay.mapper;

import com.allinpay.entity.BranchEmployeeMapping;
import com.allinpay.entity.CredentialsQueryMapping;
import com.allinpay.entity.vo.CredentialsQueryVo;
import com.allinpay.entity.vo.EmployeeQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BranchEmployeeMappingMapper {
    int insert(BranchEmployeeMapping record);

    List<BranchEmployeeMapping> selectByCondition(@Param("queryVO") EmployeeQueryVO queryVO);

    BranchEmployeeMapping selectOneByCondition(@Param("employee") BranchEmployeeMapping employeeMapping);

    int updateStatus(@Param("employee") BranchEmployeeMapping employeeMapping);

    int update(@Param("employee") BranchEmployeeMapping employeeMapping);

    List<CredentialsQueryMapping> queryCredentials(@Param("credentialsQueryVo") CredentialsQueryVo credentialsQueryVo);

    List<Map> getBranchId(String tlCustId);
}
