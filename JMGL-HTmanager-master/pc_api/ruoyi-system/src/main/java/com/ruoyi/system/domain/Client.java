package com.ruoyi.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户信息对象 client
 *
 * @author ruoyi
 * @date 2025-06-15
 */
@Data
@TableName("client")
public class Client
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type=IdType.AUTO)
    private Long id;

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    /** 姓名 */
    @Excel(name = "姓名",sort = 1)
    private String name;

    /** 电话号码 */
    @Excel(name = "电话号码",sort = 4)
    private String phone;

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    @Getter
    @Excel(name = "微信号",sort = 3)
    private String wechat;

    @Getter
    @Excel(name = "门店经营参与方式",sort = 16)
        @TableField("joinType")
    private String joinType;

    /** 用户id */
//    @Excel(name = "用户id")
    @TableField("userId")
    private Long userId;

    /** 客户等级（1=普通，2=VIP, 默认=0） */
//    @Excel(name = "客户等级", readConverterExp = "1==普通，2=VIP,,默=认=0")
    @TableField("clientLevel")
    private String clientLevel;

    @Excel(name = "客户等级",sort = 8)
    @TableField("clientLevel")
    private String clientLevelName;

    public String getClientLevelName() {
        return clientLevelName;
    }

    public void setClientLevelName(String clientLevelName) {
        this.clientLevelName = clientLevelName;
    }

    /** 客户状态（0=待跟进，1=意向中 2=已签约 3= 已放弃 0 默认值） */
//    @Excel(name = "客户状态", readConverterExp = "0==待跟进，1=意向中,2==已签约,3==,已=放弃,0=,默=认值")
    @TableField("clientStatus")
    private Integer clientStatus;

    public String getClientStatusName() {
        return clientStatusName;
    }

    public void setClientStatusName(String clientStatusName) {
        this.clientStatusName = clientStatusName;
    }

    @Excel(name = "客户状态",sort = 23)
    @TableField(exist = false)
    private String clientStatusName;

    /** 导购ID（外键） */
//    @Excel(name = "导购ID", readConverterExp = "外=键")
    @TableField("guideId")
    private Long guideId;

    @Excel(name = "跟进人",sort = 24)
    @TableField(exist = false)
    private String guideName;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd",sort = 25)
    @TableField("sTime")
    private Date sTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd",sort = 26)
    @TableField("upTime")
    private Date upTime;

    /** 群号 */
    @Excel(name = "群号",sort = 2)
    @TableField("groupNum")
    private String groupNum;

    /** 意向城市 */
//    @Excel(name = "意向城市")
    @TableField("likeCity")
    private String likeCity;

    /** 预算 */
//    @Excel(name = "预算")
//    @TableField("budget")
    private String budget;

    /** 是否接受转让 */

    @TableField("isTransfor")
    private Long isTransfor;

    @Excel(name = "是否接受转让门店",sort = 17)
    @TableField("isTransfor")
    private String isTransforName;

    public String getIsTransforName() {
        return isTransforName;
    }

    public void setIsTransforName(String isTransforName) {
        this.isTransforName = isTransforName;
    }

    /** 后期打算 */
//    @Excel(name = "后期打算")
    @TableField("plan")
    private String plan;

    /** 其他项目经验 */
//    @Excel(name = "其他项目经验")
    @TableField("otherProject")
    private String otherProject;

    /** 独资还是合伙 */

    @TableField("single")
    private Long single;

//    @Excel(name = "独资还是合伙")
    @TableField(exist = false)
    private String singleName;

    public String getSingleName() {
        return singleName;
    }

    public void setSingleName(String singleName) {
        this.singleName = singleName;
    }

    /** 是否有开多店的打算 */

    @TableField("ismore")
    private Long ismore;

//    @Excel(name = "是否有开多店的打算")
    @TableField(exist = false)
    private String ismoreName;

    public String getIsmoreName() {
        return ismoreName;
    }

    public void setIsmoreName(String ismoreName) {
        this.ismoreName = ismoreName;
    }

    /** 首次咨询时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "首次咨询时间", width = 30, dateFormat = "yyyy-MM-dd",sort = 5)
    @TableField("firstTime")
    private Date firstTime;

    /** 成交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成交时间", width = 30, dateFormat = "yyyy-MM-dd",sort = 14)
    @TableField("dealTime")
    private Date dealTime;

    /** 到访次数 */
    @Excel(name = "到访次数",sort = 7)
    @TableField("comeTime")
    private Long comeTime;

    /** 现在居住地 */

    @TableField("nowHouse")
    private String nowHouse;

    /** 籍贯 */

    @TableField("oldHouse")
    private String oldHouse;

    /** 如何看待有转让费或者其他原因 */
    @Excel(name = "如何看待有转让费",sort = 18)
    @TableField("otherThink")
    private String otherThink;

    /** 省份 */
//    @Excel(name = "省份")
    private String province;

    /** 居住地 */
    @Excel(name = "居住地",sort = 15)
    private String residence;

    /** 经营品类 */
//    @Excel(name = "经营品类")
    private String businessCategory;

    /** 经常光顾店 */
    @Excel(name = "经常光顾店",sort = 6)
    private String frequentStore;

    /** 门店形式 */
    @Excel(name = "门店形式",sort = 12)
    private String storeType;

    /** 投资预算 */
    @Excel(name = "投资预算",sort = 11)
    private String investmentBudget;

    /** 合作模式（单店加盟/区域代理/合伙经营/技术合作/其他） */
    @Excel(name = "品牌合作方式",sort = 10)
    private String cooperationMode;

    /** 期望开店地址 */
//    @Excel(name = "期望开店地址")
    private String expectedStoreAddress;

    /** 期望开店省份 */
//    @Excel(name = "期望开店省份")
    private String expectedProvince;

    /** 期望开店市 */
    @Excel(name = "期望开店市",sort = 9)
    private String expectedCity;

    /** 期望开店区 */
//    @Excel(name = "期望开店区")
    private String expectedDistrict;

    /** 从事行业（餐饮/零售/服务/教育/娱乐/其他） */
    @Excel(name = "您正在做的餐饮品牌",sort = 13)
    private String industry;

    /** 你认为的商圈（核心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他） */
//    @Excel(name = "你认为的商圈", readConverterExp = "核=心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他")
    private String businessDistrict;

    /** 备注 */
    @Excel(name = "备注",sort = 21)
    private String remarks;

    @Excel(name = "店铺查看权限",sort = 22)
    @TableField(exist = false)
    private String shopResourceName;

    /** 如何看待发展 */
//    @Excel(name = "如何看待发展")
    private String developmentOpinion;

    /** 选择我的原因 */
    @Excel(name = "那你认为柒小螺的优势",sort = 19)
    private String selectionReason;

    /** 我们提升的建议 */
    @Excel(name = "您觉得柒小螺哪些方面需要提升",sort = 20)
    private String improvementSuggestion;

    @TableField(exist = false)
    private Long clientId;


    @TableField(exist = false)
    private String keyWord;

    @TableField(exist = false)
    private String levelName;





    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getFrequentStore() {
        return frequentStore;
    }

    public void setFrequentStore(String frequentStore) {
        this.frequentStore = frequentStore;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getInvestmentBudget() {
        return investmentBudget;
    }

    public void setInvestmentBudget(String investmentBudget) {
        this.investmentBudget = investmentBudget;
    }

    public String getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(String cooperationMode) {
        this.cooperationMode = cooperationMode;
    }

    public String getExpectedStoreAddress() {
        return expectedStoreAddress;
    }

    public void setExpectedStoreAddress(String expectedStoreAddress) {
        this.expectedStoreAddress = expectedStoreAddress;
    }

    public String getExpectedProvince() {
        return expectedProvince;
    }

    public void setExpectedProvince(String expectedProvince) {
        this.expectedProvince = expectedProvince;
    }

    public String getExpectedCity() {
        return expectedCity;
    }

    public void setExpectedCity(String expectedCity) {
        this.expectedCity = expectedCity;
    }

    public String getExpectedDistrict() {
        return expectedDistrict;
    }

    public void setExpectedDistrict(String expectedDistrict) {
        this.expectedDistrict = expectedDistrict;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getBusinessDistrict() {
        return businessDistrict;
    }

    public void setBusinessDistrict(String businessDistrict) {
        this.businessDistrict = businessDistrict;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDevelopmentOpinion() {
        return developmentOpinion;
    }

    public void setDevelopmentOpinion(String developmentOpinion) {
        this.developmentOpinion = developmentOpinion;
    }

    public String getSelectionReason() {
        return selectionReason;
    }

    public void setSelectionReason(String selectionReason) {
        this.selectionReason = selectionReason;
    }

    public String getImprovementSuggestion() {
        return improvementSuggestion;
    }

    public void setImprovementSuggestion(String improvementSuggestion) {
        this.improvementSuggestion = improvementSuggestion;
    }




    @TableField("shopResource")
    private Integer shopResource;

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

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setClientLevel(String clientLevel)
    {
        this.clientLevel = clientLevel;
    }

    public String getClientLevel()
    {
        return clientLevel;
    }

    public void setClientStatus(Integer clientStatus)
    {
        this.clientStatus = clientStatus;
    }

    public Integer getClientStatus()
    {
        return clientStatus;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    public Long getGuideId()
    {
        return guideId;
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

    public void setGroupNum(String groupNum)
    {
        this.groupNum = groupNum;
    }

    public String getGroupNum()
    {
        return groupNum;
    }

    public void setLikeCity(String likeCity)
    {
        this.likeCity = likeCity;
    }

    public String getLikeCity()
    {
        return likeCity;
    }

    public void setBudget(String budget)
    {
        this.budget = budget;
    }

    public String getBudget()
    {
        return budget;
    }

    public void setIsTransfor(Long isTransfor)
    {
        this.isTransfor = isTransfor;
    }

    public Long getIsTransfor()
    {
        return isTransfor;
    }

    public void setPlan(String plan)
    {
        this.plan = plan;
    }

    public String getPlan()
    {
        return plan;
    }

    public void setOtherProject(String otherProject)
    {
        this.otherProject = otherProject;
    }

    public String getOtherProject()
    {
        return otherProject;
    }

    public void setSingle(Long single)
    {
        this.single = single;
    }

    public Long getSingle()
    {
        return single;
    }

    public void setIsmore(Long ismore)
    {
        this.ismore = ismore;
    }

    public Long getIsmore()
    {
        return ismore;
    }

    public void setFirstTime(Date firstTime)
    {
        this.firstTime = firstTime;
    }

    public Date getFirstTime()
    {
        return firstTime;
    }

    public void setDealTime(Date dealTime)
    {
        this.dealTime = dealTime;
    }

    public Date getDealTime()
    {
        return dealTime;
    }

    public void setComeTime(Long comeTime)
    {
        this.comeTime = comeTime;
    }

    public Long getComeTime()
    {
        return comeTime;
    }

    public void setNowHouse(String nowHouse)
    {
        this.nowHouse = nowHouse;
    }

    public String getNowHouse()
    {
        return nowHouse;
    }

    public void setOldHouse(String oldHouse)
    {
        this.oldHouse = oldHouse;
    }

    public String getOldHouse()
    {
        return oldHouse;
    }

    public void setOtherThink(String otherThink)
    {
        this.otherThink = otherThink;
    }

    public String getOtherThink()
    {
        return otherThink;
    }

    public Integer getShopResource() {
        return shopResource;
    }

    public void setShopResource(Integer shopResource) {
        this.shopResource = shopResource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("phone", getPhone())
            .append("userId", getUserId())
            .append("clientLevel", getClientLevel())
            .append("clientStatus", getClientStatus())
            .append("guideId", getGuideId())
            .append("sTime", getsTime())
            .append("upTime", getUpTime())
            .append("groupNum", getGroupNum())
            .append("likeCity", getLikeCity())
            .append("budget", getBudget())
            .append("isTransfor", getIsTransfor())
            .append("plan", getPlan())
            .append("otherProject", getOtherProject())
            .append("single", getSingle())
            .append("ismore", getIsmore())
            .append("firstTime", getFirstTime())
            .append("dealTime", getDealTime())
            .append("comeTime", getComeTime())
            .append("nowHouse", getNowHouse())
            .append("oldHouse", getOldHouse())
            .append("otherThink", getOtherThink())
            .toString();
    }
}
