package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.ExcelUtils;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.core.util.FileUtil;
import com.allinpay.entity.ActivityExcelModal;
import com.allinpay.entity.MerchantsExcelModal;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.vo.AcitvityQueryVo;
import com.allinpay.entity.vo.MerchantActivityVo;
import com.allinpay.mapper.TEtcActivityMapper;
import com.allinpay.mapper.TEtcMerchantsMapper;
import com.allinpay.service.ITEtcActivityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@PropertySource("file:config/resource.properties")
public class TEtcActivityServiceImpl implements ITEtcActivityService {

    @Autowired
    private TEtcActivityMapper tEtcActivityMapper;

    @Autowired
    private TEtcMerchantsMapper tEtcMerchantsMapper;

    @Value("${fileSourcePath}")
    private String fileSourcePath;

    @Override
    public ResponseBean queryPage(AcitvityQueryVo acitvityQueryVo) {
        PageHelper.startPage(acitvityQueryVo.getPageNo(), acitvityQueryVo.getPageSize());
        List<TEtcActivity> tEtcActivities=tEtcActivityMapper.selectList(
                new QueryWrapper<TEtcActivity>()
                .eq(StringUtils.isNotBlank(acitvityQueryVo.getDiscountType()),"DISCOUNT_TYPE",acitvityQueryVo.getDiscountType())
                .eq(StringUtils.isNotBlank(acitvityQueryVo.getActivityBatchno()),"ACTIVITY_BATCHNO",acitvityQueryVo.getActivityBatchno())
                .eq(StringUtils.isNotBlank(acitvityQueryVo.getActivityChnnal()),"ACTIVITY_CHNNAL",acitvityQueryVo.getActivityChnnal())
                .eq(StringUtils.isNotBlank(acitvityQueryVo.getFundType()),"FUND_TYPE",acitvityQueryVo.getFundType())
                .like(StringUtils.isNotBlank(acitvityQueryVo.getActivityName()),"ACTIVITY_NAME",acitvityQueryVo.getActivityName())
                .like(StringUtils.isNotBlank(acitvityQueryVo.getCoopOrgan()),"COOP_ORGAN",acitvityQueryVo.getCoopOrgan())
                .orderByDesc("ACTIVITY_ID")
        );
        PageInfo<TEtcActivity> pageInfo = new PageInfo<TEtcActivity>(tEtcActivities);
        log.info("查询活动数据{}",tEtcActivities);
        return ResponseBean.ok(tEtcActivities, pageInfo.getTotal());
    }

    @Override
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcActivity tEtcActivity) {
        Integer actId=tEtcActivity.getActivityId();
        String filePath=FileUtil.updataFile(request.getFile(CommonConstant.ACTIVITY_FILE),
                fileSourcePath  + CommonConstant.SUB_DIR_ACTLICENSE);
        log.info("附加上传成功{}"+filePath);
        if (null==actId){
            //新增
            tEtcActivity.setActivityFile(filePath);

            tEtcActivity.setCreateTime(new Date());
            tEtcActivity.setModifyTime(new Date());
            return ResponseBean.ok(tEtcActivityMapper.insert(tEtcActivity)>0);
        }
        //更新
        TEtcActivity oldActivity=tEtcActivityMapper.selectById(actId);
        if (StringUtils.isNotBlank(filePath)){
            oldActivity.setActivityFile(filePath);
        }
        oldActivity.setActivityName(tEtcActivity.getActivityName());
        oldActivity.setActivityBatchno(tEtcActivity.getActivityBatchno());
        oldActivity.setActivityChnnal(tEtcActivity.getActivityChnnal());
        oldActivity.setDiscountType(tEtcActivity.getDiscountType());
        oldActivity.setCoopOrgan(tEtcActivity.getCoopOrgan());
        oldActivity.setStartTime(tEtcActivity.getStartTime());
        oldActivity.setEndTime(tEtcActivity.getEndTime());
        oldActivity.setFundType(tEtcActivity.getFundType());
        oldActivity.setActivityMaster(tEtcActivity.getActivityMaster());
        oldActivity.setAblestartTime(tEtcActivity.getAblestartTime());
        oldActivity.setAbleendTime(tEtcActivity.getAbleendTime());
        oldActivity.setAbleWeek(tEtcActivity.getAbleWeek());
        oldActivity.setBankLimit(tEtcActivity.getBankLimit());
        oldActivity.setActivityRemark(tEtcActivity.getActivityRemark());
        tEtcActivity.setModifyTime(new Date());
        return ResponseBean.ok(tEtcActivityMapper.updateById(oldActivity)>0);
    }

    @Override
    public ResponseBean findDetailByactId(int actId) {
        return ResponseBean.ok(tEtcActivityMapper.selectById(actId));
    }

    @Override
    public ResponseBean deleteById(int actId) {
        tEtcActivityMapper.deleteByActId(actId);
        return ResponseBean.ok(tEtcActivityMapper.deleteById(actId)>0);
    }

    @Override
    public ResponseBean batchImport(MultipartFile multipartFile) {
        //读取数据
        log.info("文件名{}"+ multipartFile.getOriginalFilename());

        List<Object> datas= ExcelUtils.importExcel(multipartFile, ActivityExcelModal.class);
        int count=0;
        for (Object data:datas){
            TEtcActivity tEtcActivity=new TEtcActivity();
            ActivityExcelModal activityExcelModal= (ActivityExcelModal) data;
            BeanUtils.copyProperties(activityExcelModal,tEtcActivity);
            tEtcActivity.setModifyTime(new Date());
            tEtcActivity.setCreateTime(new Date());
            count+=tEtcActivityMapper.insert(tEtcActivity);
            log.info("插入数据{}",tEtcActivity);
        }
        return ResponseBean.ok(count);
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadActFile(Integer actId) {
        TEtcActivity tEtcActivity=tEtcActivityMapper.selectById(actId);
        String fileName=tEtcActivity.getActivityFile();
        log.info("文件名称{}",fileName);
        if (StringUtils.isBlank(fileName)){
            throw new AllinpayException("附件不存在");
        }
        String path=fileSourcePath  + CommonConstant.SUB_DIR_ACTLICENSE;
        String filePath=path+fileName;
        log.info("文件路径{}",filePath);
        return FileDownloader.download(filePath,fileName);
    }

    @Override
    public ResponseBean queryActMers(Integer actId,Integer pageNo,Integer pageSize){
        PageHelper.startPage(pageNo, pageSize);
        List<Integer> merIds= tEtcActivityMapper.queryActMerIds(actId);
        if (null==merIds||merIds.size()==0){
            return ResponseBean.ok(new ArrayList<>(), 0l);
        }
        List<TEtcMerchants> datas=tEtcMerchantsMapper.selectBatchIds(merIds);
        PageInfo<TEtcMerchants> pageInfo = new PageInfo<TEtcMerchants>(datas);
        return ResponseBean.ok(datas, pageInfo.getTotal());
    }

    @Override
    public ResponseBean insertByMerId(MerchantActivityVo merchantActivityVo) {
        Integer merId=merchantActivityVo.getMerId();
         if (tEtcMerchantsMapper.selectById(merId)==null) {
            return ResponseBean.error("商户不存在");
        }
         merchantActivityVo.setCreateTime(new Date());
         merchantActivityVo.setModifyTime(new Date());
        return ResponseBean.ok(tEtcActivityMapper.insertByMerId(merchantActivityVo)>0);
    }

    @Override
    public ResponseBean batchInsertByActId(MultipartFile multipartFile, Integer actId) {
        //读取数据
        List<Object> datas=ExcelUtils.importExcel(multipartFile,MerchantsExcelModal.class);
        int count=0;
        for (Object data:datas){
            MerchantsExcelModal merchantsExcelModal= (MerchantsExcelModal) data;
            String merName=merchantsExcelModal.getMerName();
            TEtcMerchants tEtcMerchants=tEtcMerchantsMapper.selectOne(new QueryWrapper<TEtcMerchants>().eq("MER_NAME",merName));
            if (tEtcMerchants==null){
                log.error("不存在的商户{}",merName);
                continue;
            }
            MerchantActivityVo merchantActivityVo=new MerchantActivityVo(tEtcMerchants.getMerId(),actId,new Date(),new Date());
            log.info("插入的商户{}",merchantActivityVo);
            count+=tEtcActivityMapper.insertByMerId(merchantActivityVo);
        }
        return ResponseBean.ok(count);
    }

    @Override
    public ResponseBean batchOutput(Integer actId, HttpServletResponse response) throws IOException {
        TEtcActivity tEtcActivity=tEtcActivityMapper.selectById(actId);
        String actName=tEtcActivity.getActivityName();
        List<Integer> merIds= tEtcActivityMapper.queryActMerIds(actId);
        if (null==merIds||merIds.size()==0){
            return ResponseBean.ok("无数据");
        }
        List<TEtcMerchants> tEtcMerchants=tEtcMerchantsMapper.selectBatchIds(merIds);
        //转换成excel实例 用数据库实例会发身mybaits错误
        List<MerchantsExcelModal> merchantsExcelModals=new ArrayList<>();
        for (TEtcMerchants tEtcMerchant:tEtcMerchants){
            MerchantsExcelModal merchantsExcelModal=new MerchantsExcelModal();
            BeanUtils.copyProperties(tEtcMerchant,merchantsExcelModal);
            merchantsExcelModals.add(merchantsExcelModal);
        }
        log.info("参加的商户{}",merchantsExcelModals);
        String fileName = actName+"商户名单"+ DateUtil.getSimpleDateFormat(new Date(),"yyyy-MM-dd")+".xls";
        return ResponseBean.ok(ExcelUtils.outPutExcel(merchantsExcelModals,response,MerchantsExcelModal.class,fileName,"商户名单"));
    }

    @Override
    public ResponseBean deleteByMerId(Integer merId, Integer actId) {
        MerchantActivityVo merchantActivityVo=new MerchantActivityVo();
        merchantActivityVo.setMerId(merId);
        merchantActivityVo.setActivityId(actId);
        return ResponseBean.ok(tEtcActivityMapper.deleteByMerId(merchantActivityVo)>0);
    }
}
