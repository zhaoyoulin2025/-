package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DistrictfollowerMapper;
import com.ruoyi.system.domain.Districtfollower;
import com.ruoyi.system.service.IDistrictfollowerService;

/**
 * 商圈跟进人员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
@Service
public class DistrictfollowerServiceImpl implements IDistrictfollowerService 
{
    @Autowired
    private DistrictfollowerMapper districtfollowerMapper;

    /**
     * 查询商圈跟进人员
     * 
     * @param id 商圈跟进人员主键
     * @return 商圈跟进人员
     */
    @Override
    public Districtfollower selectDistrictfollowerById(Long id)
    {
        return districtfollowerMapper.selectDistrictfollowerById(id);
    }

    /**
     * 查询商圈跟进人员列表
     * 
     * @param districtfollower 商圈跟进人员
     * @return 商圈跟进人员
     */
    @Override
    public List<Districtfollower> selectDistrictfollowerList(Districtfollower districtfollower)
    {
        return districtfollowerMapper.selectDistrictfollowerList(districtfollower);
    }

    /**
     * 新增商圈跟进人员
     * 
     * @param districtfollower 商圈跟进人员
     * @return 结果
     */
    @Override
    public int insertDistrictfollower(Districtfollower districtfollower)
    {
        return districtfollowerMapper.insertDistrictfollower(districtfollower);
    }

    /**
     * 修改商圈跟进人员
     * 
     * @param districtfollower 商圈跟进人员
     * @return 结果
     */
    @Override
    public int updateDistrictfollower(Districtfollower districtfollower)
    {
        return districtfollowerMapper.updateDistrictfollower(districtfollower);
    }

    /**
     * 批量删除商圈跟进人员
     * 
     * @param ids 需要删除的商圈跟进人员主键
     * @return 结果
     */
    @Override
    public int deleteDistrictfollowerByIds(Long[] ids)
    {
        return districtfollowerMapper.deleteDistrictfollowerByIds(ids);
    }

    /**
     * 删除商圈跟进人员信息
     * 
     * @param id 商圈跟进人员主键
     * @return 结果
     */
    @Override
    public int deleteDistrictfollowerById(Long id)
    {
        return districtfollowerMapper.deleteDistrictfollowerById(id);
    }
}
