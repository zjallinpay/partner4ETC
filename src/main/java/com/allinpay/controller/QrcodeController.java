package com.allinpay.controller;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.ResponseData;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.util.ExcelRead;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.entity.Qrcode;
import com.allinpay.service.IQrcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

@RestController
@RequestMapping("/manage/qrCode")
@Slf4j
public class QrcodeController {

    @Resource
    private IQrcodeService iQrcodeService;

    @RequestMapping("add")
    public ResponseData addMerchant(@RequestBody Qrcode qrcode){
        return iQrcodeService.addMerchant(qrcode);
    }

    @RequestMapping("edit")
    public ResponseData editMerchant(@RequestBody Qrcode qrcode){
        return iQrcodeService.editMerchant(qrcode);
    }

    @RequestMapping("getMchtInfo")
    public ResponseData getMchtInfo(Qrcode qrcode){
        return ResponseData.success().setData(iQrcodeService.queryMerchants(qrcode));
    }

    @RequestMapping("downloadQrCodeImg")
    public ResponseEntity<FileSystemResource> downloadQrCodeImg(Qrcode qrcode) {
        return iQrcodeService.downloadQrcode(qrcode);
    }


    /**
     * 批量导入
     * @param multipartFile
     * @return
     */
    @PostMapping("batchAdd")
    public ResponseData importData(@RequestParam MultipartFile multipartFile)  {
//        String fileName = multipartFile.getOriginalFilename();
//        log.info("接收到批导文件：{}",fileName);
//        try {
//            ExcelRead.save(multipartFile);
//            return iQrcodeService.batchImport(CommonConstant.BATCH_IMPORT_FILE_PATH+fileName);
//        } catch (IOException e) {
//            return ResponseData.failure("5006","批导失败");
//        }
        return iQrcodeService.batchImportNew(multipartFile);
    }


    @RequestMapping("downloadTemplate")
    public ResponseEntity<FileSystemResource> downloadTemplate() {
        log.info("接收到批导模板下载请求");
        return FileDownloader.download(CommonConstant.SOURCE_FILE_NAME,CommonConstant.DEST_FILE_NAME);
    }


}
