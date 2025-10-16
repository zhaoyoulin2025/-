package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.File;

/**
 * 文件存储Service接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface IFileService 
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
     * 批量删除文件存储
     * 
     * @param ids 需要删除的文件存储主键集合
     * @return 结果
     */
    public int deleteFileByIds(Long[] ids);

    /**
     * 删除文件存储信息
     * 
     * @param id 文件存储主键
     * @return 结果
     */
    public int deleteFileById(Long id);
}
