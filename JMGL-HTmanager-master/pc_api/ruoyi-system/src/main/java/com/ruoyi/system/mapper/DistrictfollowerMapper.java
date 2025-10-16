package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Districtfollower;

/**
 * 商圈跟进人员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
public interface DistrictfollowerMapper 
{
    /**
     * 查询商圈跟进人员
     * 
     * @param id 商圈跟进人员主键
     * @return 商圈跟进人员
     */
    public Districtfollower selectDistrictfollowerById(Long id);

    /**
     * 查询商圈跟进人员列表
     * 
     * @param districtfollower 商圈跟进人员
     * @return 商圈跟进人员集合
     */
    public List<Districtfollower> selectDistrictfollowerList(Districtfollower districtfollower);

    /**
     * 新增商圈跟进人员
     * 
     * @param districtfollower 商圈跟进人员
     * @return 结果
     */
    public int insertDistrictfollower(Districtfollower districtfollower);

    /**
     * 修改商圈跟进人员
     * 
     * @param districtfollower 商圈跟进人员
     * @return 结果
     */
    public int updateDistrictfollower(Districtfollower districtfollower);

    /**
     * 删除商圈跟进人员
     * 
     * @param id 商圈跟进人员主键
     * @return 结果
     */
    public int deleteDistrictfollowerById(Long id);

    /**
     * 批量删除商圈跟进人员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDistrictfollowerByIds(Long[] ids);
}
