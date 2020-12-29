package com.allinpay.mapper;

import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.vo.MerchantActivityVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TEtcActivityMapper extends BaseMapper<TEtcActivity> {

    //查询活动的参与商户id
    List<Integer> queryActMerIds(@Param("actId") Integer actId);
    //插入一条参与活动的商户
    int insertByMerId(MerchantActivityVo merchantActivityVo);
    //删除参与商户
    int deleteByMerId(MerchantActivityVo merchantActivityVo);
    //删除活动时级联删除参与商户关系
    int deleteByActId(Integer actId);
}
