package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.Design;
import com.ruoyi.system.domain.vo.WxDesignListVO;
import org.apache.ibatis.annotations.Param;

/**
 * 设计管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface DesignMapper 
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
     * 删除设计管理
     * 
     * @param id 设计管理主键
     * @return 结果
     */
    public int deleteDesignById(Long id);

    /**
     * 批量删除设计管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDesignByIds(Long[] ids);

    List<Design> getUnFinishDesgin(Long userId);

    Design clientOwnShop(@Param("clientId") Long clientId, @Param("shopId")Long shopId);

    List<WxDesignListVO> wxDesignList(@Param("userId") Long userId);

    void updateDesignStatus(@Param("designId") Long designId, @Param("status")int status);

    public Map<String,Long> getCurrentMouthDesignCount(Long userId);
}
