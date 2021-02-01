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
        log.info("开始时间{} 结束时间{}", DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReStartTime(),"yyyy-MM-dd"),DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReEndTime(),"yyyy-MM-dd"));
        tEtcStatsrecord.setCreateTime(new Date());
        tEtcStatsrecord.setModifyTime(new Date());
        return ResponseBean.ok(tEtcStatsRecordMapper.insert(tEtcStatsrecord)>0);
    }

    @Override
    public ResponseBean downloadRecord(Integer reId, HttpServletResponse response) throws IOException {
        List<TEtcMerchants> merchants=merchantsMapper.selectList(new QueryWrapper<TEtcMerchants>());
        Map<String,List<TEtcMerchants>> selectByAllinpayIds=new HashMap<>();
        Map<String,List<TEtcMerchants>> selectBywechatIds=new HashMap<>();
        Map<String,List<TEtcMerchants>> selectByNames=new HashMap<>();
        Map<String,List<TEtcMerchants>> selectByEquiment=new HashMap<>();
        for (TEtcMerchants merchant:merchants){
            generteSelectMap(selectByAllinpayIds,merchant,merchant.getAllinpayMerid());
            generteSelectMap(selectBywechatIds,merchant,merchant.getWxpayMerid());
            generteSelectMap(selectByNames,merchant,merchant.getMerName());
            generteSelectMap(selectByEquiment,merchant,merchant.getEquipId());
        }

        TEtcStatsrecord tEtcStatsrecord=tEtcStatsRecordMapper.selectById(reId);
        TEtcActivity tEtcActivity=tEtcActivityMapper.selectList(new QueryWrapper<TEtcActivity>().eq("ACTIVITY_BATCHNO",tEtcStatsrecord.getAcBatchId())).get(0);
        String startTime=tEtcStatsrecord.getReStartTime()==null?"所有": DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReStartTime(),"yyyy-MM-dd");
        String endTime=tEtcStatsrecord.getReEndTime()==null?"所有":DateUtil.getSimpleDateFormat(tEtcStatsrecord.getReEndTime(),"yyyy-MM-dd");
        String fileName =tEtcActivity.getActivityName()+ startTime+"——"+endTime+"报表";

        if ("商圈".equals(tEtcStatsrecord.getActivityType())){
            return ResponseBean.ok(genaterMerRecord(tEtcStatsrecord,fileName,response,selectBywechatIds,selectByNames,selectByEquiment));
        }

        if ("物业".equals(tEtcStatsrecord.getActivityType())){
            return ResponseBean.ok(genaterPropertyRecord(tEtcStatsrecord,tEtcActivity.getActivityName(),fileName,response));
        }

        return ResponseBean.error("无此类型报表");


    }


    private void generteSelectMap(Map<String,List<TEtcMerchants>> map,TEtcMerchants merchant,String args){
        if (map.get(args)==null){
            List<TEtcMerchants> allinpayLists=new ArrayList<>();
            allinpayLists.add(merchant);
            map.put(args,allinpayLists);
        }else {
            map.get(args).add(merchant);
        }
    }

    @Override
    public ResponseBean queryAll(String acBatchId, Integer pageNo, Integer pageSize) {
        if (pageNo==null||pageSize==null){
            pageNo=1;
            pageSize=10;
        }
        PageHelper.startPage(pageNo, pageSize);

        List<TEtcStatsrecord> tEtcStatsrecords= (List) tEtcStatsRecordMapper.selectList(
                new QueryWrapper<TEtcStatsrecord>()
                        .eq("AC_BATCH_ID",acBatchId)
                        .orderByDesc("CREATE_TIME")
        );
        PageInfo<TEtcStatsrecord> pageInfo = new PageInfo<TEtcStatsrecord>(tEtcStatsrecords);
        return ResponseBean.ok(tEtcStatsrecords, pageInfo.getTotal());
    }

    //生成商圈的报表
    private boolean genaterMerRecord(TEtcStatsrecord tEtcStatsrecord, String fileName, HttpServletResponse response,
                    Map<String,List<TEtcMerchants>> selectBywechatIds, Map<String,List<TEtcMerchants>> selectByNames,Map<String,List<TEtcMerchants>> selectByEquiment) throws IOException {

        if ("微信原生活动".equals(tEtcStatsrecord.getActivityChnnal())){
            List<MerchantStatsModal> datas= tEtcActivitydataWechatMapper.selectMerchantStatsDatas(tEtcStatsrecord);
            BigDecimal discountAmount=new BigDecimal(0);
            int checkAmount=0;
            for (int i=0;i<datas.size();i++){
                MerchantStatsModal merchantStatsModal=datas.get(i);
                selectMerWechatName(merchantStatsModal,selectBywechatIds,selectByNames,selectByEquiment);
                merchantStatsModal.setSort(i+1+"");
                merchantStatsModal.setAcBatchId(tEtcStatsrecord.getAcBatchId());
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

        log.info("无此类型报表");
        return false;
    }



    private void selectMerWechatName(MerchantStatsModal merchantStatsModal, Map<String,List<TEtcMerchants>> selectBywechatIds, Map<String,List<TEtcMerchants>> selectByNames,Map<String,List<TEtcMerchants>> selectByEquiment){
        String wechatId=merchantStatsModal.getAcMerchantId();
        String merName=merchantStatsModal.getMerName();
        String equiId=merchantStatsModal.getEquipId();
        List<TEtcMerchants> list=null;
        if (StringUtils.isNotBlank(equiId)){

            list= selectByEquiment.get(equiId);
            if (list==null||list.size()==0){
                if (StringUtils.isBlank(merName)){
                    if (StringUtils.isNotBlank(wechatId)){

                        list=selectBywechatIds.get(wechatId);
                        if (list==null||list.size()==0){
                            merchantStatsModal.setMerName("商户号:"+wechatId);
                            return;
                        }
                        if (list.size()>0){
                            merchantStatsModal.setMerName(list.get(0).getMerName()+"——设备号:"+equiId);
                            return;
                        }

                    }

                }
                return;
            }else {

                merchantStatsModal.setMerName(list.get(0).getMerName());
                merchantStatsModal.setArea(list.get(0).getArea());
                merchantStatsModal.setTradingArea(list.get(0).getTradingArea());
            }
            return;
        }


        if (StringUtils.isNotBlank(wechatId)){

            list=selectBywechatIds.get(wechatId);
            if (list==null||list.size()==0){
                merchantStatsModal.setMerName("商户号:"+wechatId);
                return;
            }
            if (list.size()>0){
                merchantStatsModal.setMerName(list.get(0).getMerName());
                merchantStatsModal.setArea(list.get(0).getArea());
                merchantStatsModal.setTradingArea(list.get(0).getTradingArea());
                return;
            }

        }
        if (StringUtils.isNotBlank(merName)){

            list=selectByNames.get(merName);
            if (list==null||list.size()==0){
                return;
            }
            if (list.size()>0){
                merchantStatsModal.setArea(list.get(0).getArea());
                merchantStatsModal.setTradingArea(list.get(0).getTradingArea());
                return;
            }
        }

    }

    //生成物业的报表
    private boolean genaterPropertyRecord(TEtcStatsrecord tEtcStatsrecord, String acName,String fileName, HttpServletResponse response) throws IOException {
        if ("微信原生活动".equals(tEtcStatsrecord.getActivityChnnal())){
            //原始数据
            List<PropertyStatsModal> ogrinData= tEtcActivitydataWechatMapper.selectPropertStatsDatas(tEtcStatsrecord);
            if (ogrinData==null||ogrinData.size()==0){
                throw new AllinpayException("无数据");
            }
            Map<String,PropertyStatsModal> selectMap=new HashMap<>();
            List<PropertyStatsModal> amountDatas=tEtcActivitydataWechatMapper.selectPropertStatsAmountDatas(tEtcStatsrecord);
            for (PropertyStatsModal amountData: amountDatas){
                selectMap.put(amountData.getAcMerchantId(),amountData);
            }
            BigDecimal discountAmount=new BigDecimal(0);
            BigDecimal wechatAmount=new BigDecimal(0);
            for (int i=0;i<ogrinData.size();i++){
                PropertyStatsModal propertyStatsModal=ogrinData.get(i);
                propertyStatsModal.setSort(i+1+"");
                BigDecimal itemDiscountAmount=new BigDecimal(propertyStatsModal.getCheckNum()).multiply(propertyStatsModal.getDiscountNum());
                discountAmount=discountAmount.add(itemDiscountAmount);
                propertyStatsModal.setWechatDiscountAmount(itemDiscountAmount.toString());
                propertyStatsModal.setAcName(acName);
                PropertyStatsModal amountData=selectMap.get(propertyStatsModal.getAcMerchantId());

                if (amountData!=null){
                    propertyStatsModal.setActiveUserCount(amountData.getActiveUserCount());
                    propertyStatsModal.setWechatAmount(amountData.getWechatAmount());
                    wechatAmount=wechatAmount.add(new BigDecimal(amountData.getWechatAmount()));
                }
            }
            PropertyStatsModal totalAmount=new PropertyStatsModal();
            totalAmount.setActiveUserCount("总计");
            totalAmount.setWechatDiscountAmount(discountAmount.toString());
            totalAmount.setWechatAmount(wechatAmount.toString());
            ogrinData.add(totalAmount);
            return ExcelUtils.outPutExcel(ogrinData,response, PropertyStatsModal.class,fileName+".xls",fileName);
        }

        if ("收银宝活动".equals(tEtcStatsrecord.getActivityChnnal())){
            List<PropertyStatsModal> ogrinData= tEtcActivitydataAllinpayMapper.selectPropertStatsDatas(tEtcStatsrecord);
            if (ogrinData==null||ogrinData.size()==0){
                throw new AllinpayException("无数据");
            }
            BigDecimal discountAmount=new BigDecimal(0);
            BigDecimal allinPayAmount=new BigDecimal(0);
            for (int i=0;i<ogrinData.size();i++){
                PropertyStatsModal propertyStatsModal=ogrinData.get(i);
                propertyStatsModal.setSort(i+1+"");
                BigDecimal itemDiscountAmount=new BigDecimal(propertyStatsModal.getCheckNum()).multiply(propertyStatsModal.getDiscountNum());
                discountAmount=discountAmount.add(itemDiscountAmount);
                propertyStatsModal.setBnakDiscountAmount(itemDiscountAmount.toString());
                propertyStatsModal.setAcName(acName);
                allinPayAmount=allinPayAmount.add(new BigDecimal(propertyStatsModal.getBankAmount()));
            }

            PropertyStatsModal totalAmount=new PropertyStatsModal();
            totalAmount.setActiveUserCount("总计");
            totalAmount.setBnakDiscountAmount(discountAmount.toString());
            totalAmount.setBankAmount(allinPayAmount.toString());
            ogrinData.add(totalAmount);
            return ExcelUtils.outPutExcel(ogrinData,response, PropertyStatsModal.class,fileName+".xls",fileName);
        }

        log.info("无此类型报表");
        return false;
    }
}
