package com.ruoyi.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户跟进记录对象 customer_follow_up
 * 
 * @author ruoyi
 * @date 2025-06-06
 */
@Data
public class CustomerFollowUp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID（自增主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 跟进用户ID */
    @Excel(name = "跟进用户ID")
    private String userId;

    @TableField(exist = false)
    private String nickName;

    /** 客户ID */
    @Excel(name = "客户ID")
    private String clientId;

    @TableField(exist = false)
    private String clientName;

    /** 跟进时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "跟进时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date followUpTime;

    /** 跟进详情描述 */
    @Excel(name = "跟进详情描述")
    private String followUpDescription;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setClientId(String clientId) 
    {
        this.clientId = clientId;
    }

    public String getClientId() 
    {
        return clientId;
    }

    public void setFollowUpTime(Date followUpTime) 
    {
        this.followUpTime = followUpTime;
    }

    public Date getFollowUpTime() 
    {
        return followUpTime;
    }

    public void setFollowUpDescription(String followUpDescription) 
    {
        this.followUpDescription = followUpDescription;
    }

    public String getFollowUpDescription() 
    {
        return followUpDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("clientId", getClientId())
            .append("followUpTime", getFollowUpTime())
            .append("followUpDescription", getFollowUpDescription())
            .toString();
    }
}
