package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.domain.MessageType;
import com.ruoyi.system.utils.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.InviteMapper;
import com.ruoyi.system.domain.Invite;
import com.ruoyi.system.service.IInviteService;

/**
 * 邀请记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-04
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteMapper,Invite> implements IInviteService
{
    @Autowired
    private InviteMapper inviteMapper;

    /**
     * 查询邀请记录
     *
     * @param id 邀请记录主键
     * @return 邀请记录
     */
    @Override
    public Invite selectInviteById(Long id)
    {
        return inviteMapper.selectInviteById(id);
    }


    /**
     * 查询邀请记录列表
     *
     * @param invite 邀请记录
     * @return 邀请记录
     */
    @Override
    public List<Invite> selectInviteList(Invite invite)
    {
        return inviteMapper.selectInviteList(invite);
    }

    /**
     * 新增邀请记录
     *
     * @param invite 邀请记录
     * @return 结果
     */
    @Override
    public int insertInvite(Invite invite) {

        //发送系统消息 -- 给 客户
//        MessageSender.send(new Message(invite.getClientId()+"")
//                .messageType(MessageType.SYSTEM.getCode())
//                .title("您有新的邀约消息")
//                .content("您有邀约消息")
//                .extraParams(new JSONObject().fluentPut("1","2"))
//                );
        return inviteMapper.insertInvite(invite);
    }

    /**
     * 修改邀请记录
     *
     * @param invite 邀请记录
     * @return 结果
     */
    @Override
    public int updateInvite(Invite invite)
    {
        return inviteMapper.updateInvite(invite);
    }

    /**
     * 批量删除邀请记录
     *
     * @param ids 需要删除的邀请记录主键
     * @return 结果
     */
    @Override
    public int deleteInviteByIds(Long[] ids)
    {
        return inviteMapper.deleteInviteByIds(ids);
    }

    /**
     * 删除邀请记录信息
     *
     * @param id 邀请记录主键
     * @return 结果
     */
    @Override
    public int deleteInviteById(Long id)
    {
        return inviteMapper.deleteInviteById(id);
    }

    @Override
    public List<Invite> inviteQueryByDate(String date) {
        LambdaQueryWrapper<Invite> inviteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        inviteLambdaQueryWrapper.eq(Invite::getInvitationTime,date);
        inviteLambdaQueryWrapper.orderByAsc(Invite::getsTime);
        return this.list(inviteLambdaQueryWrapper);
    }

    @Override
    public List<String> inviteQueryByDatePc(String date) {
        return this.baseMapper.inviteQueryByDatePc(date);
    }

    @Override
    public void cancelInvite(Long id) {
        UpdateWrapper<Invite> inviteUpdateWrapper = new UpdateWrapper<>();
        inviteUpdateWrapper.eq("id",id);
        inviteUpdateWrapper.set("inviteStatus",2);
        this.update(inviteUpdateWrapper);

    }
}
