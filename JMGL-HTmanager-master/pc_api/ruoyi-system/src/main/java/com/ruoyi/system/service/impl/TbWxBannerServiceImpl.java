package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.TbWxBanner;
import com.ruoyi.system.mapper.TbWxBannerMapper;
import com.ruoyi.system.service.TbWxBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TbWxBannerServiceImpl extends ServiceImpl<TbWxBannerMapper, TbWxBanner> implements TbWxBannerService {

    @Override
    public List<TbWxBanner> selectList(TbWxBanner tbWxBanner) {
        LambdaQueryWrapper<TbWxBanner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != tbWxBanner.getStatus(),TbWxBanner::getStatus,tbWxBanner.getStatus());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void addOrUpdate(TbWxBanner tbWxBanner) {
        if(1==tbWxBanner.getStatus()){
            UpdateWrapper<TbWxBanner> tbWxBannerUpdateWrapper = new UpdateWrapper<>();
            tbWxBannerUpdateWrapper.eq("status",1);
            tbWxBannerUpdateWrapper.set("status",0);
            this.update(tbWxBannerUpdateWrapper);
        }
        if (null !=tbWxBanner.getId()){
            UpdateWrapper<TbWxBanner> tbWxBannerUpdateWrapper2 = new UpdateWrapper<>();
            tbWxBannerUpdateWrapper2.eq("id",tbWxBanner.getId());
            tbWxBannerUpdateWrapper2.set("status",tbWxBanner.getStatus());
            this.update(tbWxBannerUpdateWrapper2);
        }else {
            this.save(tbWxBanner);
        }
    }

    @Override
    public void deleteBannerByIds(Long[] ids) {
        this.removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public TbWxBanner selectBannerById(Long id) {
        return this.getById(id);
    }

    @Override
    public String getSJWxBanner() {
        QueryWrapper<TbWxBanner> tbWxBannerQueryWrapper = new QueryWrapper<>();
        tbWxBannerQueryWrapper.eq("status",1);
        TbWxBanner one = this.getOne(tbWxBannerQueryWrapper);
        if (null != one){
            return one.getUrl();
        }
        return "";
    }
}
