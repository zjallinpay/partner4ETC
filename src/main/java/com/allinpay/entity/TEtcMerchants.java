package com.allinpay.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户信息表(TEtcMerchants)实体类
 *
 * @author makejava
 * @since 2020-12-17 13:56:27
 */
@Data
@TableName("T_ETC_MERCHANTS")
public class TEtcMerchants implements Serializable {
    private static final long serialVersionUID = 815216802698135019L;
    /**
     * 商户id主键
     */
    @TableId(value = "MER_ID",type = IdType.AUTO)
    private Integer merId;
    /**
     * 商户名称（唯一）
     */
    @NotBlank
    @TableField("MER_NAME")
    private String merName;
    /**
     * 所属行业
     */
    @TableField("BELONG_INDUSTRY")
    private String belongIndustry;

    /**
     * 商圈
     */
    @TableField("TRADING_AREA")
    private String tradingArea;
    /**
     * 地区 枚举类型值：舟山市,杭州市,嘉兴市,温州市,宁波市,绍兴市,湖州市,丽水市,台州市,金华市,衢州市。
     */
    @NotBlank
    @TableField("AREA")
    private String area;
    /**
     * 是否为通联商户 枚举类型值：通联、非通联
     */
    @NotBlank
    @TableField("IS_ALLINPAYMER")
    private String isAllinpaymer;
    /**
     * 表示商户的来源渠道 展现形式：通联自拓 合作方推荐-合作方名
     */
    @TableField("EXPAND_CHANNL")
    private String expandChannl;
    /**
     * 商户拓展人
     */
    @TableField("EXPAND_PERSON")
    private String expandPerson;
    /**
     * 收银宝商户id
     */
    @TableField("ALLINPAY_MERID")
    private String allinpayMerid;
    /**
     * 支付宝子商户id
     */
    @TableField("ALIPAY_MERID")
    private String alipayMerid;
    /**
     * 微信子商户id
     */
    @TableField("WXPAY_MERID")
    private String wxpayMerid;
    /**
     * 云闪付商户id
     */
    @TableField("CLOUDPAY_MERID")
    private String cloudpayMerid;
    /**
     * 联系人
     */
    @TableField("CONTACTS")
    private String contacts;
    /**
     * 联系方式
     */
    @TableField("CONTACTS_WAY")
    private String contactsWay;
    /**
     * 联系地址
     */
    @TableField("CONTACTS_ADDRESS")
    private String contactsAddress;
    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;



    /**
     * 设备ID
     * */
    @TableField("EQUIP_ID")
    private String equipId;


}