package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.OrderQueryVO;
import com.allinpay.entity.RentOrder;
import com.allinpay.service.IOrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author: tanguang
 * data: 2020-05-25
 **/
@Controller
@RequestMapping("/manage/trade")
public class OrderQueryController {

    @Autowired
    private IOrderQueryService orderQueryService;

    @RequestMapping("list")
    @ResponseBody
    public ResponseData getList(OrderQueryVO queryVO) {
        PageVO<RentOrder> pageVO = orderQueryService.getList(queryVO);
        return ResponseData.success().setData(pageVO);
    }
}
