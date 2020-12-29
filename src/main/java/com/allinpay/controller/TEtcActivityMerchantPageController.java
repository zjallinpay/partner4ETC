package com.allinpay.controller;

import com.allinpay.entity.PartnerInfo;
import com.allinpay.entity.TEtcSysRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//跳转参与商家页面的控制器
@Controller
@RequestMapping("/manage/activityMer")
public class TEtcActivityMerchantPageController  {


    //跳转页面
    @GetMapping("/actMersPage")
    public ModelAndView actMersPage(String actId) {
        ModelAndView modelAndView = new ModelAndView("yxpt/ActivityMerchant");
        modelAndView.addObject("actId", actId);
        return modelAndView;
    }
}
