package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.DiscountConfigMapping;
import com.allinpay.entity.vo.DiscountConfigQueryVo;

public interface DiscountConfigMappingService {
    PageVO<DiscountConfigMapping> selectByCondition(DiscountConfigQueryVo discountConfigQueryVo);

    void addDiscontconfig(DiscountConfigMapping discountConfigMapping);

    void editDiscontconfig(DiscountConfigMapping discountConfigMapping);

    void changeStatus(DiscountConfigMapping discountConfigMapping);
}
