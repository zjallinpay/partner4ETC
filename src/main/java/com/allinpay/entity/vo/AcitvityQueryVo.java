package com.allinpay.entity.vo;

import lombok.Data;

@Data
public class AcitvityQueryVo {

    private String activityName;

    private String discountType;

    private String activityBatchno;

    private String activityChnnal;

    private String coopOrgan;

    private String fundType;

    private String proId;

    private Integer pageNo;

    private Integer pageSize;

    public Integer getPageNo() {
        if (pageNo==null){
            return 1;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize==null){
            return 10;
        }
        return pageSize;
    }
}
