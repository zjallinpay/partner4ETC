package com.allinpay.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 金额处理类
 */
public final class MoneyUtil {

    /**
     * 数值：100
     */
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    /**
     * 0的字符串格式化表示
     */
    private static final String ZERO_STRING = "0.00";

    /**
     * 分转元
     *
     * @param cent
     * @return
     */
    public static String centToYuanFormat(BigDecimal cent) {
        if (Objects.isNull(cent)) {
            return null;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(cent.divide(HUNDRED));
    }

    /**
     * 元格式化输出
     *
     * @param yuan
     * @return
     */
    public static String yuanFormat(BigDecimal yuan) {
        if (Objects.isNull(yuan)) {
            return null;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(yuan);
    }

    /**
     * 金额为0的字符串
     *
     * @return
     */
    public static String zeroFormat() {
        return ZERO_STRING;
    }

}