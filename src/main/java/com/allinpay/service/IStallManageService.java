package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.StallQueryVO;
import com.allinpay.entity.StallVO;

public interface IStallManageService {
    PageVO<StallVO> getList(StallQueryVO queryVO);

    int updateStatusBatch(String status);

    void delayStallInfo();

    int updateStatus(StallQueryVO stallVO);
}
