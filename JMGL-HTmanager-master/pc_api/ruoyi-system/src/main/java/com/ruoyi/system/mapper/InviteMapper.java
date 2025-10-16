package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Invite;

/**
 * 邀请记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-04
 */
public interface InviteMapper extends BaseMapper<Invite>
{
    /**
     * 查询邀请记录
     * 
     * @param id 邀请记录主键
     * @return 邀请记录
     */
    public Invite selectInviteById(Long id);

    /**
     * 查询邀请记录列表
     * 
     * @param invite 邀请记录
     * @return 邀请记录集合
     */
    public List<Invite> selectInviteList(Invite invite);

    /**
     * 新增邀请记录
     * 
     * @param invite 邀请记录
     * @return 结果
     */
    public int insertInvite(Invite invite);

    /**
     * 修改邀请记录
     * 
     * @param invite 邀请记录
     * @return 结果
     */
    public int updateInvite(Invite invite);

    /**
     * 删除邀请记录
     * 
     * @param id 邀请记录主键
     * @return 结果
     */
    public int deleteInviteById(Long id);

    /**
     * 批量删除邀请记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInviteByIds(Long[] ids);

    public List<String> inviteQueryByDatePc(String dateStr);
}
