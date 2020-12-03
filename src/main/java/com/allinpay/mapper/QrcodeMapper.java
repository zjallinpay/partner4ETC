package com.allinpay.mapper;

import com.allinpay.entity.Qrcode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface QrcodeMapper {

    List<Qrcode> queryMerchants(Qrcode qrcode);

    void saveMerchant(Qrcode qrcode);

    void updateMerchant(Qrcode qrcode);

    void batchSaveMerchants(List<Qrcode> qrcodes);

    Qrcode queryMerchantById(Qrcode qrcode);
}
