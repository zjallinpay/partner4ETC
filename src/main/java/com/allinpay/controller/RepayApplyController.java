package com.allinpay.controller;

import com.allinpay.core.common.ResponseData;
import com.allinpay.service.IRepayApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: tanguang
 * data: 2021/1/11
 * 资金募集平台兑付申请
 **/
@RestController
@RequestMapping("/manage/jjs")
public class RepayApplyController {
    @Autowired
    private IRepayApplyService repayApplyService;

    /**
     * 查询机构账号总金额
     */
    @RequestMapping("/amount/query")
    public ResponseData queryTotalAmount() {
        repayApplyService.queryTotalAmount();
        return ResponseData.success().setData(null);
    }

    /**
     * 文件上传-客户身份验证
     */
    @RequestMapping("/customers/validate")
    public ResponseData validateCustomers() {
        repayApplyService.validateCustomers();
        return ResponseData.success().setData(null);
    }

    /**
     * 批量兑付
     */
    @RequestMapping("/repay/batch")
    public ResponseData repayBatch() {
        repayApplyService.repayBatch();
        return ResponseData.success().setData(null);
    }
}
