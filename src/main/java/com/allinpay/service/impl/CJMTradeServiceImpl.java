package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.CJMOrder;
import com.allinpay.entity.MerchantConfigMapping;
import com.allinpay.entity.vo.CJMOrderVO;
import com.allinpay.entity.vo.CJMQuery;
import com.allinpay.mapper.CJMOrderMapper;
import com.allinpay.mapper.MerchantConfigMappingMapper;
import com.allinpay.service.ICJMTradeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * author: tanguang
 * data: 2020/11/9
 **/
@Service
public class CJMTradeServiceImpl implements ICJMTradeService {

    @Autowired
    private CJMOrderMapper orderMapper;
    @Autowired
    private MerchantConfigMappingMapper merchantConfigMappingMapper;

    @Override
    public PageVO<CJMOrderVO> getList(CJMQuery query) {
        MerchantConfigMapping merchantConfigMapping = merchantConfigMappingMapper.selectByMerchantId(query.getMerchantId());
        if (Objects.isNull(merchantConfigMapping)) {
            return PageVOUtil.emptyPageVO();
        }

        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<CJMOrder> orderList = orderMapper.selectByCondition(query);

        PageVO pageVO = PageVOUtil.convert(new PageInfo<>(orderList));
        List<CJMOrderVO> orderVOList = new ArrayList<>();
        orderList.stream().forEach(order -> {
            CJMOrderVO orderVO = new CJMOrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVO.setMerchantId(order.getTlCustId());
            orderVO.setMerchantName(merchantConfigMapping.getMerchantName());
            orderVOList.add(orderVO);
        });
        pageVO.setList(orderVOList);
        return pageVO;
    }
}
