package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class WxDesignListVO {

    private Long id;

    private String designStatus;

    private String designType;

    private String designIdList;

    private String shopName;

    private String shopAddress;

    private String clientName;

    private String phone;

    private Boolean expanded =false;

    private List<HashMap<String,String>> designPhases;


}
