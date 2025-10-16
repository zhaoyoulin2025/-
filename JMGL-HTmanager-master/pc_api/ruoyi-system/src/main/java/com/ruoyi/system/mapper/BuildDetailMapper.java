package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BuildDetail;

/**
 * 建设详情Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface BuildDetailMapper 
{
    /**
     * 查询建设详情
     * 
     * @param id 建设详情主键
     * @return 建设详情
     */
    public BuildDetail selectBuildDetailById(Long id);

    /**
     * 查询建设详情列表
     * 
     * @param buildDetail 建设详情
     * @return 建设详情集合
     */
    public List<BuildDetail> selectBuildDetailList(BuildDetail buildDetail);

    /**
     * 新增建设详情
     * 
     * @param buildDetail 建设详情
     * @return 结果
     */
    public int insertBuildDetail(BuildDetail buildDetail);

    /**
     * 修改建设详情
     * 
     * @param buildDetail 建设详情
     * @return 结果
     */
    public int updateBuildDetail(BuildDetail buildDetail);

    /**
     * 删除建设详情
     * 
     * @param id 建设详情主键
     * @return 结果
     */
    public int deleteBuildDetailById(Long id);

    /**
     * 批量删除建设详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuildDetailByIds(Long[] ids);
}
