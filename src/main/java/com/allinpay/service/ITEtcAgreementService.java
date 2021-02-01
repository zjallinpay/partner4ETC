package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcAgreement;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.vo.AgreementQueryVo;
import com.allinpay.entity.vo.OrganQueryVo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

public interface ITEtcAgreementService {


    //条件查询
    ResponseBean queryCondition(AgreementQueryVo agreementQueryVo);
    //根据项目id查询关联协议
    ResponseBean queryByProId(AgreementQueryVo agreementQueryVo);

    //关联协议
    ResponseBean conectAgreement(Map params);

    //解除关联
    ResponseBean deteleConect(Map params);
    //下载协议附件
    ResponseEntity<FileSystemResource> downloadArgFile(Integer argId);
    //新增编辑
    ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcAgreement tEtcAgreement);
    //删除
    ResponseBean deleteById(Integer argId);
}
