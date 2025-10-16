package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Client;

/**
 * 客户信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-06-15
 */
public interface ClientMapper extends BaseMapper<Client>
{
    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    public Client selectClientById(Long id);

    /**
     * 查询客户信息列表
     *
     * @param client 客户信息
     * @return 客户信息集合
     */
    public List<Client> selectClientList(Client client);

    /**
     * 新增客户信息
     *
     * @param client 客户信息
     * @return 结果
     */
    public int insertClient(Client client);

    /**
     * 修改客户信息
     *
     * @param client 客户信息
     * @return 结果
     */
    public int updateClient(Client client);

    /**
     * 删除客户信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteClientById(Long id);

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClientByIds(Long[] ids);

    public Map<String,Long> getCurrentMouthCountUser(Long userId);

    public Map<String,Long> getCurrentMouthFollowCount(Long userId);


}
