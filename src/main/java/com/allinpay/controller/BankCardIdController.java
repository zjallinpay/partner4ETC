package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.BankCardIdMapping;
import com.allinpay.entity.vo.BankcardidQueryVo;
import com.allinpay.service.BankCardIdMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 崇军码卡bin管理
 * yefan
 */
@Slf4j
@RestController
@RequestMapping("/manage/bankcardid")
public class BankCardIdController {
    @Autowired
    private BankCardIdMappingService bankCardIdMappingService;

    @RequestMapping("/list")
    public ResponseData getList(BankcardidQueryVo queryVO) {
        PageVO<BankCardIdMapping> pageVO = null;
        pageVO = bankCardIdMappingService.selectByCondition(queryVO);

        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.addCardid(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/edit")
    public ResponseData edit(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.editCardid(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/status")
    public ResponseData changeStatus(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.changeStatus(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }
//
//    /**
//     * 杭叉交易查询
//     *
//     * @param credentialsQueryVo
//     * @return
//     */
//    @RequestMapping("/orderList")
//    public ResponseData queryOrderList(CredentialsQueryVo credentialsQueryVo) {
//        return ResponseData.success().setData(credentialsQueryList(credentialsQueryVo));
//    }
//
//    /**
//     * 杭叉交易信息导出
//     *
//     * @param credentialsQueryVo
//     * @return
//     */
//    @GetMapping("/credentials/export")
//    public PageVO<CredentialsQueryMapping> exportCredentials(CredentialsQueryVo credentialsQueryVo) {
//        credentialsQueryVo.setPageSize(10000000);
//        credentialsQueryVo.setPageNum(1);
//        return credentialsQueryList(credentialsQueryVo);
//    }
//
//    /**
//     * 查询交易明细信息
//     *
//     * @param credentialsQueryVo
//     * @return
//     */
//    private PageVO<CredentialsQueryMapping> credentialsQueryList(CredentialsQueryVo credentialsQueryVo) {
//        try {
//            PageVO<CredentialsQueryMapping> querylist = null;
//            TEtcSysUser user = (TEtcSysUser) SecurityUtils.getSubject().getPrincipal();
//            if ("56133105533AK04".equals(user.getUsername()) || "admin".equals(user.getUsername())) {
//                querylist = branchEmployeeMappingService.queryCredentials(credentialsQueryVo);
//            } else {
//                credentialsQueryVo.setTlCustId(user.getUsername());
//                querylist = branchEmployeeMappingService.queryCredentials(credentialsQueryVo);
//            }
//            return querylist;
//        } catch (Exception e) {
//            log.error("杭叉查询交易信息出错:", e);
//            return null;
//        }
//    }

}
