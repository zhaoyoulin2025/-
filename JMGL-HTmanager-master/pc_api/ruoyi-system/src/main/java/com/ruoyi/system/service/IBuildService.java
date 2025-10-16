package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Build;
import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.vo.SuperviserAddMeasure;
import com.ruoyi.system.domain.vo.WxBuildListVO;

/**
 * 建设管理Service接口
 *
 * @author ruoyi
 * @date 2025-06-25
 */
public interface IBuildService
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


    public List<Shop> selectShopBuildList(Shop shop);

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
     * 批量删除建设管理
     *
     * @param ids 需要删除的建设管理主键集合
     * @return 结果
     */
    public int deleteBuildByIds(Long[] ids);

    /**
     * 删除建设管理信息
     *
     * @param id 建设管理主键
     * @return 结果
     */
    public int deleteBuildById(Long id);

    int toBuild(Long[] ids);

    int addSuperviser(SuperviserAddMeasure superviserAddMeasure);

    int addFollower(SuperviserAddMeasure superviserAddMeasure);

    List<WxBuildListVO> wxBuildList(Long userId);
}
