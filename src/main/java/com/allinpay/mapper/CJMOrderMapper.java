package com.allinpay.mapper;

import com.allinpay.entity.CJMOrder;
import com.allinpay.entity.vo.CJMQuery;

import java.util.List;

public interface CJMOrderMapper {

    List<CJMOrder> selectByCondition(CJMQuery query);
}
