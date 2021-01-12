package com.allinpay.service.impl;

import com.allinpay.service.IRepayApplyService;
import org.springframework.stereotype.Service;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
@Service
public class RepayApplyServiceImpl implements IRepayApplyService {
    @Override
    public void queryTotalAmount() {

    }

    @Override
    public void validateCustomers() {
        //todo 客户身份验证 金额比对
    }

    @Override
    public void repayBatch() {
        //todo 再次校验客户信息，再发起兑付
    }
}
