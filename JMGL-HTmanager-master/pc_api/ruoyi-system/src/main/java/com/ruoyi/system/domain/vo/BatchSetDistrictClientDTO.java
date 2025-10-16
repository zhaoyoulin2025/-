package com.ruoyi.system.domain.vo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class BatchSetDistrictClientDTO {

    private Long customerId;

    /**
     * 商圈ID列表（需要批量设置的商圈主键集合）
     * 必须不为空且至少包含一个元素，使用@NotEmpty校验
     */
    @NotEmpty(message = "商圈ID列表不能为空")
    private List<Long> districtIds;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Long> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<Long> districtIds) {
        this.districtIds = districtIds;
    }
}
