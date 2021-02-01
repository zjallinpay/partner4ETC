package com.allinpay.service;

import com.allinpay.core.common.ResponseBean;
import com.allinpay.entity.TEtcActivity;
import com.allinpay.entity.vo.AcitvityQueryVo;
import com.allinpay.entity.vo.MerchantActivityVo;
import com.allinpay.entity.vo.MerchantQueryVo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ITEtcActivityService {



    //分页条件查询
    ResponseBean queryPage(AcitvityQueryVo acitvityQueryVo);

    //新增或编辑
    ResponseBean saveOrUpdata(MultipartHttpServletRequest request, TEtcActivity tEtcActivity);


    //根据id查询活动详情
    ResponseBean findDetailByactId(int actId);

    //根据id删除活动
    ResponseBean deleteById(int actId);

   /* //批量导入活动
    ResponseBean batchImport(MultipartFile multipartFile);*/

    //下载活动附件
    ResponseEntity<FileSystemResource> downloadActFile(Integer actId);

    //查询活动商家
    ResponseBean queryActMers(Integer actId,Integer pageNo,Integer pageSize);
    //插入参与商户
    ResponseBean insertByMerId( MerchantActivityVo merchantActivityVo);

    //批量插入参与商户
    ResponseBean batchInsertByActId(MultipartFile multipartFile,Integer actId);
    //批量导出参与商户
    ResponseBean batchOutput(Integer actId, HttpServletResponse response) throws IOException;

    //删除参与商户
    ResponseBean deleteByMerId(Integer merId,Integer actId);

    ResponseBean batchDelete(List ids);
}
