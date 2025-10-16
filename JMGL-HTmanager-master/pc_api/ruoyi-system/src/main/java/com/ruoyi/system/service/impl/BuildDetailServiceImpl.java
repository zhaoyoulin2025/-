package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BuildDetailMapper;
import com.ruoyi.system.domain.BuildDetail;
import com.ruoyi.system.service.IBuildDetailService;

/**
 * 建设详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class BuildDetailServiceImpl implements IBuildDetailService 
{
    @Autowired
    private BuildDetailMapper buildDetailMapper;

    /**
     * 查询建设详情
     * 
     * @param id 建设详情主键
     * @return 建设详情
     */
    @Override
    public BuildDetail selectBuildDetailById(Long id)
    {
        return buildDetailMapper.selectBuildDetailById(id);
    }

    /**
     * 查询建设详情列表
     * 
     * @param buildDetail 建设详情
     * @return 建设详情
     */
    @Override
    public List<BuildDetail> selectBuildDetailList(BuildDetail buildDetail)
    {
        return buildDetailMapper.selectBuildDetailList(buildDetail);
    }

    /**
     * 新增建设详情
     * 
     * @param buildDetail 建设详情
     * @return 结果
     */
    @Override
    public int insertBuildDetail(BuildDetail buildDetail)
    {
        return buildDetailMapper.insertBuildDetail(buildDetail);
    }

    /**
     * 修改建设详情
     * 
     * @param buildDetail 建设详情
     * @return 结果
     */
    @Override
    public int updateBuildDetail(BuildDetail buildDetail)
    {
        return buildDetailMapper.updateBuildDetail(buildDetail);
    }

    /**
     * 批量删除建设详情
     * 
     * @param ids 需要删除的建设详情主键
     * @return 结果
     */
    @Override
    public int deleteBuildDetailByIds(Long[] ids)
    {
        return buildDetailMapper.deleteBuildDetailByIds(ids);
    }

    /**
     * 删除建设详情信息
     * 
     * @param id 建设详情主键
     * @return 结果
     */
    @Override
    public int deleteBuildDetailById(Long id)
    {
        return buildDetailMapper.deleteBuildDetailById(id);
    }
}
