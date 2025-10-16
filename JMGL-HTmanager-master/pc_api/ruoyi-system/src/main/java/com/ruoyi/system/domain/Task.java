package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务管理对象 task
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class Task extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID（原外键） */
    @Excel(name = "客户ID", readConverterExp = "原=外键")
    private Long clientId;

    /** 店铺ID（原外键，引用sop表id） */
    @Excel(name = "店铺ID", readConverterExp = "原=外键，引用sop表id")
    private Long shopId;

    /** 任务状态（0=待处理，1=进行中，2=已完成） */
    @Excel(name = "任务状态", readConverterExp = "0==待处理，1=进行中，2=已完成")
    private Integer taskStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date upTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setClientId(Long clientId) 
    {
        this.clientId = clientId;
    }

    public Long getClientId() 
    {
        return clientId;
    }

    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }

    public void setTaskStatus(Integer taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskStatus() 
    {
        return taskStatus;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("shopId", getShopId())
            .append("taskStatus", getTaskStatus())
            .append("sTime", getsTime())
            .append("upTime", getUpTime())
            .toString();
    }
}
