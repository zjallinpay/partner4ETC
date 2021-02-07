package com.allinpay.core.csvparser;

import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.encoder.EncodingReader;
import com.allinpay.entity.TEtcActivtydataWechat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
/**
 *
 * 读取微信csv文件
 *
 * */
@Slf4j
public class WechatCsvParser  {


    public static void readCSV(MultipartFile multipartFile, List<TEtcActivtydataWechat> list) {
        String filename = multipartFile.getOriginalFilename();
        InputStream is = null;
        int count=1;
        try {
        if (filename != null &&filename.toLowerCase().endsWith(".csv")) {
            is = multipartFile.getInputStream();
        } else {
            throw new AllinpayException("上传文件非csv文件");
        }
           Reader reader=EncodingReader.getFileEncode(is);

            BufferedReader bufferedreader = new BufferedReader(reader);
            boolean sign = false;       //用来跳过第一行的名称

            while(bufferedreader.ready()) {
                String line = bufferedreader.readLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                String acBatchId, acDiscountId, acDiscountType, acExchangeType,acPaymentId,acPaymerchantId,acEquimentId,acBankNum;
                Date acPaytime;
                BigDecimal acDiscountNum,acOrderAmount;
                if (st.hasMoreTokens() && sign) {
                    count+=1;
                    int countTokens=st.countTokens();
                    if (countTokens<10||countTokens>11){
                        throw new AllinpayException("第"+count+"行数据不规范");
                    }
                    acBatchId = replacePoint(st.nextToken());
                    acDiscountId=replacePoint(st.nextToken());
                    acDiscountType=replacePoint(st.nextToken());
                    acDiscountNum=new BigDecimal(replacePoint(st.nextToken()));
                    acOrderAmount=new BigDecimal(replacePoint(st.nextToken()));
                    acExchangeType=replacePoint(st.nextToken());
                    acPaymentId=replacePoint(st.nextToken());
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    acPaytime= sdf.parse(replacePoint(st.nextToken()));
                    acPaymerchantId=replacePoint(st.nextToken());
                    acEquimentId=countTokens==11?replacePoint(st.nextToken()):null;
                    acBankNum=replacePoint(st.nextToken());
                    list.add(new TEtcActivtydataWechat(null,acBatchId,acDiscountId,acDiscountType,acDiscountNum,acOrderAmount,acExchangeType,acPaymentId,acPaytime,acPaymerchantId,null,acEquimentId,acBankNum,null,null));
                }
                else {
                    sign = true;
                }
            }
            bufferedreader.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new AllinpayException("解析csv文件出错");
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new AllinpayException("解析csv文件出错");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new AllinpayException("第"+count+"行转换日期格式出错请将日期数据格式修改为 yyyy-mm-dd hh:mm:ss");
        } catch (NumberFormatException e){
            throw new AllinpayException("第"+count+"行金额数据转化错误"+e.getMessage());
        }
    }

    //去掉字符串中可能出现的'`'
    private static String replacePoint(String orgStr){
        if (StringUtils.isBlank(orgStr)){
            return "";
        }
        return orgStr.replace("`","");
    }
}
