package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.vo.CJMOrderVO;
import com.allinpay.entity.vo.CJMQuery;
import com.allinpay.service.ICJMTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: tanguang
 * data: 2020/11/9
 **/
@RestController
@RequestMapping("/manage/cjm")
public class CJMTradeController {
    @Autowired
    private ICJMTradeService tradeService;

    @RequestMapping("/orderlist")
    @ResponseBody
    public ResponseData getList(CJMQuery query) {
        PageVO<CJMOrderVO> pageVO = tradeService.getList(query);
        return ResponseData.success().setData(pageVO);
    }
}
