<template>
  <view class="record-container">
    <!-- 顶部标题 -->
    <view class="header">
      <text class="title">打卡记录</text>
    </view>

    <!-- 记录列表 -->
    <view class="record-list">
      <view v-if="clockRecords.length === 0" class="empty-tip">
        <text>暂无打卡记录</text>
      </view>

      <view v-else>
        <view v-for="(record, index) in clockRecords" :key="index" class="record-item">
          <view class="record-header">
            <text class="record-time">{{ record.clockDate }}</text>
          </view>
          <view class="record-content">
            <text>{{ record.location }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
const util = require("@/utils/util.js")

export default {
  data() {
    return {
      clockRecords: [] // 打卡记录列表
    }
  },
  onLoad() {
    // 加载打卡记录
    this.fetchClockRecords();
  },
  methods: {
    // 获取打卡记录列表
    fetchClockRecords() {
      let that = this;
      uni.showLoading({ title: '加载中', mask: true });
      
      // 调用获取打卡记录的接口
      util.request('wx/api/getUserClockList', {}, 'GET', 'application/json', false, true)
        .then(res => {
          uni.hideLoading();
          if (res.code === 200) {
            that.clockRecords = res.data;
          } else {
            uni.showToast({
              title: res.msg || '获取记录失败',
              icon: 'none'
            });
          }
        })
        .catch(err => {
          uni.hideLoading();
          uni.showToast({
            title: '网络异常，请稍后再试',
            icon: 'none'
          });
        });
    
    }
  }
}
</script>

<style>
.record-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部标题 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-left: 20rpx;
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

.record-time {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.record-content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
}

/* 无数据提示 */
.empty-tip {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>