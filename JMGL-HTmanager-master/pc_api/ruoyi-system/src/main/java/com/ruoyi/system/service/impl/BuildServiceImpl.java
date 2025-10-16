package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.BuildPriodEnum;
import com.ruoyi.system.domain.Design;
import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.vo.SuperviserAddMeasure;
import com.ruoyi.system.domain.vo.WxBuildListVO;
import com.ruoyi.system.mapper.DesignMapper;
import com.ruoyi.system.mapper.ShopMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BuildMapper;
import com.ruoyi.system.domain.Build;
import com.ruoyi.system.service.IBuildService;

/**
 * 建设管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-25
 */
@Service
public class BuildServiceImpl implements IBuildService
{
    @Autowired
    private BuildMapper buildMapper;

    @Autowired
    private DesignMapper designMapper;

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 查询建设管理
     *
     * @param id 建设管理主键
     * @return 建设管理
     */
    @Override
    public Build selectBuildById(Long id)
    {
        return buildMapper.selectBuildById(id);
    }

    /**
     * 查询建设管理列表
     *
     * @param build 建设管理
     * @return 建设管理
     */
    @Override
    public List<Build> selectBuildList(Build build)
    {
        return buildMapper.selectBuildList(build);
    }

    @Override
    public List<Shop> selectShopBuildList(Shop shop) {
        shop.setShopStatus(5);



        return shopMapper.selectShopList(shop);
    }

    /**
     * 新增建设管理
     *
     * @param build 建设管理
     * @return 结果
     */
    @Override
    public int insertBuild(Build build)
    {
        return buildMapper.insertBuild(build);
    }

    /**
     * 修改建设管理
     *
     * @param build 建设管理
     * @return 结果
     */
    @Override
    public int updateBuild(Build build)
    {
        build.setUpTime(new Date());
        return buildMapper.updateBuild(build);
    }

    /**
     * 批量删除建设管理
     *
     * @param ids 需要删除的建设管理主键
     * @return 结果
     */
    @Override
    public int deleteBuildByIds(Long[] ids)
    {
        return buildMapper.deleteBuildByIds(ids);
    }

    /**
     * 删除建设管理信息
     *
     * @param id 建设管理主键
     * @return 结果
     */
    @Override
    public int deleteBuildById(Long id)
    {
        return buildMapper.deleteBuildById(id);
    }

    @Override
    public int toBuild(Long[] ids) {
        for (Long id : ids) {
            Design design = designMapper.selectDesignById(id);
            Build seaBuild = new Build();
            seaBuild.setShopId(design.getShopId());
            List<Build> builds = buildMapper.selectBuildList(seaBuild);
            if(CollectionUtils.isEmpty(builds)){
                for (BuildPriodEnum value : BuildPriodEnum.values()) {
                    Build build = new Build();
                    build.setClientId(design.getClientId());
                    build.setShopId(design.getShopId());
                    build.setSuperviseStatus(0); //0 未开始 1，营建中 2 已完成
                    build.setPeriodId((long) value.getPeriodId());
                    build.setCreateTime(new Date());
                    buildMapper.insertBuild(build);
                }
            }
            Shop shop = shopMapper.selectShopById(design.getShopId());
            shop.setRunStatus("5");
            shopMapper.updateShop(shop);
            design.setDesignStatus(1);
            designMapper.updateDesign(design);
        }
        return ids.length;
    }

    @Override
    public int addSuperviser(SuperviserAddMeasure superviserAddMeasure) {
        List<Long> measureIds = superviserAddMeasure.getMeasureIds();

        for (Long measureId : measureIds) {
            Build build = new Build();
            build.setId(measureId);
            build.setSuperviserId(superviserAddMeasure.getSuperviserId());
            buildMapper.updateBuild(build);

        }

        return measureIds.size();
    }

    @Override
    public int addFollower(SuperviserAddMeasure superviserAddMeasure) {
        List<Long> measureIds = superviserAddMeasure.getMeasureIds();
        for (Long measureId : measureIds) {
            Build build = new Build();
            build.setId(measureId);
            build.setFollowerId(superviserAddMeasure.getFollowerId());
            buildMapper.updateBuild(build);
        }

        return measureIds.size();

    }

    @Override
    public List<WxBuildListVO> wxBuildList(Long userId) {
        return buildMapper.wxBuildList(userId);
    }
}
