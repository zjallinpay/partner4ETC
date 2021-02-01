package com.allinpay.controller;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.common.ResponseData;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.vo.MerchantOutputVo;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.service.ITEtcMerchantsService;
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
@RequestMapping("/manage/merchants")
@Slf4j
@PropertySource("file:config/resource.properties")
public class TEtcMerchantsController {
    @Autowired
    private ITEtcMerchantsService merchantsService;
    @Value("${merSourcePath}")
    private String merSourcePath;

    @Value("${merTemplateFileName}")
    private String merTemplateFileName;

    //条件查询商户
    @RequestMapping("/queryMerchant")
    public ResponseBean queryMerchant(MerchantQueryVo merchantQueryVo){
        return merchantsService.queryPage(merchantQueryVo);
    }
    //更新和保存商户
    @RequestMapping("/saveOrUpdata")
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, @Valid TEtcMerchants etcMerchants,
                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }

        return merchantsService.saveOrUpdata(request,etcMerchants);
    }

    //查询商户详情
    @GetMapping("/findDetailById")
    public ResponseBean findDetailById(@RequestParam("merId") Integer merId){
        return merchantsService.findDetailByMerId(merId);
    }


    //删除商户
    @GetMapping("/deteleById")
    public ResponseBean deteleById(@RequestParam("merId") Integer merId){
        return merchantsService.deleteById(merId);
    }


    //批量导出商户
    @RequestMapping("/batchOutput")
    public ResponseBean batchOutput( MerchantQueryVo merchantQueryVo, HttpServletResponse response) throws IOException {
        return merchantsService.batchOutput(merchantQueryVo,response);
    }

    //批量导入商户
    @RequestMapping("/batchImport")
    public ResponseBean batchImport(@RequestParam MultipartFile multipartFile){
        return merchantsService.batchImport(multipartFile);
    }

    @RequestMapping("/downloadTemplate")
    public ResponseEntity<FileSystemResource> downloadTemplate() {
        log.info("接收商户批导模板下载请求");
        return FileDownloader.download(merSourcePath,merTemplateFileName);
    }


    @RequestMapping("/batchDelete")
    public ResponseBean batchDelete(@RequestBody Map params){
        if (params==null||params.get("deleteIds")==null){
            throw new AllinpayException("参数不能为空");
        }
        log.info(JSON.toJSONString(params));
        List ids= (List) params.get("deleteIds");
        return merchantsService.batchDelete(ids);
    }

/*

    //下载商家附件
    @RequestMapping("/downloadMerFile")
    public ResponseEntity<FileSystemResource> downloadActFile(@RequestParam("merId") Integer merId) {
        log.info("下载活动附件");
        return merchantsService.downloadMerFile(merId);
    }
*/


}
