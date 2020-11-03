package com.allinpay.mapper;


import com.allinpay.entity.BankCardIdMapping;
import com.allinpay.entity.vo.BankcardidQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankCardIdMappingMapper {
    int insert(BankCardIdMapping record);

    List<BankCardIdMapping> selectByCondition(@Param("queryVO") BankcardidQueryVo queryVO);

    //根据卡bin查询
    BankCardIdMapping selectOneByCondition(@Param("bankcardid") BankCardIdMapping bankCardIdMapping);

    //根据kid编号查询
    BankCardIdMapping selectEditByCondition(@Param("bankcardid") BankCardIdMapping bankCardIdMapping);

    int updateStatus(@Param("bankcardid") BankCardIdMapping bankCardIdMapping);

    int update(@Param("bankcardid") BankCardIdMapping bankCardIdMapping);

}
