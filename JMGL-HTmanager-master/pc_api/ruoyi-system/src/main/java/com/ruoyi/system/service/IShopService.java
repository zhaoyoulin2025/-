package com.ruoyi.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.domain.vo.ClientOwnShopVO;
import com.ruoyi.system.domain.vo.ShopStateVO;
import com.ruoyi.system.domain.vo.WxShopSignListVO;

/**
 * 标准作业流程/店铺信息Service接口
 *
 * @author ruoyi
 * @date 2025-06-15
 */
public interface IShopService extends IService<Shop>
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
     * 批量删除标准作业流程/店铺信息
     *
     * @param ids 需要删除的标准作业流程/店铺信息主键集合
     * @return 结果
     */
    public int deleteShopByIds(Long[] ids);



    int removeFollower(Long[] ids);

    int audit(Long[] ids);

    int auditAcc(Long[] ids);
    int toDesgin(Long[] ids);

    int addFoller(ClientAddFollwer clientAddFollwer);
    List<ClientOwnShopVO> clientOwnShopList(Long clientId);

    List<WxShopSignListVO> wxFollowShopList(Long followId);

    List<WxShopSignListVO> wxMyShopList(Long clientId);

}
