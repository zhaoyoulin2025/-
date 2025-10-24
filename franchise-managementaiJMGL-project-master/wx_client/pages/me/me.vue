<template>
	<view>
		<view class="header">
			<view class="bg">
				<view class="box">
					<view class="box-hd">
						<view>
							<view class="avator">
								<img :src="userinfo.avatar">
							</view>

							<view class="phone-number">{{ userinfo.nickName }}</view>
							<view class="phone-number">{{ maskedPhoneNumber }}</view>
						</view>
					</view>

				</view>
			</view>
		</view>
		<view class="list-content">
			<view class="list">
				<view class="li" @click="gotoUserInfo">
					<u-icon class="icon" name="coupon-fill" size="25" color="#757575"></u-icon>
					<view class="text">修改信息</view>
					<view class="arrow-wrap">
						<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
					</view>
				</view>
				<!-- <view class="li" @click="gotoUserInfo">
					<u-icon class="icon" name="coupon-fill" size="25" color="#757575"></u-icon>
					<view class="text">分享问卷</view>
					<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
				</view> -->
				<!-- <view class="li " @click="changeSkin">
					<u-icon class="icon" name="grid-fill" size="25" color="#757575"></u-icon>
					<view class="text">主题切换</view>
					<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
				</view>-->
				<!--<view class="li ">
					<u-icon class="icon" name="question-circle-fill" size="25" color="#757575"></u-icon>
					<view class="text">帮助中心</view>
					<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
				</view>-->
				<view class="li " @click="getPrebook" v-if="userinfo && userinfo.clientId">
					<u-icon class="icon" name="info-circle-fill" size="25" color="#757575"></u-icon>
					<view class="text">我的预约</view>
					<view class="arrow-wrap">
						<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
					</view>
				</view>
				<view class="li " @click="getBindUser" v-if="userinfo && !userinfo.clientId">
					<u-icon class="icon" name="info-circle-fill" size="25" color="#757575"></u-icon>
					<view class="text">我的绑定</view>
					<view class="arrow-wrap">
						<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
					</view>
				</view>
				<view class="li " @click="getMessage">
					<u-icon class="icon" name="email-fill" size="25" color="#757575"></u-icon>
					<view class="text">消息通知</view>
					<view class="arrow-wrap">
						<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
						<view v-if="showNoticeDot" class="red-dot"></view>
					</view>
				</view>
				<view class="li noborder" @click="loginOut">
					<u-icon class="icon" name="setting-fill" size="25" color="#757575"></u-icon>
					<view class="text">注销登录</view>
					<view class="arrow-wrap">
						<u-icon class="icon" name="arrow-right" size="20" color="#757575"></u-icon>
					</view>
				</view>
			</view>
		</view>
		<custom-tab-bar></custom-tab-bar>

	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	setUserInfo,
	getUserInfo,
	setToken,
	getToken,
	removeUserInfo,
	removeToken,
	setUserType
} from '@/utils/auth';
export default {
	data() {
		return {
			userinfo: {},
			questionFlag: false,
			showNoticeDot: false
		}
	},
	onShow() {
		console.log(getUserInfo())
		if (getUserInfo() != undefined) {
			this.userinfo = getUserInfo()
			this.checkNoticeDot()
		}
		else {
			uni.navigateTo({
				url: '/pages/login/login'
			})
		}
	},
	computed: {
		maskedPhoneNumber() {
			if (!this.userinfo.phonenumber) return '';
			const phone = this.userinfo.phonenumber.toString();
			return phone.substring(0, 3) + '****' + phone.substring(7);
		}
	},
	methods: {
		gotoUserInfo() {
			uni.navigateTo({
				url: '/pages/me/userinfo'
			})
		},
		getPrebook() {
			uni.navigateTo({
				url: '/pages/prebook/index'
			})
		},
		getMessage() {
			uni.navigateTo({
				url: '/pages/me/mynotice/mynotice'
			})
		},
		getBindUser() {
			uni.navigateTo({
				url: '/pages/me/bindUser'
			})
		},
		loginOut() {
			uni.showModal({
				title: '注销登录提示',
				content: '确定要注销登录吗？',
				cancelText: "取消", // 取消按钮的文字
				confirmText: "确认", // 确认按钮的文字
				confirmColor: '#f55850',
				success: (res) => {
					if (res.confirm) {
						console.log('comfirm') //点击确定之后执行的代码
						uni.$u.toast("注销登录成功，欢迎再来")
						removeUserInfo()
						removeToken()
						setUserType("")
						uni.navigateTo({
							url: '/pages/welcome/index'
						})
					} else {
						console.log('cancel') //点击取消之后执行的代码
					}
				}
			})
		},
		checkNoticeDot() {
			util.request('wx/api/getMessageList', { messageType: '' }, 'GET', 'application/json', false, true)
				.then(res => {
					if (res.code === 200) {
						const list = res.data || [];
						list.forEach(item => {
							console.log("item.status", item.status)
							if (item.status == 0) {
								this.showNoticeDot = true;
							}
							console.log("this.showNoticeDot2", this.showNoticeDot)
						})
					} else {
						this.showNoticeDot = false;
					}
				})
				.catch(() => {
					this.showNoticeDot = false;
				});
			console.log("this.showNoticeDot1", this.showNoticeDot)
		}

	},

}
</script>

<style lang="scss">
page {
	background-color: #f1f1f1;
	font-size: 28rpx;
}

.header {
	background: #fff;
	height: 660rpx;
	padding-bottom: 110rpx;

	.bg {
		width: 100%;
		height: 490rpx;
		padding-top: 310rpx;
		background: linear-gradient(to bottom, rgb(1, 94, 234), rgb(0, 192, 250), rgb(255, 255, 255));
	}
}

.box {
	width: 650rpx;
	height: 300rpx;
	border-radius: 20rpx;
	margin: 0 auto;
	background: #fff;
	box-shadow: 0 5rpx 20rpx 0rpx rgba(0, 0, 150, .2);

	.box-hd {
		display: flex;
		flex-wrap: wrap;
		flex-direction: row;
		justify-content: center;
		margin-top: 15rpx;

		.avator {

			width: 160rpx;
			height: 160rpx;
			background: #fff;
			border: 5rpx solid #f1f1f1;
			border-radius: 50%;
			margin: 0 auto;
			margin-top: -80rpx;
			overflow: hidden;

			img {
				width: 100%;
				height: 100%;
			}
		}

		.phone-number {
			width: 100%;
			text-align: center;
			margin: 50rpx auto;
		}
	}

	.box-bd {
		display: flex;
		flex-wrap: nowrap;
		flex-direction: row;
		justify-content: center;

		.item {
			flex: 1 1 auto;
			display: flex;
			flex-wrap: wrap;
			flex-direction: row;
			justify-content: center;
			border-right: 1px solid #f1f1f1;
			margin: 15rpx 0;

			&:last-child {
				border: none;
			}

			.icon {
				width: 60rpx;
				height: 60rpx;

				img {
					width: 100%;
					height: 100%;
				}
			}

			.text {
				width: 100%;
				text-align: center;
				margin-top: 10rpx;
			}
		}
	}
}

.list-content {
	background: #fff;
}

.list {
	width: 100%;
	border-bottom: 15rpx solid #f1f1f1;
	background: #fff;

	&:last-child {
		border: none;
	}

	.li {
		width: 92%;
		height: 100rpx;
		padding: 0px 4%;
		border-bottom: 1px solid #f3f3f3;
		display: flex;
		align-items: center;
		margin: 0rpx auto;

		&.noborder {
			border-bottom: 0
		}

		.icon {
			position: relative;
			flex-shrink: 0;
			width: 50rpx;
			height: 50rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.red-dot {
			position: absolute;
			top: 2rpx;
			right: -6rpx;
			width: 12rpx;
			height: 12rpx;
			background: #ff3b30;
			border-radius: 50%;
		}


		.arrow-wrap {
			margin-left: auto;
			position: relative;
			/* 添加相对定位 */
			display: flex;
			align-items: center;
		}
	}
}
</style>