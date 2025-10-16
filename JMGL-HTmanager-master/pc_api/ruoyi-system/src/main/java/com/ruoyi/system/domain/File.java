package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文件存储对象 file
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class File extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 文件类型（如设计图、合同等） */
    @Excel(name = "文件类型", readConverterExp = "如=设计图、合同等")
    private String fileItem;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long itemId;

    /** 是否使用（1=启用，0=禁用） */
    @Excel(name = "是否使用", readConverterExp = "1==启用，0=禁用")
    private Integer isUse;

    /** 文件描述 */
    @Excel(name = "文件描述")
    private String desc;

    /** 文件链接 */
    @Excel(name = "文件链接")
    private String url;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFileItem(String fileItem) 
    {
        this.fileItem = fileItem;
    }

    public String getFileItem() 
    {
        return fileItem;
    }

    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }

    public void setIsUse(Integer isUse) 
    {
        this.isUse = isUse;
    }

    public Integer getIsUse() 
    {
        return isUse;
    }

    public void setDesc(String desc) 
    {
        this.desc = desc;
    }

    public String getDesc() 
    {
        return desc;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setsTime(Date sTime) 
    {
        this.sTime = sTime;
    }

    public Date getsTime() 
    {
        return sTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fileItem", getFileItem())
            .append("itemId", getItemId())
            .append("isUse", getIsUse())
            .append("desc", getDesc())
            .append("url", getUrl())
            .append("sTime", getsTime())
            .toString();
    }
}
