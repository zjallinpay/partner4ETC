package com.allinpay.mapper;

import com.allinpay.entity.PropertyStatsModal;
import com.allinpay.entity.TEtcActivitydataAllinpay;
import com.allinpay.entity.TEtcActivtydataWechat;
import com.allinpay.entity.TEtcStatsrecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TEtcActivitydataAllinpayMapper extends BaseMapper<TEtcActivitydataAllinpay> {

    int insertBatch(List<TEtcActivitydataAllinpay> list);
    //查询统计记录
    List<PropertyStatsModal> selectPropertStatsDatas(TEtcStatsrecord tEtcStatsrecord);
}
