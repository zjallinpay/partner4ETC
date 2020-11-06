package com.allinpay.entity.vo;

import lombok.Data;

@Data
public class DiscountConfigQueryVo {


    private Integer kid;

    private String status;

    private String discountId;

    private String discountName;

    private String discountstartDate;

    private String discountendDate;

    private String discountRate;

    private String singlemaxDiscount;

    private String discountmaxTimes;

    private String remark;

    private Integer pageNum;

    private Integer pageSize;

}
