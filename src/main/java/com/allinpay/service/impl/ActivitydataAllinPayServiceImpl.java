package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.csvparser.WechatCsvParser;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.ExcelUtils;
import com.allinpay.entity.*;
import com.allinpay.entity.vo.ActivitydataAllinPayQueryVo;
import com.allinpay.entity.vo.ActivitydataWechatQueryVo;
import com.allinpay.mapper.TEtcActivitydataAllinpayMapper;
import com.allinpay.mapper.TEtcActivitydataWechatMapper;
import com.allinpay.service.IEtcActivitydataAllinPayService;
import com.allinpay.service.IEtcActivitydataWechatService;
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
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ActivitydataAllinPayServiceImpl implements IEtcActivitydataAllinPayService {

    @Autowired
    private TEtcActivitydataAllinpayMapper activitydataAllinpayMapper;
    @Autowired
    private Validator validator;

    @Override
    public ResponseBean queryCondition(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo) {
        PageHelper.startPage(activitydataAllinPayQueryVo.getPageNo(), activitydataAllinPayQueryVo.getPageSize());

        List<TEtcActivitydataAllinpay> tEtcActivitydataAllinpays= (List) activitydataAllinpayMapper.selectList(
                new QueryWrapper<TEtcActivitydataAllinpay>()
                        .eq("AC_BATCH_ID",activitydataAllinPayQueryVo.getAcBatchId())
                        .eq(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcMerchantId()),"AC_MERCHANT_ID",activitydataAllinPayQueryVo.getAcMerchantId())
                        .eq(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcExchangeStatues()),"AC_EXCHANGE_STATUES",activitydataAllinPayQueryVo.getAcExchangeStatues())
                        .like(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcMerchantName()),"AC_MERCHANT_NAME",activitydataAllinPayQueryVo.getAcMerchantName())
                        .between(activitydataAllinPayQueryVo.getCreateTimeStart()!=null&&activitydataAllinPayQueryVo.getCreateTimeEnd()!=null,
                                "CREATE_TIME",activitydataAllinPayQueryVo.getCreateTimeStart(),activitydataAllinPayQueryVo.getCreateTimeEnd())
                        .orderByDesc("AC_ALLIN_ID")

        );
        PageInfo<TEtcActivitydataAllinpay> pageInfo = new PageInfo<TEtcActivitydataAllinpay>(tEtcActivitydataAllinpays);
        return ResponseBean.ok(tEtcActivitydataAllinpays, pageInfo.getTotal());
    }


    @Override
    public ResponseBean query(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo) {

        List<TEtcActivitydataAllinpay> tEtcActivitydataAllinpays= (List) activitydataAllinpayMapper.selectList(
                new QueryWrapper<TEtcActivitydataAllinpay>()
                        .eq("AC_BATCH_ID",activitydataAllinPayQueryVo.getAcBatchId())
                        .eq(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcMerchantId()),"AC_MERCHANT_ID",activitydataAllinPayQueryVo.getAcMerchantId())
                        .eq(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcExchangeStatues()),"AC_EXCHANGE_STATUES",activitydataAllinPayQueryVo.getAcExchangeStatues())
                        .like(StringUtils.isNotBlank(activitydataAllinPayQueryVo.getAcMerchantName()),"AC_MERCHANT_NAME",activitydataAllinPayQueryVo.getAcMerchantName())
                        .between(activitydataAllinPayQueryVo.getCreateTimeStart()!=null&&activitydataAllinPayQueryVo.getCreateTimeEnd()!=null,
                                "CREATE_TIME",activitydataAllinPayQueryVo.getCreateTimeStart(),activitydataAllinPayQueryVo.getCreateTimeEnd())
                        .orderByDesc("AC_ALLIN_ID")

        );
        return ResponseBean.ok(tEtcActivitydataAllinpays);
    }

    @Override
    public ResponseBean batchImport(MultipartFile multipartFile) {
        //内存查询是否唯一
        List<TEtcActivitydataAllinpay> allData=activitydataAllinpayMapper.selectList(new QueryWrapper<TEtcActivitydataAllinpay>());
        Map<String,TEtcActivitydataAllinpay> allDataSelect=new HashMap<>();

        for (TEtcActivitydataAllinpay tEtcActivitydataAllinpay:allData){
            allDataSelect.put(tEtcActivitydataAllinpay.getAcPayNum(),tEtcActivitydataAllinpay);
        }
        //导入数据
        List<Object> datas= ExcelUtils.importExcel(multipartFile, ActivitydataAllinpayExcelModal.class);

        List<TEtcActivitydataAllinpay> insertData=new ArrayList<>();

        for(int i=0;i<datas.size();i++){
            ActivitydataAllinpayExcelModal allinpayExcelModal= (ActivitydataAllinpayExcelModal) datas.get(i);
            TEtcActivitydataAllinpay activitydataAllinpay=new TEtcActivitydataAllinpay();
            BeanUtils.copyProperties(allinpayExcelModal,activitydataAllinpay);
            BindException bindException = new BindException(activitydataAllinpay, "TEtcActivitydataAllinpay");
            validator.validate(activitydataAllinpay,bindException);
            if (bindException.hasErrors()) {
                String errorResponse = getErrorResponse(bindException);
                log.info("第"+(i+1)+"行请求参数异常:{}", errorResponse);
                throw new AllinpayException("第"+(i+1)+"行参数异常"+ errorResponse);
            }

            if (isDataOnly(allDataSelect,activitydataAllinpay)){
                activitydataAllinpay.setModifyTime(new Date());
                insertData.add(activitydataAllinpay);
            }else {
                throw new AllinpayException("第"+(i+1)+"行数据重复");
            }
        }

        int count=batchInsertData(insertData);

        log.info("{}条数据插入成功", count);
        return ResponseBean.ok(count);
    }

    @Override
    public ResponseBean batchOutput(ActivitydataAllinPayQueryVo activitydataAllinPayQueryVo, HttpServletResponse response) throws IOException {
        //获取数据
        ResponseBean responseBean=query(activitydataAllinPayQueryVo);
        List<TEtcActivitydataAllinpay> tEtcActivitydataAllinpays= (List<TEtcActivitydataAllinpay>) responseBean.getData();

        //转换成excel实例 用数据库实例会发身mybaits错误
        List<ActivitydataAllinpayExcelModal> activitydataAllinpayExcelModals=new ArrayList<>();
        for (TEtcActivitydataAllinpay tEtcActivitydataAllinpay:tEtcActivitydataAllinpays){
            ActivitydataAllinpayExcelModal allinpayExcelModal=new ActivitydataAllinpayExcelModal();
            BeanUtils.copyProperties(tEtcActivitydataAllinpay,allinpayExcelModal);
            activitydataAllinpayExcelModals.add(allinpayExcelModal);
        }
        String startTime=activitydataAllinPayQueryVo.getCreateTimeStart()==null?"所有":DateUtil.getSimpleDateFormat(activitydataAllinPayQueryVo.getCreateTimeStart(),"yyyy-MM-dd");
        String endTime=activitydataAllinPayQueryVo.getCreateTimeEnd()==null?"所有":DateUtil.getSimpleDateFormat(activitydataAllinPayQueryVo.getCreateTimeEnd(),"yyyy-MM-dd");
        String fileName = "收银宝"+ startTime+"——"+endTime+"数据";
        return ResponseBean.ok(ExcelUtils.outPutExcel(activitydataAllinpayExcelModals,response,ActivitydataAllinpayExcelModal.class,fileName+".xls",fileName));
    }



    private int batchInsertData(List<TEtcActivitydataAllinpay> insertData){
        //为了防止SQL语句超出长度出错，分成几次插入
        int count=0;
        if(insertData.size()<=300){
            count+=activitydataAllinpayMapper.insertBatch(insertData);
        }else{
            int times = (int)Math.ceil(insertData.size()/300.0 );
            for(int i=0; i<times; i++ ){
                System.out.println("分批插入："+ i);
                count+=activitydataAllinpayMapper.insertBatch(insertData.subList(i*300,Math.min((i+1)*300, insertData.size())));
            }
        }
        return count;
    }





    //唯一判断
    public boolean isDataOnly(Map<String, TEtcActivitydataAllinpay> allDataSelect, TEtcActivitydataAllinpay tEtcActivitydataAllinpay){
        return allDataSelect.get(tEtcActivitydataAllinpay.getAcPayNum())==null;
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
