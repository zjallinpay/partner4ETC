package com.allinpay.service.impl;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.OrganQueryVo;
import com.allinpay.mapper.TEtcOrganMapper;
import com.allinpay.service.ITEtcOrganService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TEtcOrganServiceImpl implements ITEtcOrganService {

    @Autowired
    private TEtcOrganMapper tEtcOrganMapper;

    @Override
    public ResponseBean queryCondition(OrganQueryVo organQueryVo) {
        PageHelper.startPage(organQueryVo.getPageNo(), organQueryVo.getPageSize());

        List<TEtcOragn> tEtcOragnList= (List) tEtcOrganMapper.selectList(
                new QueryWrapper<TEtcOragn>()
                        .like(StringUtils.isNotBlank(organQueryVo.getOgType()),"OG_TYPE",organQueryVo.getOgType())
                        .like(StringUtils.isNotBlank(organQueryVo.getOgName()),"OG_NAME",organQueryVo.getOgName())
                        .orderByDesc("OG_ID")

        );
        PageInfo<TEtcOragn> pageInfo = new PageInfo<TEtcOragn>(tEtcOragnList);
        return ResponseBean.ok(tEtcOragnList, pageInfo.getTotal());
    }

    @Override
    public List<TEtcOragn> queryByOrganName(String organName) {
        return (List) tEtcOrganMapper.selectList(
                new QueryWrapper<TEtcOragn>()
                        .like("OG_NAME",organName)
        );
    }

    @Override
    public ResponseBean saveOrUpdata(TEtcOragn tEtcOragn) {
        Integer ogId=tEtcOragn.getOgId();

        if (null==ogId){
            //新增
            tEtcOragn.setCreateTime(new Date());
            tEtcOragn.setModifyTime(new Date());
            return ResponseBean.ok(tEtcOrganMapper.insert(tEtcOragn)>0);
        }
        //修改
        TEtcOragn oldOragn=tEtcOrganMapper.selectById(ogId);
        oldOragn.setOgType(tEtcOragn.getOgType());
        oldOragn.setRemark(tEtcOragn.getRemark());
        oldOragn.setModifyTime(new Date());
        return ResponseBean.ok(tEtcOrganMapper.updateById(oldOragn)>0);
    }

    @Override
    public ResponseBean deleteById(Integer ogId) {
        return ResponseBean.ok(tEtcOrganMapper.deleteById(ogId)>0);
    }
}
