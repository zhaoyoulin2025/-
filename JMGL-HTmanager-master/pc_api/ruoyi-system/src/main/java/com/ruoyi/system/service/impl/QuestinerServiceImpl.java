package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.QuestinerMapper;
import com.ruoyi.system.domain.Questiner;
import com.ruoyi.system.service.IQuestinerService;

/**
 * 提问者Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class QuestinerServiceImpl implements IQuestinerService 
{
    @Autowired
    private QuestinerMapper questinerMapper;

    /**
     * 查询提问者
     * 
     * @param id 提问者主键
     * @return 提问者
     */
    @Override
    public Questiner selectQuestinerById(Long id)
    {
        return questinerMapper.selectQuestinerById(id);
    }

    /**
     * 查询提问者列表
     * 
     * @param questiner 提问者
     * @return 提问者
     */
    @Override
    public List<Questiner> selectQuestinerList(Questiner questiner)
    {
        return questinerMapper.selectQuestinerList(questiner);
    }

    /**
     * 新增提问者
     * 
     * @param questiner 提问者
     * @return 结果
     */
    @Override
    public int insertQuestiner(Questiner questiner)
    {
        return questinerMapper.insertQuestiner(questiner);
    }

    /**
     * 修改提问者
     * 
     * @param questiner 提问者
     * @return 结果
     */
    @Override
    public int updateQuestiner(Questiner questiner)
    {
        return questinerMapper.updateQuestiner(questiner);
    }

    /**
     * 批量删除提问者
     * 
     * @param ids 需要删除的提问者主键
     * @return 结果
     */
    @Override
    public int deleteQuestinerByIds(Long[] ids)
    {
        return questinerMapper.deleteQuestinerByIds(ids);
    }

    /**
     * 删除提问者信息
     * 
     * @param id 提问者主键
     * @return 结果
     */
    @Override
    public int deleteQuestinerById(Long id)
    {
        return questinerMapper.deleteQuestinerById(id);
    }
}
