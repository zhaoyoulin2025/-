<template>
  <view class="clock-container">
    <!-- 顶部用户信息区域 -->
    <view class="user-info">
      <view class="user-left">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'"></image>
        <text class="username">{{ userInfo.name || '未登录' }}</text>
      </view>
      <view class="attendance-btn" @tap="goToAttendance">
        我的考勤
      </view>
    </view>

    <!-- 打卡区域 -->
    <view class="clock-area">
      <!-- 当前时间显示 -->
      <view class="current-time">{{ currentTime }}</view>

      <!-- 打卡按钮 -->
      <view class="clock-btn" @tap="clockIn">
        <text>打卡</text>
      </view>

      <!-- 当前位置 -->
      <view class="location">
        <text class="location-icon cuIcon-location"></text>
        <text class="location-text">{{ location || '正在获取位置...' }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getUserInfo } from '@/utils/auth';
const util = require("@/utils/util.js")
export default {
  data() {
    return {
      userInfo: {
        avatar: '',
        name: ''
      },
      currentTime: '',
      location: '',
      timer: null,
      latitude: 0,
      longitude: 0
    }
  },
  onLoad() {
    // 获取用户信息
    this.getUserInfo();

    // 获取当前位置
    this.getLocation();

    // 启动时间更新
    this.updateTime();
    this.timer = setInterval(() => {
      this.updateTime();
    }, 1000);
  },
  onUnload() {
    // 清除定时器
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      const userInfo = getUserInfo();
      if (userInfo) {
        this.userInfo.name = userInfo.name || userInfo.nickName || '未知用户';
        this.userInfo.avatar = userInfo.avatar || userInfo.avatarUrl;
      }
    },

    // 更新当前时间
    updateTime() {
      const now = new Date();
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      this.currentTime = `${hours}:${minutes}:${seconds}`;
    },

    // 获取当前位置
    getLocation() {
      uni.getLocation({
        type: 'gcj02',
        geocode: true,
        success: (res) => {
          console.log('获取位置成功', res);
          this.latitude = res.latitude;
          this.longitude = res.longitude;

          // 使用逆地理编码获取位置详情
          this.getLocationName(res.latitude, res.longitude);
        },
        fail: (err) => {
          console.error('获取位置失败', err);
          this.location = '无法获取位置信息';

          // 提示用户开启位置权限
          uni.showModal({
            title: '提示',
            content: '需要获取您的位置信息，请开启位置权限',
            confirmText: '去设置',
            success: (res) => {
              if (res.confirm) {
                uni.openSetting();
              }
            }
          });
        }
      });
    },

    // 获取位置名称
    getLocationName(latitude, longitude) {
      let that=this;
      util.request("wx/api/getLocation", {latitude:latitude,longitude:longitude}, "GET").then(function (res) {
        uni.hideLoading();
        console.log(res);
        if (res.code === 200) {
          that.location = res.data;
        } else {
          uni.showToast({
          title: '无法获取位置信息，请稍后再试',
          icon: 'none'
        });
        }
      })
    },

    // 打卡
    clockIn() {
      if (!this.location || this.location === '正在获取位置...' || this.location === '未知位置') {
        uni.showToast({
          title: '无法获取位置信息，请稍后再试',
          icon: 'none'
        });
        return;
      }

      // 显示加载中
      uni.showLoading({
        title: '打卡中...'
      });

      // 获取当前日期时间
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const day = now.getDate().toString().padStart(2, '0');
      const dateStr = `${year}-${month}-${day}`;

      // 打卡数据
      const clockData = {
        dateStr: dateStr +" "+this.currentTime,
        location: this.location,
      };
      // 调用打卡接口
      util.request("wx/api/insertUserClock", clockData, "POST")
        .then(res => {
          uni.hideLoading();
          if (res.code === 200) {
            uni.showToast({
              title: '打卡成功',
              icon: 'success'
            });
          } else {
            uni.showToast({
              title: res.msg || '打卡失败',
              icon: 'none'
            });
          }
        })
        .catch(err => {
          uni.hideLoading();
          console.error('打卡失败', err);
          uni.showToast({
            title: '打卡失败，请稍后再试',
            icon: 'none'
          });
        });
    },

    // 跳转到考勤记录页面
    goToAttendance() {
      uni.navigateTo({
        url: '/clock/record'
      });
    }
  }
}
</script>

<style>
.clock-container {
  padding: 30rpx;
  height: 100vh;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
}

/* 用户信息区域样式 */
.user-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 60rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 20rpx;
  padding: 20rpx 30rpx;
  background-color: #ffffff;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.user-left {
  display: flex;
  align-items: center;
  /* 移除了justify-content: center，让内容自然靠左 */
  padding-left: 20rpx; /* 添加左侧内边距，确保不会贴边 */
  flex: 1; /* 保留占据更多空间 */
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.username {
  font-size: 32rpx;
  font-weight: bold;
}

.attendance-btn {
  padding: 15rpx 40rpx; /* 加宽按钮 */
  background-color: #f0f9ff;
  color: #1890ff;
  border-radius: 30rpx;
  font-size: 28rpx;
  border: 2rpx solid #1890ff; /* 添加边框 */
  min-width: 160rpx; /* 确保按钮有足够宽度 */
  text-align: center;
}

/* 打卡区域样式 */
.clock-area {
  height: 40vh; /* 减少高度至少一半，原来是flex: 1占满剩余空间 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2rpx solid #e0e0e0;
  border-radius: 20rpx;
  padding: 30rpx; /* 减少内边距 */
  background-color: #ffffff;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  margin: 0 20rpx;
}

.current-time {
  font-size: 60rpx;
  font-weight: bold;
  margin-bottom: 40rpx; /* 减少间距 */
}

.clock-btn {
  width: 200rpx; /* 减小按钮尺寸 */
  height: 200rpx; /* 减小按钮尺寸 */
  border-radius: 50%;
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 40rpx;
  font-weight: bold;
  box-shadow: 0 10rpx 20rpx rgba(24, 144, 255, 0.3);
  margin-bottom: 40rpx; /* 减少间距 */
}

.location {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 28rpx;
}

.location-icon {
  margin-right: 10rpx;
  color: #1890ff;
}

.location-text {
  max-width: 500rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>