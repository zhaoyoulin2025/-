package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.ShopSign;
import com.ruoyi.system.domain.vo.ClientOwnShopVO;
import com.ruoyi.system.mapper.ShopSignMapper;
import com.ruoyi.system.service.IShopSignService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSignServiceImpl extends ServiceImpl<ShopSignMapper, ShopSign> implements IShopSignService {
    @Override
    public List<ShopSign> followShopList(Long saleId) {
        return baseMapper.followShopList(saleId);
    }

    @Override
    public List<ClientOwnShopVO> clientOwnShopList(Long clientId) {
        return baseMapper.clientOwnShopList(clientId);
    }
}
