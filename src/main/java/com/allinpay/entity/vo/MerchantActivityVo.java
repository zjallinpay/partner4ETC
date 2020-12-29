package com.allinpay.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantActivityVo {

    private Integer merId;
    private Integer activityId;
    private Date createTime;
    private Date modifyTime;
}
