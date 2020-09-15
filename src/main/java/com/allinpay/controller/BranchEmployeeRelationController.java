package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.BranchEmployeeMapping;
import com.allinpay.entity.CredentialsQueryMapping;
import com.allinpay.entity.TEtcSysUser;
import com.allinpay.entity.vo.CredentialsQueryVo;
import com.allinpay.entity.vo.EmployeeQueryVO;
import com.allinpay.service.IBranchEmployeeMappingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 杭叉员工信息配置
 * author: tanguang
 * data: 2020/7/16
 **/
@Slf4j
@RestController
@RequestMapping("/manage/employee")
public class BranchEmployeeRelationController {
    @Autowired
    private IBranchEmployeeMappingService branchEmployeeMappingService;

    @RequestMapping("/list")
    public ResponseData getList(EmployeeQueryVO queryVO) {
        PageVO<BranchEmployeeMapping> pageVO = null;
        TEtcSysUser user = (TEtcSysUser) SecurityUtils.getSubject().getPrincipal();
        if ("56133105533AK04".equals(user.getUsername()) || "admin".equals(user.getUsername())) {
            pageVO = branchEmployeeMappingService.selectByCondition(queryVO);
        } else {
            queryVO.setTlCustId(user.getUsername());
            pageVO = branchEmployeeMappingService.selectByCondition(queryVO);
        }
        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(BranchEmployeeMapping employeeMapping) {
        String tlCustId = employeeMapping.getTlCustId();
        String branchIdMater = employeeMapping.getBranchId();
        EmployeeQueryVO employeeQueryVO = new EmployeeQueryVO();
        employeeQueryVO.setPageNum(1);
        employeeQueryVO.setPageSize(1000000);
        PageVO<BranchEmployeeMapping> selectByCondition = branchEmployeeMappingService.selectByCondition(employeeQueryVO);
        List<BranchEmployeeMapping> branchEmployeeMappingList = selectByCondition.getList();
        TEtcSysUser user = (TEtcSysUser) SecurityUtils.getSubject().getPrincipal();
        if ( !"56133105533AK04".equals(user.getUsername()) && !"admin".equals(user.getUsername())) {
            if (!tlCustId.equals(user.getUsername())) {
                //添加的商户号不对应
                return ResponseData.failure("", "请确认公司对应通联商户号!");
            }
            String flag = "0";
            for (BranchEmployeeMapping branchEmployeeMapping : branchEmployeeMappingList) {
                //公司编号与商户号对应
                if (tlCustId.equals(branchEmployeeMapping.getTlCustId()) && branchIdMater.equals(branchEmployeeMapping.getBranchId())) {
                    flag = "1";
                    break;
                }
            }
            //非本公司录入
            if ("0".equals(flag)) {
                return ResponseData.failure("","请确认公司编号!");
            }
        }
        if ("56133105533AK04".equals(user.getUsername()) || "admin".equals(user.getUsername())) {
            String flag = "0";
            String hasFlag = "0";
            for (BranchEmployeeMapping branchEmployeeMapping : branchEmployeeMappingList) {
                //公司编号与商户号对应已有，在原有基础上新增情况
                if (tlCustId.equals(branchEmployeeMapping.getTlCustId()) && branchIdMater.equals(branchEmployeeMapping.getBranchId())) {
                    flag = "1";
                    break;
                }
                //公司编号与商户号对应没有，新添加情况
                if (tlCustId.equals(branchEmployeeMapping.getTlCustId())) {
                    hasFlag = "1";
                }
                if (branchIdMater.equals(branchEmployeeMapping.getBranchId())) {
                    hasFlag = "1";
                }
                if (tlCustId.equals(branchEmployeeMapping.getTlCustId())) {
                    if (branchIdMater.equals(branchEmployeeMapping.getBranchId())) {
                        flag = "1";
                        break;
                    }
                }
                if (branchIdMater.equals(branchEmployeeMapping.getBranchId())) {
                    if (tlCustId.equals(branchEmployeeMapping.getTlCustId())) {
                        flag = "1";
                        break;
                    }
                }
            }
            //非本公司录入
            if ("0".equals(flag) && "1".equals(hasFlag)) {
                return ResponseData.failure("", "已有公司或商户号对应关系录入，请确认后添加!");
            }
        }
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

    /**
     * 杭叉交易查询
     *
     * @param credentialsQueryVo
     * @return
     */
    @RequestMapping("/orderList")
    public ResponseData queryOrderList(CredentialsQueryVo credentialsQueryVo) {
        return ResponseData.success().setData(credentialsQueryList(credentialsQueryVo));
    }

    /**
     * 杭叉交易信息导出
     *
     * @param credentialsQueryVo
     * @return
     */
    @GetMapping("/credentials/export")
    public PageVO<CredentialsQueryMapping> exportCredentials(CredentialsQueryVo credentialsQueryVo) {
        credentialsQueryVo.setPageSize(10000000);
        credentialsQueryVo.setPageNum(1);
        return credentialsQueryList(credentialsQueryVo);
    }

    /**
     * 查询交易明细信息
     *
     * @param credentialsQueryVo
     * @return
     */
    private PageVO<CredentialsQueryMapping> credentialsQueryList(CredentialsQueryVo credentialsQueryVo) {
        try {
            PageVO<CredentialsQueryMapping> querylist = null;
            TEtcSysUser user = (TEtcSysUser) SecurityUtils.getSubject().getPrincipal();
            if ("56133105533AK04".equals(user.getUsername()) || "admin".equals(user.getUsername())) {
                querylist = branchEmployeeMappingService.queryCredentials(credentialsQueryVo);
            } else {
                credentialsQueryVo.setTlCustId(user.getUsername());
                querylist = branchEmployeeMappingService.queryCredentials(credentialsQueryVo);
            }
            return querylist;
        } catch (Exception e) {
            log.error("杭叉查询交易信息出错:", e);
            return null;
        }
    }

}
