package com.ruoyi.system.domain.vo;

import java.util.List;

public class SuperviserAddMeasure {

    private Long superviserId ;
    private List<Long> measureIds;
    private Long followerId;

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getSuperviserId() {
        return superviserId;
    }

    public void setSuperviserId(Long superviserId) {
        this.superviserId = superviserId;
    }

    public List<Long> getMeasureIds() {
        return measureIds;
    }

    public void setMeasureIds(List<Long> measureIds) {
        this.measureIds = measureIds;
    }
}
