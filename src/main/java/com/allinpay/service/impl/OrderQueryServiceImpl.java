package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.util.MoneyUtil;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.OrderQueryVO;
import com.allinpay.entity.RentOrder;
import com.allinpay.mapper.RentOrderMapper;
import com.allinpay.service.IOrderQueryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * author: tanguang
 * data: 2020-05-26
 **/
@Service
public class OrderQueryServiceImpl implements IOrderQueryService {
    @Autowired
    private RentOrderMapper orderMapper;

    @Override
    public PageVO<RentOrder> getList(OrderQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<RentOrder> rentOrderList = orderMapper.selectByCondition(queryVO);
        rentOrderList.forEach(order -> {
            order.setAmount(MoneyUtil.centToYuanFormat(new BigDecimal(order.getAmount())));
        });
        return PageVOUtil.convert(new PageInfo<>(rentOrderList));
    }
}
