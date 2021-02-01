package com.allinpay.mapper;

import com.allinpay.entity.TEtcAgreement;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProAgreement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TEtcAgreementMapper extends BaseMapper<TEtcAgreement> {
    int insertBatch(List<TEtcProAgreement> list);


    int deteleConect(Map params);


    List<Integer> queryArgByProId(int proId);

    int deteleByProId(int proId);

    int deteleByArgId(int argId);
}
