package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.FranchiseInvestmentForm;

/**
 * 加盟投资意向Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface FranchiseInvestmentFormMapper extends BaseMapper<FranchiseInvestmentForm>
{
    /**
     * 查询加盟投资意向
     * 
     * @param id 加盟投资意向主键
     * @return 加盟投资意向
     */
    public FranchiseInvestmentForm selectFranchiseInvestmentFormById(Integer id);

    public FranchiseInvestmentForm selectFranchiseInvestmentFormByClientId(Integer id);

    /**
     * 查询加盟投资意向列表
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 加盟投资意向集合
     */
    public List<FranchiseInvestmentForm> selectFranchiseInvestmentFormList(FranchiseInvestmentForm franchiseInvestmentForm);

    /**
     * 新增加盟投资意向
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 结果
     */
    public int insertFranchiseInvestmentForm(FranchiseInvestmentForm franchiseInvestmentForm);

    /**
     * 修改加盟投资意向
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 结果
     */
    public int updateFranchiseInvestmentForm(FranchiseInvestmentForm franchiseInvestmentForm);

    /**
     * 删除加盟投资意向
     * 
     * @param id 加盟投资意向主键
     * @return 结果
     */
    public int deleteFranchiseInvestmentFormById(Integer id);

    /**
     * 批量删除加盟投资意向
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFranchiseInvestmentFormByIds(Integer[] ids);
}
