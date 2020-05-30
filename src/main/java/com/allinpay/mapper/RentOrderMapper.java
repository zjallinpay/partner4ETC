package com.allinpay.mapper;


import com.allinpay.entity.OrderQueryVO;
import com.allinpay.entity.RentOrder;

import java.util.List;

public interface RentOrderMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(RentOrder record);

    RentOrder selectByPrimaryKey(String orderNo);

    List<RentOrder> selectAll();

    int updateByPrimaryKey(RentOrder record);

    List<RentOrder> selectByCondition(OrderQueryVO queryVO);
}