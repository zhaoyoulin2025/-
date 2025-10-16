package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设计版本对象 designVersion
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class DesignVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务ID（原外键） */
    @Excel(name = "任务ID", readConverterExp = "原=外键")
    private Long taskId;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long versionId;

    /** 设计文件ID（原外键，引用file表id） */
    @Excel(name = "设计文件ID", readConverterExp = "原=外键，引用file表id")
    private Long desFileId;

    /** 版本描述 */
    @Excel(name = "版本描述")
    private String versionDesc;

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

    public void setDesFileId(Long desFileId) 
    {
        this.desFileId = desFileId;
    }

    public Long getDesFileId() 
    {
        return desFileId;
    }

    public void setVersionDesc(String versionDesc) 
    {
        this.versionDesc = versionDesc;
    }

    public String getVersionDesc() 
    {
        return versionDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("versionId", getVersionId())
            .append("desFileId", getDesFileId())
            .append("versionDesc", getVersionDesc())
            .toString();
    }
}
