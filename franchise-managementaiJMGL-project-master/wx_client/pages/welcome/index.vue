<template>
	<view class="welcome-container">
		<!-- 背景图片 -->
		<image class="background-wrapper"
			src="https://xmlcgl.qixiaoluo.top/manage/profile/upload/2025/09/08/welcome-bg_20250908215320A006.jpg"
			mode="scaleToFill"></image>
		<!-- 刷新按钮 -->
		<view class="refresh-btn" @click="goToStaffLogin">
			<text class="iconfont icon-switch">⇄</text>
		</view>
		<!-- 加盟按钮 -->
		<view class="button-section">
			<button class="join-btn" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">我要加盟</button>
		</view>

	</view>
</template>

<script>
import {
	getUserInfo,
	setToken,
	removeToken,
	setUserInfo,
	setUserType,
} from '@/utils/auth';
const util = require("@/utils/util.js")
export default {
	data() {
		return {

		}
	},
	onLoad() {
		// 检查用户登录状态
		const user = getUserInfo()
		if (user) {
			if (user.customerType == 4) {
				uni.navigateTo({
					url: '/pages/index/serverIndex',
				})
			} else {
				//customerType  没有提交问卷=0;  提交问卷没有预约=1;  预约了=2;  签约了的=3 不是客户 = 4
				uni.navigateTo({
					url: '/pages/welcome/customerIndex',
				})

			}
		}

	},
	methods: {
		// 跳转到问卷调查页面
		goToAppointment() {
			let that = this

		},
		// 跳转到员工登录页面
		goToStaffLogin() {
			uni.navigateTo({
				url: '/pages/login/serverLogin'
			});
		},
		getPhoneNumber(e) {
			console.log("登录")
			if (!e.detail || e.detail.errMsg !== 'getPhoneNumber:ok' || !e.detail.code) {
				uni.showToast({ title: '授权失败，请重试', icon: 'none' })
				return
			}
			uni.showLoading({
				title: '请稍后',
				mask: true
			})
			uni.login({
				provider: 'weixin',
				success: function (loginRes) {
					let form = {};
					form.wxCode = loginRes.code;
					form.wxClient = "0";
					console.log("登录", form)
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

									if (!wxGetInfoRes.user.phonenumber) {
										util.request('wxGetPhone', { code: e.detail.code }, 'POST', 'application/json', false, true)
											.then(res => {

												if (res.code === 200) {
													const phone = res.data
													if (phone) {
														let form = {
															userId: wxGetInfoRes.user.userId,
															phonenumber: phone,
														}
														util.request("wxUpdateUserInfo", form, "POST").then(function (res) {
															if (res.code == 200) {
																wxGetInfoRes.user.phonenumber = phone
																setUserInfo(wxGetInfoRes.user)
																// 保存用户类型为普通用户
																setUserType("0")
																uni.showToast({ title: '授权成功', icon: 'none' })
																uni.navigateTo({ url: '/pages/welcome/customerIndex' })
															} else {
																uni.showToast({ title: res.msg || '授权失败', icon: 'none' })
															}
														})

													} else {
														uni.showToast({ title: '未获取到手机号', icon: 'none' })
													}
												} else {
													uni.showToast({ title: res.msg || '获取手机号失败', icon: 'none' })
												}
											})
											.catch(() => {
												uni.hideLoading()
												uni.showToast({ title: '网络异常，请稍后再试', icon: 'none' })
											})
									} else {
										setUserInfo(wxGetInfoRes.user)
										uni.navigateTo({
											url: '/pages/welcome/customerIndex',
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
					uni.hideLoading();
					console.log(err)
				}
			});

		},

		closePhoneAuth() {
			this.showPhoneAuth = false
		}
	}
}
</script>

<style>
.welcome-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-end;
	height: 100vh;
	padding: 0 40rpx;
	padding-bottom: 20px;
	position: relative;
	overflow: hidden;
	background-color: #da050b
}

.background-wrapper {
	position: absolute;
	top: 45%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 80%;
	height: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 20rpx;
	/* 添加圆角让视觉效果更好 */
}

.background-image {
	width: 40%;
	/* 缩小到80%，可根据需要调整 */
	height: 40%;
	object-fit: contain;
	z-index: -1;
	object-fit: cover;
}


.button-section {
	width: 100%;
	max-width: 450rpx;
	padding: 0 100rpx;
	margin-bottom: 150rpx;
	z-index: 1;
	position: relative;
}

.join-btn {
	width: 100%;
	height: 70rpx;
	background: rgba(255, 255, 255, 0.9);
	color: #FF6B35;
	font-size: 28rpx;
	font-weight: bold;
	border-radius: 35rpx;
	border: none;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
}

.join-btn:active {
	background: rgba(255, 255, 255, 0.8);
	transform: translateY(2rpx);
}

/* 适配不同屏幕尺寸 */
@media screen and (max-width: 750rpx) {
	.button-section {
		padding: 0 80rpx;
		max-width: 350rpx;
	}
}

@media screen and (min-width: 1200rpx) {
	.button-section {
		max-width: 500rpx;
	}
}

.refresh-btn {
	position: fixed;
	top: 200rpx;
	right: 40rpx;
	width: 80rpx;
	height: 80rpx;
	background: rgba(255, 255, 255, 0.8);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 2;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
}

.refresh-btn:active {
	background: rgba(255, 255, 255, 0.6);
	transform: scale(0.95);
}

.refresh-icon {
	font-size: 30px;
	font-weight: bold;
	color: #666;
}

.iconfont {
	font-size: 45rpx;
	font-weight: bold;
	color: #000000;
}

.phone-auth-mask {
	position: fixed;
	inset: 0;
	background: rgba(0, 0, 0, 0.45);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 2000;
}

.phone-auth-dialog {
	width: 640rpx;
	background: #fff;
	border-radius: 16rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.12);
}

.phone-auth-title {
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
	margin-bottom: 12rpx;
}

.phone-auth-desc {
	font-size: 26rpx;
	color: #666;
	margin-bottom: 24rpx;
}

.phone-auth-btn {
	width: 100%;
	height: 80rpx;
	border-radius: 12rpx;
	background: #2979ff;
	color: #fff;
	font-size: 28rpx;
}

.cancel-btn {
	margin-top: 16rpx;
	width: 100%;
	height: 80rpx;
	border-radius: 12rpx;
	background: #f5f5f5;
	color: #333;
	font-size: 28rpx;
}
</style>