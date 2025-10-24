package com.ruoyi.system.domain.vo;


import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 设计流程审核操作的请求参数DTO
 * 用于接收前端传递的审核记录ID、审核结果和意见
 */
@Data
public class AuditDTO {

    /**
     * 待审核记录的ID（对应DesignFlowDetail表的主键id）
     * 必须传递，用于定位具体要审核的记录
     */

    private Long recordId;

    /**
     * 审核结果：1=通过，2=驳回
     * 必须传递，明确审核操作的结果
     */
    @NotNull(message = "审核结果不能为空")
    private Integer auditResult;


    private String auditOpinion;
}
