package com.allinpay.entity;

import lombok.Data;

@Data
public class DiscountConfigMapping {
    private Integer kid;

    private String discountId;

    private String discountName;

    private String status;

    private String discountstartDate;

    private String discountendDate;

    private String discountRate;

    private String singlemaxDiscount;

    private String discountmaxTimes;

    private String remark;

    private String insertTime;

    private String updateTime;
}