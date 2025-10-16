package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ClientEventMapper;
import com.ruoyi.system.domain.ClientEvent;
import com.ruoyi.system.service.IClientEventService;

/**
 * 客户事件Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class ClientEventServiceImpl implements IClientEventService 
{
    @Autowired
    private ClientEventMapper clientEventMapper;

    /**
     * 查询客户事件
     * 
     * @param id 客户事件主键
     * @return 客户事件
     */
    @Override
    public ClientEvent selectClientEventById(Long id)
    {
        return clientEventMapper.selectClientEventById(id);
    }

    /**
     * 查询客户事件列表
     * 
     * @param clientEvent 客户事件
     * @return 客户事件
     */
    @Override
    public List<ClientEvent> selectClientEventList(ClientEvent clientEvent)
    {
        return clientEventMapper.selectClientEventList(clientEvent);
    }

    /**
     * 新增客户事件
     * 
     * @param clientEvent 客户事件
     * @return 结果
     */
    @Override
    public int insertClientEvent(ClientEvent clientEvent)
    {
        return clientEventMapper.insertClientEvent(clientEvent);
    }

    /**
     * 修改客户事件
     * 
     * @param clientEvent 客户事件
     * @return 结果
     */
    @Override
    public int updateClientEvent(ClientEvent clientEvent)
    {
        return clientEventMapper.updateClientEvent(clientEvent);
    }

    /**
     * 批量删除客户事件
     * 
     * @param ids 需要删除的客户事件主键
     * @return 结果
     */
    @Override
    public int deleteClientEventByIds(Long[] ids)
    {
        return clientEventMapper.deleteClientEventByIds(ids);
    }

    /**
     * 删除客户事件信息
     * 
     * @param id 客户事件主键
     * @return 结果
     */
    @Override
    public int deleteClientEventById(Long id)
    {
        return clientEventMapper.deleteClientEventById(id);
    }
}
