package com.allinpay.core.util;


import com.allinpay.core.constant.enums.BizEnums;
import com.allinpay.core.exception.AllinpayException;
import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * md5工具类
 */
public class SecretManager {
    /**
     * 签名
     *
     * @param reqMap
     * @param appKey
     * @return
     */
    public static String sign(Map<String, String> reqMap, String appKey) {
        try {
            TreeMap<String, String> params = new TreeMap(reqMap);
            if (params.containsKey("sign")) {
                params.remove("sign");
            }
            params.put("key", appKey);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null && entry.getValue().length() > 0) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            String sign = md5(sb.toString().getBytes("UTF-8"));
            params.remove("key");
            return sign;
        } catch (Exception ex) {
            throw new AllinpayException(BizEnums.SIGN_ERROR.getCode(), BizEnums.SIGN_ERROR.getMsg());
        }
    }

    /**
     * 验签
     *
     * @param reqMap
     * @param appKey
     * @return
     */
    public static boolean validSign(Map<String, String> reqMap, String appKey) {
        if (!CollectionUtils.isEmpty(reqMap)) {
            if (!reqMap.containsKey("sign")) {
                return false;
            }
            String sign = reqMap.get("sign");
            return sign.toLowerCase().equals(sign(reqMap, appKey).toLowerCase());
        }
        return false;
    }

    /**
     * md5算法
     *
     * @param b
     * @return
     */
    public static String md5(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            StringBuffer outStrBuf = new StringBuffer(32);
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    outStrBuf.append('0');
                }
                outStrBuf.append(Integer.toString(v, 16).toLowerCase());
            }
            return outStrBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new String(b);
        }
    }
}
