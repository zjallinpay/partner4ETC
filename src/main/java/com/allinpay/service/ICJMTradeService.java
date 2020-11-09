package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.vo.CJMOrderVO;
import com.allinpay.entity.vo.CJMQuery;

public interface ICJMTradeService {
    PageVO<CJMOrderVO> getList(CJMQuery query);
}
