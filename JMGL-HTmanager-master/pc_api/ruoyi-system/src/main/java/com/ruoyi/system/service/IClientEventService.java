package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ClientEvent;

/**
 * 客户事件Service接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface IClientEventService 
{
    /**
     * 查询客户事件
     * 
     * @param id 客户事件主键
     * @return 客户事件
     */
    public ClientEvent selectClientEventById(Long id);

    /**
     * 查询客户事件列表
     * 
     * @param clientEvent 客户事件
     * @return 客户事件集合
     */
    public List<ClientEvent> selectClientEventList(ClientEvent clientEvent);

    /**
     * 新增客户事件
     * 
     * @param clientEvent 客户事件
     * @return 结果
     */
    public int insertClientEvent(ClientEvent clientEvent);

    /**
     * 修改客户事件
     * 
     * @param clientEvent 客户事件
     * @return 结果
     */
    public int updateClientEvent(ClientEvent clientEvent);

    /**
     * 批量删除客户事件
     * 
     * @param ids 需要删除的客户事件主键集合
     * @return 结果
     */
    public int deleteClientEventByIds(Long[] ids);

    /**
     * 删除客户事件信息
     * 
     * @param id 客户事件主键
     * @return 结果
     */
    public int deleteClientEventById(Long id);
}
