package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.Build;
import com.ruoyi.system.domain.vo.WxBuildListVO;

/**
 * 建设管理Mapper接口
 *
 * @author ruoyi
 * @date 2025-06-25
 */
public interface BuildMapper
{
    /**
     * 查询建设管理
     *
     * @param id 建设管理主键
     * @return 建设管理
     */
    public Build selectBuildById(Long id);

    /**
     * 查询建设管理列表
     *
     * @param build 建设管理
     * @return 建设管理集合
     */
    public List<Build> selectBuildList(Build build);

    /**
     * 新增建设管理
     *
     * @param build 建设管理
     * @return 结果
     */
    public int insertBuild(Build build);

    /**
     * 修改建设管理
     *
     * @param build 建设管理
     * @return 结果
     */
    public int updateBuild(Build build);

    /**
     * 删除建设管理
     *
     * @param id 建设管理主键
     * @return 结果
     */
    public int deleteBuildById(Long id);

    /**
     * 批量删除建设管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuildByIds(Long[] ids);

    List<WxBuildListVO> wxBuildList(Long userId);

    public Map<String,Long> getCurrentMouthBuildCount(Long userId);

    public Map<String,Long> getCurrentMouthBuildDownCount(Long userId);
}
