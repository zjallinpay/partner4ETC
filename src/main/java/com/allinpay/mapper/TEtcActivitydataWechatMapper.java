package com.allinpay.mapper;

import com.allinpay.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TEtcActivitydataWechatMapper extends BaseMapper<TEtcActivtydataWechat> {

    int insertBatch(List<TEtcActivtydataWechat> list);

    List<MerchantStatsModal> selectMerchantStatsDatas(TEtcStatsrecord tEtcStatsrecord);

    List<MerchantStatsModal> selectExMerchantStatsDatas(TEtcStatsrecord tEtcStatsrecord);


    List<PropertyStatsModal> selectPropertStatsDatas(TEtcStatsrecord tEtcStatsrecord);


    List<PropertyStatsModal> selectPropertStatsAmountDatas(TEtcStatsrecord tEtcStatsrecord);
}
