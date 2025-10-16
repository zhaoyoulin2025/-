package com.ruoyi.system.domain;



import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设计任务分配记录对象 design_assign_record
 *
 * @author ruoyi
 * @date 2025-09-21
 */
public class DesignAssignRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联的设计主表ID（外键） */
    @Excel(name = "设计主表ID")
    private Long designId;

    /** 设计阶段：1=平面设计，2=效果设计，3=施工设计 */
    @Excel(name = "设计阶段", readConverterExp = "1=平面设计,2=效果设计,3=施工设计")
    private Integer designStage;

    /** 设计师用户ID（关联用户表） */
    @Excel(name = "设计师用户ID")
    private Long designerId;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "分配时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date assignTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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
    public void setDesignStage(Integer designStage)
    {
        this.designStage = designStage;
    }

    public Integer getDesignStage()
    {
        return designStage;
    }
    public void setDesignerId(Long designerId)
    {
        this.designerId = designerId;
    }

    public Long getDesignerId()
    {
        return designerId;
    }
    public void setAssignTime(Date assignTime)
    {
        this.assignTime = assignTime;
    }

    public Date getAssignTime()
    {
        return assignTime;
    }
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("designId", getDesignId())
                .append("designStage", getDesignStage())
                .append("designerId", getDesignerId())
                .append("assignTime", getAssignTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
