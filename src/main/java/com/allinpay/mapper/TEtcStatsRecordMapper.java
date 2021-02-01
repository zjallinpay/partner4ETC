package com.allinpay.mapper;

import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.TEtcStatsrecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TEtcStatsRecordMapper extends BaseMapper<TEtcStatsrecord> {
}
