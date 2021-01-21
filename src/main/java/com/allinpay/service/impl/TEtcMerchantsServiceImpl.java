package com.allinpay.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.ExcelUtils;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.core.util.FileUtil;
import com.allinpay.entity.MerchantsExcelModal;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.TEtcSysUser;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.mapper.TEtcMerchantsMapper;
import com.allinpay.service.ITEtcMerchantsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@PropertySource("file:config/resource.properties")
public class TEtcMerchantsServiceImpl implements ITEtcMerchantsService {

    @Autowired
    private TEtcMerchantsMapper tEtcMerchantsMapper;

    @Value("${fileSourcePath}")
    private String fileSourcePath;

    @Override
    public ResponseBean queryPage(MerchantQueryVo merchantQueryVo) {

         PageHelper.startPage(merchantQueryVo.getPageNo(), merchantQueryVo.getPageSize());
        List<TEtcMerchants> merchantLists= (List) tEtcMerchantsMapper.selectList(
                new QueryWrapper<TEtcMerchants>()
                .eq(StringUtils.isNotBlank(merchantQueryVo.getBelongIndustry()),"BELONG_INDUSTRY",merchantQueryVo.getBelongIndustry())
                .eq(StringUtils.isNotBlank(merchantQueryVo.getArea()),"AREA",merchantQueryVo.getArea())
                .eq(StringUtils.isNotBlank(merchantQueryVo.getIsAllinpaymer()),"IS_ALLINPAYMER",merchantQueryVo.getIsAllinpaymer())
                .like(StringUtils.isNotBlank(merchantQueryVo.getMerName()),"MER_NAME",merchantQueryVo.getMerName())
                .like(StringUtils.isNotBlank(merchantQueryVo.getBrandName()),"BRAND_NAME",merchantQueryVo.getBrandName())
                .like(StringUtils.isNotBlank(merchantQueryVo.getTradingArea()),"TRADING_AREA",merchantQueryVo.getTradingArea())
                .like(StringUtils.isNotBlank(merchantQueryVo.getExpandChannl()),"EXPAND_CHANNL",merchantQueryVo.getExpandChannl())
                .orderByDesc("MER_ID")
                );
        PageInfo<TEtcMerchants> pageInfo = new PageInfo<TEtcMerchants>(merchantLists);
        return ResponseBean.ok(merchantLists, pageInfo.getTotal());
    }

    @Override
    public ResponseBean query(MerchantQueryVo merchantQueryVo) {

        List<TEtcMerchants> merchantLists= (List) tEtcMerchantsMapper.selectList(
                new QueryWrapper<TEtcMerchants>()
                        .eq(StringUtils.isNotBlank(merchantQueryVo.getBelongIndustry()),"BELONG_INDUSTRY",merchantQueryVo.getBelongIndustry())
                        .eq(StringUtils.isNotBlank(merchantQueryVo.getArea()),"AREA",merchantQueryVo.getArea())
                        .eq(StringUtils.isNotBlank(merchantQueryVo.getIsAllinpaymer()),"IS_ALLINPAYMER",merchantQueryVo.getIsAllinpaymer())
                        .like(StringUtils.isNotBlank(merchantQueryVo.getMerName()),"MER_NAME",merchantQueryVo.getMerName())
                        .like(StringUtils.isNotBlank(merchantQueryVo.getBrandName()),"BRAND_NAME",merchantQueryVo.getBrandName())
                        .like(StringUtils.isNotBlank(merchantQueryVo.getTradingArea()),"TRADING_AREA",merchantQueryVo.getTradingArea())
                        .like(StringUtils.isNotBlank(merchantQueryVo.getExpandChannl()),"EXPAND_CHANNL",merchantQueryVo.getExpandChannl())
                        .orderByDesc("MER_ID")
        );
        return ResponseBean.ok(merchantLists);
    }


    @Override
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcMerchants tEtcMerchants) {
        Integer merid=tEtcMerchants.getMerId();
        String filePath= FileUtil.updataFile(request.getFile(CommonConstant.MER_FILE),
                fileSourcePath  + CommonConstant.SUB_DIR_MERLICENSE);
        if (null==merid){
            //新增
            tEtcMerchants.setMerFile(filePath);
            tEtcMerchants.setCreateTime(new Date());
            tEtcMerchants.setModifyTime(new Date());
            return ResponseBean.ok(tEtcMerchantsMapper.insert(tEtcMerchants)>0);
        }
        //修改
        TEtcMerchants oldMerchants=tEtcMerchantsMapper.selectById(merid);
        if (StringUtils.isNotBlank(filePath)){
            oldMerchants.setMerFile(filePath);
        }
        oldMerchants.setMerName(tEtcMerchants.getMerName());
        oldMerchants.setAllinpayMerid(tEtcMerchants.getAllinpayMerid());
        oldMerchants.setAlipayMerid(tEtcMerchants.getAlipayMerid());
        oldMerchants.setCloudpayMerid(tEtcMerchants.getCloudpayMerid());
        oldMerchants.setWxpayMerid(tEtcMerchants.getWxpayMerid());
        oldMerchants.setBrandName(tEtcMerchants.getBrandName());
        oldMerchants.setArea(tEtcMerchants.getArea());
        oldMerchants.setBelongIndustry(tEtcMerchants.getBelongIndustry());
        oldMerchants.setIsAllinpaymer(tEtcMerchants.getIsAllinpaymer());
        oldMerchants.setExpandChannl(tEtcMerchants.getExpandChannl());
        oldMerchants.setExpandPerson(tEtcMerchants.getExpandPerson());
        oldMerchants.setContactsWay(tEtcMerchants.getContactsWay());
        oldMerchants.setContacts(tEtcMerchants.getContacts());
        oldMerchants.setRemark(tEtcMerchants.getRemark());
        oldMerchants.setModifyTime(new Date());
        return ResponseBean.ok(tEtcMerchantsMapper.updateById(oldMerchants)>0);
    }

    @Override
    public ResponseBean findDetailByMerId(int merId) {
        return ResponseBean.ok(tEtcMerchantsMapper.selectById(merId));

    }

    @Override
    public ResponseBean deleteById(int merId) {
        return ResponseBean.ok(tEtcMerchantsMapper.deleteById(merId)>0);
    }

    @Override
    public ResponseBean batchImport(MultipartFile multipartFile) {
        //读取数据
        List<Object> datas=ExcelUtils.importExcel(multipartFile,MerchantsExcelModal.class);
        int count=0;
        for (Object data:datas){
            TEtcMerchants tEtcMerchants=new TEtcMerchants();
            MerchantsExcelModal merchantsExcelModal= (MerchantsExcelModal) data;
            BeanUtils.copyProperties(merchantsExcelModal,tEtcMerchants);
            tEtcMerchants.setModifyTime(new Date());
            tEtcMerchants.setCreateTime(new Date());
            count+=tEtcMerchantsMapper.insert(tEtcMerchants);

        }
        return ResponseBean.ok(count);
    }

    @Override
    public ResponseBean batchOutput(MerchantQueryVo merchantQueryVo, HttpServletResponse response) throws IOException {
        //获取数据
        ResponseBean responseBean=query(merchantQueryVo);
        List<TEtcMerchants> tEtcMerchants= (List<TEtcMerchants>) responseBean.getData();

        //转换成excel实例 用数据库实例会发身mybaits错误
        List<MerchantsExcelModal> merchantsExcelModals=new ArrayList<>();
        for (TEtcMerchants tEtcMerchant:tEtcMerchants){
            MerchantsExcelModal merchantsExcelModal=new MerchantsExcelModal();
            BeanUtils.copyProperties(tEtcMerchant,merchantsExcelModal);
            merchantsExcelModals.add(merchantsExcelModal);
        }

        String fileName = "商户名单"+ DateUtil.getSimpleDateFormat(new Date(),"yyyy-MM-dd")+".xls";
      return ResponseBean.ok(ExcelUtils.outPutExcel(merchantsExcelModals,response,MerchantsExcelModal.class,fileName,"商户名单"));

    }

    @Override
    public ResponseEntity<FileSystemResource> downloadMerFile(Integer merId) {
        TEtcMerchants tEtcMerchants=tEtcMerchantsMapper.selectById(merId);
        String fileName=tEtcMerchants.getMerFile();
        if (StringUtils.isBlank(fileName)){
            throw new AllinpayException("附件不存在");
        }
        String path=fileSourcePath  + CommonConstant.SUB_DIR_MERLICENSE;
        String filePath=path+fileName;
        return FileDownloader.download(filePath,fileName);
    }


}
