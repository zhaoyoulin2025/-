<template>
    <view class="container">

        <!-- 顶部筛选：搜索 + 城市 + 区域 + 跟进人 -->
        <view class="filters-bar">
            <input class="search-input" v-model="keyword" placeholder="关键词搜索..." confirm-type="search"
                @confirm="searchList" @input="onKeywordInput" />
            <picker mode="selector" :range="cityOptions" :value="selectedCityIndex" @change="handleCityChange"
                @cancel="handleCityCancel">
                <view class="picker-chip">
                    <text>城市</text>
                    <text class="cuIcon-unfold"></text>
                </view>
            </picker>
            <picker mode="selector" :range="regionOptions" :value="selectedRegionIndex" @change="handleRegionChange"
                @cancel="handleRegionCancel" range-key="region">
                <view class="picker-chip">
                    <text>区域</text>
                    <text class="cuIcon-unfold"></text>
                </view>
            </picker>
            <picker mode="selector" :range="followerOptions" :value="selectedFollowerIndex" range-key="nickName"
                @change="handleFollowerChange" @cancel="handleFollowerCancel">
                <view class="picker-chip">
                    <text>跟进人：{{ selectedFollowerNickName }}</text>
                    <text class="cuIcon-unfold"></text>
                </view>
            </picker>
        </view>

        <!-- 列表 -->
        <view class="customer-list">
            <view v-for="(customer, index) in customerList" :key="index" class="shop-card"
                @tap="viewDetail(customer.id)" hover-class="shop-card-hover">
                <view class="card-header">
                    <text class="card-title">{{ customer.name }}</text>
                    <!-- 原右侧跟进人移除，避免占用标题行 -->
                </view>
                <view class="card-address">{{ customer.address }}</view>
                <!-- 城市、区域 与 跟进人 同一行 -->
                <view class="card-tags">
                    <view class="tags-left">
                        <view class="tag tag-blue">{{ customer.city || '-' }}</view>
                        <view class="tag tag-green">{{ customer.region || '-' }}</view>
                    </view>
                    <text class="card-follower">跟进人：{{ customer.followerName || customer.followName ||
                        customer.guideName || '-' }}</text>
                </view>
            </view>


            <!-- 移除详情按钮 -->
            <!-- <view class="action-buttons">
                    <button class="btn-detail" 详情</button>
                </view> -->
        </view>
        <!-- 在列表末尾加占位，避免被底部栏遮挡（必须在列表内部） -->
        <!-- <view class="bottom-spacer"></view> -->
        <!-- 底部固定操作栏（保持在 container 内部） -->
        <view class="bottom-action">
            <button class="action-btn" @click="insertDistrict">新增商圈</button>
            <button class="action-btn" @click="followUp">新增笔记</button>
            <button class="action-btn" @click="pushNote">推送</button>
        </view>

        <!-- 推送弹窗：包含商圈多选 + 用户多选 -->
        <view v-if="showPushDialog" class="modal-overlay">
            <view class="modal">
                <view class="modal-header">推送商圈</view>
                <view class="modal-body">
                    <view class="section-title">选择商圈（可多选）</view>
                    <checkbox-group class="options-grid" @change="handleShopCheckboxChange">
                        <label v-for="opt in pushShopOptions" :key="opt.value" class="option-item">
                            <checkbox :value="opt.value" :checked="selectedShopIds.includes(opt.value)" />
                            <text class="option-label">{{ opt.label }}</text>
                        </label>
                    </checkbox-group>

                    <view class="section-title">选择用户（可多选）</view>
                    <checkbox-group class="options-grid" @change="handleUserCheckboxChange">
                        <label v-for="user in pushUserOptions" :key="user.value" class="option-item">
                            <checkbox :value="user.value" :checked="selectedUserIds.includes(user.value)" />
                            <text class="option-label">{{ user.label }}</text>
                        </label>
                    </checkbox-group>
                </view>
                <view class="modal-actions">
                    <button class="action-btn" @click="cancelPush">取消</button>
                    <button class="action-btn primary" @click="confirmPush">确认推送</button>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
import {
    getUserInfo,

} from '@/utils/auth';
// 不再使用静态城市列表
// import { cityList as cityOptions } from '@/utils/city.js'

export default {
    data() {
        return {
            currentFilterIndex: 0,
            // 客户列表数据
            customerList: [

            ],
            cityOptions: [],
            selectedCityIndex: -1,
            selectedCity: '',
            regionOptions: [],
            selectedRegionIndex: -1,
            selectedRegion: '',
            followerOptions: [], // [{ userId, nickName }]
            selectedFollowerIndex: -1,
            selectedFollower: '', // 保存所选 userId
            selectedFollowerNickName: '', // 新增：用于展示所选跟进人的昵称
            keyword: '',          // 搜索关键词
            searchTimer: null,    // 防抖定时器
            showPushDialog: false,
            pushShopOptions: [],     // [{ value, label }]
            pushUserOptions: [],     // [{ value, label }]
            selectedShopIds: [],     // 多选商圈的 id（字符串数组）
            selectedUserIds: [],     // 多选用户的 id（字符串数组）
        }
    },
    onLoad() {
        // 页面加载时获取客户列表
        this.fetchCustomerList();
        this.loadFollowers(); // 加载跟进人供选择
        this.loadRegions();
    },
    methods: {
        // 点击“推送”打开弹窗，并准备选项
        pushNote() {
            this.buildPushShopOptions()
            this.loadPushUsers()
            this.showPushDialog = true
        },
        // 从当前列表构建商圈选项
        buildPushShopOptions() {
            const list = Array.isArray(this.customerList) ? this.customerList : []
            this.pushShopOptions = list.map(c => ({
                value: String(c.id || c.customerId || c.shopId || ''),
                label: c.name || c.shopName || c.customerName || ''
            })).filter(o => !!o.value && !!o.label)
        },
        // 加载用户选项（可替换为你的用户接口）
        loadPushUsers() {
            util.request('wx/api/getAlCustomerList', {}, 'GET', 'application/json', false, true)
                .then(res => {
                    if (res.code === 200 && Array.isArray(res.data)) {
                        this.pushUserOptions = res.data.map(u => ({
                            value: String(u.userId || u.id),
                            label: u.nickName || u.name || u.realName || u.username || ''
                        })).filter(o => !!o.value && !!o.label)
                    } else {
                        this.pushUserOptions = []
                    }
                })
                .catch(() => { this.pushUserOptions = [] })
        },
        // 商圈多选变化
        handleShopCheckboxChange(e) {
            this.selectedShopIds = Array.isArray(e.detail?.value) ? e.detail.value : []
        },
        // 用户多选变化
        handleUserCheckboxChange(e) {
            this.selectedUserIds = Array.isArray(e.detail?.value) ? e.detail.value : []
        },
        // 关闭弹窗
        cancelPush() {
            this.showPushDialog = false
        },
        // 确认推送（这里先做前端提示，后续可调用推送接口）
        confirmPush() {
            if (!this.selectedShopIds.length) {
                uni.showToast({ title: '请选择商圈', icon: 'none' })
                return
            }
            if (!this.selectedUserIds.length) {
                uni.showToast({ title: '请选择用户', icon: 'none' })
                return
            }
            this.showPushDialog = false
            uni.showLoading({
                title: '推送中',
                mask: true
            })
            console.log('确认推送：', {
                商圈: this.selectedShopIds.join(','),
                用户: this.selectedUserIds.join(',')
            })
            let that = this
            util.request("wx/api/saveUserLookBusinessDistrict", { userIds: this.selectedUserIds, districtIds: this.selectedShopIds }, "POST").then(function (res) {
                uni.hideLoading();
                if (res.code === 200) {
                    that.selectedShopIds = []
                    that.selectedUserIds = []
                    uni.showToast({
                        icon: "none",
                        title: "推送成功",
                        duration: 2000
                    })

                } else {
                    uni.showToast({
                        icon: "none",
                        title: res.msg,
                        duration: 2000
                    })
                }
            })

            // 可替换为你的推送接口
            // util.request('wx/api/pushNote', {
            //     shopIds: this.selectedShopIds.join(','),
            //     userIds: this.selectedUserIds.join(',')
            // }, 'POST', 'application/json', false, true).then(...)


        },
        onKeywordInput(e) {
            // 当前输入值（含清空场景）
            this.keyword = (e && e.detail && typeof e.detail.value === 'string')
                ? e.detail.value
                : this.keyword

            // 防抖：200ms 后触发查询
            if (this.searchTimer) clearTimeout(this.searchTimer)
            this.searchTimer = setTimeout(() => {
                this.fetchCustomerList()
            }, 200)
        },
        handleCityChange(e) {
            const idx = Number(e.detail.value)
            this.selectedCityIndex = idx
            this.selectedCity = this.cityOptions[idx] || ''
            this.fetchCustomerList()
        },
        handleCityCancel() {
            this.selectedCityIndex = -1
            this.selectedCity = ''
            this.regionOptions = []
            this.selectedRegionIndex = -1
            this.selectedRegion = ''
            this.fetchCustomerList()
        },
        handleRegionChange(e) {
            const idx = Number(e.detail.value)
            this.selectedRegionIndex = idx
            const item = this.regionOptions[idx]
            // 支持字符串或对象两种 range
            this.selectedRegion = typeof item === 'string'
                ? item
                : (item && (item.region)) || ''
            this.fetchCustomerList()
        },
        handleRegionCancel() {
            this.selectedRegionIndex = -1
            this.selectedRegion = ''
            this.fetchCustomerList()
        },
        handleFollowerChange(e) {
            const idx = Number(e.detail.value)
            this.selectedFollowerIndex = idx
            const item = this.followerOptions[idx]
            // 保存所选的 userId 和 nickName
            this.selectedFollower = item ? item.userId : ''
            this.selectedFollowerNickName = item ? item.nickName : ''
            this.fetchCustomerList()
        },
        handleFollowerCancel() {
            this.selectedFollowerIndex = -1
            this.selectedFollower = ''
            this.selectedFollowerNickName = ''
            this.fetchCustomerList()
        },

        // 城市列表（接口获取）
        loadCities() {
            util.request('wx/api/cityList', {}, 'GET', 'application/json', false, true)
                .then(res => {
                    if (res.code === 200) {
                        this.cityOptions = this.normalizeStrings(res.data, ['name', 'cityName', 'city', 'label'])
                    } else {
                        this.cityOptions = []
                    }
                })
                .catch(() => { this.cityOptions = [] })
        },
        // 区域列表（接口获取，依赖城市）
        loadRegions() {
            util.request('wx/api/getDistrictRegionList', {}, 'GET', 'application/json', false, true)
                .then(res => {
                    if (res.code === 200) {
                        this.regionOptions = res.data.map(item => ({
                            id: item.id,
                            region: item.region
                        }))
                    } else {
                        this.regionOptions = []
                    }
                })
                .catch(() => { this.regionOptions = [] })
        },
        // 跟进人列表（接口获取）
        loadFollowers() {
            util.request('wx/api/getAllFollowerList', {}, 'GET', 'application/json', false, true)
                .then(res => {
                    if (res.code === 200 && Array.isArray(res.data)) {
                        // 统一映射为 { userId, nickName }
                        this.followerOptions = res.data.map(item => ({
                            userId: item.userId,
                            nickName: item.nickName
                        })).filter(i => !!i.userId && !!i.nickName)
                    } else {
                        this.followerOptions = []
                    }
                })
                .catch(() => { this.followerOptions = [] })
        },
        handleFollowerChange(e) {
            const idx = Number(e.detail.value)
            this.selectedFollowerIndex = idx
            const item = this.followerOptions[idx]
            // 保存所选的 userId，用于查询
            this.selectedFollower = item ? item.userId : ''
            this.fetchCustomerList()
        },
        handleFollowerCancel() {
            this.selectedFollowerIndex = -1
            this.selectedFollower = ''
            this.fetchCustomerList()
        },

        fetchCustomerList() {
            console.log('获取客户列表数据');
            uni.showLoading({ title: '加载中...' });

            const params = {
                keyword: (this.keyword || '').trim(),
                region: (this.selectedRegion || '').trim(), // 新增：传入 region
                followerId: this.selectedFollower // 新增：传入 followerId
            };


            // 显示加载中
            uni.showLoading({
                title: '加载中...'
            });


            // 保留原有角色/状态逻辑
            // params.guideId = getUserInfo().userId;
            getUserInfo().roles.forEach(role => {
                if (role.roleKey != 'attract' || role.roleKey != 'attractMaster') {
                    params.status = 4;
                }
            });

            util.request('wx/api/districtList', params, 'GET', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading()
                    if (res.code === 200) {
                        this.customerList = res.data;
                    } else {
                        uni.showToast({
                            title: res.msg,
                            icon: 'none'
                        })
                    }
                })
                .catch(err => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '网络异常，请稍后再试',
                        icon: 'none'
                    })
                })
            uni.hideLoading();

            // 调用API获取客户列表
            // 实际项目中应该使用真实API
            // util.request('customer/list', params, 'GET').then(res => {
            // 	if (res.code === 200) {
            // 		this.customerList = res.data;
            // 	} else {
            // 		util.toast(res.msg);
            // 	}
            // 	uni.hideLoading();
            // }).catch(err => {
            // 	util.toast('获取数据失败');
            // 	uni.hideLoading();
            // });

            // 模拟数据，实际开发中请替换为真实API调用

        },


        // 查看客户详情
        viewDetail(customerId) {
            uni.navigateTo({
                url: `/shops/districtDetail?id=${customerId}`
            });
        },

        // 客户跟进
        followUp() {
            uni.navigateTo({
                url: `/shops/noteAdd`
            });
        },

        insertDistrict() {
            uni.navigateTo({
                url: `/shops/addDistrict`
            });
        }

    }
}
</script>

<style>
/* template 区域调整后，补充/统一以下样式，避免遮挡 */
.container {
    padding: 20rpx;
    background-color: #f5f5f5;
    min-height: 100vh;
    position: relative;

}

/* 仅保留一个 .bottom-action 定义，并保持 z-index */
.bottom-action {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 999;
    width: 100%;
    box-sizing: border-box;
    display: flex;
    padding: 64rpx 24rpx;
    gap: 24rpx;
    background: #fff;
    box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.06);
}

/* 关键：用 padding-bottom 顶起列表，移除/覆盖 height: calc(...) */
.customer-list {
    height: auto !important;
    padding-bottom: 60rpx;
}

/* 列表占位与容器预留一致 */
.bottom-spacer {
    height: 260rpx;
}

/* 兜底处理：即使没有占位，最后一个卡片也不被遮挡 */
.shop-card:last-child {
    margin-bottom: 260rpx;
}

.filters-bar {
    background: #fff;
    padding: 16rpx;
    border-radius: 16rpx;
    margin-bottom: 16rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    gap: 12rpx;
}

.search-input {
    flex: 1;
    height: 64rpx;
    padding: 0 24rpx;
    border-radius: 32rpx;
    background: #f5f5f5;
    font-size: 28rpx;
    color: #333;
}

.picker-chip {
    min-width: 144rpx;
    height: 64rpx;
    padding: 0 18rpx;
    border-radius: 32rpx;
    background: #f5f5f5;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 26rpx;
    color: #666;
}

/* 列表卡片 */
.shop-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    padding: 24rpx;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12rpx 0;
    /* 上下留白 */
}

.card-title {
    flex: 1;
    /* 标题占剩余空间，避免挤压跟进人 */
    margin-right: 16rpx;
    font-size: 34rpx;
    font-weight: 700;
    color: #2d2d2d;
}

.card-follower {
    flex: 0 0 220rpx;
    /* 增加一点宽度（可调，比如 280rpx） */
    text-align: right;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 26rpx;
    color: #999;
}

.card-address {
    margin-top: 8rpx;
    font-size: 26rpx;
    color: #666;
}

.card-tags {
    margin-top: 12rpx;
    display: flex;
    align-items: center;
    /* 保持同一行显示，右侧跟进人靠右 */
}

.tags-left {
    display: flex;
    gap: 12rpx;
}

.card-follower {
    margin-left: auto;
    /* 推到右侧 */
    flex: 0 0 280rpx;
    /* 留足宽度（可按需调整） */
    text-align: right;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 26rpx;
    color: #999;
}


.tag {
    padding: 8rpx 16rpx;
    border-radius: 28rpx;
    font-size: 24rpx;
    line-height: 1;
}

.tag-blue {
    background: #e8f3ff;
    color: #2b6de5;
}

.tag-green {
    background: #eaf8ee;
    color: #2fab62;
}

/* 详情按钮（保留） */
.action-buttons {
    display: flex;
    padding-top: 12rpx;
}

.btn-detail {
    flex: 1;
    height: 72rpx;
    line-height: 72rpx;
    font-size: 28rpx;
    border-radius: 10rpx;
    background-color: #f5f5f5;
    color: #333;
}

/* 底部固定操作栏 */
.bottom-action {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 20rpx;
    background: #fff;
    box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.06);
    display: flex;
    padding: 64rpx 24rpx;
    gap: 24rpx;
}

.action-btn {
    flex: 1;
    height: 108rpx;
    line-height: 108rpx;
    border-radius: 18rpx;
    border: 3rpx solid #2979ff;
    /* 蓝色边框 */
    background: #fff;
    color: #2979ff;
    /* 蓝字 */
    font-size: 34rpx;
    font-weight: 700;
}

.action-btn.primary {
    background: #2979ff;
    color: #fff;
}

/* 筛选区域样式 */
.filter-section {
    background: #fff;
    padding: 20rpx 30rpx;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.picker-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 80rpx;
    font-size: 30rpx;
    color: #333;
}

/* 客户列表样式 */
.customer-list {
    height: calc(100vh - 240rpx);
    /* 调整为更合理的高度 */
}

.customer-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.customer-info {
    padding: 30rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1rpx solid #f0f0f0;
}

.info-left {
    flex: 1;
}

.field-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 10rpx;
}

.field-label {
    font-size: 28rpx;
    color: #666;
    min-width: 80rpx;
    margin-right: 10rpx;
}

.customer-name {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    flex: 1;
}

.customer-phone {
    font-size: 28rpx;
    color: #666;
}

.info-right {
    margin-left: 20rpx;
}

/* 状态标签样式 */
.status-tag {
    padding: 8rpx 16rpx;
    border-radius: 8rpx;
    font-size: 24rpx;
    color: #fff;
    text-align: center;
}

.status-potential {
    background: linear-gradient(135deg, #1890ff, #36a9ff);
    color: #fff;
    font-size: 30rpx;
    padding: 8rpx 16rpx;
    border-radius: 20rpx;

}

.status-intention {
    background: linear-gradient(135deg, #fa8c16, #ffa940);
    color: #fff;
    font-size: 30rpx;
    padding: 8rpx 16rpx;
    border-radius: 20rpx;
}

.status-deal {
    background: linear-gradient(135deg, #52c41a, #73d13d);
    color: #fff;
    font-size: 30rpx;
    padding: 8rpx 16rpx;
    border-radius: 20rpx;
}

.status-lost {
    background: linear-gradient(135deg, #8c8c8c, #bfbfbf);
    color: #fff;
    font-size: 30rpx;
    padding: 8rpx 16rpx;
    border-radius: 20rpx;
}

/* 按钮区域样式 */
.action-buttons {
    display: flex;
    padding: 20rpx 30rpx;
}

.action-buttons button {
    flex: 1;
    height: 80rpx;
    line-height: 80rpx;
    font-size: 28rpx;
    margin: 0 10rpx;
    border-radius: 8rpx;
}

.btn-detail {
    background-color: #f5f5f5;
    color: #333;
}

.btn-follow {
    background-color: #1890ff;
    color: #fff;
}

/* 无数据提示 */
.empty-tip {
    text-align: center;
    padding: 100rpx 0;
    color: #999;
    font-size: 28rpx;
}

/* 新增按钮区域样式 */
.add-section {
    background: #fff;
    padding: 20rpx 30rpx;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-add {
    width: 100%;
    height: 80rpx;
    background: #1890ff;
    color: #fff;
    border: none;
    border-radius: 12rpx;
    font-size: 32rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.3);
}

.btn-add .cuIcon-add {
    margin-right: 10rpx;
    font-size: 36rpx;
}

.btn-add:active {
    transform: translateY(2rpx);
    box-shadow: 0 2rpx 8rpx rgba(0, 46, 248, 0.3);
}

/* 客户列表样式调整 */
.customer-list {
    height: calc(100vh - 320rpx);
    /* 调整高度以适应新增按钮 */
}

.modal-overlay {
    position: fixed;
    z-index: 1000;
    inset: 0;
    background: rgba(0, 0, 0, 0.45);
    display: flex;
    align-items: center;
    justify-content: center;
}

.modal {
    width: 86%;
    max-height: 70vh;
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
}

.modal-header {
    padding: 24rpx;
    font-size: 32rpx;
    font-weight: 700;
    border-bottom: 1rpx solid #f0f0f0;
}

.modal-body {
    padding: 24rpx;
    overflow-y: auto;
}

.section-title {
    font-size: 28rpx;
    color: #333;
    margin: 12rpx 0 16rpx;
}

.options-grid {
    display: flex;
    flex-wrap: wrap;
}

.options-grid .option-item {
    flex: 0 0 50%;
    box-sizing: border-box;
    padding: 8rpx 12rpx 8rpx 0;
    display: flex;
    align-items: center;
}

.option-label {
    margin-left: 12rpx;
    font-size: 28rpx;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.modal-actions {
    display: flex;
    gap: 24rpx;
    padding: 24rpx;
    border-top: 1rpx solid #f0f0f0;
}
</style>
