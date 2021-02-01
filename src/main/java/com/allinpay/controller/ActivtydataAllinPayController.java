package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.vo.ActivitydataAllinPayQueryVo;
import com.allinpay.entity.vo.ActivitydataWechatQueryVo;
import com.allinpay.service.IEtcActivitydataAllinPayService;
import com.allinpay.service.IEtcActivitydataWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/manage/activitydataAllinPay")
@Slf4j
public class ActivtydataAllinPayController {

    @Autowired
    private IEtcActivitydataAllinPayService activitydataAllinPayService;


    //条件查询
    @RequestMapping("/queryCondition")
    public ResponseBean queryCondition(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo){
        return activitydataAllinPayService.queryCondition(activitydataAllinPayQueryVo);
    }



    //批量导入收银宝数据
    @RequestMapping("/batchImport")
    public ResponseBean batchImport(@RequestParam MultipartFile multipartFile){
        return activitydataAllinPayService.batchImport(multipartFile);
    }


    //批量导出收银宝数据
    @RequestMapping("/batchOutput")
    public ResponseBean batchOutput(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo, HttpServletResponse response) throws IOException {
        return activitydataAllinPayService.batchOutput(activitydataAllinPayQueryVo,response);
    }


}
