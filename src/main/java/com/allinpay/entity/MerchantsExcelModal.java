package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户信息Excel
 *
 * @author makejava
 * @since 2020-12-17 13:56:27
 */
@Data
public class MerchantsExcelModal extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户名称（唯一）
     */
    @ExcelProperty(value="商户名称",index=0)
    private String merName;
    /**
     * 所属行业
     */
    @ExcelProperty(value="所属行业",index=1)
    private String belongIndustry;

    /**
     * 商圈
     */
    @ExcelProperty(value="商圈名称",index=3)
    private String tradingArea;
    /**
     * 地区 枚举类型值：舟山市,杭州市,嘉兴市,温州市,宁波市,绍兴市,湖州市,丽水市,台州市,金华市,衢州市。
     */
    @ExcelProperty(value="所属地区",index=2)
    private String area;
    /**
     * 是否为通联商户 枚举类型值：通联、非通联
     */
    @ExcelProperty(value="商户所属",index=4)
    private String isAllinpaymer;
    /**
     * 表示商户的来源渠道 展现形式：通联自拓 合作方推荐-合作方名
     */
    @ExcelProperty(value="商户拓展渠道",index=5)
    private String expandChannl;
    /**
     * 商户拓展人
     */
    @ExcelProperty(value="商户拓展人",index=6)
    private String expandPerson;
    /**
     * 收银宝商户id
     */
    @ExcelProperty(value="收银宝商户号",index=7)
    private String allinpayMerid;
    /**
     * 支付宝子商户id
     */
    @ExcelProperty(value="支付宝子商户号",index=9)
    private String alipayMerid;
    /**
     * 微信子商户id
     */
    @ExcelProperty(value="微信子商户号",index=8)
    private String wxpayMerid;
    /**
     * 云闪付商户id
     */
    @ExcelProperty(value="云闪付子商户号",index=10)
    private String cloudpayMerid;
    /**
     * 联系人
     */
    @ExcelProperty(value="联系人",index=11)
    private String contacts;
    /**
     * 联系方式
     */
    @ExcelProperty(value="联系方式",index=12)
    private String contactsWay;
    /**
     * 联系地址
     */
    @ExcelProperty(value="联系地址",index=13)
    private String contactsAddress;
    /**
     * 备注
     */
    @ExcelProperty(value="备注",index=14)
    private String remark;

    /**
     * 设备ID
     * */
    @ExcelProperty(value="设备ID",index=15)
    private String equipId;





}