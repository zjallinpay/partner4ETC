package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.DateUtils;
import com.allinpay.core.util.ExcelUtils;
import com.allinpay.entity.*;
import com.allinpay.mapper.*;
import com.allinpay.service.ITEtcStatsrecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class TEtcStatsrecordServiceImpl implements ITEtcStatsrecordService {

    @Autowired
    private TEtcStatsRecordMapper tEtcStatsRecordMapper;
    @Autowired
    private TEtcActivityMapper tEtcActivityMapper;

    @Autowired
    private TEtcActivitydataWechatMapper tEtcActivitydataWechatMapper;

    @Autowired
    private TEtcActivitydataAllinpayMapper tEtcActivitydataAllinpayMapper;
    @Autowired
    private TEtcMerchantsMapper merchantsMapper;
    @Override
    public ResponseBean genateRecord(TEtcStatsrecord tEtcStatsrecord) {

        TEtcActivity tEtcActivity=tEtcActivityMapper.selectList(new QueryWrapper<TEtcActivity>().eq("ACTIVITY_BATCHNO",tEtcStatsrecord.getAcBatchId())).get(0);
        if (null==tEtcActivity){
            throw new AllinpayException("无此活动");
        }
        tEtcStatsrecord.setActivityChnnal(tEtcActivity.getActivityChnnal());
        tEtcStatsrecord.setActivityType(tEtcActivity.getActivityType());
        tEtcStatsrecord.setActivityName(tEtcActivity.getActivityName());
        tEtcStatsrecord.setIsOutActivity(tEtcActivity.getIsOutActivity());
        log.info("开始时间{} 结束时间{}", DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReStartTime(),"yyyy-MM-dd"),DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReEndTime(),"yyyy-MM-dd"));
        tEtcStatsrecord.setCreateTime(new Date());
        tEtcStatsrecord.setModifyTime(new Date());
        return ResponseBean.ok(tEtcStatsRecordMapper.insert(tEtcStatsrecord)>0);
    }

    @Override
    public ResponseBean downloadRecord(Integer reId, HttpServletResponse response) throws IOException {


        TEtcStatsrecord tEtcStatsrecord=tEtcStatsRecordMapper.selectById(reId);
        String startTime=tEtcStatsrecord.getReStartTime()==null?"所有": DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReStartTime(),"yyyy-MM-dd");
        String endTime=tEtcStatsrecord.getReEndTime()==null?"所有":DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReEndTime(),"yyyy-MM-dd");
        String fileName =tEtcStatsrecord.getActivityName()+ startTime+"——"+endTime+"报表";

        //内部数据源的微信商圈报表
        if ("商圈".equals(tEtcStatsrecord.getActivityType())
                &&"微信原生活动".equals(tEtcStatsrecord.getActivityChnnal())
                &&"否".equals(tEtcStatsrecord.getIsOutActivity())){
                return ResponseBean.ok(genaterMerRecord(tEtcStatsrecord,fileName,response));
        }


        //外部数据源的微信商圈报表
        if ("商圈".equals(tEtcStatsrecord.getActivityType())
                &&"微信原生活动".equals(tEtcStatsrecord.getActivityChnnal())
                &&"是".equals(tEtcStatsrecord.getIsOutActivity())){
            return ResponseBean.ok(genaterExMerRecord(tEtcStatsrecord,fileName,response));
        }

        //微信物业报表
        if ("物业".equals(tEtcStatsrecord.getActivityType())
            &&"微信原生活动".equals(tEtcStatsrecord.getActivityChnnal())
        ){
            return ResponseBean.ok(genaterPropertyRecord(tEtcStatsrecord,fileName,response));
        }

        //收银宝物业报表
        if ("物业".equals(tEtcStatsrecord.getActivityType())
                &&"收银宝活动".equals(tEtcStatsrecord.getActivityChnnal())
        ){
            return ResponseBean.ok(genaterAllinPropertyRecord(tEtcStatsrecord,fileName,response));
        }

        return ResponseBean.error("无此类型报表");


    }


    @Override
    public ResponseBean queryAll(Integer pageNo, Integer pageSize) {
        if (pageNo==null||pageSize==null){
            pageNo=1;
            pageSize=10;
        }
        PageHelper.startPage(pageNo, pageSize);

        List<TEtcStatsrecord> tEtcStatsrecords= (List) tEtcStatsRecordMapper.selectList(
                new QueryWrapper<TEtcStatsrecord>()
                        .orderByDesc("CREATE_TIME")
        );
        PageInfo<TEtcStatsrecord> pageInfo = new PageInfo<TEtcStatsrecord>(tEtcStatsrecords);
        return ResponseBean.ok(tEtcStatsrecords, pageInfo.getTotal());
    }


    //内部数据源的微信商圈报表
    private boolean genaterMerRecord(TEtcStatsrecord tEtcStatsrecord, String fileName, HttpServletResponse response) throws IOException {

            List<MerchantStatsModal> datas= tEtcActivitydataWechatMapper.selectMerchantStatsDatas(tEtcStatsrecord);
            BigDecimal discountAmount=new BigDecimal(0);
            int checkAmount=0;
            for (int i=0;i<datas.size();i++){
                MerchantStatsModal merchantStatsModal=datas.get(i);
                merchantStatsModal.setSort(i+1+"");
                discountAmount=discountAmount.add(merchantStatsModal.getDiscountAmount());
                checkAmount+=merchantStatsModal.getCheckNum();
            }
            MerchantStatsModal totalModal=new MerchantStatsModal();
            totalModal.setMerName("总计");
            totalModal.setDiscountAmount(discountAmount);
            totalModal.setDiscountAndCheckText(checkAmount+"");
            datas.add(totalModal);
           return ExcelUtils.outPutExcel(datas,response, MerchantStatsModal.class,fileName+".xls",fileName);

    }

    //外部数据源的微信商圈报表
    private boolean genaterExMerRecord(TEtcStatsrecord tEtcStatsrecord, String fileName, HttpServletResponse response) throws IOException {

        List<MerchantStatsModal> datas= tEtcActivitydataWechatMapper.selectExMerchantStatsDatas(tEtcStatsrecord);
        BigDecimal discountAmount=new BigDecimal(0);
        int checkAmount=0;
        for (int i=0;i<datas.size();i++){
            MerchantStatsModal merchantStatsModal=datas.get(i);
            merchantStatsModal.setSort(i+1+"");
            discountAmount=discountAmount.add(merchantStatsModal.getDiscountAmount());
            checkAmount+=merchantStatsModal.getCheckNum();
        }
        MerchantStatsModal totalModal=new MerchantStatsModal();
        totalModal.setMerName("总计");
        totalModal.setDiscountAmount(discountAmount);
        totalModal.setDiscountAndCheckText(checkAmount+"");
        datas.add(totalModal);
        return ExcelUtils.outPutExcel(datas,response, MerchantStatsModal.class,fileName+".xls",fileName);

    }



    //生成物业的报表
    private boolean genaterPropertyRecord(TEtcStatsrecord tEtcStatsrecord,String fileName, HttpServletResponse response) throws IOException {
            List<PropertyStatsModal> allDatas=new ArrayList<>();
            BigDecimal discountAmount=new BigDecimal(0);
            BigDecimal wechatAmount=new BigDecimal(0);
            BigDecimal[] amountdata=new BigDecimal[2];
            amountdata[0]=discountAmount;
            amountdata[1]=wechatAmount;

            if ("15230233,15230209,15230255".contains(tEtcStatsrecord.getAcBatchId())){
                List<String> batchIds=new ArrayList<>();
                batchIds.add("15230233");
                batchIds.add("15230209");
                batchIds.add("15230255");
                tEtcStatsrecord.setAcBatchIds(batchIds);
                wechatPropertyRecord(tEtcStatsrecord,amountdata,allDatas);
            }else {
                wechatPropertyRecord(tEtcStatsrecord,amountdata,allDatas);
            }

            PropertyStatsModal totalAmount=new PropertyStatsModal();
            totalAmount.setActiveUserCount("总计");
            totalAmount.setWechatDiscountAmount(amountdata[0].toString());
            totalAmount.setWechatAmount(amountdata[1].toString());
             allDatas.add(totalAmount);
            return ExcelUtils.outPutExcel(allDatas,response, PropertyStatsModal.class,fileName+".xls",fileName);



    }


    //生成收银宝物业的报表
    private boolean genaterAllinPropertyRecord(TEtcStatsrecord tEtcStatsrecord,String fileName, HttpServletResponse response) throws IOException {

        List<PropertyStatsModal> ogrinData= tEtcActivitydataAllinpayMapper.selectPropertStatsDatas(tEtcStatsrecord);
        if (ogrinData==null||ogrinData.size()==0){
            throw new AllinpayException("无数据");
        }

        //商家数据
        List<TEtcMerchants> merchants=merchantsMapper.selectList(new QueryWrapper<TEtcMerchants>());
        Map<String,TEtcMerchants> selectMerMap=new HashMap<>();
        for (TEtcMerchants merchant :merchants){
            selectMerMap.put(merchant.getAllinpayMerid(),merchant);
        }
        BigDecimal discountAmount=new BigDecimal(0);
        BigDecimal allinPayAmount=new BigDecimal(0);
        for (int i=0;i<ogrinData.size();i++){
            PropertyStatsModal propertyStatsModal=ogrinData.get(i);
            propertyStatsModal.setSort(i+1+"");
            BigDecimal itemDiscountAmount=new BigDecimal(propertyStatsModal.getCheckNum()).multiply(propertyStatsModal.getDiscountNum());
            discountAmount=discountAmount.add(itemDiscountAmount);
            propertyStatsModal.setBnakDiscountAmount(itemDiscountAmount.toString());
            propertyStatsModal.setAcName(tEtcStatsrecord.getActivityName());
            allinPayAmount=allinPayAmount.add(new BigDecimal(propertyStatsModal.getBankAmount()));
            TEtcMerchants merchant=selectMerMap.get(propertyStatsModal.getAcMerchantId());
            if (null!=merchant){
                propertyStatsModal.setArea(merchant.getArea());
            }
        }

        PropertyStatsModal totalAmount=new PropertyStatsModal();
        totalAmount.setActiveUserCount("总计");
        totalAmount.setBnakDiscountAmount(discountAmount.toString());
        totalAmount.setBankAmount(allinPayAmount.toString());
        ogrinData.add(totalAmount);
        return ExcelUtils.outPutExcel(ogrinData,response, PropertyStatsModal.class,fileName+".xls",fileName);
    }


    private void wechatPropertyRecord(TEtcStatsrecord tEtcStatsrecord, BigDecimal[] amountdata, List<PropertyStatsModal> allDatas) {
        //原始数据
        List<PropertyStatsModal> ogrinData= tEtcActivitydataWechatMapper.selectPropertStatsDatas(tEtcStatsrecord);
        if (ogrinData==null||ogrinData.size()==0){
            return;
        }
        //商家数据
        List<TEtcMerchants> merchants=merchantsMapper.selectList(new QueryWrapper<TEtcMerchants>());
        Map<String,TEtcMerchants> selectMerMap=new HashMap<>();
        for (TEtcMerchants merchant :merchants){
            selectMerMap.put(merchant.getWxpayMerid(),merchant);
        }
        Map<String,PropertyStatsModal> selectMap=new HashMap<>();
        List<PropertyStatsModal> amountDatas=tEtcActivitydataWechatMapper.selectPropertStatsAmountDatas(tEtcStatsrecord);
        for (PropertyStatsModal amountData: amountDatas){
            selectMap.put(amountData.getAcMerchantId(),amountData);
        }

        for (int i=0;i<ogrinData.size();i++){
            PropertyStatsModal propertyStatsModal=ogrinData.get(i);
            propertyStatsModal.setSort(i+1+"");
            BigDecimal itemDiscountAmount=new BigDecimal(propertyStatsModal.getCheckNum()).multiply(propertyStatsModal.getDiscountNum());
            amountdata[0]=amountdata[0].add(itemDiscountAmount);
            propertyStatsModal.setWechatDiscountAmount(itemDiscountAmount.toString());
            propertyStatsModal.setAcName(tEtcStatsrecord.getActivityName());
            PropertyStatsModal amountData=selectMap.get(propertyStatsModal.getAcMerchantId());
            TEtcMerchants selectMerchant=selectMerMap.get(propertyStatsModal.getAcMerchantId());
            if (selectMerchant!=null){
                propertyStatsModal.setArea(selectMerchant.getArea());
                propertyStatsModal.setMerName(selectMerchant.getMerName());
            }

            if (amountData!=null){
                propertyStatsModal.setActiveUserCount(amountData.getActiveUserCount());
                propertyStatsModal.setWechatAmount(amountData.getWechatAmount());
                amountdata[1]=amountdata[1].add(new BigDecimal(amountData.getWechatAmount()));
            }
        }

        allDatas.addAll(ogrinData);
    }
}
