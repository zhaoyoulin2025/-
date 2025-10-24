package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商圈信息对象 business_district
 *
 * @author ruoyi
 * @date 2025-07-11
 */
public class BusinessDistrict extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private String followerStatus;

    public String getFollowerStatus() {
        return followerStatus;
    }

    public void setFollowerStatus(String followerStatus) {
        this.followerStatus = followerStatus;
    }

    private int remainingDays;


    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    @Excel(name = "创建人")
    private String createrName;

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    /** 商圈ID */
    private Long id;

    /** 商圈名称 */
    @Excel(name = "商圈名称")
    private String name;

    /** 商圈地址 */
    @Excel(name = "商圈地址")
    private String address;


    private Long followerId;

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String region;

    /** 关联客户ID */

    private Long customerId;


    private String statues;

    private String keyword;


    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }
    @Excel(name = "客户")
    private String customerName;

    private String followerName;

    public String followerIds;


    public String getFollowerIds() {
        return followerIds;
    }

    public void setFollowerIds(String followerIds) {
        this.followerIds = followerIds;
    }

    public List<String> followerIdList;
    public List<String> getFollowerIdList() {
        return followerIdList;
    }

    public void setFollowerIdList(List<String> followerIdList) {
        this.followerIdList = followerIdList;
    }


    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }



    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    /** 创建人ID */

    private Long creatorId;

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

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("address", getAddress())
            .append("region", getRegion())
            .append("customerId", getCustomerId())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("creatorId", getCreatorId())
            .toString();
    }
}
