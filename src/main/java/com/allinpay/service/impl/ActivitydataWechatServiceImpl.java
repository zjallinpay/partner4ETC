package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.csvparser.WechatCsvParser;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.ExcelUtils;
import com.allinpay.entity.*;
import com.allinpay.entity.vo.ActivitydataWechatQueryVo;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.mapper.TEtcActivitydataWechatMapper;
import com.allinpay.service.IEtcActivitydataWechatService;
import com.allinpay.service.ITEtcActivityService;
import com.allinpay.service.ITEtcMerchantsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ActivitydataWechatServiceImpl implements IEtcActivitydataWechatService {

    @Autowired
    private TEtcActivitydataWechatMapper tEtcActivitydataWechatMapper;

    @Autowired
    private Validator validator;
    @Autowired
    private ITEtcMerchantsService merchantsService;


    @Override
    public ResponseBean queryCondition(ActivitydataWechatQueryVo activitydataWechatQueryVo) {
        PageHelper.startPage(activitydataWechatQueryVo.getPageNo(), activitydataWechatQueryVo.getPageSize());

        List<TEtcActivtydataWechat> tEtcActivtydataWechatList= (List) tEtcActivitydataWechatMapper.selectList(
                new QueryWrapper<TEtcActivtydataWechat>()
                        .eq("AC_BATCH_ID",activitydataWechatQueryVo.getAcBatchId())
                        .eq(StringUtils.isNotBlank(activitydataWechatQueryVo.getAcExchangeType()),"AC_EXCHANGE_TYPE",activitydataWechatQueryVo.getAcExchangeType())
                        .eq(StringUtils.isNotBlank(activitydataWechatQueryVo.getAcPaymerchantId()),"AC_PAYMERCHANT_ID",activitydataWechatQueryVo.getAcPaymerchantId())
                        .between(activitydataWechatQueryVo.getAcPaytimeStart()!=null&&activitydataWechatQueryVo.getAcPaytimeEnd()!=null,
                                "AC_PAYTIME",activitydataWechatQueryVo.getAcPaytimeStart(),activitydataWechatQueryVo.getAcPaytimeEnd())
                        .orderByDesc("AC_WE_ID")

        );
        PageInfo<TEtcActivtydataWechat> pageInfo = new PageInfo<TEtcActivtydataWechat>(tEtcActivtydataWechatList);
        return ResponseBean.ok(tEtcActivtydataWechatList, pageInfo.getTotal());
    }

    @Override
    public ResponseBean batchImport(MultipartFile multipartFile) {

        String filename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(filename)){
            return ResponseBean.error("无上传文件");
        }

        List<TEtcActivtydataWechat> allData=tEtcActivitydataWechatMapper.selectList(new QueryWrapper<TEtcActivtydataWechat>());
        Map<String,TEtcActivtydataWechat> allDataSelect=new HashMap<>();

        for (TEtcActivtydataWechat tEtcActivtydataWechat:allData){
            allDataSelect.put(tEtcActivtydataWechat.getAcDiscountId()+"-"+tEtcActivtydataWechat.getAcPaymentId(),tEtcActivtydataWechat);
        }

        if (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx")) {
            //微信外部数据
            List<Object> datas= ExcelUtils.importExcel(multipartFile, ActivitydataWechatExcelModal.class);

            List<TEtcActivtydataWechat> insertData=new ArrayList<>();

            for(int i=0;i<datas.size();i++){
                ActivitydataWechatExcelModal wechatExcelModal= (ActivitydataWechatExcelModal) datas.get(i);
                TEtcActivtydataWechat activtydataWechat=new TEtcActivtydataWechat();
                BeanUtils.copyProperties(wechatExcelModal,activtydataWechat);
                BindException bindException = new BindException(activtydataWechat, "TEtcActivtydataWechat");
                validator.validate(activtydataWechat,bindException);
                if (bindException.hasErrors()) {
                    String errorResponse = getErrorResponse(bindException);
                    log.info("第"+(i+1)+"行请求参数异常:{}", errorResponse);
                    throw new AllinpayException("第"+(i+1)+"行参数异常"+ errorResponse);
                }

                if (isDataOnly(allDataSelect,activtydataWechat)){
                    activtydataWechat.setCteateTime(new Date());
                    activtydataWechat.setModifyTime(new Date());
                    insertData.add(activtydataWechat);
                }else {
                    throw new AllinpayException("第"+(i+1)+"行数据重复");
                }
            }

            int count=batchInsertData(insertData);

            log.info("{}条数据插入成功", count);
            return ResponseBean.ok(count);

        }else if (filename.toLowerCase().endsWith(".csv")){

            //微信内部数据
            List<TEtcActivtydataWechat> insertData=new ArrayList<>();
            List<TEtcActivtydataWechat> inputData=new ArrayList<>();
            WechatCsvParser.readCSV(multipartFile,inputData);

            for (int i=0;i<inputData.size();i++){
                 TEtcActivtydataWechat activtydataWechat=inputData.get(i);
                 BindException bindException = new BindException(activtydataWechat, "TEtcActivtydataWechat");
                 validator.validate(activtydataWechat,bindException);
                 if (bindException.hasErrors()) {
                     String errorResponse = getErrorResponse(bindException);
                     log.info("第"+(i+1)+"行请求参数异常:{}", errorResponse);
                     throw new AllinpayException("第"+(i+1)+"行参数异常"+ errorResponse);
                 }
                if (isDataOnly(allDataSelect,activtydataWechat)){
                    activtydataWechat.setCteateTime(new Date());
                    activtydataWechat.setModifyTime(new Date());
                    insertData.add(activtydataWechat);
                }else {
                    throw new AllinpayException("第"+(i+1)+"行数据重复");
                }

            }

            int count=batchInsertData(insertData);
            log.info("{}条数据插入成功", count);
            return ResponseBean.ok(count);

        }else {
            return ResponseBean.error("错误的文件格式");
        }


    }



    @Override
    public ResponseBean query(ActivitydataWechatQueryVo activitydataWechatQueryVo) {
        List<TEtcActivtydataWechat> tEtcActivtydataWechatList= (List) tEtcActivitydataWechatMapper.selectList(
                new QueryWrapper<TEtcActivtydataWechat>()
                        .eq("AC_BATCH_ID",activitydataWechatQueryVo.getAcBatchId())
                        .eq(StringUtils.isNotBlank(activitydataWechatQueryVo.getAcExchangeType()),"AC_EXCHANGE_TYPE",activitydataWechatQueryVo.getAcExchangeType())
                        .eq(StringUtils.isNotBlank(activitydataWechatQueryVo.getAcPaymerchantId()),"AC_PAYMERCHANT_ID",activitydataWechatQueryVo.getAcPaymerchantId())
                        .between(activitydataWechatQueryVo.getAcPaytimeStart()!=null&&activitydataWechatQueryVo.getAcPaytimeEnd()!=null,
                                "AC_PAYTIME",activitydataWechatQueryVo.getAcPaytimeStart(),activitydataWechatQueryVo.getAcPaytimeEnd())
                        .orderByDesc("AC_WE_ID")

        );
        return ResponseBean.ok(tEtcActivtydataWechatList);
    }

    @Override
    public ResponseBean batchOutput(ActivitydataWechatQueryVo activitydataWechatQueryVo, HttpServletResponse response) throws IOException {
        //获取数据
        ResponseBean responseBean=query(activitydataWechatQueryVo);
        List<TEtcActivtydataWechat> tEtcActivtydataWechatList= (List<TEtcActivtydataWechat>) responseBean.getData();

        //转换成excel实例 用数据库实例会发身mybaits错误
        List<ActivityAlldataWechatExcelModal> activityAlldataWechatExcelModals=new ArrayList<>();
        for (TEtcActivtydataWechat tEtcActivtydataWechat:tEtcActivtydataWechatList){
            ActivityAlldataWechatExcelModal activityAlldataWechatExcelModal=new ActivityAlldataWechatExcelModal();
            BeanUtils.copyProperties(tEtcActivtydataWechat,activityAlldataWechatExcelModal);
            activityAlldataWechatExcelModals.add(activityAlldataWechatExcelModal);
        }
        String startTime=activitydataWechatQueryVo.getAcPaytimeStart()==null?"所有":DateUtil.getSimpleDateFormat(activitydataWechatQueryVo.getAcPaytimeStart(),"yyyy-MM-dd");
        String endTime=activitydataWechatQueryVo.getAcPaytimeEnd()==null?"所有":DateUtil.getSimpleDateFormat(activitydataWechatQueryVo.getAcPaytimeStart(),"yyyy-MM-dd");
        String fileName = "微信"+ startTime+"——"+endTime+"数据";
        return ResponseBean.ok(ExcelUtils.outPutExcel(activityAlldataWechatExcelModals,response,ActivityAlldataWechatExcelModal.class,fileName+".xls",fileName));
    }


    private int batchInsertData(List<TEtcActivtydataWechat> insertData){
        //为了防止SQL语句超出长度出错，分成几次插入
        int count=0;
        if(insertData.size()<=300){
            count+=tEtcActivitydataWechatMapper.insertBatch(insertData);
        }else{
            int times = (int)Math.ceil(insertData.size()/300.0 );
            for(int i=0; i<times; i++ ){
                System.out.println("分批插入："+ i);
                count+=tEtcActivitydataWechatMapper.insertBatch(insertData.subList(i*300,Math.min((i+1)*300, insertData.size())));
            }
        }
        return count;
    }




    //唯一判断
    public boolean isDataOnly(Map<String, TEtcActivtydataWechat> allDataSelect, TEtcActivtydataWechat activtydataWechat){
        return allDataSelect.get(activtydataWechat.getAcDiscountId()+"-"+activtydataWechat.getAcPaymentId())==null;
    }


    /**
     * 获取错误返回信息
     *
     * @param bindingResult
     * @return
     */
    public String getErrorResponse(BindingResult bindingResult) {
        StringBuffer sb = new StringBuffer();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            sb.append(objectError.getDefaultMessage()).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
