<template>
  <view class="custom-tab-bar">
    <view v-for="(item, index) in tabs" :key="index" class="tab-item" :class="{ active: currentTab === index }"
      @click="switchTab(item.pagePath, index)">
      <image class="tab-image" :src="currentTab === index ? item.selectedIconPath : item.iconPath"></image>
      <text class="tab-text">{{ item.text }}</text>
    </view>
  </view>
</template>

<script>
import { getUserType } from '@/utils/auth'

export default {
  name: 'CustomTabBar',
  data() {
    return {
      currentTab: 0,
      staffUserTabs: [
        {
          pagePath: '/pages/index/serverIndex',
          iconPath: '/static/tabbar/home.png',
          selectedIconPath: '/static/tabbar/home_ac.png',
          text: '首页'
        },
        {
          pagePath: '/pages/me/me',
          iconPath: '/static/tabbar/me.png',
          selectedIconPath: '/static/tabbar/me_ac.png',
          text: '我的'
        }
      ],
      normalUserTabs: [
        {
          pagePath: '/pages/index/index',
          iconPath: '/static/tabbar/home.png',
          selectedIconPath: '/static/tabbar/home_ac.png',
          text: '首页'
        },
        {
          pagePath: '/shops/list',
          iconPath: '/static/tabbar/shop.png',
          selectedIconPath: '/static/tabbar/shop_ac.png',
          text: '铺位'
        },
        {
          pagePath: '/pages/me/me',
          iconPath: '/static/tabbar/me.png',
          selectedIconPath: '/static/tabbar/me_ac.png',
          text: '我的'
        }
      ]
    }
  },
  computed: {
    tabs() {
      const userType = getUserType()
      return userType === "1" ? this.staffUserTabs : this.normalUserTabs
    }
  },
  methods: {
    switchTab(url, index) {
      if (this.currentTab !== index) {
        uni.redirectTo({
          url
        })
        this.setCurrentTab(index)
      }
    },
    setCurrentTab(index) {
      this.currentTab = index
    },
    // 根据当前页面路径设置选中状态
    updateCurrentTab() {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1]
      if (currentPage) {
        const path = '/' + currentPage.route
        const index = this.tabs.findIndex(item => item.pagePath === path)
        if (index !== -1) {
          this.currentTab = index
        }
      }
    }
  },
  mounted() {
    this.updateCurrentTab()
  },
  // 每次显示时更新选中状态
  onShow() {
    this.updateCurrentTab()
  }
}
</script>

<style lang="scss">
.custom-tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200rpx;
  background-color: #ffffff;
  display: flex;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  z-index: 999;

  .tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .tab-image {
      // 改为类选择器
      width: 48rpx;
      height: 48rpx;
      margin-bottom: 6rpx;
    }

    .tab-text {
      // 改为类选择器
      font-size: 24rpx;
      color: #7A7E83;
    }

    &.active .tab-text {
      // 改为类选择器
      color: #0a2576;
    }
  }
}
</style>