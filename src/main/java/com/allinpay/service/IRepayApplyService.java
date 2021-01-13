package com.allinpay.service;

import com.allinpay.entity.vo.JJSCustomerValidateResult;
import com.allinpay.entity.vo.JJSInstAmountVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
public interface IRepayApplyService {
    JJSInstAmountVO queryTotalAmount();

    JJSCustomerValidateResult validateCustomers(MultipartFile multipartFile);

    String repayBatch(String batchNo);
}
