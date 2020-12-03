package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.MerchantConfigMapping;
import com.allinpay.entity.vo.MerchantConfigQueryVo;

public interface MerchantConfigMappingService {

    PageVO<MerchantConfigMapping> selectByCondition(MerchantConfigQueryVo merchantConfigQueryVo);

    void addMerchantconfig(MerchantConfigMapping merchantConfigMapping);

    void editMerchantconfig(MerchantConfigMapping merchantConfigMapping);

    void changeStatus(MerchantConfigMapping merchantConfigMapping);
}
