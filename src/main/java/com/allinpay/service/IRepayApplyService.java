package com.allinpay.service;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
public interface IRepayApplyService {
    void queryTotalAmount();

    void validateCustomers();

    void repayBatch();
}
