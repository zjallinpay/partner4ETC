package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.vo.ActivitydataWechatQueryVo;
import com.allinpay.entity.vo.AgreementQueryVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IEtcActivitydataWechatService {


    //条件查询
    ResponseBean queryCondition(ActivitydataWechatQueryVo activitydataWechatQueryVo);

    //批量导入微信数据
    @Transactional
    ResponseBean batchImport(MultipartFile multipartFile);

    //查询
    ResponseBean query(ActivitydataWechatQueryVo activitydataWechatQueryVo);

    //批量导出微信数据
    ResponseBean batchOutput(ActivitydataWechatQueryVo activitydataWechatQueryVo, HttpServletResponse response) throws IOException;

    //删除批次号下所有数据
    ResponseBean deleteByActId(String barchId);
}
