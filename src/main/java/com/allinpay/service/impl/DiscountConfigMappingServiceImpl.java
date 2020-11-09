package com.allinpay.service.impl;


import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.DiscountConfigMapping;
import com.allinpay.entity.vo.DiscountConfigQueryVo;
import com.allinpay.mapper.DiscountConfigMappingMapper;
import com.allinpay.service.DiscountConfigMappingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DiscountConfigMappingServiceImpl implements DiscountConfigMappingService {
    @Autowired
    private DiscountConfigMappingMapper discountConfigMappingMapper;

    @Override
    public PageVO<DiscountConfigMapping> selectByCondition(DiscountConfigQueryVo discountConfigQueryVo) {
        PageHelper.startPage(discountConfigQueryVo.getPageNum(), discountConfigQueryVo.getPageSize());
        List<DiscountConfigMapping> discountConfigMappingList = discountConfigMappingMapper.selectByCondition(discountConfigQueryVo);
        return PageVOUtil.convert(new PageInfo<>(discountConfigMappingList));
    }

    @Override
    public PageVO<DiscountConfigMapping> selectActivity(DiscountConfigQueryVo discountConfigQueryVo) {
        List<DiscountConfigMapping> discountConfigMappingList = discountConfigMappingMapper.selectActivity(discountConfigQueryVo);
        return PageVOUtil.convert(new PageInfo<>(discountConfigMappingList));
    }

    @Override
    public void addDiscontconfig(DiscountConfigMapping discountConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        discountConfigMapping.setStatus("0");
        discountConfigMapping.setInsertTime(simpleDateFormat.format(new Date()));
        discountConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        DiscountConfigMapping discountConfigMapping1 = discountConfigMappingMapper.selectOneByCondition(discountConfigMapping);
        if (Objects.nonNull(discountConfigMapping1)) {
            throw new AllinpayException(BizEnums.ACTIVITYID_ALREADY_EXIST.getCode(), BizEnums.ACTIVITYID_ALREADY_EXIST.getMsg());
        }
        discountConfigMappingMapper.insert(discountConfigMapping);
    }

    @Override
    public void editDiscontconfig(DiscountConfigMapping discountConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        discountConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        DiscountConfigMapping discountConfigMapping1 = discountConfigMappingMapper.selectEditByCondition(discountConfigMapping);
        DiscountConfigMapping discountConfigMapping2 = discountConfigMappingMapper.selectOneByCondition(discountConfigMapping);
        if (Objects.nonNull(discountConfigMapping2) && !discountConfigMapping1.getDiscountId().equals(discountConfigMapping.getDiscountId())) {
            throw new AllinpayException(BizEnums.BANKCARDBIN_ALREADY_EXIST.getCode(), BizEnums.BANKCARDBIN_ALREADY_EXIST.getMsg());
        }
        discountConfigMappingMapper.update(discountConfigMapping);
    }

    @Override
    public void changeStatus(DiscountConfigMapping discountConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        discountConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        discountConfigMappingMapper.updateStatus(discountConfigMapping);
    }
}
