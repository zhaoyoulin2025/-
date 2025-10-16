package com.ruoyi.system.service.impl;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Validator;

import com.ruoyi.system.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    protected Validator validator;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ShopSignMapper shopSignMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private DesignMapper designMapper;

    @Autowired
    private BuildMapper buildMapper;
    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    @Override
    public List<SysUser> getAllSuperviser() {
        return userMapper.getAllSupvisers();
    }

    @Override
    public List<SysUser> getAllDesgins() {
        return userMapper.getAllDesgins();
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    public SysUser loadUserByUserOpenId(String openId) {
        return userMapper.loadUserByUserOpenId(openId);
    }

    @Override
    public void cancelBind(Long userId) {
        userMapper.cancelBind(userId);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    @Override
    public int insertWxUser(SysUser user) {
        int rows = userMapper.insertUser(user);
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    @Override
    public int updateWxUser(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public Map<String, Object> getRoleMetrics(Long userId) {
        String roleKey = "";
        HashMap<String, Object> map = new HashMap<>();
        SysUser sysUser1 = this.selectUserById(userId);
        List<SysRole> list = roleMapper.selectRolesByUserName(sysUser1.getUserName());
        for (SysRole sysRole : list) {
            roleKey = sysRole.getRoleKey();
            break;
        }
        switch (roleKey) {
            case "shop":
                map.put("role", "shop");
                // 本月新增客户 和增长率
                Map<String, Long> currentMouthCountUser = clientMapper.getCurrentMouthCountUser(userId);
                double currentCount = new Double(currentMouthCountUser.get("currentCount"));
                double lastCount = new Double(currentMouthCountUser.get("lastCount"));
                String growthRate = calculateGrowthRate(currentCount, lastCount);
                map.put("clientGrowthRate", growthRate);
                map.put("clientCurrentCount", currentCount);

                Map<String, Long> currentMouthCount = shopSignMapper.getCurrentMouthCount(userId);
                double currentSignCount = new Double(currentMouthCount.get("currentCount"));
                double lastSignCount = new Double(currentMouthCount.get("lastCount"));
                String signRate = calculateGrowthRate(currentSignCount, lastSignCount);
                map.put("signRate", signRate);
                map.put("currentSignCount", currentSignCount);

                Map<String, Long> currentMouthFollowCount = clientMapper.getCurrentMouthFollowCount(userId);
                double currentFollowCount = new Double(currentMouthFollowCount.get("currentCount"));
                double lastFollowCount = new Double(currentMouthFollowCount.get("lastCount"));
                String followRate = calculateGrowthRate(currentFollowCount, lastFollowCount);
                map.put("followRate", followRate);
                map.put("currentFollowCount", currentFollowCount);
                break;
            case "attract":
                map.put("role", "attract");
                Map<String, Long> currentMouthShopCount = shopMapper.getCurrentMouthShopCount(userId);
                double currentShopCount = new Double(currentMouthShopCount.get("currentCount"));
                double lastShopCount = new Double(currentMouthShopCount.get("lastCount"));
                String shopRate = calculateGrowthRate(currentShopCount, lastShopCount);
                map.put("shopRate", shopRate);
                map.put("currentShopCount", currentShopCount);

                Map<String, Long> currentMouthShopSignCount = shopSignMapper.getCurrentMouthCount(userId);
                double currentShopSignCount = new Double(currentMouthShopSignCount.get("currentCount"));
                double lastShopSignCount = new Double(currentMouthShopSignCount.get("lastCount"));
                String shopSignRate = calculateGrowthRate(currentShopSignCount, lastShopSignCount);
                map.put("shopSignRate", shopSignRate);
                map.put("currentShopSignCount", currentShopSignCount);

                Map<String, Long> currentMouthShopWaitCount = shopMapper.getCurrentMouthShopWaitCount(userId);
                double currentShopWaitCount = new Double(currentMouthShopWaitCount.get("currentCount"));
                double lastShopWaitCount = new Double(currentMouthShopWaitCount.get("lastCount"));
                String shopWaitRate = calculateGrowthRate(currentShopWaitCount, lastShopWaitCount);
                map.put("shopWaitRate", shopWaitRate);
                map.put("currentShopWaitCount", currentShopWaitCount);
                break;

            case "design":
                map.put("role", "design");
                Map<String, Long> currentMouthDesignCount = designMapper.getCurrentMouthDesignCount(userId);
                double currentDesignCount = new Double(currentMouthDesignCount.get("currentCount"));
                double lastDesignCount = new Double(currentMouthDesignCount.get("lastCount"));
                String designRate = calculateGrowthRate(currentDesignCount, lastDesignCount);
                map.put("designRate", designRate);
                map.put("currentDesignCount", currentDesignCount);
                break;
            case "manage":
                map.put("role", "manage");
                Map<String, Long> currentMouthBuildCount = buildMapper.getCurrentMouthBuildCount(userId);
                double currentBuildCount = new Double(currentMouthBuildCount.get("currentCount"));
                double lastBuildCount = new Double(currentMouthBuildCount.get("lastCount"));
                String buildRate = calculateGrowthRate(currentBuildCount, lastBuildCount);
                map.put("buildRate", buildRate);
                map.put("currentBuildCount", currentBuildCount);

                Map<String, Long> currentMouthBuildDownCount = buildMapper.getCurrentMouthBuildDownCount(userId);
                double currentBuildDownCount = new Double(currentMouthBuildDownCount.get("currentCount"));
                double lastBuildDownCount = new Double(currentMouthBuildDownCount.get("lastCount"));
                String buildDownRate = calculateGrowthRate(currentBuildDownCount, lastBuildDownCount);
                map.put("buildDownRate", buildDownRate);
                map.put("currentBuildDownCount", currentBuildDownCount);

                break;
        }
        return map;
    }

    @Override
    public String selectUserOpenId() {
        return userMapper.selectUserOpenId();
    }

    public static String calculateGrowthRate(double currentValue, double previousValue) {
        // 处理除数为0的情况
        if (previousValue == 0) {
            if (currentValue > 0) {
                return "0.00%"; // 正无穷大
            } else if (currentValue < 0) {
                return "0.00%"; // 负无穷大
            } else {
                return "0.00%"; // 0/0情况
            }
        }

        double growthRate = ((currentValue - previousValue) / previousValue) * 100;

        // 格式化并添加符号
        return formatGrowthRate(growthRate);
    }

    private static String formatGrowthRate(double growthRate) {
        DecimalFormat df = new DecimalFormat("0.00");

        if (growthRate > 0) {
            return "+" + df.format(growthRate) + "%";
        } else if (growthRate < 0) {
            return df.format(growthRate) + "%"; // 负数自带负号
        } else {
            return "0.00%";
        }
    }
}
