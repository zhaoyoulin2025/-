<template>
	<view class="login-container">
		<view class="header">
			<text class="welcome-text">您好，欢迎登录</text>
			<text class="platform-text">商铺运营管理平台！</text>
		</view>

		<view class="form-container">
			<view class="input-group">
				<input class="input-field" type="text" v-model="username" placeholder="请输入账号" maxlength="30" />
			</view>

			<view class="input-group">
				<input class="input-field" password="true" v-model="password" placeholder="请输入密码" maxlength="30" />
			</view>

			<button class="login-btn" @click="userLogin">登录</button>

			<button class="wechat-login-btn" @click="wxUserLoginser">
				快捷登录
			</button>
		</view>

		<view class="wx-auth-modal" v-if="showAuthModal">
			<view class="wx-auth-container">
				<view class="wx-auth-header">
					<text class="wx-auth-title">快捷登录</text>
				</view>
				<view class="wx-auth-content">
					<!-- <view class="wx-auth-icon">
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

<script>``
import {
	setUserInfo,
	getUserInfo,
	setToken,
	setUserType,
	getToken,
} from '@/utils/auth';
const util = require("@/utils/util.js")
export default {
	data() {
		return {
			currentDate: '',
			username: '',
			password: '',
			showAuthModal: false // 控制授权弹窗显示
		}
	},

	methods: {
		// 手机号登录
		userLogin() {
			if (!this.username) {
				util.toast("请输入账号")
				return
			}
			if (!this.password) {
				util.toast("请输入密码")
				return
			}
			let form = {};
			form.username = this.username;
			form.password = this.password;
			util.request("wxUserLogin", form).then(function (wxloginRes) {
				if (wxloginRes.code == 200) {
					setToken(wxloginRes.token)
					util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
						setUserInfo(wxGetInfoRes.user)
						// 保存用户类型为员工端用户
						setUserType("1")
						uni.redirectTo({
							url: '/pages/index/serverIndex',
						})
					})
				}

			})
		},

		// 微信登录
		wxUserLoginser() {
			this.showAuthModal = true
		},
		confirmAuth() {
			let that = this;
			this.showAuthModal = false; // 隐藏弹窗
			uni.login({
				provider: 'weixin',
				success: function (loginRes) {
					let form = {};
					form.wxCode = loginRes.code;
					form.wxClient = "1";
					uni.showLoading({
						title: '登陆中'
					});
					util.request("wxloginByCode", form).then(function (wxloginRes) {
						console.log("wxloginRes", wxloginRes)
						uni.hideLoading();
						if (wxloginRes.code == 200) {
							setToken(wxloginRes.token)
							setUserType("1")
							util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
								setUserInfo(wxGetInfoRes.user)
								// 保存用户类型为员工端用户
								if (!wxGetInfoRes.user.password) {
									uni.redirectTo({
										url: '/pages/me/bindUser',
									})
								} else {
									uni.redirectTo({
										url: '/pages/index/serverIndex',
									})
								}


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
	}
}
</script>

<style lang="scss">
.login-container {
	padding: 0 60rpx;
	min-height: 100vh;
	background-color: #fff;
}

.header {
	padding-top: 200rpx;
	margin-bottom: 120rpx;

	.welcome-text {
		display: block;
		font-size: 48rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 10rpx;
	}

	.platform-text {
		display: block;
		font-size: 48rpx;
		font-weight: 500;
		color: #333;
	}
}

.form-container {
	.input-group {
		margin-bottom: 40rpx;

		.input-field {
			width: 100%;
			height: 88rpx;
			padding: 0 30rpx;
			font-size: 32rpx;
			color: #333;
			background-color: #f8f8f8;
			border-radius: 12rpx;
			border: none;
			box-sizing: border-box;

			&::placeholder {
				color: #999;
			}
		}
	}

	.verification-group {
		display: flex;
		align-items: center;
		gap: 20rpx;

		.verification-input {
			flex: 1;
		}

		.send-code-btn {
			font-size: 28rpx;
			color: #007AFF;
			white-space: nowrap;
			cursor: pointer;
		}
	}

	.login-btn {
		width: 100%;
		height: 88rpx;
		background-color: #007AFF;
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 12rpx;
		border: none;
		margin-top: 60rpx;
		margin-bottom: 40rpx;

		&:active {
			background-color: #0056CC;
		}
	}

	.wechat-login-btn {
		width: 100%;
		height: 88rpx;
		background-color: #07C160;
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 12rpx;
		border: none;

		&:active {
			background-color: #06A050;
		}
	}
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