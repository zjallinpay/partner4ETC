package com.allinpay.controller;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.common.ResponseData;
import com.allinpay.entity.BankCardIdMapping;
import com.allinpay.entity.vo.BankcardidQueryVo;
import com.allinpay.service.BankCardIdMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 崇军码卡bin管理
 * yefan
 */
@Slf4j
@RestController
@RequestMapping("/manage/bankcardid")
public class BankCardIdController {
    @Autowired
    private BankCardIdMappingService bankCardIdMappingService;

    @RequestMapping("/list")
    public ResponseData getList(BankcardidQueryVo queryVO) {
        PageVO<BankCardIdMapping> pageVO = null;
        pageVO = bankCardIdMappingService.selectByCondition(queryVO);

        return ResponseData.success().setData(pageVO);
    }

    @RequestMapping("/add")
    public ResponseData add(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.addCardid(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/edit")
    public ResponseData edit(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.editCardid(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }

    @RequestMapping("/status")
    public ResponseData changeStatus(BankCardIdMapping bankCardIdMapping) {
        bankCardIdMappingService.changeStatus(bankCardIdMapping);
        return ResponseData.success().setData(null);
    }


}
