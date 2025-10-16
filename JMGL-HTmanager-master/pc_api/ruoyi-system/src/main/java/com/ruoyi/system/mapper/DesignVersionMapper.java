package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DesignVersion;

/**
 * 设计版本Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface DesignVersionMapper 
{
    /**
     * 查询设计版本
     * 
     * @param id 设计版本主键
     * @return 设计版本
     */
    public DesignVersion selectDesignVersionById(Long id);

    /**
     * 查询设计版本列表
     * 
     * @param designVersion 设计版本
     * @return 设计版本集合
     */
    public List<DesignVersion> selectDesignVersionList(DesignVersion designVersion);

    /**
     * 新增设计版本
     * 
     * @param designVersion 设计版本
     * @return 结果
     */
    public int insertDesignVersion(DesignVersion designVersion);

    /**
     * 修改设计版本
     * 
     * @param designVersion 设计版本
     * @return 结果
     */
    public int updateDesignVersion(DesignVersion designVersion);

    /**
     * 删除设计版本
     * 
     * @param id 设计版本主键
     * @return 结果
     */
    public int deleteDesignVersionById(Long id);

    /**
     * 批量删除设计版本
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDesignVersionByIds(Long[] ids);
}
