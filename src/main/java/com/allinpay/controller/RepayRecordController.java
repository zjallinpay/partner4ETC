package com.allinpay.controller;

import com.allinpay.core.common.ResponseData;
import com.allinpay.service.IRepayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: tanguang
 * data: 2021/1/11
 * 资金募集平台兑付记录查询
 **/
@RestController
@RequestMapping("/manage/jjs")
public class RepayRecordController {
    @Autowired
    private IRepayRecordService repayRecordService;

    /**
     * 兑付记录查询
     */
    @RequestMapping("/repay/record")
    public ResponseData queryTotalAmount() {
        repayRecordService.queryRepayRecord();
        return ResponseData.success().setData(null);
    }
}
