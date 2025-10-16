package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商圈跟进笔记（含打卡）对象 note
 *
 * @author ruoyi
 * @date 2025-07-11
 */
public class Note extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public String getStatusss() {
        return statusss;
    }

    public void setStatusss(String statusss) {
        this.statusss = statusss;
    }

    private String statusss;
    private String followerName;

    private String businessName;

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /** 笔记ID */
    private Long id;

    private String traceId;

    /** 关联商圈ID */
    @Excel(name = "关联商圈ID")
    private Long businessDistrictId;

    /** 跟进人员ID */
    @Excel(name = "跟进人员ID")
    private Long followerId;

    /** 文字描述 */
    @Excel(name = "文字描述")
    private String description;

    /** 图片URL集合（逗号分隔） */
    @Excel(name = "图片URL集合", readConverterExp = "逗=号分隔")
    private String imageUrl;


    private List<String> imageUrls;

    /** 视频URL */
    @Excel(name = "视频URL")
    private String videoUrl;

    /** 打卡位置（经纬度+地址） */
    @Excel(name = "打卡位置", readConverterExp = "经=纬度+地址")
    private String checkinLocation;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setBusinessDistrictId(Long businessDistrictId)
    {
        this.businessDistrictId = businessDistrictId;
    }

    public Long getBusinessDistrictId()
    {
        return businessDistrictId;
    }

    public void setFollowerId(Long followerId)
    {
        this.followerId = followerId;
    }

    public Long getFollowerId()
    {
        return followerId;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl()
    {
        return videoUrl;
    }

    public void setCheckinLocation(String checkinLocation)
    {
        this.checkinLocation = checkinLocation;
    }

    public String getCheckinLocation()
    {
        return checkinLocation;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("businessDistrictId", getBusinessDistrictId())
            .append("followerId", getFollowerId())
            .append("description", getDescription())
            .append("imageUrls", getImageUrls())
            .append("videoUrl", getVideoUrl())
            .append("checkinLocation", getCheckinLocation())
            .append("updatedAt", getUpdatedAt())
            .append("traceId", getTraceId())
            .toString();
    }
}
