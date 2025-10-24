<template>
	<view class="content">
		<view class="hd">
			<view class="logo-container">
				<image class="logo" src="https://xmlcgl.qixiaoluo.top/manage/profile/upload/2025/09/08/welcome-bg_20250908215320A006.jpg"></image>
				<!-- <view class="staff-login" @click="goToStaffLogin">员工端登录</view> -->
			</view>
			<view class="title">柒小螺欢迎您!</view>
		</view>
		<view class="btn spacing">
			<button class="weui_btn weui_btn_primary" @click="wxUserLoginser">
				快捷登录
			</button>
		</view>

		<view class="wx-auth-modal" v-if="showAuthModal">
			<view class="wx-auth-container">
				<view class="wx-auth-header">
					<text class="wx-auth-title">快捷登录</text>
				</view>
				<view class="wx-auth-content">
				<!-- 	<view class="wx-auth-icon">
						<view class="wx-auth-icon-box"></view>
					</view> -->
					<view class="wx-auth-text">
						<text class="wx-auth-main-text">商铺小程序 申请获得以下权限</text>
						<text class="wx-auth-sub-text">• 获取你的昵称、头像、地区及性别</text>
					</view>
				</view>
				<view class="wx-auth-footer">
					<view class="wx-auth-btn wx-auth-cancel" @click="cancelAuth">拒绝</view>
					<view class="wx-auth-btn wx-auth-confirm" @click="confirmAuth">允许</view>
				</view>
			</view>
		</view>
	</view>

</template>

<script>
import {
	setUserInfo,
	getUserInfo,
	setToken,
	setUserType,
	getToken,
	removeToken
} from '@/utils/auth';
const util = require("@/utils/util.js")
export default {
	data() {
		return {
			currentDate: '',
			showAuthModal: false // 控制授权弹窗显示
		}
	},
	onLoad() {
		// 检查用户是否已登录
		const userInfo = getUserInfo();
		if (!userInfo) {
			uni.reLaunch({
				url: '/customer/welcome',
			});
		}
	},

	methods: {
		wxUserLoginser() {
			this.showAuthModal = true;

		},
		confirmAuth() {
			let that = this;
			this.showAuthModal = false; // 隐藏弹窗
			uni.showLoading({
				title: '登陆中'
			});
			// 执行微信登录
			uni.login({
				provider: 'weixin',
				success: function (loginRes) {
					let form = {};
					form.wxCode = loginRes.code;
					form.wxClient = "0";
					util.request("wxloginByCode", form).then(function (wxloginRes) {
						console.log("wxloginRes", wxloginRes)
						uni.hideLoading();
						if (wxloginRes.code == 200) {
							setToken(wxloginRes.token)
							util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
								
								if (wxGetInfoRes.user.customerType == 4) {
									util.toast("请进入员工端登录!")
									removeToken()
									uni.redirectTo({
										url: '/pages/login/serverLogin',
									})

								} else {
									console.log(" getCurrentPages()", getCurrentPages())
									let backUrl = "/"+ getCurrentPages()[getCurrentPages().length-2].route;
									console.log("backUrl", backUrl)
									setUserInfo(wxGetInfoRes.user)
									// 保存用户类型为普通用户
									setUserType("0")
									uni.reLaunch({
										url: backUrl,
									})
								}

								//customerType  没有提交问卷=0;  提交问卷没有预约=1;  预约了=2;  签约了的=3 不是客户 = 4




							})
						} else {
							util.toast("登录失败，请稍后再试~")
						}
					});
				},
				fail(err) {
					uni.hideLoading();
					console.log(err)
				}
			});
		},
		// 取消授权
		cancelAuth() {
			this.showAuthModal = false; // 隐藏弹窗
			console.log('用户拒绝授权登录');
		},
		// 跳转到员工登录页面
		goToStaffLogin() {
			console.log('跳转到员工登录页面');
			uni.redirectTo({
				url: '/pages/login/serverLogin',
				success: function () {
					console.log('跳转成功');
				},
				fail: function (err) {
					console.error('跳转失败:', err);
					uni.showToast({
						title: '跳转失败: ' + JSON.stringify(err),
						icon: 'none'
					});
				}
			});
		}
	}
}
</script>

<style lang="scss">
.content {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	padding: 40rpx;
	box-sizing: border-box;
}

.hd {
	display: flex;
	width: 100%;
	max-width: 600rpx;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	margin-bottom: 80rpx;
}

.logo-container {
	position: relative;
	width: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-bottom: 40rpx;
}

.hd .logo {
	width: 400rpx;
	height: 400rpx;
	border-radius: 20rpx;
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
}

.staff-login {
	position: absolute;
	right: 16rpx;
	top: 0;
	background-color: #04be02;
	color: white;
	padding: 12rpx 24rpx;
	border-radius: 20rpx;
	font-size: 24rpx;
	cursor: pointer;
	box-shadow: 0 4rpx 12rpx rgba(4, 190, 2, 0.3);
}

.hd .title {
	text-align: center;
	font-size: 42rpx;
	color: #333;
	font-weight: 600;
}

.btn {
	width: 100%;
	max-width: 500rpx;
	padding: 0;
}

.weui_btn_primary {
	background-color: #04be02;
}

.weui_btn {
	position: relative;
	display: block;
	margin-left: auto;
	margin-right: auto;
	padding-left: 14px;
	padding-right: 14px;
	box-sizing: border-box;
	font-size: 18px;
	text-align: center;
	text-decoration: none;
	color: #fff;
	line-height: 2.33333333;
	border-radius: 5px;
	-webkit-tap-highlight-color: rgba(0, 0, 0, 0);
	overflow: hidden;
}

.weui_btn:after {
	content: " ";
	width: 200%;
	height: 200%;
	position: absolute;
	top: 0;
	left: 0;
	border: 1px solid rgba(0, 0, 0, 0.2);
	-webkit-transform: scale(0.5);
	-ms-transform: scale(0.5);
	transform: scale(0.5);
	-webkit-transform-origin: 0 0;
	-ms-transform-origin: 0 0;
	transform-origin: 0 0;
	box-sizing: border-box;
	border-radius: 10px;
}

// 自定义微信授权弹窗样式
.wx-auth-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.6);
	z-index: 999;
	display: flex;
	justify-content: center;
	align-items: center;
}

.wx-auth-container {
	width: 80%;
	background-color: #fff;
	border-radius: 12px;
	overflow: hidden;
}

.wx-auth-header {
	padding: 20px 0;
	text-align: center;
	border-bottom: 1px solid #eee;
}

.wx-auth-title {
	font-size: 18px;
	font-weight: 500;
}

.wx-auth-content {
	padding: 30px 20px;
}

.wx-auth-icon {
	display: flex;
	justify-content: center;
	margin-bottom: 20px;
}

.wx-auth-icon-box {
	width: 50px;
	height: 60px;
	background-color: #e8f8e8;
}

.wx-auth-text {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.wx-auth-main-text {
	font-size: 16px;
	margin-bottom: 15px;
}

.wx-auth-sub-text {
	font-size: 14px;
	color: #666;
}

.wx-auth-footer {
	display: flex;
	border-top: 1px solid #eee;
}

.wx-auth-btn {
	flex: 1;
	padding: 12px 0;
	text-align: center;
	font-size: 16px;
}

.wx-auth-cancel {
	color: #000;
	border-right: 1px solid #eee;
}

.wx-auth-confirm {
	color: #07C160;
}
</style>