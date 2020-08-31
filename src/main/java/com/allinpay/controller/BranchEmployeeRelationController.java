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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        TEtcSysUser user = (TEtcSysUser) SecurityUtils.getSubject().getPrincipal();
        if ( !"56133105533AK04".equals(user.getUsername()) && !"admin".equals(user.getUsername())) {
            List<Map> branchId = branchEmployeeMappingService.getBranchId(tlCustId);//获取分店编号
            //非本公司录入
            if( !branchId.get(0).get("branchId").equals(tlCustId) ){
                return ResponseData.failure("","请确认公司编号!");
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
        PageVO<CredentialsQueryMapping> credentialsQueryList = credentialsQueryList(credentialsQueryVo);
        List<CredentialsQueryMapping> newList = new ArrayList<>();
        if (credentialsQueryList != null) {
            for (CredentialsQueryMapping credentialsQueryMapping : credentialsQueryList.getList()) {
                credentialsQueryMapping.setMerchantName(credentialsQueryMapping.getBranchName());
                if(credentialsQueryMapping.getUpdateTime() != null && !"".equals(credentialsQueryMapping.getUpdateTime())){
                    String date = credentialsQueryMapping.getUpdateTime().substring(0,10);
                    credentialsQueryMapping.setUpdateDate(date);
                }
                if ("VSP001".equals(credentialsQueryMapping.getPayType())) {
                    credentialsQueryMapping.setPayType("消费");
                }
                if ("VSP501".equals(credentialsQueryMapping.getPayType())) {
                    credentialsQueryMapping.setPayType("微信支付");
                }
                if ("VSP511".equals(credentialsQueryMapping.getPayType())) {
                    credentialsQueryMapping.setPayType("支付宝支付");
                }
                if ("VSP505".equals(credentialsQueryMapping.getPayType())) {
                    credentialsQueryMapping.setPayType("手机QQ支付");
                }
                if ("VSP551".equals(credentialsQueryMapping.getPayType())) {
                    credentialsQueryMapping.setPayType("银联扫码支付");
                }

                if ("VSP001".equals(credentialsQueryMapping.getPayType())) {
                    if ("00".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("借记卡");
                    }
                    if ("01".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("存折");
                    }
                    if ("02".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("信用卡");
                    }
                    if ("03".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("准贷记卡");
                    }
                    if ("04".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("预付费卡");
                    }
                    if ("05".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("境外卡");
                    }
                    if ("99".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("其他");
                    }
                } else {
                    if ("02".equals(credentialsQueryMapping.getAcctType())) {
                        credentialsQueryMapping.setAcctType("信用卡");
                    } else {
                        credentialsQueryMapping.setAcctType("借记卡");
                    }
                }
                if("0000".equals(credentialsQueryMapping.getPayStatus())){
                    credentialsQueryMapping.setPayStatus("成功");
                }else{
                    credentialsQueryMapping.setPayStatus("失败");
                }
                newList.add(credentialsQueryMapping);
            }
        }

        credentialsQueryList.setList(newList);
        return credentialsQueryList;
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
