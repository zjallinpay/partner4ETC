package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.entity.vo.ProjectQueryVo;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ITEtcProjectService {


    //分页条件查询
    ResponseBean queryCondition(ProjectQueryVo projectQueryVo);



    //新增和更新
    ResponseBean saveOrUpdata(TEtcProject tEtcProject);

    //删除项目
    ResponseBean deleteById(Integer proId);
}
