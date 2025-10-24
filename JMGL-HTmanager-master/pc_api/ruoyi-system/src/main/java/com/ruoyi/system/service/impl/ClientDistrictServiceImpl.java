package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.ClientDistrict;
import com.ruoyi.system.mapper.ClientDistrictMapper;
import com.ruoyi.system.service.ClientDistrictService;
import org.springframework.stereotype.Service;

@Service
public class ClientDistrictServiceImpl  extends ServiceImpl<ClientDistrictMapper, ClientDistrict> implements ClientDistrictService {
    @Override
    public void deleteRepeatData() {
        baseMapper.deleteRepeatData();
    }
}
