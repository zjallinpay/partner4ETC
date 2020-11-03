package com.allinpay.entity;

import lombok.Data;

@Data
public class BankCardIdMapping {
    private Integer kid;

    private String bankId;

    private String cardType;

    private String bankName;

    private String cardName;

    private String status;

    private String insertTime;

    private String updateTime;
}