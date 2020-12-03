package com.allinpay.core.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.allinpay.core.constant.CommonConstant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExcelRead {


    // 读取excel
    public static Workbook readExcel(String filePath) throws IOException {
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream inputStream = new FileInputStream(filePath);
        if (".xls".equals(extString)) {
            return new HSSFWorkbook(inputStream);
        } else if (".xlsx".equals(extString)) {
            return new XSSFWorkbook(inputStream);
        }
        return null;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cellValue = cell.getStringCellValue();
//			// 判断cell类型
//			switch (cell.getCellType()) {
//			case Cell.CELL_TYPE_NUMERIC: {
//				cell.setCellType(Cell.CELL_TYPE_STRING);
////				cellValue = String.valueOf(cell.getNumericCellValue());
//				cellValue = cell.getStringCellValue();
//				break;
//			}
//			case Cell.CELL_TYPE_FORMULA: {
//				// 判断cell是否为日期格式
//				if (DateUtil.isCellDateFormatted(cell)) {
//					// 转换为日期格式YYYY-mm-dd
//					cellValue = cell.getDateCellValue();
//				} else {
//					// 数字
//					cellValue = String.valueOf(cell.getNumericCellValue());
//				}
//				break;
//			}
//			case Cell.CELL_TYPE_STRING: {
//				cellValue = cell.getRichStringCellValue().getString();
//				break;
//			}
//			default:
//				cellValue = "";
//			}
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public static <T> List<T> mapToSYBMchtTrade(List<Map<String, Object>> dataList, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        if (dataList != null && dataList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = dataList.size(); i < size; i++) {
                map = dataList.get(i);
                try {
                    bean = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//				map = EntityMap.toSYBMchtTradeMap(map);
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }


    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public static void save(MultipartFile file) throws IOException {
        HttpServletRequest request = null;
        if (file != null){
            String filename = file.getOriginalFilename();
            saveFileFromInputStream(file.getInputStream(), CommonConstant.BATCH_IMPORT_FILE_PATH,filename);//保存到服务器的路径
        }
    }
    public static void saveFileFromInputStream(InputStream stream, String path, String fileName) throws IOException
    {
        FileOutputStream fs=new FileOutputStream(path+fileName);
        byte[] buffer =new byte[1024];
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1)
        {
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }


    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

}

