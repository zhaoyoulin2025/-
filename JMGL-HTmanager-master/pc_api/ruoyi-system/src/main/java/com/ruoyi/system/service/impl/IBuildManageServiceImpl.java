package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.service.IIBuildManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.IBuildManageMapper;
import com.ruoyi.system.domain.BuildManage;

/**
 * 监理详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-16
 */
@Service
public class IBuildManageServiceImpl implements IIBuildManageService
{
    @Autowired
    private IBuildManageMapper buildManageMapper;

    /**
     * 查询监理详情
     * 
     * @param id 监理详情主键
     * @return 监理详情
     */
    @Override
    public BuildManage selectBuildManageById(Integer id)
    {
        return buildManageMapper.selectBuildManageById(id);
    }

    /**
     * 查询监理详情列表
     * 
     * @param buildManage 监理详情
     * @return 监理详情
     */
    @Override
    public List<BuildManage> selectBuildManageList(BuildManage buildManage)
    {
        return buildManageMapper.selectBuildManageList(buildManage);
    }

    @Override
    public List<BuildManage> selectBuildManageListByshop(BuildManage buildManage)
    {
        return buildManageMapper.selectBuildManageListByshop(buildManage);
    }

    /**
     * 新增监理详情
     * 
     * @param buildManage 监理详情
     * @return 结果
     */
    @Override
    public int insertBuildManage(BuildManage buildManage)
    {
        return buildManageMapper.insertBuildManage(buildManage);
    }

    /**
     * 修改监理详情
     * 
     * @param buildManage 监理详情
     * @return 结果
     */
    @Override
    public int updateBuildManage(BuildManage buildManage)
    {
        return buildManageMapper.updateBuildManage(buildManage);
    }

    /**
     * 批量删除监理详情
     * 
     * @param ids 需要删除的监理详情主键
     * @return 结果
     */
    @Override
    public int deleteBuildManageByIds(Integer[] ids)
    {
        return buildManageMapper.deleteBuildManageByIds(ids);
    }

    /**
     * 删除监理详情信息
     * 
     * @param id 监理详情主键
     * @return 结果
     */
    @Override
    public int deleteBuildManageById(Integer id)
    {
        return buildManageMapper.deleteBuildManageById(id);
    }
}
