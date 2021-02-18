package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcMerchants;
import com.allinpay.entity.vo.MerchantQueryVo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ITEtcMerchantsService {
    //分页条件查询
    ResponseBean queryPage(MerchantQueryVo merchantQueryVo);

    ResponseBean query(MerchantQueryVo merchantQueryVo);

    //新增和更新
    ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcMerchants tEtcMerchants);

    //根据id查询商户详情
    ResponseBean findDetailByMerId(int merId);

    //根据id删除商户
    ResponseBean deleteById(int merId);

    //批量导入商户
    @Transactional
    ResponseBean batchImport(MultipartFile multipartFile);

    //批量导出商户
    ResponseBean batchOutput(MerchantQueryVo merchantQueryVo,HttpServletResponse response) throws IOException;

    ResponseBean batchDelete(List ids);

    TEtcMerchants queryByMerName(String merName);
}
