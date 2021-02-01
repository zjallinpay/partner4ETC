package com.allinpay.mapper;

import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TEtcOrganMapper extends BaseMapper<TEtcOragn> {

}
