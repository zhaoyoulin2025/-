package com.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@TableName("tb_question")
public class TbQuestion {

    private Long id;

    @NotBlank(message = "预算不能为空")
    private String budget;

    @NotBlank(message = "地区不能为空")
    private String area;

    private Long createBy;

    private Date createTime;

}
