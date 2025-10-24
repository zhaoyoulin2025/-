package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ClientDistrict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientDistrictMapper extends BaseMapper<ClientDistrict> {

    void deleteRepeatData();

}
