package com.allinpay.core.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.listener.ExcelListener;
import com.allinpay.entity.MerchantsExcelModal;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static <T> boolean outPutExcel(List<T> datas, HttpServletResponse response,Class clazz,String fileName,String sheetName) throws IOException {
        ExcelWriter writer = null;
        OutputStream outputStream = response.getOutputStream();
        try {

            //添加响应头信息
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
            response.setContentType("application/msexcel;charset=UTF-8");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            response.setCharacterEncoding("utf-8");
            //实例化 ExcelWriter
            writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLS, true);

            //实例化表单
            Sheet sheet = new Sheet(1, 0,clazz);
            sheet.setSheetName(sheetName);


            //输出
            writer.write(datas, sheet);
            writer.finish();

            outputStream.flush();
            outputStream.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static List importExcel(MultipartFile uploadFile, Class clazz) {

        InputStream is = null;
        try {
            String filename = uploadFile.getOriginalFilename();
            if (filename != null && (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx"))) {
                is = new BufferedInputStream(uploadFile.getInputStream());
            } else {
                throw new AllinpayException("上传文件非excel文件");
            }

            ExcelReader excelReader = null;
            ExcelListener listener = new ExcelListener();
            excelReader = EasyExcelFactory.getReader(is, listener);
            excelReader.read(new Sheet(1, 1, clazz));

            List<Object> importList = listener.getDatas();
            return importList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AllinpayException("上传文件出错"+e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
