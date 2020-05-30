package com.allinpay.service.impl;

import com.allinpay.core.common.PageVO;
import com.allinpay.core.util.DateUtil;
import com.allinpay.core.util.PageVOUtil;
import com.allinpay.entity.StallQueryVO;
import com.allinpay.entity.StallReservation;
import com.allinpay.entity.StallVO;
import com.allinpay.mapper.StallReservationMapper;
import com.allinpay.service.IStallManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StallManageServiceImpl implements IStallManageService {
    @Autowired
    private StallReservationMapper stallReservationMapper;

    @Override
    public PageVO<StallVO> getList(StallQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPageNum(), queryVO.getPageSize());
        List<StallReservation> reservationList = stallReservationMapper.selectByCondition(queryVO);
        PageVO pageVO = PageVOUtil.convert(new PageInfo<>(reservationList));
        pageVO.setList(transefer(pageVO.getList()));
        return pageVO;
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
