package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 建设管理对象 build
 *
 * @author ruoyi
 * @date 2025-06-25
 */
public class Build extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String followerName;
    private String shopName;

    private String superviserName;
    private String clientName;

    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSuperviserName() {
        return superviserName;
    }

    public void setSuperviserName(String superviserName) {
        this.superviserName = superviserName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    private Long periodId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    /** 主键ID */
    private Long id;

    /** 监理Id */
    @Excel(name = "监理Id")
    private Long superviserId;

    /** 跟随人（原外键） */
    @Excel(name = "跟随人", readConverterExp = "原=外键")
    private Long followerId;

    /** 店铺ID（原外键，引用sop表id） */
    @Excel(name = "店铺ID", readConverterExp = "原=外键，引用sop表id")
    private Long shopId;

    /** 客户ID（原外键） */
    @Excel(name = "客户ID", readConverterExp = "原=外键")
    private Long clientId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date upTime;

    /** 营建状态（0=未开始，1=进行中，2=已完成） */
    @Excel(name = "营建状态", readConverterExp = "0==未开始，1=进行中，2=已完成")
    private Integer superviseStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setSuperviserId(Long superviserId)
    {
        this.superviserId = superviserId;
    }

    public Long getSuperviserId()
    {
        return superviserId;
    }

    public void setFollowerId(Long followerId)
    {
        this.followerId = followerId;
    }

    public Long getFollowerId()
    {
        return followerId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getClientId()
    {
        return clientId;
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

    public void setSuperviseStatus(Integer superviseStatus)
    {
        this.superviseStatus = superviseStatus;
    }

    public Integer getSuperviseStatus()
    {
        return superviseStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("superviserId", getSuperviserId())
                .append("followerId", getFollowerId())
                .append("shopId", getShopId())
                .append("clientId", getClientId())
                .append("sTime", getsTime())
                .append("upTime", getUpTime())
                .append("superviseStatus", getSuperviseStatus())
                .toString();
    }
}
