package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.CustomerFollowUp;
import org.apache.ibatis.annotations.Param;

/**
 * 客户跟进记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-06
 */
public interface CustomerFollowUpMapper extends BaseMapper<CustomerFollowUp>
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
    public List<CustomerFollowUp> wxCustomerFollowList(@Param("clientId") Long clientId,@Param("userId") Long userID);

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
     * 删除客户跟进记录
     * 
     * @param id 客户跟进记录主键
     * @return 结果
     */
    public int deleteCustomerFollowUpById(Long id);

    /**
     * 批量删除客户跟进记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerFollowUpByIds(Long[] ids);
}
