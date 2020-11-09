package com.allinpay.core.util;

import com.allinpay.core.common.PageVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 分页查询结果对象转换工具类
 */
@Component
public class PageVOUtil {

    private PageVOUtil() {
    }

    public static PageVO emptyPageVO() {
        PageVO pageVO = new PageVO();
        pageVO.setList(new ArrayList());
        pageVO.setTotal(0L);
        pageVO.setTotalAmount("0.00");
        pageVO.setPageNum(1);
        pageVO.setPageSize(10);
        return pageVO;
    }

    /**
     * 将pageInfo中的分页信息转换到PageVO中
     */
    public static <K> PageVO<K> convert(PageInfo<K> pageInfo) {
        PageVO<K> pageVO = new PageVO<>();
        pageVO.setPageNum(pageInfo.getPageNum());
        pageVO.setPageSize(pageInfo.getPageSize());
        pageVO.setSize(pageInfo.getSize());
        pageVO.setTotal(pageInfo.getTotal());
        pageVO.setPages(pageInfo.getPages());
        pageVO.setFirstPage(pageInfo.getNavigateFirstPage());
        pageVO.setPrePage(pageInfo.getPrePage());
        pageVO.setNextPage(pageInfo.getNextPage());
        pageVO.setLastPage(pageInfo.getNavigateLastPage());
        pageVO.setList(pageInfo.getList());
        return pageVO;
    }
}