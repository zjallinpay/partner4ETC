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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (RentOrder order : rentOrderList) {
            order.setAmount(MoneyUtil.centToYuanFormat(new BigDecimal(order.getAmount())));
            try {
                order.setPayTime(simpleDateFormat.format(dateFormat.parse(order.getPayTime())));
            } catch (ParseException e) {
            }
        }

        //金额汇总
        PageVO<RentOrder> pageVO = PageVOUtil.convert(new PageInfo<>(rentOrderList));
        String totalAmount = orderMapper.selectTotalAmountByCondition(queryVO);
        pageVO.setTotalAmount(MoneyUtil.centToYuanFormat(new BigDecimal(StringUtils.isBlank(totalAmount) ? "0" : totalAmount)));
        return pageVO;
    }
}
