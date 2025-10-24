package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 标准作业流程/店铺信息对象 shop
 *
 * @author ruoyi
 * @date 2025-06-15
 */
@Data
public class Shop
{
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Long warningDay;

    @TableField(exist = false)
    private String process;

    @TableField(exist = false)
    private String sortProp;

    @TableField(exist = false)
    private String sortOrder;

    @TableField(exist = false)
    private String rate;

    /** 主键ID */
    @TableId(type=IdType.AUTO)
    private Long id;




    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    /** 店铺名称 */
    @Excel(name = "店铺名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 区域 */
    @Excel(name = "区域")
    private String area;

    /** 地区ID（外键） */
    @Excel(name = "地区ID", readConverterExp = "外=键")
    @TableField("regionId")
    private Long regionId;

    /** 租金 */
    @Excel(name = "租金")

    private BigDecimal rent;

    /** 店铺信息 */
    @Excel(name = "店铺信息")
    @TableField("shopDesc")
    private String shopDesc;

    @TableField("runStatus")
    private String runStatus;


    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    /** 周边描述 */
    @Excel(name = "周边描述")
    @TableField("surroundDesc")
    private String surroundDesc;

    /** 交通描述 */
    @Excel(name = "交通描述")
    @TableField("trafficDesc")
    private String trafficDesc;

    @TableField(exist = false)
    private String followerName;

    @TableField(exist = false)
    private String  clientName;

    @TableField(exist = false)
    private String inValiday;

    @TableField(exist = false)
    private String rentStatus;

    @TableField(exist = false)
    private String keyWord;

    @TableField("districtId")
    private Long districtId;

    @TableField(exist = false)
    private String districtName;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getInValiday() {
        return inValiday;
    }

    public void setInValiday(String inValiday) {
        this.inValiday = inValiday;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAcondition() {
        return acondition;
    }

    public void setAcondition(String acondition) {
        this.acondition = acondition;
    }

    /** 店铺照片 */
    @Excel(name = "店铺照片")
    @TableField("imgUrl")
    private String imgUrl;

    /** 视频地址 */
    @Excel(name = "视频地址")
    @TableField("videoUrl")
    private String videoUrl;

    /** 店铺状态（0=待出租，1=已出租，2=已关闭） */
    @Excel(name = "店铺状态", readConverterExp = "0==待出租，1=已出租，2=已关闭")
    @TableField("shopStatus")
    private Integer shopStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("sTime")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("upTime")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date upTime;

    /** 创建人 */
    @Excel(name = "创建人")
    @TableField("owner")
    private String owner;

    /** 跟随人 */
    @Excel(name = "跟随人")
    @TableField("follower")
    private String follower;

    /** 客户id */
    @Excel(name = "客户id")
    @TableField("clientId")
    private Long clientId;

    /** pdf文件地址 */
    @Excel(name = "pdf文件地址")
    @TableField("pdfUrl")
    private String pdfUrl;

    /** 商铺面积 */
    @Excel(name = "商铺面积")
    @TableField("shopSize")
    private String shopSize;

    /** 动线 */
    @Excel(name = "动线")
    @TableField("dongxian")
    private String dongxian;

    /** 户型 */
    @Excel(name = "户型")
    @TableField("huxing")
    private String huxing;

    /** 门宽 */
    @Excel(name = "门宽")
    @TableField("menkuan")
    private String menkuan;

    /** 外摆 */
    @Excel(name = "外摆")
    @TableField("waibai")
    private String waibai;

    /** 年限 */
    @Excel(name = "年限")
    @TableField("nianxian")
    private String nianxian;

    /** 递增 */
    @Excel(name = "递增")
    @TableField("dizneg")
    private String dizneg;

    /** 装修免租期 */
    @Excel(name = "装修免租期")
    @TableField("norent")
    private String norent;

    /** 电费 */
    @Excel(name = "电费")
    @TableField("dian")
    private String dian;

    /** 水费 */
    @Excel(name = "水费")
    @TableField("shui")
    private String shui;

    /** 装修押金 */
    @Excel(name = "装修押金")
    @TableField("yajin")
    private String yajin;

    /** 装让费 */
    @Excel(name = "装让费")
    @TableField("zhuanrangfei")
    private String zhuanrangfei;

    /** 工程条件 */
    @Excel(name = "工程条件")
    @TableField("acondition")
    private String acondition;

    /** 广告位 */
    @Excel(name = "广告位")
    @TableField("guanggao")
    private String guanggao;

    /** 总结 */
    @Excel(name = "总结")
    @TableField("allDesc")
    private String allDesc;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public Long getRegionId()
    {
        return regionId;
    }

    public void setRent(BigDecimal rent)
    {
        this.rent = rent;
    }

    public BigDecimal getRent()
    {
        return rent;
    }

    public void setShopDesc(String shopDesc)
    {
        this.shopDesc = shopDesc;
    }

    public String getShopDesc()
    {
        return shopDesc;
    }

    public void setSurroundDesc(String surroundDesc)
    {
        this.surroundDesc = surroundDesc;
    }

    public String getSurroundDesc()
    {
        return surroundDesc;
    }

    public void setTrafficDesc(String trafficDesc)
    {
        this.trafficDesc = trafficDesc;
    }

    public String getTrafficDesc()
    {
        return trafficDesc;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl()
    {
        return videoUrl;
    }

    public void setShopStatus(Integer shopStatus)
    {
        this.shopStatus = shopStatus;
    }

    public Integer getShopStatus()
    {
        return shopStatus;
    }

    public void setsTime(Date sTime)
    {
        this.sTime = sTime;
    }

    public Date getsTime()
    {
        return sTime;
    }

    public void setUpTime(Date upTime)
    {
        this.upTime = upTime;
    }

    public Date getUpTime()
    {
        return upTime;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setFollower(String follower)
    {
        this.follower = follower;
    }

    public String getFollower()
    {
        return follower;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setPdfUrl(String pdfUrl)
    {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfUrl()
    {
        return pdfUrl;
    }

    public void setShopSize(String shopSize)
    {
        this.shopSize = shopSize;
    }

    public String getShopSize()
    {
        return shopSize;
    }

    public void setDongxian(String dongxian)
    {
        this.dongxian = dongxian;
    }

    public String getDongxian()
    {
        return dongxian;
    }

    public void setHuxing(String huxing)
    {
        this.huxing = huxing;
    }

    public String getHuxing()
    {
        return huxing;
    }

    public void setMenkuan(String menkuan)
    {
        this.menkuan = menkuan;
    }

    public String getMenkuan()
    {
        return menkuan;
    }

    public void setWaibai(String waibai)
    {
        this.waibai = waibai;
    }

    public String getWaibai()
    {
        return waibai;
    }

    public void setNianxian(String nianxian)
    {
        this.nianxian = nianxian;
    }

    public String getNianxian()
    {
        return nianxian;
    }

    public void setDizneg(String dizneg)
    {
        this.dizneg = dizneg;
    }

    public String getDizneg()
    {
        return dizneg;
    }

    public void setNorent(String norent)
    {
        this.norent = norent;
    }

    public String getNorent()
    {
        return norent;
    }

    public void setDian(String dian)
    {
        this.dian = dian;
    }

    public String getDian()
    {
        return dian;
    }

    public void setShui(String shui)
    {
        this.shui = shui;
    }

    public String getShui()
    {
        return shui;
    }

    public void setYajin(String yajin)
    {
        this.yajin = yajin;
    }

    public String getYajin()
    {
        return yajin;
    }

    public void setZhuanrangfei(String zhuanrangfei)
    {
        this.zhuanrangfei = zhuanrangfei;
    }

    public String getZhuanrangfei()
    {
        return zhuanrangfei;
    }

    public void setCondition(String acondition)
    {
        this.acondition = acondition;
    }

    public String getCondition()
    {
        return acondition;
    }

    public void setGuanggao(String guanggao)
    {
        this.guanggao = guanggao;
    }

    public String getGuanggao()
    {
        return guanggao;
    }

    public void setAllDesc(String allDesc)
    {
        this.allDesc = allDesc;
    }

    public String getAllDesc()
    {
        return allDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("address", getAddress())
            .append("area", getArea())
            .append("regionId", getRegionId())
            .append("rent", getRent())
            .append("shopDesc", getShopDesc())
            .append("surroundDesc", getSurroundDesc())
            .append("trafficDesc", getTrafficDesc())
            .append("imgUrl", getImgUrl())
            .append("videoUrl", getVideoUrl())
            .append("shopStatus", getShopStatus())
            .append("sTime", getsTime())
            .append("upTime", getUpTime())
            .append("owner", getOwner())
            .append("follower", getFollower())
            .append("clientId", getClientId())
            .append("pdfUrl", getPdfUrl())
            .append("shopSize", getShopSize())
            .append("dongxian", getDongxian())
            .append("huxing", getHuxing())
            .append("menkuan", getMenkuan())
            .append("waibai", getWaibai())
            .append("nianxian", getNianxian())
            .append("dizneg", getDizneg())
            .append("norent", getNorent())
            .append("dian", getDian())
            .append("shui", getShui())
            .append("yajin", getYajin())
            .append("zhuanrangfei", getZhuanrangfei())
            .append("condition", getCondition())
            .append("guanggao", getGuanggao())
            .append("allDesc", getAllDesc())
            .toString();
    }
}
