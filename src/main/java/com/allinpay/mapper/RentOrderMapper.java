package com.allinpay.mapper;


import com.allinpay.entity.OrderQueryVO;
import com.allinpay.entity.RentOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentOrderMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(RentOrder record);

    RentOrder selectByPrimaryKey(String orderNo);

    List<RentOrder> selectAll();

    int updateByPrimaryKey(RentOrder record);

    List<RentOrder> selectByCondition(OrderQueryVO queryVO);

    List<RentOrder> findOrderNeedToDelay(String nearlySunday);

    int updateRentEndTimeBatch(@Param("list") List<RentOrder> orderList,
                               @Param("sunday") String sunday);
}