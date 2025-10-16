package com.ruoyi.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邀请记录对象 invite
 *
 * @author ruoyi
 * @date 2025-06-04
 */
@Data
public class Invite
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String timeMoment;

    /** 客户ID（原外键） */
    @TableField("clientId")
    private Long clientId;

    /** 导购ID（原外键） */
    @TableField("guideId")
    private Long guideId;

    /** 邀请时间 */
    @TableField("invitationTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "邀请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date invitationTime;

    /** 创建时间 */
    @TableField("sTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sTime;


    @TableField("confirmTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /** 邀请描述 */
    @Excel(name = "邀请描述")
    @TableField("invitationDesc")
    private String invitationDesc;

    @Excel(name = "邀约地点")
    @TableField("invitePlace")
    private String invitePlace;

    /** 邀约状态 */
    @TableField("inviteStatus")
    private Long inviteStatus;

    @TableField("inviteStatus")
    @Excel(name = "邀约状态")
    private String inviteStatusName;


    public String getInviteStatusName() {
        return inviteStatusName;
    }

    public void setInviteStatusName(String inviteStatusName) {
        this.inviteStatusName = inviteStatusName;
    }

    @TableField("clientName")
    @Excel(name = "客户")
    private String clientName;

    @TableField("guideName")
    @Excel(name = "导购人")
    private String guideName;


    @TableField(exist = false)
    private String dateStr;

    @TableField(exist = false)
    private String phone;

    public String getTimeMoment() {
        return timeMoment;
    }

    public void setTimeMoment(String timeMoment) {
        this.timeMoment = timeMoment;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

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

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    public Long getGuideId()
    {
        return guideId;
    }

    public void setInvitationTime(Date invitationTime)
    {
        this.invitationTime = invitationTime;
    }

    public Date getInvitationTime()
    {
        return invitationTime;
    }

    public void setsTime(Date sTime)
    {
        this.sTime = sTime;
    }

    public Date getsTime()
    {
        return sTime;
    }

    public void setInvitationDesc(String invitationDesc)
    {
        this.invitationDesc = invitationDesc;
    }

    public String getInvitationDesc()
    {
        return invitationDesc;
    }

    public void setInviteStatus(Long inviteStatus)
    {
        this.inviteStatus = inviteStatus;
    }

    public Long getInviteStatus()
    {
        return inviteStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("guideId", getGuideId())
            .append("invitationTime", getInvitationTime())
            .append("sTime", getsTime())
            .append("invitationDesc", getInvitationDesc())
            .append("inviteStatus", getInviteStatus())
            .toString();
    }
}
