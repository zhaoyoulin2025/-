package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户事件对象 clientEvent
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class ClientEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID（外键） */
    @Excel(name = "客户ID", readConverterExp = "外=键")
    private Long clientId;

    /** 事件类型（1=咨询，2=投诉，3=跟进等） */
    @Excel(name = "事件类型", readConverterExp = "1==咨询，2=投诉，3=跟进等")
    private Integer eType;

    /** 事件时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "事件时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date eTime;

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

    public void seteType(Integer eType) 
    {
        this.eType = eType;
    }

    public Integer geteType() 
    {
        return eType;
    }

    public void seteTime(Date eTime) 
    {
        this.eTime = eTime;
    }

    public Date geteTime() 
    {
        return eTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("eType", geteType())
            .append("eTime", geteTime())
            .toString();
    }
}
