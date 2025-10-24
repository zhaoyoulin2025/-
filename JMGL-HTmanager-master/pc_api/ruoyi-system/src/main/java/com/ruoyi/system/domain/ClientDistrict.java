package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("client_district")
public class ClientDistrict {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long businessDistrictId;

    @TableField(exist = false)
    private List<Long> userIds;

    @TableField(exist = false)
    private List<Long> districtIds;

}
