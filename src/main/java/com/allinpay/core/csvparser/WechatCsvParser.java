package com.allinpay.core.csvparser;

import com.allinpay.core.exception.AllinpayException;
import com.allinpay.entity.TEtcActivtydataWechat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
@Slf4j
public class WechatCsvParser  {


    public static void readCSV(MultipartFile multipartFile, List<TEtcActivtydataWechat> list) {
        String filename = multipartFile.getOriginalFilename();
        InputStream is = null;
        try {
        if (filename != null &&filename.toLowerCase().endsWith(".csv")) {
            is = multipartFile.getInputStream();
        } else {
            throw new AllinpayException("上传文件非csv文件");
        }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"GBK"));
            boolean sign = false;       //用来跳过第一行的名称
            int count=0;
            while(reader.ready()) {
                String line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                String acBatchId, acDiscountId, acDiscountType, acExchangeType,acPaymentId,acPaymerchantId,acEquimentId,acBankNum;
                Date acPaytime;
                BigDecimal acDiscountNum,acOrderAmount;
                if (st.hasMoreTokens() && sign) {
                    count+=1;
                    int countTokens=st.countTokens();
                    if (countTokens<10){
                        throw new AllinpayException("第"+count+"行数据不规范");
                    }
                    acBatchId = st.nextToken();
                    acDiscountId=st.nextToken();
                    acDiscountType=st.nextToken();
                    acDiscountNum=new BigDecimal(st.nextToken());
                    acOrderAmount=new BigDecimal(st.nextToken());
                    acExchangeType=st.nextToken();
                    acPaymentId=st.nextToken();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    acPaytime= sdf.parse(st.nextToken());
                    acPaymerchantId=st.nextToken();
                    acEquimentId=countTokens==11?st.nextToken():null;
                    acBankNum=st.nextToken();
                    list.add(new TEtcActivtydataWechat(null,acBatchId,acDiscountId,acDiscountType,acDiscountNum,acOrderAmount,acExchangeType,acPaymentId,acPaytime,acPaymerchantId,null,acEquimentId,acBankNum,null,null));
                }
                else {
                    sign = true;
                }
            }
            reader.close();

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
            throw new AllinpayException("解析csv文件出错");
        }
    }
}
