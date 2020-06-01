package com.allinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.allinpay.core.common.PageVO;
import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.RentOrder;
import com.allinpay.entity.StallQueryVO;
import com.allinpay.entity.StallReservation;
import com.allinpay.entity.StallVO;
import com.allinpay.mapper.RentOrderMapper;
import com.allinpay.mapper.StallReservationMapper;
import com.allinpay.service.IStallManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StallManageServiceImpl implements IStallManageService {
    @Autowired
    private StallReservationMapper stallReservationMapper;
    @Autowired
    private RentOrderMapper rentOrderMapper;

    @Override
    public PageVO<StallVO> getList(StallQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<StallReservation> reservationList = stallReservationMapper.selectByCondition(queryVO);
        PageVO pageVO = PageVOUtil.convert(new PageInfo<>(reservationList));
        pageVO.setList(transefer(pageVO.getList()));
        return pageVO;
    }

    @Override
    public int updateStatusBatch(String status) {
        return stallReservationMapper.updateStatusBatch(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delayStallInfo() {
        log.info("本周场次延期开始。。。");
        //延期时间 做接口幂等控制
        //先确认当前已歇业
        List<StallReservation> stallReservations = stallReservationMapper.selectAll();
        stallReservations.forEach(reservation -> {
            if ("0".equals(reservation.getStatus())) {
                throw new AllinpayException(BizEnums.STALL_IN_USE.getCode(), BizEnums.STALL_IN_USE.getMsg());
            }
        });
        //查找需要延期的订单
        List<RentOrder> orderList = rentOrderMapper.findOrderNeedToDelay(DateUtil.getNearlySunday());
        if (CollectionUtils.isEmpty(orderList)) {
            throw new AllinpayException(BizEnums.NO_STALL_DEALY.getCode(), BizEnums.NO_STALL_DEALY.getMsg());
        }
        List<StallReservation> reservationList = new ArrayList<>();
        orderList.forEach(order -> {
            try {
                order.setRentEndTime(DateUtil.getSimpleSunnday(2));

                String[] stallArray = order.getStall().split(",");
                for (String stall : stallArray) {
                    StallReservation stallReservation = new StallReservation();
                    stallReservation.setAreaId(order.getAreaId());
                    stallReservation.setStall(stall);
                    stallReservation.setRentEndTime(order.getRentEndTime());
                    reservationList.add(stallReservation);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        //更新订单的场次时间和摊位的场次时间，只能延期到下周
        log.info("延期订单列表：{}", JSON.toJSONString(orderList.stream().map(order -> order.getOrderNo()).collect(Collectors.toList())));
        rentOrderMapper.updateRentEndTimeBatch(orderList, DateUtil.getNearlySunday());

        //摊位信息去重，如果有相同的摊位，保留到期时间长的摊位信息
        log.info("延期摊位信息：{}", JSON.toJSONString(reservationList));
        stallReservationMapper.updateRentEndTimeBatch(filterStallReservation(reservationList));
        log.info("本周场次延期完成。。。");
    }

    private List<StallReservation> filterStallReservation(List<StallReservation> reservationList) {
        ListIterator<StallReservation> outerIterator = reservationList.listIterator();
        ListIterator<StallReservation> innerIterator = reservationList.listIterator();
        while (outerIterator.hasNext()) {
            StallReservation outer = outerIterator.next();
            while (innerIterator.hasNext()) {
                StallReservation inner = innerIterator.next();
                if (outer.getAreaId().equals(inner.getAreaId())
                        && outer.getStall().equals(inner.getStall())) {
                    if (DateUtil.compare(outer.getRentEndTime(), inner.getRentEndTime()) > 0) {
                        innerIterator.remove();
                    }
                }
            }
        }

        return reservationList;
    }

    private List<StallVO> transefer(List<StallReservation> reservationList) {
        List<StallVO> stallVOList = new ArrayList<>();
        String nearlySunday = DateUtil.getNearlySunday();
        reservationList.forEach(reservation -> {
            StallVO stallVO = new StallVO();
            BeanUtils.copyProperties(reservation, stallVO);
            stallVO.setRentStatus(DateUtil.compare(nearlySunday, reservation.getRentEndTime()) > 0 ? "未出租" : "已出租");
            stallVOList.add(stallVO);
        });
        return stallVOList;
    }
}
