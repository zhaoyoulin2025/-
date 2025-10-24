package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.ClientDistrict;


public interface ClientDistrictService extends IService<ClientDistrict> {

    void deleteRepeatData();

}
