package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.MerchantConfigMapping;
import com.allinpay.entity.vo.MerchantConfigQueryVo;
import com.allinpay.service.MerchantConfigMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage/merchantConfig")
public class MerchantConfigController {


    @Autowired
    private MerchantConfigMappingService merchantConfigMappingService;

    @RequestMapping("/list")
    public ResponseData getList(MerchantConfigQueryVo merchantConfigQueryVo) {
        PageVO<MerchantConfigMapping> pageVO = null;
        pageVO = merchantConfigMappingService.selectByCondition(merchantConfigQueryVo);

        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(MerchantConfigMapping merchantConfigMapping) {
        merchantConfigMappingService.addMerchantconfig(merchantConfigMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/edit")
    public ResponseData edit(MerchantConfigMapping merchantConfigMapping) {
        merchantConfigMappingService.editMerchantconfig(merchantConfigMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/status")
    public ResponseData changeStatus(MerchantConfigMapping merchantConfigMapping) {
        merchantConfigMappingService.changeStatus(merchantConfigMapping);
        return ResponseData.success().setData(null);
    }


}
