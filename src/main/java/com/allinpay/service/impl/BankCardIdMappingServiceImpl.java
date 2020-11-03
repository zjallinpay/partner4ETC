package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.BankCardIdMapping;
import com.allinpay.entity.vo.BankcardidQueryVo;
import com.allinpay.mapper.BankCardIdMappingMapper;
import com.allinpay.service.BankCardIdMappingService;
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
public class BankCardIdMappingServiceImpl implements BankCardIdMappingService {
    @Autowired
    private BankCardIdMappingMapper bankCardIdMappingMapper;

    @Override
    public PageVO<BankCardIdMapping> selectByCondition(BankcardidQueryVo queryVO) {
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<BankCardIdMapping> bankcardidMappingList = bankCardIdMappingMapper.selectByCondition(queryVO);
        return PageVOUtil.convert(new PageInfo<>(bankcardidMappingList));
    }

    @Override
    public void addCardid(BankCardIdMapping bankCardIdMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        bankCardIdMapping.setStatus("0");
        bankCardIdMapping.setInsertTime(simpleDateFormat.format(new Date()));
        bankCardIdMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        BankCardIdMapping bankCardIdMapping1 = bankCardIdMappingMapper.selectOneByCondition(bankCardIdMapping);
        if (Objects.nonNull(bankCardIdMapping1)) {
            throw new AllinpayException(BizEnums.BANKCARDBIN_ALREADY_EXIST.getCode(), BizEnums.BANKCARDBIN_ALREADY_EXIST.getMsg());
        }
        bankCardIdMappingMapper.insert(bankCardIdMapping);
    }

    @Override
    public void editCardid(BankCardIdMapping bankCardIdMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        bankCardIdMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        BankCardIdMapping bankCardIdMapping1 = bankCardIdMappingMapper.selectEditByCondition(bankCardIdMapping);
        BankCardIdMapping bankCardIdMapping2 = bankCardIdMappingMapper.selectOneByCondition(bankCardIdMapping);
        if (Objects.nonNull(bankCardIdMapping2) && !bankCardIdMapping1.getBankId().equals(bankCardIdMapping.getBankId())) {
            throw new AllinpayException(BizEnums.BANKCARDBIN_ALREADY_EXIST.getCode(), BizEnums.BANKCARDBIN_ALREADY_EXIST.getMsg());
        }
        bankCardIdMappingMapper.update(bankCardIdMapping);
    }

    @Override
    public void changeStatus(BankCardIdMapping bankCardIdMapping) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        bankCardIdMapping.setUpdateTime(simpleDateFormat.format(new Date()));
        bankCardIdMappingMapper.updateStatus(bankCardIdMapping);
    }

}

