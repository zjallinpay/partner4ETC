package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.FileDownloader;
import com.allinpay.core.util.FileUtil;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcAgreement;
import com.allinpay.entity.TEtcProAgreement;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.AgreementQueryVo;
import com.allinpay.mapper.TEtcAgreementMapper;
import com.allinpay.service.ITEtcAgreementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

@Service
@Slf4j
@PropertySource("file:config/resource.properties")
public class TEtcAgreementServiceImpl implements ITEtcAgreementService {

    @Autowired
    private TEtcAgreementMapper agreementMapper;

    @Value("${fileSourcePath}")
    private String fileSourcePath;

    @Override
    public ResponseBean queryCondition(AgreementQueryVo agreementQueryVo) {
        PageHelper.startPage(agreementQueryVo.getPageNo(), agreementQueryVo.getPageSize());

        List<TEtcAgreement> tEtcAgreementList= (List) agreementMapper.selectList(
                new QueryWrapper<TEtcAgreement>()
                        .eq(StringUtils.isNotBlank(agreementQueryVo.getArgId()),"ARG_ID",agreementQueryVo.getArgId())
                        .like(StringUtils.isNotBlank(agreementQueryVo.getArgName()),"ARG_NAME",agreementQueryVo.getArgName())
                        .like(StringUtils.isNotBlank(agreementQueryVo.getCoopOrgtype()),"COOP_ORGTYPE",agreementQueryVo.getCoopOrgtype())
                        .orderByDesc("ARG_ID")

        );
        PageInfo<TEtcAgreement> pageInfo = new PageInfo<TEtcAgreement>(tEtcAgreementList);
        return ResponseBean.ok(tEtcAgreementList, pageInfo.getTotal());
    }

    @Override
    public ResponseBean queryByProId(AgreementQueryVo agreementQueryVo) {

        List<Integer> argIds=agreementMapper.queryArgByProId(agreementQueryVo.getProId());
        if (argIds.size()==0){
            argIds.add(-1);
        }
        List<TEtcAgreement> tEtcAgreementList=null;
        if ("true".equals(agreementQueryVo.getIsExpand())){
            //查非所属的协议
            tEtcAgreementList=(List) agreementMapper.selectList(
                    new QueryWrapper<TEtcAgreement>()
                            .eq(StringUtils.isNotBlank(agreementQueryVo.getArgId()),"ARG_ID",agreementQueryVo.getArgId())
                            .like(StringUtils.isNotBlank(agreementQueryVo.getArgName()),"ARG_NAME",agreementQueryVo.getArgName())
                            .notIn("ARG_ID", argIds.toArray()));
        }else {
            //查所属协议
            tEtcAgreementList=(List) agreementMapper.selectList(
                    new QueryWrapper<TEtcAgreement>()
                            .eq(StringUtils.isNotBlank(agreementQueryVo.getArgId()),"ARG_ID",agreementQueryVo.getArgId())
                            .like(StringUtils.isNotBlank(agreementQueryVo.getArgName()),"ARG_NAME",agreementQueryVo.getArgName())
                            .in("ARG_ID",argIds.toArray()));
        }
        return  ResponseBean.ok(tEtcAgreementList);
    }

    @Override
    public ResponseBean conectAgreement(Map params) {
        String proId= (String) params.get("proId");
        List argIds= (List) params.get("argIds");

        List<TEtcProAgreement> list=new ArrayList<>();
        for (Object id:argIds){
            TEtcProAgreement tEtcProAgreement=new TEtcProAgreement();
            tEtcProAgreement.setCreateTime(new Date());
            tEtcProAgreement.setModiifyTime(new Date());
            tEtcProAgreement.setProId(Integer.valueOf(proId));
            tEtcProAgreement.setArgId((Integer) id);
            list.add(tEtcProAgreement);
        }
        return ResponseBean.ok(agreementMapper.insertBatch(list));
    }

    @Override
    public ResponseBean deteleConect(Map params) {
        return ResponseBean.ok(agreementMapper.deteleConect(params));
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadArgFile(Integer argId) {
        TEtcAgreement tEtcAgreement=agreementMapper.selectById(argId);
        String fileName=tEtcAgreement.getArgFile();
        log.info("文件名称{}",fileName);
        if (StringUtils.isBlank(fileName)){
            throw new AllinpayException("附件不存在");
        }
        String path=fileSourcePath  + CommonConstant.SUB_DIR_AGREEMENTATTCH;
        String filePath=path+fileName;
        log.info("文件路径{}",filePath);
        return FileDownloader.download(filePath,fileName);
    }

    @Override
    public ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcAgreement tEtcAgreement) {
        Integer argId=tEtcAgreement.getArgId();
        String filePath= FileUtil.updataFile(request.getFile(CommonConstant.AGREEMENT_ATTCH),
                fileSourcePath  + CommonConstant.SUB_DIR_AGREEMENTATTCH);
        log.info("附加上传成功{}"+filePath);
        if (null==argId){
            //新增
            tEtcAgreement.setArgFile(filePath);
            tEtcAgreement.setCreateTime(new Date());
            tEtcAgreement.setModifyTime(new Date());
            return ResponseBean.ok(agreementMapper.insert(tEtcAgreement)>0);
        }
        //更新
        TEtcAgreement oldAgreement=agreementMapper.selectById(argId);
        if (StringUtils.isNotBlank(filePath)){
            oldAgreement.setArgFile(filePath);
        }

        oldAgreement.setCoopOrgtype(tEtcAgreement.getCoopOrgtype());
        oldAgreement.setModifyTime(new Date());
        return ResponseBean.ok(agreementMapper.updateById(oldAgreement)>0);
    }

    @Override
    public ResponseBean deleteById(Integer argId) {
        agreementMapper.deteleByArgId(argId);
        return ResponseBean.ok(agreementMapper.deleteById(argId)>0);
    }
}
