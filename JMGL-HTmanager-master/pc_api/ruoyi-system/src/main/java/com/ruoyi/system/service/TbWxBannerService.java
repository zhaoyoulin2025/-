package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.TbWxBanner;

import java.util.List;

public interface TbWxBannerService extends IService<TbWxBanner> {

    List<TbWxBanner> selectList(TbWxBanner tbWxBanner);

    void addOrUpdate(TbWxBanner tbWxBanner);

    void deleteBannerByIds(Long[] ids);

    TbWxBanner selectBannerById(Long id);

    String getSJWxBanner();
}
