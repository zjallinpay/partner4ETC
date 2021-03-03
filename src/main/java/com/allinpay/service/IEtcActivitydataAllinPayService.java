package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.vo.ActivitydataAllinPayQueryVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IEtcActivitydataAllinPayService {
    ResponseBean queryCondition(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo);

    ResponseBean query(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo);

    @Transactional
    ResponseBean batchImport(MultipartFile multipartFile);

    ResponseBean batchOutput(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo, HttpServletResponse response) throws IOException;

    ResponseBean deleteByActId(String barchId);
}
