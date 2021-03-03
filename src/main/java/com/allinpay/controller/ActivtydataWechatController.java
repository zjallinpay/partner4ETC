package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.vo.ActivitydataWechatQueryVo;
import com.allinpay.entity.vo.AgreementQueryVo;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.service.IEtcActivitydataWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/manage/activitydatawechat")
@Slf4j
public class ActivtydataWechatController {

    @Autowired
    private IEtcActivitydataWechatService activitydataWechatService;


    //条件查询
    @RequestMapping("/queryCondition")
    public ResponseBean queryCondition(ActivitydataWechatQueryVo activitydataWechatQueryVo){
        return activitydataWechatService.queryCondition(activitydataWechatQueryVo);
    }



    //批量导入微信数据
    @RequestMapping("/batchImport")
    public ResponseBean batchImport(@RequestParam MultipartFile multipartFile){
        return activitydataWechatService.batchImport(multipartFile);
    }


    //批量导出微信数据
    @RequestMapping("/batchOutput")
    public ResponseBean batchOutput(ActivitydataWechatQueryVo activitydataWechatQueryVo, HttpServletResponse response) throws IOException {
        return activitydataWechatService.batchOutput(activitydataWechatQueryVo,response);
    }

    @RequestMapping("/deleteByActId")
    public ResponseBean deleteByActId(String barchId){
        return activitydataWechatService.deleteByActId(barchId);
    }


}
