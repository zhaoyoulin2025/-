package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Design;
import com.ruoyi.system.domain.DesignFlowDetail;
import com.ruoyi.system.domain.WxDesignUpdateStatusDTO;
import com.ruoyi.system.domain.vo.AuditDTO;
import com.ruoyi.system.domain.vo.DesignTaskAssignDTO;
import com.ruoyi.system.domain.vo.WxDesignListVO;

/**
 * 设计管理Service接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface IDesignService 
{
    /**
     * 查询设计管理
     * 
     * @param id 设计管理主键
     * @return 设计管理
     */
    public Design selectDesignById(Long id);

    /**
     * 查询设计管理列表
     * 
     * @param design 设计管理
     * @return 设计管理集合
     */
    public List<Design> selectDesignList(Design design);

    /**
     * 新增设计管理
     * 
     * @param design 设计管理
     * @return 结果
     */
    public int insertDesign(Design design);

    /**
     * 修改设计管理
     * 
     * @param design 设计管理
     * @return 结果
     */
    public int updateDesign(Design design);

    /**
     * 批量删除设计管理
     * 
     * @param ids 需要删除的设计管理主键集合
     * @return 结果
     */
    public int deleteDesignByIds(Long[] ids);

    /**
     * 删除设计管理信息
     * 
     * @param id 设计管理主键
     * @return 结果
     */
    public int deleteDesignById(Long id);

    Design clientOwnShop(Long clientId,Long shopId);

    List<WxDesignListVO> wxDesignList(Long userId);

    void updateDesignStatus(WxDesignUpdateStatusDTO wxDesignUpdateStatusDTO);

    int assignDesignTask(DesignTaskAssignDTO designTaskAssignDTO);

    int add(DesignFlowDetail detail);

    void auditDesignRecord(AuditDTO auditDTO);
}
