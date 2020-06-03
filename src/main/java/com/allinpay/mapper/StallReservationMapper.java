package com.allinpay.mapper;

import com.allinpay.entity.StallQueryVO;
import com.allinpay.entity.StallReservation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StallReservationMapper {
    int deleteByPrimaryKey(@Param("areaId") Integer areaId, @Param("stall") String stall);

    int insert(StallReservation record);

    StallReservation selectByPrimaryKey(@Param("areaId") Integer areaId, @Param("stall") String stall);

    List<StallReservation> selectAll();

    int updateByPrimaryKey(StallReservation record);

    List<StallReservation> selectByCondition(StallQueryVO queryVO);

    int updateStatusBatch(@Param("status") String status);

    int updateRentEndTimeBatch(@Param("list") List<StallReservation> reservationList);

    int updateStatus(StallQueryVO stallVO);
}