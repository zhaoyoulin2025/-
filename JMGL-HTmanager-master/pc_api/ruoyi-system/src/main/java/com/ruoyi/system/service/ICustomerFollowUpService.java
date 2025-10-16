package com.ruoyi.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.CustomerFollowUp;

/**
 * 客户跟进记录Service接口
 * 
 * @author ruoyi
 * @date 2025-06-06
 */
public interface ICustomerFollowUpService extends IService<CustomerFollowUp>
{
    /**
     * 查询客户跟进记录
     * 
     * @param id 客户跟进记录主键
     * @return 客户跟进记录
     */
    public CustomerFollowUp selectCustomerFollowUpById(Long id);

    /**
     * 查询客户跟进记录列表
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 客户跟进记录集合
     */
    public List<CustomerFollowUp> selectCustomerFollowUpList(CustomerFollowUp customerFollowUp);
    public List<CustomerFollowUp> wxCustomerFollowList(Long clientId,Long userId);

    /**
     * 新增客户跟进记录
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 结果
     */
    public int insertCustomerFollowUp(CustomerFollowUp customerFollowUp);

    /**
     * 修改客户跟进记录
     * 
     * @param customerFollowUp 客户跟进记录
     * @return 结果
     */
    public int updateCustomerFollowUp(CustomerFollowUp customerFollowUp);

    /**
     * 批量删除客户跟进记录
     * 
     * @param ids 需要删除的客户跟进记录主键集合
     * @return 结果
     */
    public int deleteCustomerFollowUpByIds(Long[] ids);

    /**
     * 删除客户跟进记录信息
     * 
     * @param id 客户跟进记录主键
     * @return 结果
     */
    public int deleteCustomerFollowUpById(Long id);
}
