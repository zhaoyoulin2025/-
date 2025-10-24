package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Data
public class DesignProcessRecord  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long designId;

    private String stageName;

    private Date operateTime;

    private Integer auditStatus;

    private Long auditId;

    private String statusDesc;

    private Integer submitStatus;
    private Long designTaskId;
    private Integer stage; // 设计阶段
    private ProcessStatus status; // 阶段状态
    private Long responsibleUserId; // 负责人ID
    private String responsibleUserName; // 负责人姓名
    private LocalDateTime startTime; // 阶段开始时间
    private LocalDateTime endTime; // 阶段结束时间
    private String resultDescription; // 阶段成果描述
    private String version; // 版本号
    private String externalResourceId; // 外部资源ID
    private LocalDateTime createTime; // 记录创建时间
    private LocalDateTime updateTime; // 记录更新时间（作为操作时间）

    private boolean stageWarn;

    /**
     * 设计阶段枚举
     */
    public enum DesignStage {
        REQUIREMENT_ANALYSIS("需求分析"),
        PROTOTYPE_DESIGN("原型设计"),
        UI_DESIGN("UI设计"),
        TECHNICAL_DESIGN("技术设计"),
        REVIEW("评审"),
        OPTIMIZATION("优化"),
        FINALIZATION("定稿");

        private final String desc;
        DesignStage(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }
    }

    /**
     * 流程状态枚举
     */
    public enum ProcessStatus {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        PENDING_REVIEW("待评审"),
        PASSED("已通过"),
        REJECTED("已驳回"),
        COMPLETED("已完成");

        private final String desc;
        ProcessStatus(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }
    }

    // 阶段操作方法
    public void start(LocalDateTime startTime) {
        this.startTime = startTime;
        this.status = ProcessStatus.IN_PROGRESS;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void complete(LocalDateTime endTime, String resultDescription) {
        this.endTime = endTime;
        this.resultDescription = resultDescription;
        this.status = ProcessStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }

    public void submitForReview() {
        if (this.status == ProcessStatus.IN_PROGRESS) {
            this.status = ProcessStatus.PENDING_REVIEW;
            this.updateTime = LocalDateTime.now();
        }
    }

    public void handleReviewResult(boolean isPassed) {
        if (this.status == ProcessStatus.PENDING_REVIEW) {
            this.status = isPassed ? ProcessStatus.PASSED : ProcessStatus.REJECTED;
            this.updateTime = LocalDateTime.now();
        }
    }

    // 适配排序逻辑的方法



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesignProcessRecord that = (DesignProcessRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(designTaskId, that.designTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, designTaskId);
    }


}
