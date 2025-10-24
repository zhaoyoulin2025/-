package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusinessDistrict;
import com.ruoyi.system.domain.vo.BatchSetDistrictClientDTO;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.domain.vo.DistrctStatus;

/**
 * 商圈信息Service接口
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
public interface IBusinessDistrictService 
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

    void wxInsertDistrict(BusinessDistrict businessDistrict);

    /**
     * 修改商圈信息
     * 
     * @param businessDistrict 商圈信息
     * @return 结果
     */
    public int updateBusinessDistrict(BusinessDistrict businessDistrict);

    /**
     * 批量删除商圈信息
     * 
     * @param ids 需要删除的商圈信息主键集合
     * @return 结果
     */
    public int deleteBusinessDistrictByIds(Long[] ids);

    /**
     * 删除商圈信息信息
     * 
     * @param id 商圈信息主键
     * @return 结果
     */
    public int deleteBusinessDistrictById(Long id);

    int addFoller(ClientAddFollwer clientAddFollwer);

    int audit(DistrctStatus distrctStatus);

    int batchSetClient(BatchSetDistrictClientDTO batchSetDistrictClientDTO);

    int queryUserShop(Long clientId);

    List<BusinessDistrict> getDistrictRegionList();
}
