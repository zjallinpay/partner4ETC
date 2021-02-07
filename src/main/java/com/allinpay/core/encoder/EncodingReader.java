package com.allinpay.core.encoder;


import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class EncodingReader {

    /**
     * 得到文件的编码
     *
     * @param inputStream 文件路径
     * @return 文件的编码
     */
    public static Reader getFileEncode(InputStream inputStream) throws IOException {

        BytesEncodingDetect s = new BytesEncodingDetect();
        byte[] bytes=Byte2InputStream.inputStream2byte(inputStream);
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(bytes)];
        log.info("长度{}编码{}",bytes.length,fileCode);
        return new InputStreamReader(Byte2InputStream.byte2InputStream(bytes),fileCode);
    }

    public static String getFileEncode(File file) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(file)];
        return fileCode;
    }

}
