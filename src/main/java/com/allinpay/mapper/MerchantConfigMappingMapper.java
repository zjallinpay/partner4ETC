package com.allinpay.mapper;

import com.allinpay.entity.MerchantConfigMapping;
import com.allinpay.entity.vo.MerchantConfigQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantConfigMappingMapper {

    List<MerchantConfigMapping> selectByCondition(@Param("queryVO") MerchantConfigQueryVo merchantConfigQueryVo);

    int insert(MerchantConfigMapping record);

    int update(@Param("merchantconfig") MerchantConfigMapping merchantConfigMapping);

    int updateStatus(@Param("merchantconfig") MerchantConfigMapping merchantConfigMapping);

    MerchantConfigMapping selectOneByCondition(@Param("merchantconfig") MerchantConfigMapping merchantConfigMapping);

    MerchantConfigMapping selectEditByCondition(@Param("merchantconfig") MerchantConfigMapping merchantConfigMapping);


    MerchantConfigMapping selectByMerchantId(String merchantId);
}
