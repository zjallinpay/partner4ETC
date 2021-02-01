package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.vo.OrganQueryVo;
import com.allinpay.entity.vo.ProjectQueryVo;

import java.util.List;

public interface ITEtcOrganService {


    //条件查询
    ResponseBean queryCondition(OrganQueryVo organQueryVo);


    //根据机构名模糊查询机构
    List<TEtcOragn> queryByOrganName(String organName);

    ResponseBean saveOrUpdata(TEtcOragn tEtcOragn);

    ResponseBean deleteById(Integer ogId);
}
