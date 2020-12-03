package com.allinpay.core.util;

import com.allinpay.core.constant.CommonConstant;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class FileDownloader {

    public static ResponseEntity<FileSystemResource> download(String sourceFile, String destFile){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                    new String(destFile.getBytes("gbk"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(new FileSystemResource(new File(sourceFile)), headers, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
