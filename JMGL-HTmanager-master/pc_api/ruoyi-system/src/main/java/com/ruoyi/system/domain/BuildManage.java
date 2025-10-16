package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("build_manage")
public class BuildManage {

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    private Long userId;

    private String project;

    private String quality;

    private String question;

    private String suggestion;

    private String img;

    private String progress;

    private Long shopId;

    private String shopName;

    private String superviserName;



}
