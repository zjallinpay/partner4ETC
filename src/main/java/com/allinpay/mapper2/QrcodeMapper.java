package com.allinpay.mapper2;

import com.allinpay.entity.Qrcode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QrcodeMapper {

    List<Qrcode> queryMerchants(Qrcode qrcode);

    void saveMerchant(Qrcode qrcode);

    void updateMerchant(Qrcode qrcode);

    int batchSaveMerchants(List<Qrcode> qrcodes);

    Qrcode queryMerchantById(Qrcode qrcode);
}
