package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("shop_sign")
public class ShopSign {

    @TableId(type= IdType.AUTO)
    private Long id;

    private Long shopId;

    private Long clientId;

    private Long saleId;

    private String money;

    private String year;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date saleTime;

    @TableField(exist = false)
    private String clientName;

    @TableField(exist = false)
    private String shopName;




}
