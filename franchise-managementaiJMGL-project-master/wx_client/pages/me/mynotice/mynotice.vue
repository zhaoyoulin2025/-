<template>
	<view>
		<view class="header-action">
			<view class="read-all" @tap="markAllRead">全部已读</view>
		</view>
		<view class="tab-header">
			<view class="tab-list">
				<view class="tab-item" :class="{ active: currentTab === 0 }" @tap="switchTab(0)">全部</view>
				<view class="tab-item" :class="{ active: currentTab === 1 }" @tap="switchTab(1)">系统消息</view>
				<view class="tab-item" :class="{ active: currentTab === 2 }" @tap="switchTab(2)">进度通知</view>
				<view class="tab-item" :class="{ active: currentTab === 3 }" @tap="switchTab(3)">提醒</view>
			</view>
		</view>

		<view class="notice-list">
			<template>
				<view class="notice-item" :class="{
					'unread-system': item.status == 0 && item.messageType === 'system',
					'unread-progress': item.status == 0 && item.messageType === 'progress',
					'unread-reminder': item.status == 0 && item.messageType === 'reminder'
				}" v-for="(item, index) in noticelist" :key="index" @tap="readNotice(item)">
					<view class="notice-content">
						<view class="notice-type-text" :class="{
							'system': item.messageType === 'system',
							'progress': item.messageType === 'progress',
							'reminder': item.messageType === 'reminder'
						}">{{ item.messageType === 'system' ? '系统消息' : item.messageType === 'progress' ? '进度通知' : item.messageType ===
							'reminder' ? '提醒' : '' }}</view>
						<view class="notice-header">
							<text class="notice-title">{{ item.title }}</text>
							<text class="notice-time">{{ item.sendTime }}</text>
						</view>
						<view class="notice-desc">{{ item.content }}</view>
					</view>
				</view>
			</template>
		</view>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
export default {
	data() {
		return {
			currentTab: 0,
			noticelist: [],
			typeMap: {
				'系统消息': 0,
				'进度通知': 1,
				'提醒': 2
			}
		}
	},
	onLoad() {
		this.getNoticelist()
	},
	methods: {
		getNoticelist(messageType = '') { // 增加 messageType 参数，默认为空字符串表示获取全部
			uni.showLoading({ title: '加载中', mask: true });
			util.request('wx/api/getMessageList', { messageType: messageType }, 'GET', 'application/json', false, true)
				.then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						this.noticelist = res.data;
					} else {
						uni.showToast({
							title: res.msg || '获取记录失败',
							icon: 'none'
						});
					}
				})
				.catch(err => {
					uni.hideLoading();
					uni.showToast({
						title: '网络异常，请稍后再试',
						icon: 'none'
					});
				});
		},
		switchTab(index) {
			this.currentTab = index
			let messageType = ''
			switch (index) {
				case 0: // 全部
					messageType = ''
					break
				case 1: // 系统消息
					messageType = 'system'
					break
				case 2: // 进度通知
					messageType = 'progress'
					break
				case 3: // 提醒
					messageType = 'reminder'
					break
			}
			this.getNoticelist(messageType); // 调用 getNoticelist 并传入对应的 messageType
		},
		markAllRead() {
			uni.showLoading({ title: '加载中', mask: true });
			util.request('wx/api/readAllMessage', {}, 'POST', 'application/json', false, true)
				.then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						this.noticelist = res.data;
					} else {
						uni.showToast({
							title: res.msg || '获取记录失败',
							icon: 'none'
						});
					}
					let messageType = ''
					switch (this.currentTab) {
						case 0: // 全部
							messageType = ''
							break
						case 1: // 系统消息
							messageType = 'system'
							break
						case 2: // 进度通知
							messageType = 'progress'
							break
						case 3: // 提醒
							messageType = 'reminder'
							break
					}
					this.getNoticelist(messageType);
				})
				.catch(err => {
					console.log(err)
					uni.hideLoading();
					uni.showToast({
						title: '网络异常，请稍后再试',
						icon: 'none'
					});
				});

		},
		getTypeClass(type) {
			const typeMap = {
				'系统消息': 'system',
				'进度通知': 'progress',
				'提醒': 'reminder'
			}
			return typeMap[type] || 'system'
		},
		readNotice(item) {
			console.log(item)
			if (item.status === 0) {
				uni.showLoading({ title: '加载中', mask: true });
				util.request('wx/api/readMessage', { id: item.id }, 'POST', 'application/json', false, true)
					.then(res => {
						uni.hideLoading();
						if (res.code === 200) {
							this.noticelist = res.data;
						} else {
							uni.showToast({
								title: res.msg || '获取记录失败',
								icon: 'none'
							});
						}
						let messageType = ''
						switch (this.currentTab) {
							case 0: // 全部
								messageType = ''
								break
							case 1: // 系统消息
								messageType = 'system'
								break
							case 2: // 进度通知
								messageType = 'progress'
								break
							case 3: // 提醒
								messageType = 'reminder'
								break
						}
						this.getNoticelist(messageType);
					})
					.catch(err => {
						console.log(err)
						uni.hideLoading();
						uni.showToast({
							title: '网络异常，请稍后再试',
							icon: 'none'
						});
					});

				// 这里可以调用接口更新已读状态
				// this.request('readNotice', 'POST', { id: item.id })
			}
		}
	}
}
</script>

<style>
.header-action {
	padding: 40rpx 30rpx;
	display: flex;
	justify-content: flex-end;
	background-color: #fff;
}

.read-all {
	color: #2979ff;
	font-size: 27rpx;
	padding: 10rpx 20rpx;
}

.tab-header {
	padding: 20rpx 30rpx;
	background-color: #fff;
	border-bottom: 1rpx solid #eee;
}

.tab-list {
	display: flex;
	justify-content: space-between;
}

.tab-item {
	position: relative;
	padding: 0 10rpx;
	font-size: 28rpx;
	color: #666;
}

.tab-item.active {
	color: #2979ff;
	font-weight: bold;
}

.tab-item.active:after {
	content: '';
	position: absolute;
	bottom: -10rpx;
	left: 50%;
	width: 40rpx;
	height: 4rpx;
	background: #2979ff;
	transform: translateX(-50%);
}

.read-all {
	position: absolute;
	transform: translateY(-50%);
	color: #2979ff;
	font-size: 24rpx;
	padding: 10rpx 20rpx;
}

.notice-list {
	padding: 20rpx;
}

.notice-item {
	display: flex;
	padding: 20rpx;
	background: #fff;
	border-radius: 12rpx;
	margin-bottom: 20rpx;
	border-left: 8rpx solid transparent;
}

.notice-item.unread-system {
	border-left-color: #2979ff;
}

.notice-item.unread-progress {
	border-left-color: #19be6b;
}

.notice-item.unread-reminder {
	border-left-color: #ff9900;
}

.notice-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 10rpx;
}

.notice-title {
	font-size: 30rpx;
	color: #333;
}

.notice-time {
	font-size: 24rpx;
	color: #999;
}

.notice-type {
	width: 8rpx;
	height: auto;
	min-height: 100rpx;
	margin-right: 20rpx;
	border-radius: 4rpx;
}

.notice-type.system {
	background-color: #2979ff;
}

.notice-type.progress {
	background-color: #19be6b;
}

.notice-type.reminder {
	background-color: #ff9900;
}

.notice-content {
	flex: 1;
}

.notice-title {
	font-size: 30rpx;
	color: #333;
	margin-bottom: 10rpx;
}

.notice-desc {
	font-size: 26rpx;
	color: #666;
	margin-bottom: 20rpx;
}

.notice-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.notice-time {
	font-size: 24rpx;
	color: #999;
}

.notice-detail {
	font-size: 24rpx;
	color: #2979ff;
}

.notice-type-text {
	font-size: 24rpx;
	margin-bottom: 10rpx;
	font-weight: bold;
}

.notice-type-text.system {
	color: #2979ff;
}

.notice-type-text.progress {
	color: #19be6b;
}

.notice-type-text.reminder {
	color: #ff9900;
}
</style>
