package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.BranchEmployeeMapping;
import com.allinpay.entity.CredentialsQueryMapping;
import com.allinpay.entity.vo.CredentialsQueryVo;
import com.allinpay.entity.vo.EmployeeQueryVO;
import com.allinpay.mapper.BranchEmployeeMappingMapper;
import com.allinpay.service.IBranchEmployeeMappingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * author: tanguang
 * data: 2020/7/10
 **/
@Service
@Slf4j
public class BranchEmployeeMappingServiceImpl implements IBranchEmployeeMappingService {
    @Autowired
    private BranchEmployeeMappingMapper branchEmployeeMappingMapper;


    @Override
    public PageVO<BranchEmployeeMapping> selectByCondition(EmployeeQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<BranchEmployeeMapping> employeeMappingList = branchEmployeeMappingMapper.selectByCondition(queryVO);
        return PageVOUtil.convert(new PageInfo<>(employeeMappingList));
    }

    @Override
    public void addEmployee(BranchEmployeeMapping employeeMapping) {
        validateParams(employeeMapping);
        BranchEmployeeMapping queryEmployee = branchEmployeeMappingMapper.selectOneByCondition(employeeMapping);
        if (Objects.nonNull(queryEmployee)) {
            throw new AllinpayException(BizEnums.EMPLOYEE_ALREADY_EXIST.getCode(), BizEnums.EMPLOYEE_ALREADY_EXIST.getMsg());
        }
        employeeMapping.setStatus("0");
        branchEmployeeMappingMapper.insert(employeeMapping);
    }

    @Override
    public void editEmployee(BranchEmployeeMapping employeeMapping) {
        validateParams(employeeMapping);
        BranchEmployeeMapping queryEmployee = branchEmployeeMappingMapper.selectOneByCondition(employeeMapping);
        if (Objects.nonNull(queryEmployee) && !queryEmployee.getKid().equals(employeeMapping.getKid())) {
            throw new AllinpayException(BizEnums.EMPLOYEE_ALREADY_EXIST.getCode(), BizEnums.EMPLOYEE_ALREADY_EXIST.getMsg());
        }
        branchEmployeeMappingMapper.update(employeeMapping);
    }

    @Override
    public void changeStatus(BranchEmployeeMapping employeeMapping) {
        branchEmployeeMappingMapper.updateStatus(employeeMapping);
    }

    @Override
    public PageVO<CredentialsQueryMapping> queryCredentials(CredentialsQueryVo credentialsQueryVo) {
        PageHelper.startPage(credentialsQueryVo.getPageNum(), credentialsQueryVo.getPageSize());
        List<CredentialsQueryMapping> credentialsQueryMappings = branchEmployeeMappingMapper.queryCredentials(credentialsQueryVo);
        return PageVOUtil.convert(new PageInfo<>(credentialsQueryMappings));
    }

    @Override
    public List<Map> getBranchId(String tlCustId) {
        return branchEmployeeMappingMapper.getBranchId(tlCustId);
    }


    private boolean validateParams(BranchEmployeeMapping employeeMapping) {
        if (StringUtils.isBlank(employeeMapping.getTlCustId()) || StringUtils.isBlank(employeeMapping.getBranchId()) ||
                StringUtils.isBlank(employeeMapping.getBranchName()) || StringUtils.isBlank(employeeMapping.getEmployeeId()) ||
                StringUtils.isBlank(employeeMapping.getEmployeeName())) {
            throw new AllinpayException(BizEnums.MISSING_PARAM.getCode(), BizEnums.MISSING_PARAM.getMsg());
        }
        return true;
    }
}
