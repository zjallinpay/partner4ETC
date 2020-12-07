package com.allinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.util.ExcelRead;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.Qrcode;
import com.allinpay.mapper2.QrcodeMapper;
import com.allinpay.service.IQrcodeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class QrcodeServiceImpl implements IQrcodeService {

    @Resource
    private QrcodeMapper qrcodeMapper;

    /**
     * 新增商户
     * @param qrcode
     */
    @Override
    public ResponseData addMerchant(Qrcode qrcode) {
        log.info("新增商户：{}",JSON.toJSONString(qrcode));
        Qrcode merchant = qrcodeMapper.queryMerchantById(qrcode);
        log.info("查询结果：{}",JSON.toJSONString(merchant));
        if (merchant!=null)
            return ResponseData.failure("5001","商户已经存在");
        qrcodeMapper.saveMerchant(qrcode);
        return ResponseData.success();
    }


    /**
     * 下载二维码
     * @param qrcode
     * @return
     */
    @Override
    public ResponseEntity<FileSystemResource> downloadQrcode(Qrcode qrcode) {
        log.info("下载码牌：{}",qrcode.getMchtId());
        String sourceFile = CommonConstant.QR_CODE_IMAGE_PATH + qrcode.getMchtId() + ".png";
        String destFile = qrcode.getMchtId()+".png";
        if (new File(sourceFile).exists())
            return FileDownloader.download(sourceFile,destFile);
        getQrCode(qrcode);
        return FileDownloader.download(sourceFile,destFile);
    }

    /**
     * 生产二维码图片
     * @param qrcode
     */
    private void getQrCode(Qrcode qrcode) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrUrl = CommonConstant.QR_URL + qrcode.getMchtId();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, CommonConstant.QR_IMAGE_WIDTH, CommonConstant.QR_IMAGE_LENGTH);
            Path path = FileSystems.getDefault().getPath(CommonConstant.QR_CODE_IMAGE_PATH+qrcode.getMchtId()+".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    批导商户
     */
//    @Override
//    public ResponseData batchImport(String filePath) {
//        return handleImport(filePath);
//    }

    @Override
    public ResponseData batchImportNew(MultipartFile multipartFile) {
        int counts = 0;
        try {
            Workbook workbook;
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            if (fileName.contains(".xlsx"))
                workbook = new XSSFWorkbook(inputStream);
            else
                workbook = new HSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            List<Qrcode> qrcodeList = new ArrayList<>();
            for (int index=0;index<numberOfSheets;index++) {
                Sheet sheet = workbook.getSheetAt(index);
                int rownum = sheet.getPhysicalNumberOfRows();
                for (int line=1;line<rownum;line++) {
                    Row row = sheet.getRow(line);
                    Qrcode qrcode = new Qrcode();
                    if (ExcelRead.getCellFormatValue(row.getCell(0))==null)
                        continue;
                    qrcode.setMchtId(ExcelRead.getCellFormatValue(row.getCell(0)).toString().trim());
                    qrcode.setMchtName(ExcelRead.getCellFormatValue(row.getCell(1)).toString());
                    qrcode.setAppId(ExcelRead.getCellFormatValue(row.getCell(2)).toString().trim());
                    qrcode.setAppKey(ExcelRead.getCellFormatValue(row.getCell(3)).toString().trim());
                    qrcode.setPartnerModel(ExcelRead.getCellFormatValue(row.getCell(4)).toString());
                    qrcode.setOrgId(ExcelRead.getCellFormatValue(row.getCell(5)).toString());
                    qrcodeList.add(qrcode);
                }
            }
            log.info("批量导入商户：{}",JSON.toJSONString(qrcodeList));
            counts = qrcodeMapper.batchSaveMerchants(qrcodeList);
        } catch (Exception e) {
            log.error("批导存储失败：{}",e);
            return ResponseData.failure("5005","批量导入失败："+e.getMessage());
        }
        return ResponseData.success().setData(counts);
    }

    @Override
    public ResponseData editMerchant(Qrcode qrcode) {
        log.info("编辑商户：{}",JSON.toJSONString(qrcode));
        qrcodeMapper.updateMerchant(qrcode);
        return ResponseData.success();
    }

    /*
    查询商户信息
     */
    @Override
    public PageVO<Qrcode> queryMerchants(Qrcode qrCode) {
        log.info("查询客户二维码列表：{}", JSON.toJSONString(qrCode));
        PageHelper.startPage(qrCode.getPageNum(), qrCode.getPageSize());
        List<Qrcode> qrCodes = qrcodeMapper.queryMerchants(qrCode);
        return PageVOUtil.convert(new PageInfo<>(qrCodes));
    }



//    public ResponseData handleImport(String filePath) {
//        try {
//            Workbook workbook = ExcelRead.readExcel(filePath);
//            int numberOfSheets = workbook.getNumberOfSheets();
//            List<Qrcode> qrcodeList = new ArrayList<>();
//            for (int index=0;index<numberOfSheets;index++) {
//                String  columns[]={"商户号","商户名","APPID","APPKEY","合作方编号","是否合作方模式"};
//                Sheet sheet = workbook.getSheetAt(index);
//                int rownum = sheet.getPhysicalNumberOfRows();
//                for (int line=1;line<rownum;line++) {
//                    Row row = sheet.getRow(line);
//                    Qrcode qrcode = new Qrcode();
//                    qrcode.setMchtId("".equals(ExcelRead.getCellFormatValue(row.getCell(0)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(0)).toString());
//                    qrcode.setMchtName("".equals(ExcelRead.getCellFormatValue(row.getCell(1)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(1)).toString());
//                    qrcode.setAppId("".equals(ExcelRead.getCellFormatValue(row.getCell(2)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(2)).toString());
//                    qrcode.setAppKey("".equals(ExcelRead.getCellFormatValue(row.getCell(3)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(3)).toString());
//                    qrcode.setPartnerModel("".equals(ExcelRead.getCellFormatValue(row.getCell(4)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(4)).toString());
//                    qrcode.setOrgId("".equals(ExcelRead.getCellFormatValue(row.getCell(5)).toString())?"null":ExcelRead.getCellFormatValue(row.getCell(5)).toString());
//                    qrcodeList.add(qrcode);
//                }
//            }
//            log.info("批量导入商户：{}",JSON.toJSONString(qrcodeList));
//            qrcodeMapper.batchSaveMerchants(qrcodeList);
//        } catch (Exception e) {
//            log.error("批导存储失败：{}",e);
//            return ResponseData.failure("5005","批量导入失败："+e.getMessage());
//        }
//        return ResponseData.success();
//    }


}
