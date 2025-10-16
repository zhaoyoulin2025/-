package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.ShopSign;
import com.ruoyi.system.domain.vo.ClientOwnShopVO;

import java.util.List;


public interface IShopSignService extends IService<ShopSign> {

    List<ShopSign> followShopList(Long saleId);

    List<ClientOwnShopVO> clientOwnShopList(Long clientId);


}
