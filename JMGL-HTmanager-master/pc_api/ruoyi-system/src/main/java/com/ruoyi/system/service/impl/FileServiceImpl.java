package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FileMapper;
import com.ruoyi.system.domain.File;
import com.ruoyi.system.service.IFileService;

/**
 * 文件存储Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class FileServiceImpl implements IFileService 
{
    @Autowired
    private FileMapper fileMapper;

    /**
     * 查询文件存储
     * 
     * @param id 文件存储主键
     * @return 文件存储
     */
    @Override
    public File selectFileById(Long id)
    {
        return fileMapper.selectFileById(id);
    }

    /**
     * 查询文件存储列表
     * 
     * @param file 文件存储
     * @return 文件存储
     */
    @Override
    public List<File> selectFileList(File file)
    {
        return fileMapper.selectFileList(file);
    }

    /**
     * 新增文件存储
     * 
     * @param file 文件存储
     * @return 结果
     */
    @Override
    public int insertFile(File file)
    {
        return fileMapper.insertFile(file);
    }

    /**
     * 修改文件存储
     * 
     * @param file 文件存储
     * @return 结果
     */
    @Override
    public int updateFile(File file)
    {
        return fileMapper.updateFile(file);
    }

    /**
     * 批量删除文件存储
     * 
     * @param ids 需要删除的文件存储主键
     * @return 结果
     */
    @Override
    public int deleteFileByIds(Long[] ids)
    {
        return fileMapper.deleteFileByIds(ids);
    }

    /**
     * 删除文件存储信息
     * 
     * @param id 文件存储主键
     * @return 结果
     */
    @Override
    public int deleteFileById(Long id)
    {
        return fileMapper.deleteFileById(id);
    }
}
