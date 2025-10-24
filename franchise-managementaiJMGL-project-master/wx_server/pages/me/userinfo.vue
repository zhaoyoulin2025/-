<template>
	<view class="container" :style="{'min-height':screenHeight+'px'}">
		
		<view class="content_view">
			<button class="avatar-wrapper" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
				<image class="avatar" :src="avatarUrl"></image>
			</button>
			<input type="nickname" class="weui-input" placeholder="请输入昵称" maxlength="15" :value="nickName"
				@change="getNickname" />
			<button class="bind-btn" open-type="getPhoneNumber" @getphonenumber="onGetPhoneNumber">
				<text class="bind-text">
					{{ phone ? phone : '请点击绑定手机号' }}
				</text>
			</button>
		</view>
		<view class="bottom_view">
			<view class="report_view" @click="submit">
				<text class="report_text">提交</text>
			</view>
		</view>
	</view>
</template>
<script>
	const defaultAvatarUrl =
		'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

	import {
		getToken,
		getUserInfo,
		setUserInfo
	} from '@/utils/auth';
	const util = require("@/utils/util.js")
	export default {
		data() {
			return {
				userInfo: {},
				avatarUrl: defaultAvatarUrl,
				nickName: "",
				phone: "",
				userId:""
			}
		},
		created() {
			this.userInfo = getUserInfo();
			this.userId = this.userInfo.userId
			this.nickName = this.userInfo.nickName == "暂无昵称"?"":this.userInfo.nickName;
			this.avatarUrl = this.userInfo.avatar
			this.phone = this.userInfo.phonenumber
			console.log("userInfo", this.userInfo)
		},
		mounted() {

		},
		methods: {
			clickBack() {
				uni.navigateBack();
			},
			getNickname(e) {
				this.nickName = e.detail.value
			},
			checkNickName() {
				if (!this.nickName) {
					uni.showToast({
						title: '请输入昵称',
						icon: 'none'
					})
					return false
				}
				let str = this.nickName.trim();
				if (str.length == 0) {
					uni.showToast({
						title: '请输入正确的昵称',
						icon: 'none'
					})
					return false
				}
				this.nickName = str
				// if ((/[^/a-zA-Z0-9\u4E00-\u9FA5]/g).test(str)) {
				//  uni.showToast({
				//      title: '请输入中英文和数字',
				//      icon: 'none'
				//  })
				//  return false
				// }
				return true
			},
			onChooseAvatar(e) {
				let that = this  // 保存 this 引用
				let tmpFilePath = e.detail.avatarUrl;
			
				//对临时图片链接进行base64编码
				var avatarUrl_base64 = 'data:image/jpeg;base64,' + wx.getFileSystemManager().readFileSync(tmpFilePath,
					'base64')
				var reqData = {
					"base64Str": avatarUrl_base64
				}

				util.request("common/uploadBase64Img", reqData, "POST").then((res) => {  // 使用箭头函数
					if (res.code === 200) {
						that.avatarUrl = res.url;  // 使用保存的 that 引用
						console.log("this.avatarUrl", that.avatarUrl)
					} else {
						uni.showToast({
							icon: "none",
							title: "获取失败，请稍后试试！",
							duration: 2000
						})
					}
				})
			},
			onGetPhoneNumber(e) {
				console.log(e)
				let that = this  // 保存 this 引用
				if (e.detail.errMsg == "getPhoneNumber:fail user deny") { //用户决绝授权  

					//拒绝授权后弹出一些提示  

				} else { //允许授权  
					let form = {
						code: e.detail.code,
					}
					console.log(form)
					util.request("wxGetPhone", form, "POST").then((res) => {  
						if (res.code === 200) {
							uni.hideLoading()
							setTimeout(function() {
								uni.hideLoading();
							}, 2000);
							console.log(res)
							that.phone = res.data
						} else {
							uni.showToast({
								icon: "none",
								title: "获取失败，请稍后试试！",
								duration: 2000
							})
						}
					})
				}
			},
			submit() {
				console.log("submit提交", this.nickName, this.avatarUrl)
				// return
				if (!this.avatarUrl) {
					uni.showToast({
						title: '请上传头像',
						icon: 'none'
					})
					return
				}
				if (!this.checkNickName()) {
					return
				}
				if (!this.phone) {
					uni.showToast({
						title: '请绑定手机',
						icon: 'none'
					})
					return
				}
				let form = {
					nickName: this.nickName,
					avatar: this.avatarUrl,
					userId: this.userId,
					phonenumber: this.phone
				}
				console.log("form====", form)
				// return

				uni.showLoading({
					title: '加载中',
					mask: true
				})
				util.request("wxUpdateUserInfo", form, "POST").then(function(res) {
					uni.hideLoading();
					if (res.code === 200) {
						let user = res.user;
						setUserInfo(user)
						if(user.customerType ==0){
							uni.reLaunch({
								url: '/pages/question/index',
							})
						}else if(user.customerType ==1 || user.customerType ==2){
							uni.reLaunch({
								url: '/pages/prebook/index',
							})
						}else if(user.customerType ==3){
							uni.reLaunch({
								url: '/pages/index/index',
							})
						}
					} else {
						uni.showToast({
							icon: "none",
							title: res.msg,
							duration: 2000
						})
					}
				})
			},
		}
	}
</script>
<style lang='scss'>
	.container {
		display: flex;
		flex-direction: column;
		margin-top: 200rpx;
		
	}

	/* 边框样式 */
	button::after {
		border: 0;
	}

	.content_view {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 100%;
		margin-top: 68px;
		position: relative;
		/* background-color: #F6F6FC; */
	}

	.avatar-wrapper {
		width: 100px;
		height: 100px;
		padding: 0 !important;
		margin: 0 !important;
		border-radius: 100px;
	}

	.avatar {
		width: 100px;
		height: 100px;
		border-radius: 100px;
		border-style: solid;
		border-width: 1px;
		border-color: #d6d6d5;
	}

	.weui-input {
		margin-top: 40px;
		width: 250px;
		height: 40px;
		background: #f4f4f6;
		line-height: 40px;
		text-align: center;
	}

	.bottom_view {
		position: fixed;
		bottom: 0;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		width: 100%;
		background-color: white;
		z-index: 20;
		padding-top: 10px;
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);
	}

	.report_view {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		width: 706rpx;
		height: 76rpx;
		background-color: #e60012;
		border-radius: 19px;
		margin-bottom: 10px;
	}

	.report_text {
		color: #fff;
		font-size: 15px;
	}

	.bind-btn {
		width: 250px;
		height: 90rpx;
		line-height: 90rpx;
		background: none;
		padding: 0 30rpx;
		text-align: left;
		position: relative;
		margin-top: 50rpx;
		background-color: #f4f4f6;
	}

	.bind-text {
		font-size: 28rpx;
		display: block;
		width: 100%;
		text-align: center;
	}
</style>