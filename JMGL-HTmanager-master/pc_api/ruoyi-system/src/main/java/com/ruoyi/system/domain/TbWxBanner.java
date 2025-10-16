package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * wxBanaer对象 tb_wx_banaer
 * 
 * @author ruoyi
 * @date 2025-07-13
 */
@Data
@TableName("tb_wx_banner")
public class TbWxBanner
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 图片地址 */
    private String url;

    /** 0 =下架 1= 上架 */
    private Integer status;

    private Date createTime;


}
