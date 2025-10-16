package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图纸信息对象 drawings
 *
 * @author ruoyi
 * @date 2025-06-18
 */
public class Drawings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图纸主表ID */
    private Long id;

    /** 设计id */
    @Excel(name = "设计id")
    private Long designId;

    /** 商铺编码 */
    @Excel(name = "商铺编码")
    private Long shopId;

    /** 图纸类型，0封面，1目录，2施工设计说明，3室内装修材料，4材料表，5平面图，6立面图，7大图样 */
    @Excel(name = "图纸类型，0封面，1目录，2施工设计说明，3室内装修材料，4材料表，5平面图，6立面图，7大图样")
    private Long drawingType;

    /** 图幅 */
    @Excel(name = "图幅")
    private String itemSize;

    /** 图纸编号（如 P-01、E-02 ） */
    @Excel(name = "图纸编号", readConverterExp = "如=,P=-01、E-02")
    private String drawingNo;

    /** 图纸名称（如 平面布置图 ） */
    @Excel(name = "图纸名称", readConverterExp = "如=,平=面布置图")
    private String drawingName;

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

    /** 关联最新版本ID（关联 drawing_versions.id ） */
    @Excel(name = "关联最新版本ID", readConverterExp = "关=联,d=rawing_versions.id")
    private Long versionId;

    /** 是否有效（1-有效，0-作废） */
    @Excel(name = "是否有效", readConverterExp = "1=-有效，0-作废")
    private Integer isActive;


    private String shopName;


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private String designName;
    private String clientName;

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

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

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setDrawingType(Long drawingType)
    {
        this.drawingType = drawingType;
    }

    public Long getDrawingType()
    {
        return drawingType;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public void setDrawingNo(String drawingNo)
    {
        this.drawingNo = drawingNo;
    }

    public String getDrawingNo()
    {
        return drawingNo;
    }

    public void setDrawingName(String drawingName)
    {
        this.drawingName = drawingName;
    }

    public String getDrawingName()
    {
        return drawingName;
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

    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getVersionId()
    {
        return versionId;
    }

    public void setIsActive(Integer isActive)
    {
        this.isActive = isActive;
    }

    public Integer getIsActive()
    {
        return isActive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("designId", getDesignId())
            .append("shopId", getShopId())
            .append("drawingType", getDrawingType())
            .append("pageSize", getItemSize())
            .append("drawingNo", getDrawingNo())
            .append("drawingName", getDrawingName())
            .append("fileUrl", getFileUrl())
            .append("creator", getCreator())
            .append("createTime", getCreateTime())
            .append("upTime", getUpTime())
            .append("versionId", getVersionId())
            .append("isActive", getIsActive())
            .toString();
    }
}
