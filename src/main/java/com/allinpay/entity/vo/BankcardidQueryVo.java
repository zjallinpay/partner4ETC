package com.allinpay.entity.vo;


import lombok.Data;

@Data
public class BankcardidQueryVo {
    private String kid;

    private String bankId;

    private String banName;

    private String cardName;

    private String cardType;

    private String status;

    private Integer pageNum;

    private Integer pageSize;
}
