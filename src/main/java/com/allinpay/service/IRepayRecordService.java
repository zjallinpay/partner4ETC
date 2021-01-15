package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.vo.RepayQueryVO;
import com.allinpay.entity.vo.RepayRecordVO;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
public interface IRepayRecordService {
    PageVO<RepayRecordVO> queryRepayRecord(RepayQueryVO repayQueryVO);

}
