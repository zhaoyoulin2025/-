package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Shop;

/**
 * 标准作业流程/店铺信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-06-04
 */
public interface ShopMapper extends BaseMapper<Shop>
{
    /**
     * 查询标准作业流程/店铺信息
     *
     * @param id 标准作业流程/店铺信息主键
     * @return 标准作业流程/店铺信息
     */
    public Shop selectShopById(Long id);

    /**
     * 查询标准作业流程/店铺信息列表
     *
     * @param shop 标准作业流程/店铺信息
     * @return 标准作业流程/店铺信息集合
     */
    public List<Shop> selectShopList(Shop shop);

    /**
     * 新增标准作业流程/店铺信息
     *
     * @param shop 标准作业流程/店铺信息
     * @return 结果
     */
    public int insertShop(Shop shop);

    /**
     * 修改标准作业流程/店铺信息
     *
     * @param shop 标准作业流程/店铺信息
     * @return 结果
     */
    public int updateShop(Shop shop);

    /**
     * 删除标准作业流程/店铺信息
     *
     * @param id 标准作业流程/店铺信息主键
     * @return 结果
     */
    public int deleteShopById(Long id);

    /**
     * 批量删除标准作业流程/店铺信息
     *
     *
     * @return 结果
     */
    public int updateShopNull(Long id);

    void audit(Long id);

    void auditAcc(Long id);


    public Map<String,Long> getCurrentMouthShopCount(Long userId);

    public Map<String,Long> getCurrentMouthShopWaitCount(Long userId);
}
