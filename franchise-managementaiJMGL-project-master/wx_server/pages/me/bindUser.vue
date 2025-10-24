<template>
	<view class="container">
		<view class="bind-info">
			<view class="info-row">
				<text class="info-label">绑定用户名</text>
				<text class="info-value">{{ bindUsername || '暂未绑定账号' }}</text>
				<button class="action-btn" @click="toggleBindStatus">
					{{ bindUsername ? '我要解绑' : '我要绑定' }}
				</button>
			</view>
		</view>
		
		<view class="bind-form" v-if="showBindForm">
			<view class="input-group">
				<input class="input-field" type="text" v-model="username" placeholder="请输入用户名" maxlength="30" />
			</view>
			
			<view class="input-group">
				<input class="input-field" password="true" v-model="password" placeholder="请输入密码" maxlength="30" />
			</view>
			
			<button class="bind-btn" @click="bindAccount">绑定账号</button>
		</view>
	</view>
</template>

<script>
import {
	getUserInfo,
	setUserInfo
} from '@/utils/auth';
const util = require("@/utils/util.js")

export default {
	data() {
		return {
			bindUsername: '',
			bindPassword: '',
			showBindForm: false,
			username: '',
			password: '',
			userInfo: null
		}
	},
	created() {
		// 从缓存获取用户信息
		this.userInfo = getUserInfo();
		if(this.userInfo.password){
			this.bindUsername = this.userInfo.username
		}
		
	},
	methods: {
		// 切换绑定状态
		toggleBindStatus() {
			if (this.bindUsername) {
				// 已绑定，执行解绑操作
				util.modal('解绑确认', '确定要解除账号绑定吗？', true, (res) => {
					if (res) {
						this.unbindAccount();
					}
				});
			} else {
				// 未绑定，显示绑定表单
				this.showBindForm = true;
			}
		},
		
		// 绑定账号
		bindAccount() {
			// 表单验证
			if (!this.username) {
				util.toast("请输入用户名");
				return;
			}
			if (!this.password) {
				util.toast("请输入密码");
				return;
			}
			
			// 构建请求参数
			const form = {
				username: this.username,
				password: this.password,
                uId: this.userInfo.userId
			};
			
			util.request("wxUserBind", form).then(function (wxloginRes) {
				if (wxloginRes.code == 200) {
                    setUserInfo(wxGetInfoRes.user)
                    util.toast("绑定成功", 2000, 'success');
                    uni.switchTab({
                        url: '/pages/index/index',
                    })
					
				}else{
					util.toast(wxloginRes.msg);
				}

			})
		
		},
		
		// 解绑账号
		unbindAccount() {
			// 这里可以添加实际的解绑API请求
			// 示例：调用API进行解绑
			// util.request("unbindUserAccount", {}).then((res) => {
			// 	if (res.code === 200) {
			// 		// 解绑成功，清除缓存
			// 		this.clearBindInfo();
			// 		util.toast("解绑成功", 2000, 'success');
			// 	} else {
			// 		util.toast(res.msg || "解绑失败");
			// 	}
			// });
			
			// 模拟解绑成功
			
		},
		
		
		// 清除绑定信息
		clearBindInfo() {
			this.bindUsername = '';
			this.bindPassword = '';
			this.showBindForm = false;
			
			// 清除缓存
			removeRememberInfo();
		}
	}
}
</script>

<style lang="scss">
.container {
	padding: 30rpx;
	min-height: 100vh;
	background-color: #f8f8f8;
}

.bind-info {
	background-color: #fff;
	border-radius: 12rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.info-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		
		.info-label {
			font-size: 28rpx;
			color: #333;
			font-weight: bold;
		}
		
		.info-value {
			font-size: 28rpx;
			color: #666;
			flex: 1;
			margin-left: 20rpx;
		}
		
		.action-btn {
			width: auto;
			height: 60rpx;
			line-height: 60rpx;
			font-size: 24rpx;
			padding: 0 20rpx;
			background-color: #007AFF;
			color: #fff;
			border-radius: 30rpx;
			margin: 0;
		}
	}
}

.bind-form {
	background-color: #fff;
	border-radius: 12rpx;
	padding: 30rpx;
	
	.input-group {
		margin-bottom: 30rpx;
		
		.input-field {
			width: 100%;
			height: 88rpx;
			padding: 0 30rpx;
			font-size: 28rpx;
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
	
	.bind-btn {
		width: 100%;
		height: 88rpx;
		line-height: 88rpx;
		background-color: #007AFF;
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 12rpx;
		border: none;
		margin-top: 30rpx;
		
		&:active {
			background-color: #0056CC;
		}
	}
}
</style>