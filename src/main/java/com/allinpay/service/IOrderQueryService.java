package com.allinpay.service;


import com.allinpay.core.common.PageVO;
import com.allinpay.entity.OrderQueryVO;
import com.allinpay.entity.RentOrder;

public interface IOrderQueryService {
    PageVO<RentOrder> getList(OrderQueryVO queryVO);
}
