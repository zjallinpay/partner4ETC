package com.allinpay.controller;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.TEtcProject;
import com.allinpay.entity.vo.MerchantQueryVo;
import com.allinpay.entity.vo.ProjectQueryVo;
import com.allinpay.service.ITEtcProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/manage/project")
public class TEtcProjectController {


    @Autowired
    private ITEtcProjectService projectService;

    //条件查询项目
    @RequestMapping("/queryCondition")
    public ResponseBean queryCondition(ProjectQueryVo projectQueryVo){
        return projectService.queryCondition(projectQueryVo);
    }



    //新增及编辑
    @RequestMapping("/saveOrUpdata")
    public ResponseBean saveOrUpdata(@Valid TEtcProject tEtcProject,
                                     BindingResult bindingResult){
        log.info("提交参数{}",tEtcProject);
        if (bindingResult.hasErrors()){
            return ResponseBean.error("提交参数有误");
        }
        return projectService.saveOrUpdata(tEtcProject);
    }



    //删除项目
    @GetMapping("/deteleById")
    public ResponseBean deteleById(@RequestParam("proId") Integer proId){
        return projectService.deleteById(proId);
    }
}
