package com.ruoyi.system.mapper;



import com.ruoyi.system.domain.DesignFlowDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设计流程详情数据访问层
 * 对应 Mapper 文件：DesignFlowDetailMapper.xml
 */
public interface DesignFlowDetailMapper {

    /**
     * 新增设计流程详情记录
     * @param designFlowDetail 设计流程详情实体
     * @return 影响行数
     */
    int insertDesignFlowDetail(DesignFlowDetail designFlowDetail);

    /**
     * 更新设计流程详情记录
     * @param designFlowDetail 设计流程详情实体（需包含id）
     * @return 影响行数
     */
    int updateDesignFlowDetail(DesignFlowDetail designFlowDetail);

    /**
     * 根据设计ID和流程阶段查询详情
     * @param designId 设计ID
     * @param flowStage 流程阶段（1-测量阶段，2-平面设计，3-效果设计，4-施工设计）
     * @return 设计流程详情实体
     */
    DesignFlowDetail selectByDesignIdAndStage(
            @Param("designId") Long designId,
            @Param("flowStage") Integer flowStage
    );
    List<DesignFlowDetail> selectByDesignIdAndStageList(
            @Param("designId") Long designId
    );

    DesignFlowDetail select(
            @Param("id") Long id
    );
}

