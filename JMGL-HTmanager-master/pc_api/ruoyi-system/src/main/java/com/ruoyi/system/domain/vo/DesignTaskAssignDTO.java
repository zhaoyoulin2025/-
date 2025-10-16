package com.ruoyi.system.domain.vo;

import lombok.Data;

@Data
public class DesignTaskAssignDTO {
    private Long designId;
    // 测量人ID（可选）
    private Long measurePeople;
    // 平面图设计师ID（可选）
    private Long dimensional;
    // 效果图设计师ID（可选）
    private Long visualization;
    // 施工图设计师ID（可选）
    private Long construction;
}
