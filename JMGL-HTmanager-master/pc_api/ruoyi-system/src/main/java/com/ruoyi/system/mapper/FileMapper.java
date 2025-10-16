package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.File;

/**
 * 文件存储Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface FileMapper 
{
    /**
     * 查询文件存储
     * 
     * @param id 文件存储主键
     * @return 文件存储
     */
    public File selectFileById(Long id);

    /**
     * 查询文件存储列表
     * 
     * @param file 文件存储
     * @return 文件存储集合
     */
    public List<File> selectFileList(File file);

    /**
     * 新增文件存储
     * 
     * @param file 文件存储
     * @return 结果
     */
    public int insertFile(File file);

    /**
     * 修改文件存储
     * 
     * @param file 文件存储
     * @return 结果
     */
    public int updateFile(File file);

    /**
     * 删除文件存储
     * 
     * @param id 文件存储主键
     * @return 结果
     */
    public int deleteFileById(Long id);

    /**
     * 批量删除文件存储
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileByIds(Long[] ids);
}
