package com.allinpay.service.impl;

import com.allinpay.controller.query.OrgAuditQuery;
import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.MD5Util;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.core.util.ShiroUtils;
import com.allinpay.entity.PartnerAudit;
import com.allinpay.entity.PartnerInfo;
import com.allinpay.mapper.PartnerAuditMapper;
import com.allinpay.mapper.PartnerInfoMapper;
import com.allinpay.service.IOrgAuditService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@PropertySource("file:config/resource.properties")
public class OrgAuditServiceImpl implements IOrgAuditService {
    @Autowired
    private PartnerAuditMapper auditMapper;
    @Autowired
    private PartnerInfoMapper infoMapper;
    @Value("${orgDir}")
    private String orgDir;
    @Value("${tempDir}")
    private String tempDir;

    @Override
    public PageVO<PartnerAudit> selectByCondition(OrgAuditQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<PartnerAudit> partnerAuditList = auditMapper.selectList(query);
        PageVO<PartnerAudit> pageVO = PageVOUtil.convert(new PageInfo(partnerAuditList));
        return pageVO;
    }


    @Override
    public void auditRefuse(String partnerId, String failReason) {
        String sysUser = ShiroUtils.getUserEntity().getUsername();
        //先判断该机构是否存在
        PartnerAudit partnerAudit = auditMapper.selectOne(partnerId, CommonConstant.STATUS_AUDIT);
        if (Objects.isNull(partnerAudit)) {
            log.error("机构编号{}信息不存在！", partnerId);
            throw new AllinpayException(BizEnums.ORG_NOT_EXIST.getCode(), BizEnums.ORG_NOT_EXIST.getMsg());
        }
        auditMapper.updateRefuseStatus(partnerId, failReason, sysUser);
        //根据是否颁发了秘钥判断,新增记录需要更新状态，编辑记录不需要同步状态
        PartnerInfo partnerInfo = infoMapper.selectOne(partnerId);
        if (Objects.isNull(partnerInfo.getSecretKey())) {
            infoMapper.updateRefuseStatus(partnerId, sysUser);
        }
    }

    @Override
    public void auditApprove(PartnerAudit audit) {
        try {
            String partnerId = audit.getPartnerId();
            String sysUser = ShiroUtils.getUserEntity().getUsername();
            String secretKey = generateSecretKey();
            //先判断该机构是否存在
            PartnerAudit partnerAudit = auditMapper.selectOne(partnerId, CommonConstant.STATUS_AUDIT);
            if (Objects.isNull(partnerAudit)) {
                log.error("机构编号{}信息不存在！", audit.getPartnerId());
                throw new AllinpayException(BizEnums.ORG_NOT_EXIST.getCode(), BizEnums.ORG_NOT_EXIST.getMsg());
            }
            auditMapper.updateApproveStatus(partnerId, "审核已通过", sysUser);
            //通过机构id查询机构信息，看是否颁发过秘钥
            PartnerInfo partnerInfo = infoMapper.selectOne(partnerId);

            //未生成过秘钥，且机构状态是审核中
            if (Objects.isNull(partnerInfo.getSecretKey())
                    && CommonConstant.STATUS_AUDIT.equals(partnerInfo.getStatus())) {
                //更改机构表中的记录为正常，更改用户名sysUser,生成秘钥
                infoMapper.updateApproveStatus(partnerId, sysUser, secretKey);
                log.info("机构信息审核成功，生成秘钥");
                return;
            }

            //审核通过数据同步
            PartnerInfo info = new PartnerInfo();
            BeanUtils.copyProperties(partnerAudit, info);
            info.setStatus(CommonConstant.STATUS_NORMAL);
            //获取当前登录用户
            info.setSysUser(sysUser);
            info.setModifyTime(new Date());
            if (Objects.isNull(partnerInfo.getSecretKey())
                    && CommonConstant.STATUS_FAIL.equals(partnerInfo.getStatus())) {
                //未生成过秘钥，且机构状态是审核失败
                info.setSecretKey(secretKey);
            }

            infoMapper.updateApproveData(info);
            //同步审核通过的图片到机构中
            FileUtils.deleteDirectory(new File(orgDir + partnerId));
            FileUtils.copyDirectory(new File(tempDir + partnerId), new File(orgDir + partnerId));
            log.info("机构信息审核成功，同步至机构表成功");
        } catch (AllinpayException all) {
            throw all;
        } catch (Exception e) {
            log.error("机构信息审核失败", e);
            throw new AllinpayException(BizEnums.ORG_AUDIT_FAIL.getCode(), BizEnums.ORG_AUDIT_FAIL.getMsg());
        }
    }

    private String generateSecretKey() {
        return MD5Util.md5(UUID.randomUUID().toString().replace("-", "").toUpperCase());
    }
}
