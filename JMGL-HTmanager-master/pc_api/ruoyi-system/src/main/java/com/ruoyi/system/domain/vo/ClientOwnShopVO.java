package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientOwnShopVO {

    private Long shopId;

    private String shopName;

    private String clientName;

    private String phone;

    private String shopAddress;

    private String shopSize;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date saleTime;

    private Boolean isExpanded = false;

    private List<ShopStateVO> stages;




}
