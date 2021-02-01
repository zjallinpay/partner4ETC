package com.allinpay.controller;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcAgreement;
import com.allinpay.entity.vo.AcitvityQueryVo;
import com.allinpay.entity.vo.AgreementQueryVo;
import com.allinpay.entity.vo.MerchantActivityVo;
import com.allinpay.service.ITEtcActivityService;
import com.allinpay.service.ITEtcAgreementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manage/agreement")
@Slf4j
public class TEtcAgreementController {

    @Autowired
    private ITEtcAgreementService agreementService;



    //条件查询
    @RequestMapping("/queryCondition")
    public ResponseBean queryCondition(AgreementQueryVo agreementQueryVo){
        return agreementService.queryCondition(agreementQueryVo);
    }

    //更新和保存
    @RequestMapping("/saveOrUpdata")
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, @Valid TEtcAgreement tEtcAgreement,
                                     BindingResult bindingResult){
        log.info("提交参数{}",tEtcAgreement);
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }

        return agreementService.saveOrUpdata(request,tEtcAgreement);
    }


    //下载活动附件
    @RequestMapping("/downloadArgFile")
    public ResponseEntity<FileSystemResource> downloadArgFile(@RequestParam("argId") Integer argId) {
        log.info("下载活动附件");
        return agreementService.downloadArgFile(argId);
    }


    //删除协议
    @GetMapping("/deteleById")
    public ResponseBean deteleById(@RequestParam("argId") Integer argId){
        return agreementService.deleteById(argId);
    }

    //查询项目关联协议
    @GetMapping("/queryByProId")
    public ResponseBean queryByProId(AgreementQueryVo agreementQueryVo){
        return agreementService.queryByProId(agreementQueryVo);
    }

    @RequestMapping("/conectAgreement")
    public ResponseBean conectAgreement(@RequestBody Map params){
        return agreementService.conectAgreement(params);
    }

    @RequestMapping("/deteleConect")
    public ResponseBean deteleConect(@RequestBody Map params){
        return agreementService.deteleConect(params);
    }




}
