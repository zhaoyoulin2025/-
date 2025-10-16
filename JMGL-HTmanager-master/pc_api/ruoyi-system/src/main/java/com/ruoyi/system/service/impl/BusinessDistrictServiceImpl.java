package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Districtfollower;
import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.vo.BatchSetDistrictClientDTO;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.domain.vo.DistrctStatus;
import com.ruoyi.system.mapper.DistrictfollowerMapper;
import com.ruoyi.system.mapper.ShopMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusinessDistrictMapper;
import com.ruoyi.system.domain.BusinessDistrict;
import com.ruoyi.system.service.IBusinessDistrictService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 商圈信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-07-11
 */
@Service
public class BusinessDistrictServiceImpl implements IBusinessDistrictService {
    @Autowired
    private BusinessDistrictMapper businessDistrictMapper;

    @Autowired
    private ShopMapper shopMapper;


    @Autowired
    private DistrictfollowerMapper districtfollowerMapper;

    /**
     * 查询商圈信息
     *
     * @param id 商圈信息主键
     * @return 商圈信息
     */
    @Override
    public BusinessDistrict selectBusinessDistrictById(Long id) {

        BusinessDistrict businessDistrict = businessDistrictMapper.selectBusinessDistrictById(id);

        businessDistrict.setFollowerStatus(StringUtils.isEmpty(businessDistrict.getFollowerIds())?"1":"2");
        return businessDistrict;
    }

    /**
     * 查询商圈信息列表
     *
     * @param businessDistrict 商圈信息
     * @return 商圈信息
     */
    @Override
    public List<BusinessDistrict> selectBusinessDistrictList(BusinessDistrict businessDistrict) {
        List<BusinessDistrict> businessDistricts = businessDistrictMapper.selectBusinessDistrictList(businessDistrict);


        for (BusinessDistrict district : businessDistricts) {
            district.setFollowerStatus(StringUtils.isEmpty(district.getFollowerIds())?"1":"2");
        }
        return businessDistricts;
    }

    /**
     * 新增商圈信息
     *
     * @param businessDistrict 商圈信息
     * @return 结果
     */
    @Override
    public int insertBusinessDistrict(BusinessDistrict businessDistrict) {
        int i = businessDistrictMapper.insertBusinessDistrict(businessDistrict);
        List<String> followerIdList = businessDistrict.getFollowerIdList();
        if (CollectionUtils.isNotEmpty(followerIdList)) {
            followerIdList.forEach(c -> {
                Districtfollower districtfollower = new Districtfollower();
                districtfollower.setDistrictId(businessDistrict.getId());
                districtfollower.setFollowerId(Long.parseLong(c));
                List<Districtfollower> districtfollowers = districtfollowerMapper.selectDistrictfollowerList(districtfollower);
                if(CollectionUtils.isEmpty(districtfollowers)){
                    districtfollower.setCreatedAt(new Date());
                    districtfollower.setUpdatedAt(new Date());
                    districtfollowerMapper.insertDistrictfollower(districtfollower);
                }
            }
            );
        }
        return i;
    }

    @Override
    public void wxInsertDistrict(BusinessDistrict businessDistrict) {
        businessDistrictMapper.insertBusinessDistrict(businessDistrict);
    }

    /**
     * 修改商圈信息
     *
     * @param businessDistrict 商圈信息
     * @return 结果
     */
    @Override
    public int updateBusinessDistrict(BusinessDistrict businessDistrict) {
        List<String> followerIdList = businessDistrict.getFollowerIdList();
        //先查对应的关联关系直接删除
        Districtfollower districtfollower1 = new Districtfollower();
        districtfollower1.setDistrictId(businessDistrict.getId());
        List<Districtfollower> districtfollowers1 = districtfollowerMapper.selectDistrictfollowerList(districtfollower1);

        if(CollectionUtils.isNotEmpty(districtfollowers1)){
            districtfollowers1.forEach(c->{
                districtfollowerMapper.deleteDistrictfollowerById(c.getId());
            });
        }

        if (CollectionUtils.isNotEmpty(followerIdList)) {
            followerIdList.forEach(c -> {
                        Districtfollower districtfollower = new Districtfollower();
                        districtfollower.setDistrictId(businessDistrict.getId());
                        districtfollower.setFollowerId(Long.parseLong(c));
                List<Districtfollower> districtfollowers = districtfollowerMapper.selectDistrictfollowerList(districtfollower);
                if(CollectionUtils.isEmpty(districtfollowers)){
                    districtfollower.setCreatedAt(new Date());
                    districtfollower.setUpdatedAt(new Date());
                    districtfollowerMapper.insertDistrictfollower(districtfollower);
                }}
            );

        }

        return businessDistrictMapper.updateBusinessDistrict(businessDistrict);
    }

    /**
     * 批量删除商圈信息
     *
     * @param ids 需要删除的商圈信息主键
     * @return 结果
     */
    @Override
    public int deleteBusinessDistrictByIds(Long[] ids) {
        return businessDistrictMapper.deleteBusinessDistrictByIds(ids);
    }

    /**
     * 删除商圈信息信息
     *
     * @param id 商圈信息主键
     * @return 结果
     */
    @Override
    public int deleteBusinessDistrictById(Long id) {
        return businessDistrictMapper.deleteBusinessDistrictById(id);
    }

    @Override
    public int addFoller(ClientAddFollwer clientAddFollwer) {
        Long follower = clientAddFollwer.getFollower();
        clientAddFollwer.getShopIds().forEach(c->{
            BusinessDistrict businessDistrict = businessDistrictMapper.selectBusinessDistrictById(c);
            businessDistrict.setStatues("0");
            businessDistrictMapper.updateBusinessDistrict(businessDistrict);
            Districtfollower districtfollower = new Districtfollower();
                    districtfollower.setDistrictId(c);
                    List<Districtfollower> districtfollowers = districtfollowerMapper.selectDistrictfollowerList(districtfollower);
                    if(CollectionUtils.isNotEmpty(districtfollowers)){
                        for (Districtfollower districtfollower1 : districtfollowers) {
                            districtfollower1.setFollowerId(follower);
                            districtfollowerMapper.deleteDistrictfollowerById(districtfollower1.getId());
                        }
                    }
                    districtfollower.setFollowerId(follower);
                    districtfollowerMapper.insertDistrictfollower(districtfollower);
        });

        return clientAddFollwer.getShopIds().size();
    }

    @Override
    public int audit(DistrctStatus distrctStatus) {

        Long[] ids = distrctStatus.getIds();
        if(!ObjectUtils.isEmpty(ids)){
            for (Long id : ids) {
                BusinessDistrict businessDistrict = businessDistrictMapper.selectBusinessDistrictById(id);
                businessDistrict.setStatues(distrctStatus.getStatus());
                businessDistrictMapper.updateBusinessDistrict(businessDistrict);
            }
            return ids.length;
        }
        return 0;
    }

    @Override
    @Transactional
    public int batchSetClient(BatchSetDistrictClientDTO batchSetDistrictClientDTO) {
        for (Long districtId : batchSetDistrictClientDTO.getDistrictIds()) {
            BusinessDistrict businessDistrict = businessDistrictMapper.selectBusinessDistrictById(districtId);
            businessDistrict.setCustomerId(batchSetDistrictClientDTO.getCustomerId());
            businessDistrict.setStatues("4");
            businessDistrictMapper.updateBusinessDistrict(businessDistrict);

            //还需要创建店铺
            Shop shop = new Shop();

            shop.setClientId(batchSetDistrictClientDTO.getCustomerId());
            shop.setAddress(businessDistrict.getAddress());
            shop.setName(businessDistrict.getName());
            //查询关联跟进人员
            Districtfollower districtfollower = new Districtfollower();
            districtfollower.setDistrictId(businessDistrict.getId());
            List<Districtfollower> districtfollowers = districtfollowerMapper.selectDistrictfollowerList(districtfollower);
            if(CollectionUtils.isNotEmpty(districtfollowers)){
                Districtfollower districtfollower1 = districtfollowers.get(0);
                shop.setFollower(districtfollower1.getFollowerId()+"");
                shop.setOwner(districtfollower1.getFollowerId()+"");
            }
            shop.setsTime(new Date());
            shop.setUpTime(new Date());
            shop.setRunStatus("1");
            shopMapper.insertShop(shop);

        }


        return batchSetDistrictClientDTO.getDistrictIds().size();
    }
}
