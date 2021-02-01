package com.allinpay.entity.vo;

import lombok.Data;

/**
 * 协议查询
 * */
@Data
public class AgreementQueryVo {

    /**
     * 协议名称
     */
    private String argName;

    /**
     * 协议id
     */
    private String argId;

    /**
     * 合作机构类型
     */
    private String coopOrgtype;

    private Integer pageNo;

    private Integer pageSize;

    private Integer proId;
    //是否查询可关联的更多协议
    private String isExpand;

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
