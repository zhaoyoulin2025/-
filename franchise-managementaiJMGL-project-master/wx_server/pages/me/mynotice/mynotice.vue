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
					'unread-system': !item.isRead && item.noticeType === '系统消息',
					'unread-progress': !item.isRead && item.noticeType === '进度通知',
					'unread-reminder': !item.isRead && item.noticeType === '提醒'
				}" v-for="(item, index) in noticelist" :key="index" @tap="readNotice(item)">
					<view class="notice-content">
						<view class="notice-type-text" :class="{
							'system': item.noticeType === '系统消息',
							'progress': item.noticeType === '进度通知',
							'reminder': item.noticeType === '提醒'
						}">{{item.noticeType}}</view>
						<view class="notice-header">
							<text class="notice-title">{{item.noticeTitle}}</text>
							<text class="notice-time">{{item.createTime}}</text>
						</view>
						<view class="notice-desc">{{item.noticeDesc}}</view>
					</view>
				</view>
			</template>
		</view>
	</view>
</template>

<script>
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
		getNoticelist() {
			// let that = this
			this.noticelist = [
				{
					noticeType: '系统消息',
					noticeTitle: '系统消息标题1',
					noticeDesc: '系统消息内容1',
					createTime: '2023-05-10 10:00:00',
					isRead: false
				},
				{
					noticeType: '进度通知',
					noticeTitle: '进度通知标题1',		
					noticeDesc: '进度通知内容1',
					createTime: '2023-05-10 10:00:00',
					isRead: false
				},
				{
					noticeType: '提醒',
					noticeTitle: '提醒标题1',
					noticeDesc: '提醒内容1',
					createTime: '2023-05-10 10:00:00',
					isRead: false
				},
			]
			console.log(this.noticelist)
			// that.request('loadNotice', 'GET').then(res => {
			// 	if (res) {
			// 		this.noticelist = res.data
			// 	}
			// })
		},
		switchTab(index) {
			this.currentTab = index
		},
		markAllRead() {
			// 调用标记全部已读的接口
			// this.request('markAllNoticeRead', 'POST').then(res => {
			// 	if (res.code === 200) {
			// 		uni.showToast({
			// 			title: '已全部标记为已读',
			// 			icon: 'success'
			// 		})
			// 		this.getNoticelist()
			// 	}
			// })
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
			if (!item.isRead) {
				item.isRead = true
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
.notice-item.unread-system { border-left-color: #2979ff; }
.notice-item.unread-progress { border-left-color: #19be6b; }
.notice-item.unread-reminder { border-left-color: #ff9900; }

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

.notice-type.system { background-color: #2979ff; }
.notice-type.progress { background-color: #19be6b; }
.notice-type.reminder { background-color: #ff9900; }

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

.notice-type-text.system { color: #2979ff; }
.notice-type-text.progress { color: #19be6b; }
.notice-type-text.reminder { color: #ff9900; }
</style>
