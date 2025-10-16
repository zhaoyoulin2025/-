package com.ruoyi.system.domain;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class DesignFlowDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 关联设计ID */
    private Long designId;

    /** 操作设计师ID */
    private Long designerId;

    /** 流程阶段（1=测量，2=平面，3=效果，4=施工） */
    private Integer flowStage;

    /** 上传文件路径（多文件逗号分隔） */
    private String fileUrl;

    /** 上传文件名称（多文件逗号分隔） */
    private String fileName;

    /** 提交状态（0=未提交，1=已提交） */
    private Integer submitStatus;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 审核状态（0=待审核，1=通过，2=驳回） */
    private Integer auditStatus;

    /** 审核意见 */
    private String auditFeedback;

    /** 审核人ID */
    private Long auditorId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    // 非数据库字段：用于前端显示（可选）
    private String designerName; // 设计师姓名
    private String auditorName;  // 审核人姓名
    private Long stageStayTime;  // 阶段停留时间（单位：小时，用于预警）
}
