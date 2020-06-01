package com.allinpay.entity;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StallReservation that = (StallReservation) o;
        return areaId.equals(that.areaId) &&
                stall.equals(that.stall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaId, stall);
    }
}