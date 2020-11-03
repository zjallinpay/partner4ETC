package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.entity.BankCardIdMapping;
import com.allinpay.entity.vo.BankcardidQueryVo;

public interface BankCardIdMappingService {

    PageVO<BankCardIdMapping> selectByCondition(BankcardidQueryVo queryVO);

    void addCardid(BankCardIdMapping bankCardIdMapping);

    void editCardid(BankCardIdMapping bankCardIdMapping);

    void changeStatus(BankCardIdMapping bankCardIdMapping);


}
