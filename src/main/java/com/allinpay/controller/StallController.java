package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.StallQueryVO;
import com.allinpay.entity.StallVO;
import com.allinpay.service.IStallManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: tanguang
 * data: 2020-05-30
 **/
@RestController
@RequestMapping("/stall")
public class StallController {

    @Autowired
    private IStallManageService stallManageService;

    @RequestMapping("/list")
    public ResponseData getList(StallQueryVO queryVO) {
        PageVO<StallVO> pageVO = stallManageService.getList(queryVO);
        return ResponseData.success().setData(pageVO);
    }
}
