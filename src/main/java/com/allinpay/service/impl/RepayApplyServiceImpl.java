package com.allinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.constant.CommonConstant;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.ShiroUtils;
import com.allinpay.entity.vo.JJSCustomerVO;
import com.allinpay.entity.vo.JJSCustomerValidateResult;
import com.allinpay.entity.vo.JJSInstAmountVO;
import com.allinpay.service.IRepayApplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author: tanguang
 * data: 2021/1/11
 **/
@Service
@Slf4j
public class RepayApplyServiceImpl implements IRepayApplyService {
    @Autowired
    private JJSClient jjsClient;

    @Override
    public JJSInstAmountVO queryTotalAmount() {
        log.info("jjs query total amount -> {}", ShiroUtils.getUserEntity().getUsername());
        Map<String, String> responseMap = jjsClient.execute(new HashMap<>(), CommonConstant.JJS_BALANCE_MONEY);
        return JSON.parseObject(responseMap.get("data"), JJSInstAmountVO.class);
    }

    @Override
    public JJSCustomerValidateResult validateCustomers(MultipartFile customerFile) {
        List<JJSCustomerVO> list = dealExcel(customerFile);
        //先校验机构总额
        JJSInstAmountVO jjsInstAmountVO = queryTotalAmount();
        BigDecimal countAmount = list.stream()
                .map(u -> new BigDecimal(u.getTradeAmount()))
                .reduce((u1, u2) -> u1.add(u2)).get();
        if (countAmount.compareTo(new BigDecimal(jjsInstAmountVO.getAvailableAmt())) > 0) {
            throw new AllinpayException(BizEnums.TOTAL_AMOUNT_EXCEED_FAIL.getCode(),
                    BizEnums.TOTAL_AMOUNT_EXCEED_FAIL.getMsg());
        }
        Map<String, String> bizMap = new HashMap<>();
        bizMap.put("information", JSON.toJSONString(list));
        log.info("jjs validate customers -> {}", JSON.toJSONString(list));
        // 全部验证成功会返回批次号
        Map<String, String> responseMap = jjsClient.execute(bizMap, CommonConstant.JJS_INFORMATION_VERIFY);
        JJSCustomerValidateResult result = JSON.parseObject(responseMap.get("data"), JJSCustomerValidateResult.class);
        //对结果排序，将不符合条件的结果放前
        List<JJSCustomerVO> sortedList = result.getList().stream()
                .sorted((u1, u2) -> CommonConstant.RESPONSE_SUCCESS_0000.equals(u1.getResult()) ? 1 : -1)
                .map(customer -> {
                    if (CommonConstant.RESPONSE_SUCCESS_0000.equals(customer.getResult())) {
                        customer.setResult("成功");
                    } else {
                        customer.setResult("失败");
                    }
                    return customer;
                })
                .collect(Collectors.toList());
        result.setList(sortedList);
        return result;
    }

    private List<JJSCustomerVO> dealExcel(MultipartFile multipartFile) {
        List<JJSCustomerVO> list = new ArrayList<>();
        String fileName = multipartFile.getOriginalFilename();
        if (!CommonConstant.EXCEL_XLSX.equals(fileName.substring(fileName.length() - 5))) {
            throw new AllinpayException(BizEnums.FILE_TYPE_ERROR.getCode(), BizEnums.FILE_TYPE_ERROR.getMsg() + "，不是xlsx类型文件");
        }
        try {
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headRow = sheet.getRow(0);
            if (!("姓名".equals(headRow.getCell(0).getStringCellValue())
                    && "身份证号".equals(headRow.getCell(1).getStringCellValue())
                    && "银行卡号".equals(headRow.getCell(2).getStringCellValue())
                    && "金额(元)".equals(headRow.getCell(3).getStringCellValue()))) {
                throw new AllinpayException(BizEnums.FILE_TEMPLATE_ERROR.getCode(), BizEnums.FILE_TEMPLATE_ERROR.getMsg());
            }
            int number = sheet.getPhysicalNumberOfRows();
            if (number == 1) {
                throw new AllinpayException(BizEnums.FILE_EMPTY_ERROR.getCode(), BizEnums.FILE_EMPTY_ERROR.getMsg());
            } else {
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    Row row = sheet.getRow(i);
                    String realName = getCellValue(row.getCell(0));
                    String cardNo = getCellValue(row.getCell(1));
                    String idNo = getCellValue(row.getCell(2));
                    String amount = getCellValue(row.getCell(3));
                    if (StringUtils.isBlank(realName)
                            || StringUtils.isBlank(cardNo)
                            || StringUtils.isBlank(idNo)
                            || StringUtils.isBlank(amount)) {
                        //存在信息不全记录，过滤
                        continue;
                    }
                    JJSCustomerVO customerVO = new JJSCustomerVO(realName, idNo, cardNo, amount);
                    list.add(customerVO);
                }
            }
            return list;
        } catch (Exception e) {
            log.error("读取excel文件失败：{}", e.getMessage());
            throw new AllinpayException(BizEnums.FILE_OPERATE_EXCEPTION.getCode(), BizEnums.FILE_OPERATE_EXCEPTION.getMsg());
        }
    }

    private String getCellValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        String value;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = numberFormat.format(cell.getNumericCellValue());
                break;
            default:
                value = "";
        }
        return value;
    }

    @Override
    public String repayBatch(String batchNo) {
        log.info("jjs repay batch -> {}", batchNo);
        Map<String, String> bizMap = new HashMap<>();
        bizMap.put("batchNo", batchNo);
        Map<String, String> responseMap = jjsClient.execute(bizMap, CommonConstant.JJS_BATCH_PAYMENTS);
        return responseMap.get("data");
    }
}
