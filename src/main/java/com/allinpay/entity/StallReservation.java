package com.allinpay.entity;

import lombok.Data;

@Data
public class StallReservation {
    private Integer areaId;

    private String stall;

    private String area;

    private String status;

    private String rentStartTime;

    private String rentEndTime;

    private String price;

    private String isLock;
}