package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 建设详情对象 buildDetail
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class BuildDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务ID（原外键） */
    @Excel(name = "任务ID", readConverterExp = "原=外键")
    private Long taskId;

    /** 版本ID（原外键） */
    @Excel(name = "版本ID", readConverterExp = "原=外键")
    private Long versionId;

    /** 建设ID（原外键） */
    @Excel(name = "建设ID", readConverterExp = "原=外键")
    private Long buildId;

    /** 建设类型（1=装修，2=改造，3=新建等） */
    @Excel(name = "建设类型", readConverterExp = "1==装修，2=改造，3=新建等")
    private Integer buildType;

    /** 建设描述 */
    @Excel(name = "建设描述")
    private String buildDesc;

    /** 关联文件ID（原外键，引用file表id） */
    @Excel(name = "关联文件ID", readConverterExp = "原=外键，引用file表id")
    private Long buildFile;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }

    public void setVersionId(Long versionId) 
    {
        this.versionId = versionId;
    }

    public Long getVersionId() 
    {
        return versionId;
    }

    public void setBuildId(Long buildId) 
    {
        this.buildId = buildId;
    }

    public Long getBuildId() 
    {
        return buildId;
    }

    public void setBuildType(Integer buildType) 
    {
        this.buildType = buildType;
    }

    public Integer getBuildType() 
    {
        return buildType;
    }

    public void setBuildDesc(String buildDesc) 
    {
        this.buildDesc = buildDesc;
    }

    public String getBuildDesc() 
    {
        return buildDesc;
    }

    public void setBuildFile(Long buildFile) 
    {
        this.buildFile = buildFile;
    }

    public Long getBuildFile() 
    {
        return buildFile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("versionId", getVersionId())
            .append("buildId", getBuildId())
            .append("buildType", getBuildType())
            .append("buildDesc", getBuildDesc())
            .append("buildFile", getBuildFile())
            .toString();
    }
}
