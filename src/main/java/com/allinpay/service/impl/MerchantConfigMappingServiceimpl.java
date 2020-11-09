package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.MerchantConfigMapping;
import com.allinpay.entity.vo.MerchantConfigQueryVo;
import com.allinpay.mapper.MerchantConfigMappingMapper;
import com.allinpay.service.MerchantConfigMappingService;
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
public class MerchantConfigMappingServiceimpl implements MerchantConfigMappingService {

    @Autowired
    private MerchantConfigMappingMapper merchantConfigMappingMapper;

    @Override
    public PageVO<MerchantConfigMapping> selectByCondition(MerchantConfigQueryVo merchantConfigQueryVo) {
        PageHelper.startPage(merchantConfigQueryVo.getPageNum(), merchantConfigQueryVo.getPageSize());
        List<MerchantConfigMapping> merchantConfigMappingList = merchantConfigMappingMapper.selectByCondition(merchantConfigQueryVo);
        return PageVOUtil.convert(new PageInfo<>(merchantConfigMappingList));
    }

    @Override
    public void addMerchantconfig(MerchantConfigMapping merchantConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        merchantConfigMapping.setStatus("0");
        merchantConfigMapping.setInsertTime(simpleDateFormat.format(new Date()));
        merchantConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        MerchantConfigMapping merchantConfigMapping1 = merchantConfigMappingMapper.selectOneByCondition(merchantConfigMapping);
        if (Objects.nonNull(merchantConfigMapping1)) {
            throw new AllinpayException(BizEnums.MERCHANTID_ALREADY_EXIST.getCode(), BizEnums.MERCHANTID_ALREADY_EXIST.getMsg());
        }
        merchantConfigMappingMapper.insert(merchantConfigMapping);
    }

    @Override
    public void editMerchantconfig(MerchantConfigMapping merchantConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        merchantConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        MerchantConfigMapping merchantConfigMapping1 = merchantConfigMappingMapper.selectEditByCondition(merchantConfigMapping);
        MerchantConfigMapping merchantConfigMapping2 = merchantConfigMappingMapper.selectOneByCondition(merchantConfigMapping);
        if (Objects.nonNull(merchantConfigMapping2) && !merchantConfigMapping1.getMerchantId().equals(merchantConfigMapping.getMerchantId())) {
            throw new AllinpayException(BizEnums.MERCHANTID_ALREADY_EXIST.getCode(), BizEnums.MERCHANTID_ALREADY_EXIST.getMsg());
        }
        merchantConfigMappingMapper.update(merchantConfigMapping);
    }

    @Override
    public void changeStatus(MerchantConfigMapping merchantConfigMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        merchantConfigMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        merchantConfigMappingMapper.updateStatus(merchantConfigMapping);
    }
}
