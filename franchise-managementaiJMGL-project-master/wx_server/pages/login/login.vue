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
				微信登录
			</button>
		</view>
	</view>
</template>

<script>``
import {
	setUserInfo,
	getUserInfo,
	setToken,
	getToken,
} from '@/utils/auth';
const util = require("@/utils/util.js")
export default {
	data() {
		return {
			currentDate: '',
			username: '',
			password: ''
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

						uni.switchTab({
							url: '/pages/index/index',
						})
					})
				}

			})
		},

		// 微信登录
		wxUserLoginser() {
			let that = this
			uni.login({
				provider: 'weixin',
				success: function (loginRes) {
					let form = {};
					form.wxCode = loginRes.code;
					form.wxClient = "1";
					util.request("wxloginByCode", form).then(function (wxloginRes) {
						console.log("wxloginRes", wxloginRes)
						if (wxloginRes.code == 200) {
							setToken(wxloginRes.token)
							util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
								setUserInfo(wxGetInfoRes.user)
								if (wxGetInfoRes.user.customerType != 4) {
									util.toast("请进入客户端小程序!")
								}else{
									if (!wxGetInfoRes.user.password) {
										uni.redirectTo({
											url: '/pages/me/bindUser',
										})
									} else {
										uni.switchTab({
											url: '/pages/index/index',
										})
									}
								}
								
							})
						} else {
							util.toast("登录失败，请稍后再试~")
						}
					});
				},
				fail(err) {
					console.log(err)
				}
			});
		}
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
</style>