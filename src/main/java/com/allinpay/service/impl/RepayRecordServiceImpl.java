package com.allinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.entity.vo.RepayQueryVO;
import com.allinpay.entity.vo.RepayRecordVO;
import com.allinpay.service.IRepayRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
@Service
@Slf4j
public class RepayRecordServiceImpl implements IRepayRecordService {
    @Autowired
    private JJSClient jjsClient;

    @Override
    public PageVO<RepayRecordVO> queryRepayRecord(RepayQueryVO repayQueryVO) {
        Map<String, String> bizMap = new HashMap<>();
        bizMap.put("batchNo", repayQueryVO.getBatchNo());
        bizMap.put("hldName", repayQueryVO.getHldName());
        bizMap.put("tradeStartTime", repayQueryVO.getTradeStartTime());
        bizMap.put("tradeEndTime", repayQueryVO.getTradeEndTime());
        bizMap.put("status", repayQueryVO.getStatus());
        bizMap.put("pageNum", String.valueOf(repayQueryVO.getPageNum()));
        bizMap.put("pageSize", String.valueOf(repayQueryVO.getPageSize()));
        log.info("jjs repay record request：{}", JSON.toJSONString(repayQueryVO));
        Map<String, String> responseMap = jjsClient.execute(bizMap, CommonConstant.JJS_TRADE_QUERY);
        return JSON.parseObject(responseMap.get("data"), PageVO.class);
    }
}
