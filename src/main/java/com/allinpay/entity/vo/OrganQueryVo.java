package com.allinpay.entity.vo;

import lombok.Data;

/**
 * 机构查询
 * */
@Data
public class OrganQueryVo {

    /**
     * 机构名称
     */
    private String ogName;

    /**
     * 机构类型
     */
    private String ogType;

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
