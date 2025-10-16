package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ClientMapper;
import com.ruoyi.system.domain.Client;
import com.ruoyi.system.service.IClientService;

/**
 * 客户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-04
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper,Client> implements IClientService
{
    @Autowired
    private ClientMapper clientMapper;

    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    @Override
    public Client selectClientById(Long id)
    {
        return clientMapper.selectClientById(id);
    }

    /**
     * 查询客户信息列表
     *
     * @param client 客户信息
     * @return 客户信息
     */
    @Override
    public List<Client> selectClientList(Client client)
    {
        return clientMapper.selectClientList(client);
    }

    /**
     * 新增客户信息
     *
     * @param client 客户信息
     * @return 结果
     */
    @Override
    public int insertClient(Client client)
    {
        return clientMapper.insertClient(client);
    }

    /**
     * 修改客户信息
     *
     * @param client 客户信息
     * @return 结果
     */
    @Override
    public int updateClient(Client client)
    {
        return clientMapper.updateClient(client);
    }

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteClientByIds(Long[] ids)
    {
        return clientMapper.deleteClientByIds(ids);
    }

    /**
     * 删除客户信息信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteClientById(Long id)
    {
        return clientMapper.deleteClientById(id);
    }

    @Override
    public int addFoller(ClientAddFollwer clientAddFollwer) {
        int i = 0;
        for (Long clientid : clientAddFollwer.getClientIds()) {
            Client client =new Client();
            client.setId(clientid);
            client.setGuideId(clientAddFollwer.getFollower());
            client.setUpTime(new Date());
            int i1 = clientMapper.updateClient(client);
            i+=i1;
        }

        return i;
    }
}
