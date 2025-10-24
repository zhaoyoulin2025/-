<template>
    <view class="follow-container">
        <!-- 顶部标题和添加按钮 -->
        <view class="header">
            <text class="title">跟进记录</text>
            <button class="add-btn" @click="showAddDialog">添加记录</button>
        </view>

        <!-- 记录列表 -->
        <view class="record-list">
            <view v-if="followRecords.length === 0" class="empty-tip">
                <text>暂无跟进记录</text>
            </view>

            <view v-else>
                <view v-for="(record, index) in followRecords" :key="index" class="record-item">
                    <view class="record-header">
                        <text class="record-name">{{ record.nickName }}</text>
                        <text class="record-time">{{ record.followUpTime }}</text>
                    </view>
                    <view class="record-content">
                        <text>{{ record.followUpDescription }}</text>
                    </view>
                </view>
            </view>
        </view>

        <!-- 添加记录弹窗 -->
        <view class="dialog-mask" v-if="showDialog" @click="cancelAdd"></view>
        <view class="dialog" v-if="showDialog">
            <view class="dialog-header">
                <text class="dialog-title">添加跟进记录</text>
            </view>
            <view class="dialog-body">
                <textarea class="dialog-textarea" v-model="newRecord.content" placeholder="请输入跟进内容..." :maxlength="-1"
                    :auto-height="false" :show-confirm-bar="false" :cursor-spacing="100" :rows="5"></textarea>
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
            customerId: '', // 客户ID
            showDialog: false, // 是否显示添加记录弹窗
            newRecord: {
                content: '' // 新记录内容
            },
            followRecords: [] // 跟进记录列表
        }
    },
    onLoad(options) {
        if (options.id) {
            this.customerId = options.id;
            this.fetchFollowRecords();
        }
    },
    methods: {
        // 获取跟进记录列表
        fetchFollowRecords() {
            // 这里是模拟数据，实际开发时替换为接口调用
            let that = this;
            uni.showLoading({ title: '加载中', mask: true });
            util.request('wx/api/wxCustomerFollowList', { clientId: this.customerId }, 'GET', 'application/json', false, true)
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

        // 显示添加记录弹窗
        showAddDialog() {
            this.showDialog = true;
            this.newRecord.content = '';
        },

        // 取消添加
        cancelAdd() {
            this.showDialog = false;
        },

        // 保存记录
        saveRecord() {
            if (!this.newRecord.content.trim()) {
                uni.showToast({
                    title: '请输入跟进内容',
                    icon: 'none'
                });
                return;
            }

            // 构建新记录对象
            const record = {
                userId: getUserInfo().userId,
                clientId: this.customerId,
                followUpDescription: this.newRecord.content
            };
            uni.showLoading({ title: '提交中', mask: true });
            util.request('wx/api/insertCustomerFollow', record, 'POST', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading()
                    if (res.code === 200) {
                        
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
            this.showDialog = false;
            uni.navigateBack({
                delta: 1
            });
            

            // 实际API调用示例
            // uni.request({
            //   url: 'your-api-url/follow/add',
            //   data: {
            //     customerId: this.customerId,
            //     content: this.newRecord.content
            //   },
            //   method: 'POST',
            //   success: (res) => {
            //     if (res.data.code === 200) {
            //       this.fetchFollowRecords(); // 重新获取列表
            //       this.showDialog = false;
            //     }
            //   }
            // });
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
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    /* 增加左右内边距 */
    margin-bottom: 20rpx;
}

.title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-left: 20rpx;
    /* 标题右移 */
}

.add-btn {
    background: linear-gradient(135deg, #1890ff, #0050b3);
    color: #fff;
    font-size: 28rpx;
    padding: 10rpx 30rpx;
    border-radius: 30rpx;
    box-shadow: 0 4rpx 12rpx rgba(24, 144, 255, 0.3);
    margin-right: 10rpx;
    /* 按钮右移 */
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
    word-wrap: break-word;
    white-space: pre-wrap;
    word-break: break-all;
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