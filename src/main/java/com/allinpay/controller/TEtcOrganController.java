package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcOragn;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.OrganQueryVo;
import com.allinpay.entity.vo.ProjectQueryVo;
import com.allinpay.service.ITEtcOrganService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/manage/organ")
public class TEtcOrganController {
    @Autowired
    private ITEtcOrganService itEtcOrganService;

    //条件查询项目
    @RequestMapping("/queryCondition")
    public ResponseBean queryCondition(OrganQueryVo organQueryVo){
        return itEtcOrganService.queryCondition(organQueryVo);
    }




    //新增及编辑
    @RequestMapping("/saveOrUpdata")
    public ResponseBean saveOrUpdata(@Valid TEtcOragn tEtcOragn,
                                     BindingResult bindingResult){
        log.info("提交参数{}",tEtcOragn);
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }
        return itEtcOrganService.saveOrUpdata(tEtcOragn);
    }



    //删除机构
    @GetMapping("/deteleById")
    public ResponseBean deteleById(@RequestParam("ogId") Integer ogId){
        return itEtcOrganService.deleteById(ogId);
    }
}
