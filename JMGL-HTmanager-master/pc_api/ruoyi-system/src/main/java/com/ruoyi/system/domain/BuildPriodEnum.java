package com.ruoyi.system.domain;

public enum BuildPriodEnum {

    PHASE_ONE(1, "第一阶段"),

    PHASE_TWO(2, "第二阶段"),
    PHASE_THREE(3, "第三阶段"),
    PHASE_FOUR(4, "第四阶段"),
    PHASE_FIVE(5, "第五阶段");

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    private int periodId;
    private String periodName;

    BuildPriodEnum(int periodId, String periodName) {
        this.periodId = periodId;
        this.periodName = periodName;
    }



}
