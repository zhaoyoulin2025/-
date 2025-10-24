<template>
    <view class="follow-container">
        <!-- 顶部标题和添加按钮 -->
        <view class="header">
            <text class="title">签约记录</text>
            <button class="add-btn" @click="showAddDialog">添加签约</button>
        </view>

        <!-- 记录列表 -->
        <view class="record-list">
            <view v-if="followRecords.length === 0" class="empty-tip">
                <text>暂无签约记录</text>
            </view>

            <view v-else>
                <view v-for="(record, index) in followRecords" :key="index" class="record-item">
                    <view class="record-header">
                        <text class="record-name">{{ record.shopName }}</text>
                        <!-- <text class="record-time">{{ record.saleTime }}</text> -->
                    </view>
                    <view class="record-detail">
                        <text class="detail-item">签约客户：{{ record.clientName }}</text>
                    </view>
                    <view class="record-detail">
                        <text class="detail-item">签约时间：{{ record.saleTime }}</text>
                    </view>
                    <!-- <view class="record-detail">
                        <text class="detail-item">租期：{{ record.year }}年</text>
                    </view> -->
                </view>
            </view>
        </view>

        <!-- 添加记录弹窗 -->
        <view class="dialog-mask" v-if="showDialog" @click="cancelAdd"></view>
        <view class="dialog" v-if="showDialog">
            <view class="dialog-header">
                <text class="dialog-title">添加签约记录</text>
            </view>
            <view class="dialog-body">
                <picker mode="selector" :range="clientList" range-key="name" @change="onClientChange">
                    <view class="picker">
                        {{ clientList[clientIndex] ? clientList[clientIndex].name : '请选择客户' }}
                    </view>
                </picker>
                <picker mode="selector" :range="shopList" range-key="name" @change="onShopChange">
                    <view class="picker">
                        {{ shopList[shopIndex] ? shopList[shopIndex].name : '请选择商铺' }}
                    </view>
                </picker>
                <input class="dialog-input" type="number" v-model="newRecord.money" placeholder="请输入金额(元/月)" />
                <input class="dialog-input" type="number" v-model="newRecord.year" placeholder="请输入年数" />
            </view>
            <view class="dialog-footer">
                <button class="btn-cancel" @click="cancelAdd">取消</button>
                <button class="btn-save" @click="saveRecord">保存</button>
            </view>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
import {
    getUserInfo,
} from '@/utils/auth';
export default {
    data() {
        return {
            showDialog: false, // 是否显示添加记录弹窗
            newRecord: {
                clientId: '',
                shopId: '',
                money: '',
                year: ''
            },
            followRecords: [], // 跟进记录列表
            clientList: [],
            shopList: [],
            clientIndex: -1,
            shopIndex: -1
        }
    },
    onLoad() {
        this.fetchFollowRecords();
    },
    methods: {
        // 获取跟进记录列表
        fetchFollowRecords() {
            console.log("fetchFollowRecords")
            // 这里是模拟数据，实际开发时替换为接口调用
            let that = this;
            uni.showLoading({ title: '加载中', mask: true });
            util.request('wx/api/followShopList', {},'GET')
                .then(res => {
                    uni.hideLoading()
                    if (res.code === 200) {
                        that.followRecords = res.data;
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
        },

        // 获取客户列表
        fetchClients() {
            let that = this;
            util.request('wx/api/clientList', {guideId:getUserInfo().userId}, 'GET').then(res => {
                if (res.code === 200) {
                    that.clientList = res.data;
                }
            });
        },

        // 获取商铺列表
        fetchShops() {
            let that = this;
            util.request('wx/api/shopList', {shopStatus:0}, 'GET').then(res => {
                if (res.code === 200) {
                    that.shopList = res.data;
                }
            });
        },

        onClientChange(e) {
            this.clientIndex = e.detail.value;
            this.newRecord.clientId = this.clientList[this.clientIndex].id;
        },

        onShopChange(e) {
            this.shopIndex = e.detail.value;
            this.newRecord.shopId = this.shopList[this.shopIndex].id;
        },

        // 显示添加记录弹窗
        showAddDialog() {
            this.showDialog = true;
            this.newRecord = { clientId: '', shopId: '', money: '', year: '' };
            this.clientIndex = -1;
            this.shopIndex = -1;
            this.fetchClients();
            this.fetchShops();
        },

        // 取消添加
        cancelAdd() {
            this.showDialog = false;
        },

        // 保存记录
        saveRecord() {
            if (!this.newRecord.clientId) {
                uni.showToast({ title: '请选择客户', icon: 'none' });
                return;
            }
            if (!this.newRecord.shopId) {
                uni.showToast({ title: '请选择商铺', icon: 'none' });
                return;
            }
            if (!this.newRecord.money) {
                uni.showToast({ title: '请输入金额', icon: 'none' });
                return;
            }
            if (!this.newRecord.year) {
                uni.showToast({ title: '请输入年数', icon: 'none' });
                return;
            }

            const record = {
                saleId: getUserInfo().userId,
                clientId: this.newRecord.clientId,
                shopId: this.newRecord.shopId,
                money: this.newRecord.money,
                year: this.newRecord.year
            };
            uni.showLoading({ title: '提交中', mask: true });
            util.request('wx/api/insertShopFollow', record, 'POST', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading();
                    if (res.code === 200) {
                        uni.showToast({ title: '保存成功', icon: 'success' });
                        this.showDialog = false;
                        this.fetchFollowRecords(); // 刷新列表
                    } else {
                        uni.showToast({ title: res.msg, icon: 'none' });
                    }
                })
                .catch(err => {
                    uni.hideLoading();
                    uni.showToast({ title: '网络异常，请稍后再试', icon: 'none' });
                });
        },

        // 格式化日期
        formatDate(date) {
            const year = date.getFullYear();
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const day = date.getDate().toString().padStart(2, '0');
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');

            return `${year}-${month}-${day} ${hours}:${minutes}`;
        }
    }
}
</script>

<style>
.follow-container {
    padding: 20rpx;
    background-color: #f5f5f5;
    min-height: 100vh;
}

/* 顶部标题和按钮 */
.header {
    display: flex;
    justify-content: space-between; /* 实现标题居左，按钮居右 */
    align-items: center; /* 垂直居中对齐 */
    padding: 20rpx 30rpx;
    margin-bottom: 20rpx;
    background-color: #fff;
    border-radius: 16rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.add-btn {
    width: 160rpx;
    height: 68rpx;
    background: linear-gradient(135deg, #1890ff, #40a9ff);
    color: #fff;
    font-size: 26rpx;
    border-radius: 34rpx;
    border: none;
    padding: 0;
    box-shadow: 0 4rpx 12rpx rgba(24, 144, 255, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: -10rpx; /* 负边距，让按钮更靠右 */
}

.add-btn:active {
    background: linear-gradient(135deg, #0050b3, #1890ff);
    transform: translateY(1rpx);
}

/* 记录列表 */
.record-list {
    margin-top: 20rpx;
}

.record-item {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 20rpx 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.record-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
    padding-bottom: 16rpx;
}

.record-detail {
    margin-top: 20rpx;
}

.detail-item {
    display: flex;
    align-items: center;
    font-size: 28rpx; /* 调整字体大小 */
    color: #666;
    margin-bottom: 20rpx; /* 调整行距 */
}

.detail-item::before {
    content: '·'; /* 使用圆点作为前缀 */
    margin-right: 10rpx;
    color: #1890ff; /* 品牌蓝色 */
    font-weight: bold;
}

.record-name {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
}

.record-time {
    font-size: 26rpx;
    color: #999;
}

.record-content {
    font-size: 28rpx;
    color: #666;
    line-height: 1.5;
}

/* 弹窗样式 */
.dialog-mask {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}

.dialog {
    position: fixed;
    left: 50%;
    top: 30%;
    transform: translate(-50%, -50%);
    width: 80%;
    background-color: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    z-index: 1000;
    box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.15);
}

.dialog-header {
    padding: 30rpx;
    text-align: center;
    border-bottom: 1rpx solid #f0f0f0;
}

.dialog-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.dialog-body {
    padding: 30rpx;
}

.picker {
    height: 80rpx;
    line-height: 80rpx;
    border: 1rpx solid #e8e8e8;
    border-radius: 8rpx;
    padding: 0 20rpx;
    margin-bottom: 20rpx;
    font-size: 28rpx;
    color: #333;
    background-color: #f9f9f9;
}

.dialog-input {
    height: 80rpx;
    line-height: 80rpx;
    border: 1rpx solid #e8e8e8;
    border-radius: 8rpx;
    padding: 0 20rpx;
    margin-bottom: 20rpx;
    font-size: 28rpx;
    color: #333;
    background-color: #f9f9f9;
}

.dialog-textarea {
    width: 100%;
    height: 240rpx;
    border: 1rpx solid #e8e8e8;
    border-radius: 8rpx;
    padding: 20rpx;
    font-size: 28rpx;
    color: #333;
    background-color: #f9f9f9;
}

.dialog-footer {
    display: flex;
    border-top: 1rpx solid #f0f0f0;
}

.dialog-footer button {
    flex: 1;
    height: 90rpx;
    line-height: 90rpx;
    text-align: center;
    font-size: 30rpx;
    border-radius: 0;
    margin: 0;
}

.btn-cancel {
    background-color: #f5f5f5;
    color: #666;
}

.btn-save {
    background: linear-gradient(135deg, #1890ff, #0050b3);
    color: #fff;
}

/* 无数据提示 */
.empty-tip {
    text-align: center;
    padding: 100rpx 0;
    color: #999;
    font-size: 28rpx;
}
</style>