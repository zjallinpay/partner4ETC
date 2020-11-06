package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.DiscountConfigMapping;
import com.allinpay.entity.vo.DiscountConfigQueryVo;
import com.allinpay.service.DiscountConfigMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage/discountConfig")
public class DiscountConfigController {

    @Autowired
    private DiscountConfigMappingService discountConfigMappingService;

    @RequestMapping("/list")
    public ResponseData getList(DiscountConfigQueryVo discountConfigQueryVo) {
        PageVO<DiscountConfigMapping> pageVO = null;
        pageVO = discountConfigMappingService.selectByCondition(discountConfigQueryVo);

        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(DiscountConfigMapping discountConfigMapping) {
        discountConfigMappingService.addDiscontconfig(discountConfigMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/edit")
    public ResponseData edit(DiscountConfigMapping discountConfigMapping) {
        discountConfigMappingService.editDiscontconfig(discountConfigMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/status")
    public ResponseData changeStatus(DiscountConfigMapping discountConfigMapping) {
        discountConfigMappingService.changeStatus(discountConfigMapping);
        return ResponseData.success().setData(null);
    }

}
