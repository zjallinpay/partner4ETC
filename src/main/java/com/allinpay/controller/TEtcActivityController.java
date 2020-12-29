package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.vo.AcitvityQueryVo;
import com.allinpay.entity.vo.MerchantActivityVo;
import com.allinpay.entity.vo.MerchantOutputVo;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.service.ITEtcActivityService;
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

@RestController
@RequestMapping("/manage/activity")
@Slf4j
@PropertySource("file:config/resource.properties")
public class TEtcActivityController {

    @Autowired
    private ITEtcActivityService activityService;

    @Value("${activitySourcePath}")
    private String activitySourcePath;

    @Value("${activityTemplateFileName}")
    private String activityTemplateFileName;

    //条件查询活动
    @RequestMapping("/queryActivity")
    public ResponseBean queryMerchant(AcitvityQueryVo acitvityQueryVo){
        return activityService.queryPage(acitvityQueryVo);
    }

    //更新和保存商户
    @RequestMapping("/saveOrUpdata")
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, @Valid TEtcActivity tEtcActivity,
                                     BindingResult bindingResult){
        log.info("提交参数{}",tEtcActivity);
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }

        return activityService.saveOrUpdata(request,tEtcActivity);
    }

    //查询商户详情
    @GetMapping("/findDetailById")
    public ResponseBean findDetailById(@RequestParam("actId") Integer actId){
        return activityService.findDetailByactId(actId);
    }



    //批量引入活动
    @RequestMapping("/batchImport")
    public ResponseBean batchImport(@RequestParam MultipartFile multipartFile){
        return activityService.batchImport(multipartFile);
    }

    //下载模板
    @RequestMapping("/downloadTemplate")
    public ResponseEntity<FileSystemResource> downloadTemplate() {
        log.info("接收活动批导模板下载请求");
        return FileDownloader.download(activitySourcePath,activityTemplateFileName);
    }

    //下载活动附件
    @RequestMapping("/downloadActFile")
    public ResponseEntity<FileSystemResource> downloadActFile(@RequestParam("actId") Integer actId) {
        log.info("下载活动附件");
        return activityService.downloadActFile(actId);
    }


    //删除活动
    @GetMapping("/deteleById")
    public ResponseBean deteleById(@RequestParam("actId") Integer actId){
        return activityService.deleteById(actId);
    }


    //查询活动参与商家
    @GetMapping("/queryActMers")
    public ResponseBean queryActMers(@RequestParam("actId") Integer actId,@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize){
        return activityService.queryActMers(actId,pageNo,pageSize);
    }

    //根据id插入参与的商户
    @RequestMapping("/insertByMerId")
    public ResponseBean insertByMerId(@RequestBody MerchantActivityVo merchantActivityVo){
        return activityService.insertByMerId(merchantActivityVo);
    }

    //根据活动id批量导入参与商户
    @RequestMapping("/batchInsertByActId")
    public ResponseBean batchInsertByActId(@RequestParam("file") MultipartFile multipartFile,@RequestParam("actId") Integer actId){
        return activityService.batchInsertByActId(multipartFile,actId);
    }


    //批量导出活动商户
    @GetMapping("/batchOutput")
    public ResponseBean batchOutput(@RequestParam("actId") Integer actId, HttpServletResponse response) throws IOException {
        return activityService.batchOutput(actId,response);
    }

    @GetMapping("/deleteByMerId")
    public ResponseBean deleteByMerId(Integer actId,Integer merId){
        return activityService.deleteByMerId(merId,actId);
    }


}
