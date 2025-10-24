package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.domain.vo.DesignProcessRecord;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设计管理对象 design
 *
 * @author ruoyi
 * @date 2025-06-18
 */
@Data
public class Design extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private boolean auditor;
    private boolean del;
    private Integer current_design_stage;




    private Long stageStayTime;

    private String currentStageTag;

    private String current_stage_desc;

    private Long foller;
    private String current_designer_name;

    private Long current_designer_id;
    private String clientName;

    private String desginName;

    private String shopName;

    private List<Map<String,String>> measureFiles;
    private List<Map<String,String>> floorFiles;
    private List<Map<String,String>> effectFiles;
    private List<Map<String,String>> constructionFiles;

    private List<DesignProcessRecord> processRecords;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDesginName() {
        return desginName;
    }

    public void setDesginName(String desginName) {
        this.desginName = desginName;
    }

    /** 主键ID */
    private Long id;

    /** 店铺ID（原外键，引用sop表id） */
    @Excel(name = "店铺ID", readConverterExp = "原=外键，引用sop表id")
    private Long shopId;

    /** 客户ID（原外键） */
    @Excel(name = "客户ID", readConverterExp = "原=外键")
    private Long clientId;

    /** 设计状态（0=待设计，1=设计中，2=已完成） */
    @Excel(name = "设计状态", readConverterExp = "0==待设计，1=设计中，2=已完成")
    private Integer designStatus;

    /** 设计类型（目前不知道什么意思） */
    @Excel(name = "设计类型", readConverterExp = "目=前不知道什么意思")
    private Integer designType;

    /** 设计师姓名 */
    @Excel(name = "设计师姓名")
    private Long designerId;

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

    public void setDesignStatus(Integer designStatus)
    {
        this.designStatus = designStatus;
    }

    public Integer getDesignStatus()
    {
        return designStatus;
    }

    public void setDesignType(Integer designType)
    {
        this.designType = designType;
    }

    public Integer getDesignType()
    {
        return designType;
    }

    public void setDesignerId(Long designerId)
    {
        this.designerId = designerId;
    }

    public Long getDesignerId()
    {
        return designerId;
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
            .append("shopId", getShopId())
            .append("clientId", getClientId())
            .append("designStatus", getDesignStatus())
            .append("designType", getDesignType())
            .append("designerId", getDesignerId())
            .append("sTime", getsTime())
            .append("upTime", getUpTime())
            .toString();
    }
}
