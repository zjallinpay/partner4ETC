package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.ProjectQueryVo;
import com.allinpay.mapper.TEtcAgreementMapper;
import com.allinpay.mapper.TEtcOrganMapper;
import com.allinpay.mapper.TEtcProjectMapper;
import com.allinpay.service.ITEtcOrganService;
import com.allinpay.service.ITEtcProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TEtcProjectServiceImpl implements ITEtcProjectService {

    @Autowired
    private TEtcProjectMapper tEtcProjectMapper;
    @Autowired
    private ITEtcOrganService organService;
    @Autowired
    private TEtcAgreementMapper agreementMapper;
    @Override
    public ResponseBean queryCondition(ProjectQueryVo projectQueryVo) {
        PageHelper.startPage(projectQueryVo.getPageNo(), projectQueryVo.getPageSize());

        List<TEtcProject> tEtcProjectList= (List) tEtcProjectMapper.selectList(
                new QueryWrapper<TEtcProject>()
                        .like(StringUtils.isNotBlank(projectQueryVo.getProName()),"PRO_NAME",projectQueryVo.getProName())
                        .orderByDesc("PRO_ID")

        );
        PageInfo<TEtcProject> pageInfo = new PageInfo<TEtcProject>(tEtcProjectList);
        return ResponseBean.ok(tEtcProjectList, pageInfo.getTotal());
    }

    @Override
    public ResponseBean saveOrUpdata(TEtcProject tEtcProject) {
        Integer proId=tEtcProject.getProId();

        if (null==proId){
            //新增
            List<TEtcOragn> tEtcOragnList=organService.queryByOrganName(tEtcProject.getCoopOragnname());
            if (tEtcOragnList.size()==0){
                return ResponseBean.error("无此合作机构");
            }
            tEtcProject.setCoopOragnname(tEtcOragnList.get(0).getOgName());
            tEtcProject.setCoopOrganid(tEtcOragnList.get(0).getOgId());
            tEtcProject.setCreateTime(new Date());
            tEtcProject.setModifyTime(new Date());
            return ResponseBean.ok(tEtcProjectMapper.insert(tEtcProject)>0);
        }
        //修改
        TEtcProject oldProject=tEtcProjectMapper.selectById(proId);
        oldProject.setProStarttime(tEtcProject.getProStarttime());
        oldProject.setProEndtime(tEtcProject.getProEndtime());
        oldProject.setRemark(tEtcProject.getRemark());
        oldProject.setModifyTime(new Date());
        return ResponseBean.ok(tEtcProjectMapper.updateById(oldProject)>0);
    }

    @Override
    public ResponseBean deleteById(Integer proId) {
        agreementMapper.deteleByProId(proId);
        return ResponseBean.ok(tEtcProjectMapper.deleteById(proId)>0);
    }


}
