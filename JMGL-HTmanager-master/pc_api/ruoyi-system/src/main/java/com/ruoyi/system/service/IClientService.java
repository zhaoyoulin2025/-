package com.ruoyi.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.Client;
import com.ruoyi.system.domain.vo.ClientAddFollwer;

/**
 * 客户信息Service接口
 * 
 * @author ruoyi
 * @date 2025-06-04
 */
public interface IClientService extends IService<Client>
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
     * 批量删除客户信息
     * 
     * @param ids 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteClientByIds(Long[] ids);

    /**
     * 删除客户信息信息
     * 
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteClientById(Long id);

    int addFoller(ClientAddFollwer clientAddFollwer);
}
