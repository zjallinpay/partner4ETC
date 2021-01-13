package com.allinpay.controller;

import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.vo.JJSCustomerValidateResult;
import com.allinpay.entity.vo.JJSInstAmountVO;
import com.allinpay.service.IRepayApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        JJSInstAmountVO jjsInstAmountVO = repayApplyService.queryTotalAmount();
        return ResponseData.success().setData(jjsInstAmountVO);
    }

    /**
     * 文件上传-客户身份验证
     */
    @RequestMapping("/customers/validate")
    public ResponseData validateCustomers(MultipartFile customerFile) {
        JJSCustomerValidateResult result = repayApplyService.validateCustomers(customerFile);
        return ResponseData.success().setData(result);
    }

    /**
     * 批量兑付
     */
    @RequestMapping("/repay/batch")
    public ResponseData repayBatch(@RequestParam String batchNo) {
        String batch = repayApplyService.repayBatch(batchNo);
        return ResponseData.success().setData(batch);
    }
}
