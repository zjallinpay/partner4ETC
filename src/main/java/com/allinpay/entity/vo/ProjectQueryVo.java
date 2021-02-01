package com.allinpay.entity.vo;

import lombok.Data;
/**
 * 项目查询
 * */
@Data
public class ProjectQueryVo {

    /**
     * 项目名称
     */
    private String proName;

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
