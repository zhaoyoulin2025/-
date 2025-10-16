package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 加盟投资意向对象 franchise_investment_form
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Data
public class FranchiseInvestmentForm
{
    private static final long serialVersionUID = 1L;

    private Long clientId;


    /** 主键ID */
    @TableId(type=IdType.AUTO)
    private Integer id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String phone;

    /** 省份 */
    @Excel(name = "省份")
    private String province;

    /** 居住地 */
    @Excel(name = "居住地")
    private String residence;

    /** 经常光顾店 */
    @Excel(name = "经常光顾店")
    private String frequentStore;

    /** 门店形式（直营店/加盟店/联营店/经销商/其他） */
    @Excel(name = "门店形式", readConverterExp = "直=营店/加盟店/联营店/经销商/其他")
    private String storeType;

    //
    private String businessCategory;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 投资预算（5万以下/5-10万/10-20万/20-50万/50-100万/100万以上） */
    @Excel(name = "投资预算", readConverterExp = "5=万以下/5-10万/10-20万/20-50万/50-100万/100万以上")
    private String investmentBudget;

    /** 合作模式（单店加盟/区域代理/合伙经营/技术合作/其他） */
    @Excel(name = "合作模式", readConverterExp = "单=店加盟/区域代理/合伙经营/技术合作/其他")
    private String cooperationMode;

    /** 期望开店地址 */
    @Excel(name = "期望开店地址")
    private String expectedStoreAddress;

    /** 期望开店省份 */
    @Excel(name = "期望开店省份")
    private String expectedProvince;

    /** 期望开店市 */
    @Excel(name = "期望开店市")
    private String expectedCity;

    /** 期望开店区 */
    @Excel(name = "期望开店区")
    private String expectedDistrict;

    /** 从事行业（餐饮/零售/服务/教育/娱乐/其他） */
    @Excel(name = "从事行业", readConverterExp = "餐=饮/零售/服务/教育/娱乐/其他")
    private String industry;

    /** 如何看待发展 */
    @Excel(name = "如何看待发展")
    private String developmentOpinion;

    /** 选择我的原因 */
    @Excel(name = "选择我的原因")
    private String selectionReason;

    /** 我们提升的建议 */
    @Excel(name = "我们提升的建议")
    private String improvementSuggestion;

    /** 你认为的商圈（核心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他） */
    @Excel(name = "你认为的商圈", readConverterExp = "核=心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他")
    private String businessDistrict;

    private Date createTime;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
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

    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }

    public void setResidence(String residence) 
    {
        this.residence = residence;
    }

    public String getResidence() 
    {
        return residence;
    }

    public void setFrequentStore(String frequentStore) 
    {
        this.frequentStore = frequentStore;
    }

    public String getFrequentStore() 
    {
        return frequentStore;
    }

    public void setStoreType(String storeType) 
    {
        this.storeType = storeType;
    }

    public String getStoreType() 
    {
        return storeType;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    public void setInvestmentBudget(String investmentBudget) 
    {
        this.investmentBudget = investmentBudget;
    }

    public String getInvestmentBudget() 
    {
        return investmentBudget;
    }

    public void setCooperationMode(String cooperationMode) 
    {
        this.cooperationMode = cooperationMode;
    }

    public String getCooperationMode() 
    {
        return cooperationMode;
    }

    public void setExpectedStoreAddress(String expectedStoreAddress) 
    {
        this.expectedStoreAddress = expectedStoreAddress;
    }

    public String getExpectedStoreAddress() 
    {
        return expectedStoreAddress;
    }

    public void setExpectedProvince(String expectedProvince) 
    {
        this.expectedProvince = expectedProvince;
    }

    public String getExpectedProvince() 
    {
        return expectedProvince;
    }

    public void setExpectedCity(String expectedCity) 
    {
        this.expectedCity = expectedCity;
    }

    public String getExpectedCity() 
    {
        return expectedCity;
    }

    public void setExpectedDistrict(String expectedDistrict) 
    {
        this.expectedDistrict = expectedDistrict;
    }

    public String getExpectedDistrict() 
    {
        return expectedDistrict;
    }

    public void setIndustry(String industry) 
    {
        this.industry = industry;
    }

    public String getIndustry() 
    {
        return industry;
    }

    public void setDevelopmentOpinion(String developmentOpinion) 
    {
        this.developmentOpinion = developmentOpinion;
    }

    public String getDevelopmentOpinion() 
    {
        return developmentOpinion;
    }

    public void setSelectionReason(String selectionReason) 
    {
        this.selectionReason = selectionReason;
    }

    public String getSelectionReason() 
    {
        return selectionReason;
    }

    public void setImprovementSuggestion(String improvementSuggestion) 
    {
        this.improvementSuggestion = improvementSuggestion;
    }

    public String getImprovementSuggestion() 
    {
        return improvementSuggestion;
    }

    public void setBusinessDistrict(String businessDistrict) 
    {
        this.businessDistrict = businessDistrict;
    }

    public String getBusinessDistrict() 
    {
        return businessDistrict;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("phone", getPhone())
            .append("province", getProvince())
            .append("residence", getResidence())
            .append("frequentStore", getFrequentStore())
            .append("storeType", getStoreType())
            .append("remarks", getRemarks())
            .append("investmentBudget", getInvestmentBudget())
            .append("cooperationMode", getCooperationMode())
            .append("expectedStoreAddress", getExpectedStoreAddress())
            .append("expectedProvince", getExpectedProvince())
            .append("expectedCity", getExpectedCity())
            .append("expectedDistrict", getExpectedDistrict())
            .append("industry", getIndustry())
            .append("developmentOpinion", getDevelopmentOpinion())
            .append("selectionReason", getSelectionReason())
            .append("improvementSuggestion", getImprovementSuggestion())
            .append("businessDistrict", getBusinessDistrict())
            .append("createTime", getCreateTime())
            .toString();
    }
}
