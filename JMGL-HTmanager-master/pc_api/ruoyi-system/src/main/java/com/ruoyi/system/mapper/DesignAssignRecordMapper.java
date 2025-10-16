package com.ruoyi.system.mapper;



import java.util.List;
import com.ruoyi.system.domain.DesignAssignRecord;
import org.apache.ibatis.annotations.Param;


/**
 * 设计任务分配记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-21
 */
public interface DesignAssignRecordMapper
{
    /**
     * 查询设计任务分配记录
     *
     * @param id 设计任务分配记录主键
     * @return 设计任务分配记录
     */
    public DesignAssignRecord selectDesignAssignRecordById(Long id);

    /**
     * 查询设计任务分配记录列表
     *
     * @param designAssignRecord 设计任务分配记录
     * @return 设计任务分配记录集合
     */
    public List<DesignAssignRecord> selectDesignAssignRecordList(DesignAssignRecord designAssignRecord);

    /**
     * 根据设计ID查询所有分配记录
     *
     * @param designId 设计主表ID
     * @return 设计任务分配记录集合
     */
    public List<DesignAssignRecord> selectByDesignId(Long designId);

    /**
     * 根据设计ID和阶段查询分配记录
     *
     * @param designAssignRecord 包含designId和designStage的查询条件
     * @return 设计任务分配记录
     */
    public DesignAssignRecord selectByDesignIdAndStage(DesignAssignRecord designAssignRecord);

    /**
     * 新增设计任务分配记录
     *
     * @param designAssignRecord 设计任务分配记录
     * @return 结果
     */
    public int insertDesignAssignRecord(DesignAssignRecord designAssignRecord);

    /**
     * 批量新增设计任务分配记录
     *
     * @param list 设计任务分配记录列表
     * @return 结果
     */
    public int batchInsertDesignAssignRecord(List<DesignAssignRecord> list);

    /**
     * 修改设计任务分配记录
     *
     * @param designAssignRecord 设计任务分配记录
     * @return 结果
     */
    public int updateDesignAssignRecord(DesignAssignRecord designAssignRecord);

    /**
     * 删除设计任务分配记录
     *
     * @param id 设计任务分配记录主键
     * @return 结果
     */
    public int deleteDesignAssignRecordById(Long id);

    /**
     * 批量删除设计任务分配记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDesignAssignRecordByIds(Long[] ids);

    List<Integer> getResponsibleStages(@Param("designId") Long id, @Param("designerId") Long currentLoginUserId);
}

