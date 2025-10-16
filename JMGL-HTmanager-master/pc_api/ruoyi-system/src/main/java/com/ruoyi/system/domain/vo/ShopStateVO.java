package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShopStateVO {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String status;

    private String statusText;

    private Boolean showDetail = false;

    private Boolean isDetailExpanded =false;

    private String detailTitle;

    private String detailStatus;

    private String constructionTime;

    private String constructor;

    private String constructorPhone;

    private List<String> photos;






}
