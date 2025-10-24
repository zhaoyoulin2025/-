package com.ruoyi.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WxRequestUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.domain.ReviceCheckImageVO;
import com.ruoyi.system.domain.WxDesignUpdateStatusDTO;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import com.ruoyi.utils.SignUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


@RestController
@RequestMapping("/wx/api")
public class WxApiController extends BaseController {

    @Autowired
    private WxRequestUtils wxRequestUtils;

    @Autowired
    private IFranchiseInvestmentFormService franchiseInvestmentFormService;

    @Autowired
    private IInviteService inviteService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private ICustomerFollowUpService customerFollowUpService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private UserClockService userClockService;

    @Autowired
    private SendMessage sendMessageService;

    @Autowired
    private IShopSignService shopSignService;

    @Autowired
    private INoteService noteService;

    @Autowired
    private IBusinessDistrictService businessDistrictService;


    @Autowired
    private ISysUserService userService;

    @Autowired
    private IDesignService designService;

    @Autowired
    private IBuildService buildService;

    @Autowired
    private IBuildManageService buildManageService;

    @Autowired
    private ClientDistrictService clientDistrictService;


    public String baiduUrl = "https://api.map.baidu.com/reverse_geocoding/v3/";

    @Value("${baiduAk}")
    private String baiduAk;


    @PostMapping("/saveQuestion")
    public AjaxResult saveQuestion(@RequestBody Client client) {
        Client enity = new Client();
        BeanUtils.copyProperties(client, enity);
        enity.setId(client.getClientId());
        clientService.updateById(enity);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(client.getUserId());
        sysUser.setUserName(client.getName());
        sysUser.setNickName(client.getName());
        userService.updateWxUser(sysUser);
        return AjaxResult.success("提交成功");
    }

    @GetMapping("/inviteList")
    public AjaxResult inviteList(Long clientId) {
        Invite invite = new Invite();
        invite.setClientId(clientId);
        return AjaxResult.success(inviteService.selectInviteList(invite));
    }


    @PostMapping("/confirmInvite")
    public AjaxResult confirmInvite(@RequestBody Invite insertInviteDTO) {
        UpdateWrapper<Invite> inviteUpdateWrapper = new UpdateWrapper<>();
        inviteUpdateWrapper.eq("id",insertInviteDTO.getId());
        inviteUpdateWrapper.set("inviteStatus",1);
        inviteUpdateWrapper.set("confirmTime",new Date());
        return AjaxResult.success(inviteService.update(inviteUpdateWrapper));
    }

    @GetMapping("/inviteQueryByDate")
    public AjaxResult inviteQueryByDate(String date) {
        return AjaxResult.success(inviteService.inviteQueryByDate(date));
    }

    @PostMapping("/insertInvite")
    @ResponseBody
    public AjaxResult insertInviteByWx(@RequestBody Invite insertInviteDTO) {
        // 创建日期格式器
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = dateFormat.parse(insertInviteDTO.getDateStr());
            insertInviteDTO.setInvitationTime(parse);
            inviteService.save(insertInviteDTO);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    @GetMapping("/cancelInvite")
    public AjaxResult cancelInvite(Long id) {
        inviteService.cancelInvite(id);
        return AjaxResult.success();
    }

    @GetMapping("/clientList")
    public AjaxResult clientList(Client client) {
        LambdaQueryWrapper<Client> clientLambdaQueryWrapper = new LambdaQueryWrapper<>();
        clientLambdaQueryWrapper.eq(null != client.getGuideId(),Client::getGuideId, client.getGuideId());
        clientLambdaQueryWrapper.eq(null != client.getClientStatus(), Client::getClientStatus, client.getClientStatus());
        clientLambdaQueryWrapper.eq(null != client.getExpectedCity(), Client::getExpectedCity, client.getExpectedCity());

        if (StringUtils.isNotEmpty(client.getLevelName())){
            String level = "";
            switch (client.getLevelName()){
                case "A":
                    level = "1";
                    break;
                case "B":
                    level = "2";
                    break;
                case "C":
                    level = "3";
                    break;
                case "D":
                    level = "4";
                    break;
            }
            clientLambdaQueryWrapper.eq(Client::getClientLevel, level);

        }
        if(StringUtils.isNotEmpty(client.getKeyWord())) {
            String keywords = client.getKeyWord();
            clientLambdaQueryWrapper.and(
                    wq -> wq
                            .like(Client::getName, keywords)
                            .or()
                            .like(Client::getPhone, keywords)
            );

        }


        return AjaxResult.success("", clientService.list(clientLambdaQueryWrapper));
    }

    @GetMapping("/clientDetail")
    public AjaxResult clientDetail(Long id) {
        return AjaxResult.success(clientService.selectClientById(id));
    }

    @GetMapping("/wxCustomerFollowList")
    public AjaxResult wxCustomerFollowList(Long clientId) {

        return AjaxResult.success("", customerFollowUpService.wxCustomerFollowList(clientId, getUserId()));
    }

    @PostMapping("/insertCustomerFollow")
    @ResponseBody
    public AjaxResult insertCustomerFollow(@RequestBody CustomerFollowUp customerFollowUp) {
        customerFollowUp.setFollowUpTime(new Date());
        customerFollowUpService.insertCustomerFollowUp(customerFollowUp);
        return AjaxResult.success();
    }

    @PostMapping("/insertShop")
    @ResponseBody
    public AjaxResult insertShop(@RequestBody Shop shop) {
        shop.setsTime(new Date());
        shop.setOwner(String.valueOf(getUserId()));
        shopService.save(shop);
        return AjaxResult.success();
    }

    @GetMapping("/getLocation")
    public AjaxResult getLocation(String latitude, String longitude) {
        String params = StrUtil.format("ak={}&location={}&output=json", baiduAk, latitude + "," + longitude);
        //开始发起网络请求,若依管理系统自带网络请求工具，直接使用即可
        String res = HttpUtils.sendGet(baiduUrl, params);
        JSONObject jsonObject = JSON.parseObject(res);
        JSONObject resultObj = jsonObject.getJSONObject("result");
        String address = resultObj.getString("formatted_address");
        return AjaxResult.success("", address);
    }

    @GetMapping("/getUserClockList")
    public AjaxResult getUserClockList() {
        LambdaQueryWrapper<UserClock> userClockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userClockLambdaQueryWrapper.eq(UserClock::getUserId, getUserId());
        return AjaxResult.success("", userClockService.list(userClockLambdaQueryWrapper));
    }

    @PostMapping("/insertUserClock")
    @ResponseBody
    public AjaxResult insertUserClock(@RequestBody UserClock userClock) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(userClock.getDateStr());
            userClock.setClockDate(parse);
            userClock.setUserId(getUserId());
            userClockService.save(userClock);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @GetMapping("/getMessageList")
    public AjaxResult getMessageList(String messageType) {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getReceiverId, getUserId());
        messageLambdaQueryWrapper.eq(StringUtils.isNotEmpty(messageType), Message::getMessageType, messageType);
        return AjaxResult.success("", sendMessageService.list(messageLambdaQueryWrapper));
    }

    @PostMapping("/readMessage")
    @Transactional
    public AjaxResult readMessage(@RequestBody Message message) {
        message.setReadTime(new Date());
        message.setStatus(1);
        sendMessageService.updateById(message);
        return AjaxResult.success();

    }

    @PostMapping("/readAllMessage")
    @ResponseBody
    public AjaxResult readAllMessage() {
        UpdateWrapper<Message> messageLambdaQueryWrapper = new UpdateWrapper<>();
        messageLambdaQueryWrapper.eq("receiver_id", getUserId());
        messageLambdaQueryWrapper.set("status", 1);
        sendMessageService.update(messageLambdaQueryWrapper);
        return AjaxResult.success();

    }

    @GetMapping("/shopList")
    public AjaxResult shopList(Shop shop) {
        LambdaQueryWrapper<Shop> shopLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shopLambdaQueryWrapper.eq(Shop::getFollower, getUserId());
        if(null == shop.getShopStatus()){
            shopLambdaQueryWrapper.isNull(Shop::getClientId);
        }
//        shopLambdaQueryWrapper.eq(null != shop.getShopStatus() && 9 != shop.getShopStatus(), Shop::getShopStatus, shop.getShopStatus());
        return AjaxResult.success("", shopService.list(shopLambdaQueryWrapper));
    }

    @GetMapping("/shopDetail")
    public AjaxResult shopDetail(Long id) {
        return AjaxResult.success(shopService.selectShopById(id));
    }

    @PostMapping("/updateShop")
    @ResponseBody
    public AjaxResult updateShop(@RequestBody Shop shop) {
        shop.setUpTime(new Date());
        shopService.updateById(shop);
        return AjaxResult.success();
    }

    @GetMapping("/followShopList")
    public AjaxResult followShopList() {
//        LambdaQueryWrapper<Shop> shopLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        shopLambdaQueryWrapper.eq(Shop::getFollower,getUserId());
//        shopLambdaQueryWrapper.isNotNull(Shop::getClientId);
        return AjaxResult.success("", shopService.wxFollowShopList(getUserId()));
    }

    @GetMapping("/myShopList")
    public AjaxResult myShopList(Shop shop) {
        return AjaxResult.success("", shopService.wxMyShopList(shop.getClientId()));
    }

    @PostMapping("/insertShopFollow")
    @ResponseBody
    public AjaxResult insertShopFollow(@RequestBody ShopSign shopSign) {
//        shopSign.setSaleTime(new Date());
//        shopSignService.save(shopSign);
        Shop shop = new Shop();
        shop.setId(shopSign.getShopId());
        shop.setClientId(shopSign.getClientId());
        shop.setRent(new BigDecimal(shopSign.getMoney()));
        shop.setShopStatus(2);
        shopService.updateById(shop);
        return AjaxResult.success();
    }

    @GetMapping("/allShopList")
    public TableDataInfo allShopList(Shop shop) {
        startPage();
        LambdaQueryWrapper<Shop> shopLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 关键字非空时才添加模糊条件
        if (StringUtils.isNotBlank(shop.getKeyWord())) {
            shopLambdaQueryWrapper.and(qw -> qw
                    .like(Shop::getName, shop.getKeyWord())
                    .or()
                    .like(Shop::getAddress, shop.getKeyWord())
            );
        }

        // 状态条件始终添加 (IN 1,2)
        shopLambdaQueryWrapper.in(Shop::getShopStatus, Arrays.asList(0));
        return getDataTable(shopService.list(shopLambdaQueryWrapper));
    }

    @GetMapping("/clientOwnShopList")
    public TableDataInfo clientOwnShopList(Long clientId) {
        startPage();
        return getDataTable(shopService.clientOwnShopList(clientId));
    }

    @GetMapping("/noteList")
    public AjaxResult noteList() {
        Note note = new Note();
        SysUser currentUser = getLoginUser().getUser();
        if (!currentUser.isAdmin()) {
            note.setFollowerId(getUserId());
        }
        return AjaxResult.success("", noteService.selectNoteList(note));
    }

    @GetMapping("/allNoteList")
    public AjaxResult allNoteList(Note note) {
        note.setStatusss("1");
        return AjaxResult.success("", noteService.selectNoteList(note));
    }

    @GetMapping(value = "/noteDetail")
    public AjaxResult getInfo(Long id) {
        return success(noteService.selectWXNoteById(id));
    }

    @GetMapping("/districtList")
    public AjaxResult districtList( BusinessDistrict businessDistrict) {

//        SysUser currentUser = getLoginUser().getUser();
//        if (!currentUser.isAdmin()) {
//            businessDistrict.setFollowerId(getUserId());
//        }

        return success(businessDistrictService.selectBusinessDistrictList(businessDistrict));
    }

    @PostMapping("/addNote")
    @ResponseBody
    public AjaxResult addNote(@RequestBody Note note) {
        note.setUpdatedAt(new Date());
        note.setFollowerId(getUserId());
        ArrayList<String> traceIdList = new ArrayList<>();
        String openId = userService.selectUserOpenId();
        //将文本，图片和视频 全部传给微信校验
        Boolean contentBool = wxRequestUtils.wxCheckContent(openId, note.getDescription());
        System.out.println("校验文本结果");
        System.out.println(contentBool);
        if (!contentBool) {
            return error("描述文本校验失败！");
        }
        //校验图片
        if (StringUtils.isNotEmpty(note.getImageUrl())) {
            String[] split = note.getImageUrl().split(",");
            for (String imgUrl : split) {
                String traceId = wxRequestUtils.wxCheckMedia(openId, imgUrl, 2);
                System.out.println("校验图片返回traceId");
                System.out.println(traceId);
                traceIdList.add(traceId);
            }

        }
        //校验视频
        if (StringUtils.isNotEmpty(note.getVideoUrl())) {
            String traceId = wxRequestUtils.wxCheckMedia(openId, note.getVideoUrl(), 1);
            System.out.println("校验视频返回traceId");
            System.out.println(traceId);
            traceIdList.add(traceId);
        }

        if(!traceIdList.isEmpty()){
            note.setTraceId(StringUtils.join(traceIdList,","));
        }
        note.setStatusss("0");
        noteService.insertNote(note);
        return success();
    }

    @GetMapping("/designList")
    public TableDataInfo designList() {
        startPage();
        return getDataTable(designService.wxDesignList(getUserId()));
    }

    @PostMapping("/updateDesignStatus")
    @ResponseBody
    public AjaxResult updateDesignStatus(@RequestBody WxDesignUpdateStatusDTO wxDesignUpdateStatusDTO) {
        designService.updateDesignStatus(wxDesignUpdateStatusDTO);
        return success();
    }

    @PostMapping("/wxInsertDistrict")
    @ResponseBody
    public AjaxResult wxInsertDistrict(@RequestBody BusinessDistrict businessDistrict) {
        businessDistrict.setCreatorId(getUserId());
        businessDistrict.setCreatedAt(new Date());
        businessDistrict.setStatues("0");
        businessDistrictService.wxInsertDistrict(businessDistrict);
        return success();
    }

    @GetMapping("/buildList")
    public AjaxResult buildList() {
        return AjaxResult.success(buildService.wxBuildList(getUserId()));
    }


    @PostMapping("/insertBuildManage")
    @ResponseBody
    public AjaxResult insertBuildManage(@RequestBody BuildManage buildManage) {
        buildManage.setUserId(getUserId());
        buildManageService.save(buildManage);
        return success();
    }

    @GetMapping("/buildManageList")
    public AjaxResult buildManageList(Long shopId) {
        LambdaQueryWrapper<BuildManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BuildManage::getShopId, shopId);
        return AjaxResult.success(buildManageService.list(queryWrapper));
    }

    @GetMapping("/getRoleMetrics")
    public AjaxResult getRoleMetrics() {
        return AjaxResult.success("",userService.getRoleMetrics(getUserId()));
    }


    @GetMapping("/allDistrictList")
    public TableDataInfo allDistrictList(BusinessDistrict businessDistrict ) {
        startPage();
        businessDistrict.setStatues("3");
        return getDataTable(businessDistrictService.selectBusinessDistrictList(businessDistrict));
    }

    @GetMapping("/getAllFollowerList")
    public AjaxResult getAllFollowerList() {
        return AjaxResult.success(userService.getAllFollowerList());
    }

    @GetMapping("/getAlCustomerList")
    public AjaxResult getAlCustomerList() {
        return AjaxResult.success(userService.getAlCustomerList());
    }

    @GetMapping("/getDistrictRegionList")
    public AjaxResult getDistrictRegionList() {
        return AjaxResult.success(businessDistrictService.getDistrictRegionList());
    }

    @PostMapping("/saveUserLookBusinessDistrict")
    public AjaxResult saveUserLookBusinessDistrict(@RequestBody ClientDistrict clientDistrict) {
        ArrayList<ClientDistrict> clientDistricts = new ArrayList<>();
        for (Long districtId : clientDistrict.getDistrictIds()) {
            for (Long userId : clientDistrict.getUserIds()) {
                ClientDistrict result = new ClientDistrict();
                result.setBusinessDistrictId(districtId);
                result.setUserId(userId);
                clientDistricts.add(result);
            }
        }
        clientDistrictService.saveBatch(clientDistricts);
        clientDistrictService.deleteRepeatData();
        return AjaxResult.success();
    }

}
