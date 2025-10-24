<template>
	<view class="customer-index-container">
		<!-- 顶部图片区域 -->
		<view class="header-image-container">
			<image :src="headerImageUrl" mode="aspectFit" class="header-image"></image>
		</view>

		<!-- 导航卡片区域 -->
		<view class="nav-cards-container">
			<!-- 我要加盟 -->
			<view class="nav-card" @click="goToFranchise">
				<view class="card-content">
					<view class="card-title">我要加盟</view>
					<!-- <view class="card-subtitle">填写资料，开启您的事业</view> -->
				</view>
				<view class="card-arrow">
					<text class="arrow-icon">></text>
				</view>
			</view>

			<!-- 我的预约 -->
			<view class="nav-card" :class="{ disabled: !canAccessInvite }"  @click="goToAppointment">
				<view class="card-content">
					<view class="card-title">我的预约</view>
					<!-- <view class="card-subtitle">按加盟意向后可预约洽谈</view> -->
				</view>
				<view class="card-arrow">
					<text class="arrow-icon">></text>
				</view>
				
			</view>

			<!-- 选址推荐 -->
			<view class="nav-card" :class="{ disabled: !canAccessRecommend }" @click="goToRecommend">
				<view class="nav-content">
					<view class="card-title">选址推荐</view>
				</view>
				<view class="card-arrow">
					<text class="arrow-icon">></text>
				</view>
				
			</view>

			<!-- 我的门店 -->
			<view class="nav-card" @click="goToMyShop" :class="{ disabled: !canAccessMySHop }">
				<view class="card-content">
					<view class="card-title">我的门店</view>
					<!-- <view class="card-subtitle">签约成功后查看建店进度</view> -->
				</view>
				<view class="card-arrow">
					<text class="arrow-icon">></text>
				</view>
				
			</view>
		</view>
		<!-- 退出按钮 -->
		<view class="logout-btn" @click="logout">
			←
		</view>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo,
	removeToken,
	removeUserInfo,
} from '@/utils/auth';

export default {
	data() {
		return {
			headerImageUrl: '/static/lsf.png' // 默认图片
		}
	},
	onLoad() {
		// 从后台获取头部图片
		this.getHeaderImage();
	},
	computed: {
		canAccessRecommend() {
			const user = getUserInfo();
			return user && user.shopResource == 1;
		},
		canAccessMySHop() {
			const user = getUserInfo();
			return user && user.customerType == 3;
		},
		canAccessInvite() {
			const user = getUserInfo();
			return user && user.inviteFlag == 1;
		},
	},
	methods: {
		// 获取头部图片
		getHeaderImage() {
			util.request('wxGetBanner', {}, 'GET')
				.then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						this.headerImageUrl = res.data;
					} else {
						uni.showToast({ title: res.msg, icon: 'none' });
					}
				})
				.catch(err => {
					uni.hideLoading();
					uni.showToast({ title: '网络异常，请稍后再试', icon: 'none' });
				});
		},
		// 跳转到加盟页面
		goToFranchise() {
			let user = getUserInfo()
			if (user) {
				if (user.customerType == 0) {
					uni.navigateTo({
						url: '/pages/question/index'
					})
				} else {
					uni.showToast({ title: "您已提交了问卷", icon: 'none' });
				}
			} else {
				uni.navigateTo({
					url: '/pages/question/index'
				})
			}

		},
		// 跳转到选址推荐页面
		goToRecommend() {
			if (!this.canAccessRecommend) {
				return; // 不可点击时直接返回
			}
			const user = getUserInfo();
			if (!user) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return;
			}
			if (user.shopResource == 1) {
				uni.navigateTo({
					url: '/shops/commonList'
				})
			} else {
				uni.showToast({ title: "您暂未开通查看铺位资源", icon: 'none' });
			}
		},

		// 跳转到预约页面
		goToAppointment() {
			if (!this.canAccessInvite) {
				return; // 不可点击时直接返回
			}
			const user = getUserInfo();
			if (!user) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return;
			}
			uni.navigateTo({
				url: '/pages/prebook/index'
			})
		},
		// 跳转到我的门店页面
		goToMyShop() {
			if (!this.canAccessMySHop) {
				return; // 不可点击时直接返回
			}
			const user = getUserInfo();
			if (!user) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return;
			}
			if (user) {
				if (user.customerType == 3) {
					uni.navigateTo({
						url: '/pages/index/index'
					})
				} else {
					uni.showToast({ title: "您暂未签约", icon: 'none' });
				}
			}

		},
		// 退出登录
		logout() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				success: (res) => {
					if (res.confirm) {
						// 清除缓存
						removeToken();
						removeUserInfo();
						// 清除本地存储
						uni.clearStorageSync();
						// 跳转到欢迎页
						uni.reLaunch({
							url: '/pages/welcome/index'
						});
					}
				}
			});
		},
	}
}
</script>

<style>
.customer-index-container {
	display: flex;
	flex-direction: column;
	min-height: 100vh;
	background-color: #f5f5f5;
}

.header-image-container {
	width: 100%;
	height: 400rpx;
	overflow: hidden;
}

.header-image {
	width: 100%;
	height: 100%;
}

.nav-cards-container {
	padding: 30rpx;
	margin-top: -50rpx;
}

.nav-card {
	background-color: #ffffff;
	font-size: 36rpx;
	border-radius: 16rpx;
	padding: 40rpx 30rpx;
	margin-bottom: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	position: relative;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-content {
	flex: 1;
}

.card-title {
	font-size: 36rpx;
	font-weight: 500;
	color: #333333;
	margin-bottom: 10rpx;
}

.card-subtitle {
	font-size: 28rpx;
	color: #999999;
}

.card-arrow {
	width: 40rpx;
	height: 40rpx;
	display: flex;
	justify-content: center;
	align-items: center;
}

.arrow-icon {
	color: #cccccc;
	font-size: 36rpx;
}

.card-icon {
	position: absolute;
	right: 80rpx;
	top: 50%;
	transform: translateY(-50%);
	width: 80rpx;
	height: 80rpx;
}

.icon-image {
	width: 100%;
	height: 100%;
}

.nav-card.disabled {
	background-color: #f5f5f5 !important;
	opacity: 0.6;
	pointer-events: none;
}

.nav-card.disabled .nav-title,
.nav-card.disabled .nav-subtitle {
	color: #999 !important;
}

.nav-card.disabled .nav-arrow {
	color: #ccc !important;
}

/* 退出按钮样式 */
.logout-btn {
	position: fixed;
	bottom: 60rpx;
	right: 60rpx;
	width: 80rpx;
	height: 80rpx;
	background-color: #5dade2;
	border-radius: 50%;
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 999;
	box-shadow: 0 4rpx 12rpx rgba(93, 173, 226, 0.3);
}

.logout-text {
	color: #ffffff;
	font-size: 24rpx;
	font-weight: 500;
}
</style>