package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BuildManage;

/**
 * 监理详情Service接口
 * 
 * @author ruoyi
 * @date 2025-08-16
 */
public interface IIBuildManageService
{
    /**
     * 查询监理详情
     * 
     * @param id 监理详情主键
     * @return 监理详情
     */
    public BuildManage selectBuildManageById(Integer id);

    /**
     * 查询监理详情列表
     * 
     * @param buildManage 监理详情
     * @return 监理详情集合
     */
    public List<BuildManage> selectBuildManageList(BuildManage buildManage);



    public List<BuildManage> selectBuildManageListByshop(BuildManage buildManage);

    /**
     * 新增监理详情
     * 
     * @param buildManage 监理详情
     * @return 结果
     */
    public int insertBuildManage(BuildManage buildManage);

    /**
     * 修改监理详情
     * 
     * @param buildManage 监理详情
     * @return 结果
     */
    public int updateBuildManage(BuildManage buildManage);

    /**
     * 批量删除监理详情
     * 
     * @param ids 需要删除的监理详情主键集合
     * @return 结果
     */
    public int deleteBuildManageByIds(Integer[] ids);

    /**
     * 删除监理详情信息
     * 
     * @param id 监理详情主键
     * @return 结果
     */
    public int deleteBuildManageById(Integer id);
}
