package com.allinpay.mapper;

import com.allinpay.entity.DiscountConfigMapping;
import com.allinpay.entity.vo.DiscountConfigQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscountConfigMappingMapper {

    //查询方法
    List<DiscountConfigMapping> selectByCondition(@Param("queryVO") DiscountConfigQueryVo discountConfigQueryVo);

    int insert(DiscountConfigMapping record);

    int update(@Param("discountconfig") DiscountConfigMapping discountConfigMapping);

    int updateStatus(@Param("discountconfig") DiscountConfigMapping discountConfigMapping);

    //根据活动号查询
    DiscountConfigMapping selectOneByCondition(@Param("discountconfig") DiscountConfigMapping discountConfigMapping);

    //根据kid编号查询
    DiscountConfigMapping selectEditByCondition(@Param("discountconfig") DiscountConfigMapping discountConfigMapping);


}
