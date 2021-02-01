package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcStatsrecord;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.service.ITEtcStatsrecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/manage/statsrecord")
public class TEtcStatsrecordController {

    @Autowired
    private ITEtcStatsrecordService itEtcStatsrecordService;
    //生成记录
    @RequestMapping("/genateRecord")
    public ResponseBean genateRecord(@Valid TEtcStatsrecord tEtcStatsrecord, BindingResult bindingResult){

        log.info("提交参数{}",tEtcStatsrecord);
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }

        return itEtcStatsrecordService.genateRecord(tEtcStatsrecord);
    }


    //下载记录
    @GetMapping("/downloadRecord")
    public ResponseBean downloadRecord(@RequestParam("reId") Integer reId, HttpServletResponse response) throws IOException {
        return itEtcStatsrecordService.downloadRecord(reId,response);
    }

    //查询所有活动所属记录
    @RequestMapping("/queryAll")
    public ResponseBean queryAll(String acBatchId,Integer pageNo,Integer pageSize){
        return itEtcStatsrecordService.queryAll(acBatchId,pageNo,pageSize);
    }


}
