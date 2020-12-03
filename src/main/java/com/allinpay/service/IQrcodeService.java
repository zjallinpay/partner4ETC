package com.allinpay.service;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.Qrcode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface IQrcodeService {
    ResponseData addMerchant(Qrcode Qrcode);

    PageVO<Qrcode> queryMerchants(Qrcode qrcode);

    ResponseEntity<FileSystemResource> downloadQrcode(Qrcode qrcode);

//    ResponseData batchImport(String filePath);

    ResponseData batchImportNew(MultipartFile multipartFile);

    ResponseData editMerchant(Qrcode qrcode);
}
