package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DesignVersionMapper;
import com.ruoyi.system.domain.DesignVersion;
import com.ruoyi.system.service.IDesignVersionService;

/**
 * 设计版本Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class DesignVersionServiceImpl implements IDesignVersionService 
{
    @Autowired
    private DesignVersionMapper designVersionMapper;

    /**
     * 查询设计版本
     * 
     * @param id 设计版本主键
     * @return 设计版本
     */
    @Override
    public DesignVersion selectDesignVersionById(Long id)
    {
        return designVersionMapper.selectDesignVersionById(id);
    }

    /**
     * 查询设计版本列表
     * 
     * @param designVersion 设计版本
     * @return 设计版本
     */
    @Override
    public List<DesignVersion> selectDesignVersionList(DesignVersion designVersion)
    {
        return designVersionMapper.selectDesignVersionList(designVersion);
    }

    /**
     * 新增设计版本
     * 
     * @param designVersion 设计版本
     * @return 结果
     */
    @Override
    public int insertDesignVersion(DesignVersion designVersion)
    {
        return designVersionMapper.insertDesignVersion(designVersion);
    }

    /**
     * 修改设计版本
     * 
     * @param designVersion 设计版本
     * @return 结果
     */
    @Override
    public int updateDesignVersion(DesignVersion designVersion)
    {
        return designVersionMapper.updateDesignVersion(designVersion);
    }

    /**
     * 批量删除设计版本
     * 
     * @param ids 需要删除的设计版本主键
     * @return 结果
     */
    @Override
    public int deleteDesignVersionByIds(Long[] ids)
    {
        return designVersionMapper.deleteDesignVersionByIds(ids);
    }

    /**
     * 删除设计版本信息
     * 
     * @param id 设计版本主键
     * @return 结果
     */
    @Override
    public int deleteDesignVersionById(Long id)
    {
        return designVersionMapper.deleteDesignVersionById(id);
    }
}
