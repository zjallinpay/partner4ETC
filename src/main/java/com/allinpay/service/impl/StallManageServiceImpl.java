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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        String nearlySunday = DateUtil.getNearlySunday();
        queryVO.setNearlySunday(nearlySunday);
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<StallReservation> reservationList = stallReservationMapper.selectByCondition(queryVO);
        PageVO pageVO = PageVOUtil.convert(new PageInfo<>(reservationList));
        pageVO.setList(transefer(pageVO.getList(), queryVO));
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
                order.setRentEndTime(DateUtil.getNearlySunday(order.getRentEndTime()));

                String[] stallArray = order.getStall().split(",");
                for (String stall : stallArray) {
                    StallReservation stallReservation = new StallReservation();
                    stallReservation.setAreaId(order.getAreaId());
                    stallReservation.setStall(stall);
                    stallReservation.setRentEndTime(order.getRentEndTime());
                    reservationList.add(stallReservation);
                }
            } catch (AllinpayException e) {
                throw e;
            }
        });
        //更新订单的场次时间和摊位的场次时间，只能延期到下周
        log.info("延期订单列表：{}", JSON.toJSONString(orderList.stream().map(order -> order.getOrderNo()).collect(Collectors.toList())));
        rentOrderMapper.updateRentEndTimeBatch(orderList, DateUtil.getNearlySunday());

        //摊位信息去重，如果有相同的摊位，保留到期时间长的摊位信息
        List<StallReservation> updateList = filterStallReservation(reservationList);
        log.info("延期摊位信息：{}", JSON.toJSONString(updateList));
        stallReservationMapper.updateRentEndTimeBatch(updateList);
        log.info("本周场次延期完成。。。");
    }

    @Override
    public int updateStatus(StallQueryVO stallVO) {
        return stallReservationMapper.updateStatus(stallVO);
    }

    private List<StallReservation> filterStallReservation(List<StallReservation> reservationList) {
        //StallReservation 设置了equals和hashcode方法
        Set<StallReservation> set = new HashSet<>();
        for (StallReservation reservation : reservationList) {
            set.add(reservation);
        }
        return new ArrayList<>(set);
    }

    private List<StallVO> transefer(List<StallReservation> reservationList, StallQueryVO queryVO) {
        List<StallVO> stallVOList = new ArrayList<>();
        reservationList.forEach(reservation -> {
            StallVO stallVO = new StallVO();
            BeanUtils.copyProperties(reservation, stallVO);
            if ("0".equals(queryVO.getRentStatus())) {
                stallVO.setRentStatus("未出租");
            } else if ("1".equals(queryVO.getRentStatus())) {
                stallVO.setRentStatus("已出租");
            } else {
                stallVO.setRentStatus(DateUtil.compare(queryVO.getNearlySunday(), reservation.getRentEndTime()) > 0 ? "未出租" : "已出租");
            }
            stallVOList.add(stallVO);
        });
        return stallVOList;
    }
}
