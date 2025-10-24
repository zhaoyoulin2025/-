package com.ruoyi.system.service.impl;


import java.util.*;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BusinessDistrict;
import com.ruoyi.system.domain.Design;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.domain.vo.*;
import com.ruoyi.system.mapper.BusinessDistrictMapper;
import com.ruoyi.system.mapper.DesignMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.IDesignService;
import com.ruoyi.system.service.IShopService;
import com.ruoyi.system.service.IShopSignService;
import com.ruoyi.system.utils.MessageSender;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ShopMapper;
import com.ruoyi.system.domain.Shop;
import org.springframework.util.ObjectUtils;

import static com.ruoyi.system.domain.vo.MessageType.getMessageType;

/**
 * 标准作业流程/店铺信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-15
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements IShopService
{


    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private DesignMapper designMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private IShopSignService shopSignService;

    @Autowired
    private IDesignService designService;

    @Autowired
    private BusinessDistrictMapper businessDistrictMapper;

    /**
     * 查询标准作业流程/店铺信息
     *
     * @param id 标准作业流程/店铺信息主键
     * @return 标准作业流程/店铺信息
     */
    @Override
    public Shop selectShopById(Long id)
    {
        return shopMapper.selectShopById(id);
    }

    /**
     * 查询标准作业流程/店铺信息列表
     *
     * @param shop 标准作业流程/店铺信息
     * @return 标准作业流程/店铺信息
     */
    @Override
    public List<Shop> selectShopList(Shop shop)
    {
        List<Shop> shops = shopMapper.selectShopList(shop);
        for (Shop shop1 : shops) {
            if(null != shop1.getUpTime()){
                shop1.setWarningDay(getDaysBetween(shop1.getUpTime()));
            }
            shop1.setProcess(getProcess(shop1.getRunStatus()));
        }

        return shops;
    }

    private String getProcess(String runStatus) {

        if (StringUtils.isEmpty(runStatus)) {
            runStatus ="1";
        }
        int i = Integer.parseInt(runStatus);
        double percentage = ((i-1)/ 8.0) * 100;
        // 直接输出（保留原始精度）

        // 格式化输出（保留1位小数，更符合阅读习惯）
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        String formattedPercentage = df.format(percentage);
       return formattedPercentage + "%";

    }

    public static long getDaysBetween(Date date1) {
        // 计算毫秒差（避免溢出，用long）
        long diffMillis = Math.abs(new Date().getTime() - date1.getTime());
        // 转换为天数（TimeUnit.DAYS.convert 自动处理整除）
        long days = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        // 根据日期前后调整正负（若date1在date2之后，返回负数）
        return days;
    }

    /**
     * 新增标准作业流程/店铺信息
     *
     * @param shop 标准作业流程/店铺信息
     * @return 结果
     */
    @Override
    public int insertShop(Shop shop)
    {

        Long districtId = shop.getDistrictId();
        BusinessDistrict businessDistrict = businessDistrictMapper.selectBusinessDistrictById(districtId);
        shop.setClientId(businessDistrict.getCustomerId());
        shop.setUpTime(new Date());
        return shopMapper.insertShop(shop);
    }

    /**
     * 修改标准作业流程/店铺信息
     *
     * @param shop 标准作业流程/店铺信息
     * @return 结果
     */
    @Override
    public int updateShop(Shop shop)
    {
        Shop shop1 = shopMapper.selectShopById(shop.getId());
        if(!StringUtils.isEmpty(shop1.getFollower())){
            if (!shop1.getFollower().equals(shop.getFollower())) {
                shop.setUpTime(new Date());
            }
        }
        //判断时间
        return shopMapper.updateShop(shop);
    }

    /**
     * 批量删除标准作业流程/店铺信息
     *
     * @param ids 需要删除的标准作业流程/店铺信息主键
     * @return 结果
     */
    @Override
    public int deleteShopByIds(Long[] ids)
    {
        for (Long id : ids) {
            shopMapper.deleteShopById(id);
        }
        return ids.length;
    }

    @Override
    public int removeFollower(Long[] ids) {
        for (Long id : ids) {
            shopMapper.updateShopNull(id);
        }

        return ids.length;
    }

    @Override
    public int audit(Long[] ids) {
        //需要通知对应的人
        for (Long id : ids) {
            Message message = new Message();
            Shop shop = shopMapper.selectShopById(id);
            message.setReceiverId(shop.getOwner());
            message.setSendTime(new Date());
            message.setMessageType(getMessageType(MessageType.通知));
            message.setTitle("店铺审核通知");
            message.setContent(shop.getName()+"审核通过");
            MessageSender.send(message);
            shopMapper.audit(id);
        }
        return ids.length;
    }

    @Override
    public int auditAcc(Long[] ids) {
        //需要通知对应的人
        for (Long id : ids) {
            Message message = new Message();
            Shop shop = shopMapper.selectShopById(id);
            message.setReceiverId(shop.getOwner());
            message.setMessageType(getMessageType(MessageType.通知));
            message.setSendTime(new Date());
            message.setTitle("店铺审核通知");
            message.setContent(shop.getName()+"审核不通过");
            MessageSender.send(message);
            shopMapper.auditAcc(id);
        }

        return ids.length;
    }

    @Override
    public int toDesgin(Long[] ids) {
        for (Long id : ids) {
            Design design = new Design();
            //1

            //先判断 当前有没有
            Design desginByShopId = designMapper.getDesginByShopId(id);
            if(!ObjectUtils.isEmpty(desginByShopId)){
                return 0;
            }
            Shop shop = shopMapper.selectShopById(id);
            design.setCreateTime(new Date());
            design.setShopId(id);
            design.setUpTime(new Date());
            design.setDesignStatus(0);
//            design.setDesignType(1); //平面图
            design.setClientId(shop.getClientId());
            designMapper.insertDesign(design);
            //发送消息
            Message message = new Message();
            shop.setRunStatus("1");
            message.setReceiverId(shop.getClientId()+"");
            message.setMessageType(getMessageType(MessageType.通知));
            message.setSendTime(new Date());
            message.setTitle("店铺开始设计");
            message.setContent(shop.getName()+"设计开始");
            MessageSender.send(message);
            shop.setUpTime(new Date());
            shopMapper.updateShop(shop);
        }

        return ids.length;
    }

    @Override
    public int addFoller(ClientAddFollwer clientAddFollwer) {

        List<Long> shopIds = clientAddFollwer.getShopIds();
        if(CollectionUtils.isNotEmpty(shopIds)){
            for (Long shopId : shopIds) {
                Shop shop = shopMapper.selectShopById(shopId);
                shop.setFollower(clientAddFollwer.getFollower()+"");
                shop.setUpTime(new Date());
                shopMapper.updateShop(shop);

            }
        }
        return shopIds.size();
    }

    @Override
    public List<ClientOwnShopVO> clientOwnShopList(Long clientId) {
        ArrayList<ClientOwnShopVO> clientOwnShopVOS = new ArrayList<>();
        List<ClientOwnShopVO> clientOwnShopList = shopSignService.clientOwnShopList(clientId);
        if (null != clientOwnShopList && !clientOwnShopList.isEmpty()){
            for (ClientOwnShopVO clientOwnShopVO : clientOwnShopList) {
                ArrayList<ShopStateVO> shopStateVOS = new ArrayList<>();
                //第一个是资料审核已通过
                ShopStateVO shopStateVO = new ShopStateVO();
                shopStateVO.setName("资料审核");
                shopStateVO.setDate(clientOwnShopVO.getSaleTime());
                shopStateVO.setStatus("completed");
                shopStateVO.setStatusText("资料审核通过，进入设计阶段");
                shopStateVOS.add(shopStateVO);

                //第二个是看设计阶段
                Design design = designService.clientOwnShop(clientId, clientOwnShopVO.getShopId());
                ShopStateVO shopStateDesignVO = new ShopStateVO();
                if (null != design && null != design.getDesignStatus() && design.getDesignStatus().equals(1)){

                    shopStateDesignVO.setName("设计方案");
                    shopStateDesignVO.setDate(design.getUpTime());
                    shopStateDesignVO.setStatus("completed");
                    shopStateDesignVO.setStatusText("设计方案已确认，正在施工中");
                }else {
                    shopStateDesignVO.setName("设计方案");
                    shopStateDesignVO.setDate(design.getUpTime());
                    shopStateDesignVO.setStatus("active");
                    shopStateDesignVO.setStatusText("正在设计阶段中");

                }
                //第三个看 营建阶段/施工阶段

            }



        }
        return clientOwnShopVOS;

    }

    @Override
    public List<WxShopSignListVO> wxFollowShopList(Long followId) {
        return baseMapper.wxFollowShopList(followId);
    }

    @Override
    public List<WxShopSignListVO> wxMyShopList(Long clientId) {
        return baseMapper.wxMyShopList(clientId);
    }
}
