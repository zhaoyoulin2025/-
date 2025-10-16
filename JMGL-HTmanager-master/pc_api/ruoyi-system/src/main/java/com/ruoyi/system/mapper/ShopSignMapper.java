package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ShopSign;
import com.ruoyi.system.domain.vo.ClientOwnShopVO;

import java.util.List;
import java.util.Map;

public interface ShopSignMapper extends BaseMapper<ShopSign> {

    List<ShopSign> followShopList(Long userId);

    List<ClientOwnShopVO> clientOwnShopList(Long clientId);

    Map<String,Long> getCurrentMouthCount(Long clientId);

}
