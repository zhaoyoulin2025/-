package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusinessDistrict;

/**
 * 商圈信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
public interface BusinessDistrictMapper 
{
    /**
     * 查询商圈信息
     * 
     * @param id 商圈信息主键
     * @return 商圈信息
     */
    public BusinessDistrict selectBusinessDistrictById(Long id);

    /**
     * 查询商圈信息列表
     * 
     * @param businessDistrict 商圈信息
     * @return 商圈信息集合
     */
    public List<BusinessDistrict> selectBusinessDistrictList(BusinessDistrict businessDistrict);

    /**
     * 新增商圈信息
     * 
     * @param businessDistrict 商圈信息
     * @return 结果
     */
    public int insertBusinessDistrict(BusinessDistrict businessDistrict);

    /**
     * 修改商圈信息
     * 
     * @param businessDistrict 商圈信息
     * @return 结果
     */
    public int updateBusinessDistrict(BusinessDistrict businessDistrict);

    /**
     * 删除商圈信息
     * 
     * @param id 商圈信息主键
     * @return 结果
     */
    public int deleteBusinessDistrictById(Long id);

    /**
     * 批量删除商圈信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusinessDistrictByIds(Long[] ids);

    int queryUserShop(Long clientId);

    List<BusinessDistrict> getDistrictRegionList();
}
