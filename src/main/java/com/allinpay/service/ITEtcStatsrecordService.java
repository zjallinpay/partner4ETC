package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcStatsrecord;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ITEtcStatsrecordService {
    ResponseBean genateRecord(TEtcStatsrecord tEtcStatsrecord);

    ResponseBean downloadRecord(Integer reId, HttpServletResponse response) throws IOException;

    ResponseBean queryAll(String acBatchId, Integer pageNo, Integer pageSize);
}
