package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Questiner;

/**
 * 提问者Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface QuestinerMapper 
{
    /**
     * 查询提问者
     * 
     * @param id 提问者主键
     * @return 提问者
     */
    public Questiner selectQuestinerById(Long id);

    /**
     * 查询提问者列表
     * 
     * @param questiner 提问者
     * @return 提问者集合
     */
    public List<Questiner> selectQuestinerList(Questiner questiner);

    /**
     * 新增提问者
     * 
     * @param questiner 提问者
     * @return 结果
     */
    public int insertQuestiner(Questiner questiner);

    /**
     * 修改提问者
     * 
     * @param questiner 提问者
     * @return 结果
     */
    public int updateQuestiner(Questiner questiner);

    /**
     * 删除提问者
     * 
     * @param id 提问者主键
     * @return 结果
     */
    public int deleteQuestinerById(Long id);

    /**
     * 批量删除提问者
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestinerByIds(Long[] ids);
}
