package com.allinpay.core.constant;

/**
 * 公共常量类
 */
public class CommonConstant {
    /**
     * 图片存放子目录，营业执照图片
     */
    public static String SUB_DIR_LICENSE = "/license/";
    /**
     * 图片存放子目录，协议照片
     */
    public static String SUB_DIR_AGREEMENT = "/agreement/";
    /**
     * 图片存放子目录，身份证正面照
     */
    public static String SUB_DIR_FRONT = "/front/";
    /**
     * 图片存放子目录，身份证背面照
     */
    public static String SUB_DIR_BACK = "/back/";

    /**
     * 页面file域名称
     */
    public static String LICENSE_FILE = "licenseFile";
    public static String FRONT_FILE = "legalFront";
    public static String BACK_FILE = "legalBack";
    public static String AGREEMENT_FILE = "agreementFile";

    /**
     * 机构状态 1 正常 2 注销 3 冻结 4 审核中 5 审核成功 6 审核失败 7 未提交
     */
    public static Integer STATUS_NORMAL = 1;
    public static Integer STATUS_LOGOUT = 2;
    public static Integer STATUS_FROZEN = 3;
    public static Integer STATUS_AUDIT = 4;
    public static Integer STATUS_SUCCESS = 5;
    public static Integer STATUS_FAIL = 6;
    public static Integer STATUS_TEMP = 7;

    /**
     * 保存图片路径,如：D:\d\4\
     */
    public static String imagPath;

    /**
     * 银行基础信息数据状态、机构银行映射关系状态 0 禁用 1 有效
     */
    public static String BANK_STATUS_NORMAL = "1";
    public static String BANK_STATUS_ABNORMAL = "0";

    /**
     * 银行卡类型 1 借记卡 2 贷记卡
     */
    public static String CARD_TYPE_DEBT = "1";
    public static String CARD_TYPE_CREDIT = "2";




    /**
     * 营销后台
     *
     * */
    public static String MER_TEMPLATE_FILE_NAME = "mertemplate.xlsx";

    public static String MER_SOURCE_PATH = "D:\\smjproject\\template\\mertemplate.xlsx";

    //public static String MER_SOURCE_PATH = "/project/manage/template/mertemplate.xlsx";

    public static String ACTIVITY_TEMPLATE_FILE_NAME = "activitytemplate.xlsx";

    public static String ACTIVITY_SOURCE_PATH = "D:\\smjproject\\template\\activitytemplate.xlsx";

    //public static String ACTIVITY_SOURCE_PATH = "/project/manage/template/activitytemplate.xlsx";

    public static String ACT_SOURCE_PATH = "D:\\smjproject\\file";

    //public static String ACT_SOURCE_PATH = "/project/manage/file";
    //活动附件字段
    public static String ACTIVITY_FILE = "activity_file";
    //商品附件字段
    public static String MER_FILE = "mer_file";

    /**
     * 用于放置活动附件的目录
     */
    public static String SUB_DIR_ACTLICENSE = "/actlicense/";

    /**
     * 用于放置商家附件的目录
     */
    public static String SUB_DIR_MERLICENSE = "/merlicense/";

    /**
     * 码牌管理
     */
    public static String DEST_FILE_NAME = "batchImportQrMerchant.xlsx";
//    public static String SOURCE_FILE_NAME = "E:\\项目资料\\码牌管理\\批量导入模板.xlsx";
//    public static String QR_CODE_IMAGE_PATH = "E:\\项目资料\\码牌管理\\QRIMAGE\\";

    public static String SOURCE_FILE_NAME = "/app/zjjt/etc-manage/tempfile/batchImportQrMerchant.xlsx";
    public static String QR_CODE_IMAGE_PATH = "/app/zjjt/etc-manage/qrimage/";

    public static String BATCH_IMPORT_FILE_PATH = "E:\\项目资料\\码牌管理\\批导文件\\";
    public static int QR_IMAGE_WIDTH = 350;
    public static int QR_IMAGE_LENGTH = 300;
    public static String QR_URL = "https://issue.allinpay.com/h5pay/sjq/index/";

    public static final String RESPONSE_SUCCESS_0000 = "0000";
    public static final String EXCEL_XLSX = ".xlsx";

    public static final String JJS_BALANCE_MONEY = "http://172.16.211.22:20070/jjs/balance/money";
    public static final String JJS_INFORMATION_VERIFY = "http://172.16.211.22:20070/jjs/information/verify";
    public static final String JJS_BATCH_PAYMENTS = "http://172.16.211.22:20070/jjs/batch/payments";
    public static final String JJS_TRADE_QUERY = "http://172.16.211.22:20070/jjs/repayment/trade/query";
}
