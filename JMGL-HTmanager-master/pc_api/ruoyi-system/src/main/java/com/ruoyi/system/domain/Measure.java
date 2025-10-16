package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 测量信息对象 measure
 *
 * @author ruoyi
 * @date 2025-06-18
 */
public class Measure extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private String designName;


    private String creatorNeme;

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getCreatorNeme() {
        return creatorNeme;
    }

    public void setCreatorNeme(String creatorNeme) {
        this.creatorNeme = creatorNeme;
    }

    /** 测量ID */
    private Long id;

    /** 设计id */
    @Excel(name = "设计id")
    private Long designId;

    /** 测量来源 */
    @Excel(name = "测量来源")
    private Long measureType;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String fileUrl;

    /** 创建人 */
    @Excel(name = "创建人")
    private String creator;



    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date upTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setDesignId(Long designId)
    {
        this.designId = designId;
    }

    public Long getDesignId()
    {
        return designId;
    }

    public void setMeasureType(Long measureType)
    {
        this.measureType = measureType;
    }

    public Long getMeasureType()
    {
        return measureType;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setUpTime(Date upTime)
    {
        this.upTime = upTime;
    }

    public Date getUpTime()
    {
        return upTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("designId", getDesignId())
            .append("measureType", getMeasureType())
            .append("fileUrl", getFileUrl())
            .append("creator", getCreator())
            .append("createTime", getCreateTime())
            .append("upTime", getUpTime())
            .toString();
    }
}
