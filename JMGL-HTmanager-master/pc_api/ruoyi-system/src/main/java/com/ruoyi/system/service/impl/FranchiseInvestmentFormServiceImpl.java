package com.ruoyi.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Questiner;
import com.ruoyi.system.mapper.QuestinerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FranchiseInvestmentFormMapper;
import com.ruoyi.system.domain.FranchiseInvestmentForm;
import com.ruoyi.system.service.IFranchiseInvestmentFormService;

/**
 * 加盟投资意向Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class FranchiseInvestmentFormServiceImpl extends ServiceImpl<FranchiseInvestmentFormMapper,FranchiseInvestmentForm> implements IFranchiseInvestmentFormService
{
    @Autowired
    private FranchiseInvestmentFormMapper franchiseInvestmentFormMapper;

    @Autowired
    private QuestinerMapper questinerMapper;


    /**
     * 查询加盟投资意向
     * 
     * @param id 加盟投资意向主键
     * @return 加盟投资意向
     */
    @Override
    public FranchiseInvestmentForm selectFranchiseInvestmentFormById(Integer id)
    {
        return franchiseInvestmentFormMapper.selectFranchiseInvestmentFormById(id);
    }

    @Override
    public FranchiseInvestmentForm selectFranchiseInvestmentFormByClientId(Integer id)
    {
        return franchiseInvestmentFormMapper.selectFranchiseInvestmentFormByClientId(id);
    }

    /**
     * 查询加盟投资意向列表
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 加盟投资意向
     */
    @Override
    public List<FranchiseInvestmentForm> selectFranchiseInvestmentFormList(FranchiseInvestmentForm franchiseInvestmentForm)
    {
        return franchiseInvestmentFormMapper.selectFranchiseInvestmentFormList(franchiseInvestmentForm);
    }

    /**
     * 新增加盟投资意向
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 结果
     */
    @Override
    public int insertFranchiseInvestmentForm(FranchiseInvestmentForm franchiseInvestmentForm)
    {

        //添加成功之后 需要加入一个用户id，在问卷表里面
        franchiseInvestmentForm.setCreateTime(DateUtils.getNowDate());
        return franchiseInvestmentFormMapper.insertFranchiseInvestmentForm(franchiseInvestmentForm);
    }

    /**
     * 修改加盟投资意向
     * 
     * @param franchiseInvestmentForm 加盟投资意向
     * @return 结果
     */
    @Override
    public int updateFranchiseInvestmentForm(FranchiseInvestmentForm franchiseInvestmentForm)
    {
        return franchiseInvestmentFormMapper.updateFranchiseInvestmentForm(franchiseInvestmentForm);
    }

    /**
     * 批量删除加盟投资意向
     * 
     * @param ids 需要删除的加盟投资意向主键
     * @return 结果
     */
    @Override
    public int deleteFranchiseInvestmentFormByIds(Integer[] ids)
    {
        return franchiseInvestmentFormMapper.deleteFranchiseInvestmentFormByIds(ids);
    }

    /**
     * 删除加盟投资意向信息
     * 
     * @param id 加盟投资意向主键
     * @return 结果
     */
    @Override
    public int deleteFranchiseInvestmentFormById(Integer id)
    {
        return franchiseInvestmentFormMapper.deleteFranchiseInvestmentFormById(id);
    }
}
