package com.ruoyi.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CustomerFollowUpMapper;
import com.ruoyi.system.domain.CustomerFollowUp;
import com.ruoyi.system.service.ICustomerFollowUpService;

/**
 * 客户跟进记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-06
 */
@Service
public class CustomerFollowUpServiceImpl extends ServiceImpl<CustomerFollowUpMapper,CustomerFollowUp> implements ICustomerFollowUpService
{
    @Autowired
    private CustomerFollowUpMapper customerFollowUpMapper;

    /**
     * 查询客户跟进记录
     * 
     * @param id 客户跟进记录主键
     * @return 客户跟进记录
     */
    @Override
    public CustomerFollowUp selectCustomerFollowUpById(Long id)
    {
        return customerFollowUpMapper.selectCustomerFollowUpById(id);
    }

    /**
     * 查询客户跟进记录列表
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 客户跟进记录
     */
    @Override
    public List<CustomerFollowUp> selectCustomerFollowUpList(CustomerFollowUp customerFollowUp)
    {
        return customerFollowUpMapper.selectCustomerFollowUpList(customerFollowUp);
    }

    @Override
    public List<CustomerFollowUp> wxCustomerFollowList(Long clientId,Long userId) {
        return customerFollowUpMapper.wxCustomerFollowList(clientId,userId);
    }

    /**
     * 新增客户跟进记录
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 结果
     */
    @Override
    public int insertCustomerFollowUp(CustomerFollowUp customerFollowUp)
    {
        return customerFollowUpMapper.insertCustomerFollowUp(customerFollowUp);
    }

    /**
     * 修改客户跟进记录
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 结果
     */
    @Override
    public int updateCustomerFollowUp(CustomerFollowUp customerFollowUp)
    {
        return customerFollowUpMapper.updateCustomerFollowUp(customerFollowUp);
    }

    /**
     * 批量删除客户跟进记录
     * 
     * @param ids 需要删除的客户跟进记录主键
     * @return 结果
     */
    @Override
    public int deleteCustomerFollowUpByIds(Long[] ids)
    {
        return customerFollowUpMapper.deleteCustomerFollowUpByIds(ids);
    }

    /**
     * 删除客户跟进记录信息
     * 
     * @param id 客户跟进记录主键
     * @return 结果
     */
    @Override
    public int deleteCustomerFollowUpById(Long id)
    {
        return customerFollowUpMapper.deleteCustomerFollowUpById(id);
    }
}
