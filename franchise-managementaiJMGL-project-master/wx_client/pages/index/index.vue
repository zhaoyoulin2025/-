<template>
	<view class="container">
		<view class="page-title">我的店铺</view>
		<scroll-view class="store-list" scroll-y>
			<view v-for="(store, index) in storeList" :key="index" class="store-card">
				<!-- 店铺信息卡片 -->
				<!-- <view class="store-info" @click="toggleStore(index)"> -->
				<view class="store-info">
					<view class="store-title">{{ store.shopName }}</view>
					<!-- <view class="info-row">
						<text class="label">申请人：</text>
						<text class="value">{{ store.applicant }}</text>
					</view>
					<view class="info-row">
						<text class="label">联系电话：</text>
						<text class="value">{{ store.phone }}</text>
					</view> -->
					<view class="info-row">
						<text class="label">店铺地址：</text>
						<text class="value">{{ store.address }}</text>
					</view>
					<!-- <view class="info-row">
						<text class="label">店铺面积：</text>
						<text class="value">{{ store.area }}㎡</text>
					</view> -->
				</view>

				<!-- 进度时间轴 -->
				<view class="progress-timeline" v-if="store.isExpanded">
					<view v-for="(stage, stageIndex) in store.stages" :key="stageIndex" class="stage-item" :class="{
						'active': stage && stage.status === 'active',
						'completed': stage && stage.status === 'completed'
					}" @click="toggleStageDetail(index, stageIndex)">
						<view class="stage-dot"></view>
						<view class="stage-content">
							<view class="stage-header">
								<text class="stage-name">{{ stage ? stage.name : '' }}</text>
								<text class="stage-date">{{ stage && stage.date ? stage.date : '待定' }}</text>
							</view>
							<text class="stage-status">{{ stage ? stage.statusText : '' }}</text>

							<!-- 阶段详情 -->
							<view class="stage-detail" v-if="stage && stage.showDetail && stage.isDetailExpanded">
								<view class="detail-title">进度详情</view>
								<view class="detail-item">
									<text class="detail-label">{{ stage.detailTitle }}</text>
									<text class="detail-status">{{ stage.detailStatus }}</text>
								</view>
								<view class="detail-info">
									<view class="info-item">施工时间：{{ stage.constructionTime }}</view>
									<view class="info-item">施工负责人：{{ stage.constructor }}</view>
									<view class="info-item">联系电话：{{ stage.constructorPhone }}</view>
								</view>
								<view class="site-photos" v-if="stage.photos && stage.photos.length">
									<image v-for="(photo, photoIndex) in stage.photos" :key="photoIndex" :src="photo"
										mode="aspectFill" class="site-photo" />
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<custom-tab-bar></custom-tab-bar>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo,
	setUserInfo,
	getUserType
} from '@/utils/auth';
export default {
	data() {
		return {
			// storeList: [
			// 	{
			// 		name: '北部商业中心店',
			// 		applicant: '张三',
			// 		phone: '13800138000',
			// 		address: '北部商业中心A区1层',
			// 		area: '120',
			// 		isExpanded: false,
			// 		stages: [{
			// 			name: '申请提交',
			// 			date: '2024-04-01',
			// 			status: 'completed',
			// 			statusText: '已提交加盟申请，等待审核',
			// 			showDetail: true,
			// 			isDetailExpanded: false,
			// 			detailTitle: '申请审核',
			// 			detailStatus: '已完成',
			// 			constructionTime: '2024-04-01 至 2024-04-03',
			// 			constructor: '审核部门',
			// 			constructorPhone: '13800138001',
			// 		}, {
			// 			name: '资料审核',
			// 			date: '2024-04-03',
			// 			status: 'completed',
			// 			statusText: '资料审核通过，进入设计阶段',
			// 			showDetail: false,
			// 			isDetailExpanded: false
			// 		}, {
			// 			name: '设计方案',
			// 			date: '2024-04-10',
			// 			status: 'completed',
			// 			statusText: '设计方案已确认，开始施工',
			// 			showDetail: false,
			// 			isDetailExpanded: false
			// 		}, {
			// 			name: '施工阶段',
			// 			date: '2024-04-15',
			// 			status: 'active',
			// 			statusText: '正在进行水电改造，预计5月1日完成',
			// 			showDetail: true,
			// 			isDetailExpanded: false,
			// 			detailTitle: '水电改造',
			// 			detailStatus: '进行中',
			// 			constructionTime: '2024-04-15 至 2024-05-01',
			// 			constructor: '李四',
			// 			constructorPhone: '13900139000',
			// 			photos: ['施工现场', '施工现场']
			// 		}, {
			// 			name: '验收阶段',
			// 			status: 'pending',
			// 			statusText: '等待施工完成后进行验收',
			// 			showDetail: false,
			// 			isDetailExpanded: false
			// 		}, {
			// 			name: '开业准备',
			// 			status: 'pending',
			// 			statusText: '等待验收通过后进行开业准备',
			// 			showDetail: false,
			// 			isDetailExpanded: false
			// 		}]
			// 	}
			// ]
			storeList: [],
		}
	},
	onLoad() {
		// 首先检查用户类型，如果是员工端用户，直接跳转到员工端首页
		const userType = getUserType()
		if (userType === "1") {
			uni.reLaunch({
				url: '/pages/index/serverIndex'
			})
			return // 重要：阻止后续代码执行
		}
		// 以下是原有逻辑，只有普通用户才会执行
		let that = this
		util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
			setUserInfo(wxGetInfoRes.user)
			if (wxGetInfoRes.user.customerType == 4) {
				util.toast("请进入小程序员工端!")
			}
			else {
				console.log(wxGetInfoRes.user.customerType)
				if (wxGetInfoRes.user.customerType != 3) {
					uni.reLaunch({
						url: '/pages/welcome/customerIndex',
					})
				}else {
					// 用户类型验证通过后，获取店铺列表
					that.fetchStoreList();
				}
				//customerType  没有提交问卷=0;  提交问卷没有预约=1;  预约了=2;  签约了的=3 不是客户 = 4

			}
		})
	},
	methods: {
		// 展开/收起店铺详情
		toggleStore(index) {
			this.storeList[index].isExpanded = !this.storeList[index].isExpanded
			// 收起时同时收起所有阶段详情
			if (!this.storeList[index].isExpanded) {
				this.storeList[index].stages.forEach(stage => {
					stage.isDetailExpanded = false
				})
			}
		},
		// 展开/收起阶段详情
		toggleStageDetail(storeIndex, stageIndex) {
			if (this.storeList[storeIndex].stages[stageIndex].detailStatus) {
				this.storeList[storeIndex].stages[stageIndex].isDetailExpanded =
					!this.storeList[storeIndex].stages[stageIndex].isDetailExpanded
			}

		},
		fetchStoreList() {
			uni.showLoading({
				title: '加载中...'
			});

			const userInfo = getUserInfo();
			const params = {
				clientId: userInfo.clientId // 根据用户ID获取对应的店铺列表
			};

			util.request('wx/api/myShopList', params, 'GET', 'application/json', false, true)
				.then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						this.storeList = res.data || [];
						// 处理店铺数据，确保每个店铺都有必要的字段
						this.storeList.forEach(store => {
							// 确保每个店铺都有展开状态
							if (store.isExpanded === undefined) {
								store.isExpanded = false;
							}
							// 处理阶段数据
							if (store.stages && Array.isArray(store.stages)) {
								store.stages.forEach(stage => {
									if (stage.isDetailExpanded === undefined) {
										stage.isDetailExpanded = false;
									}
								});
							}
						});
					} else {
						uni.showToast({
							title: res.msg || '获取店铺列表失败',
							icon: 'none'
						});
					}
				})
				.catch(err => {
					uni.hideLoading();
					console.error('获取店铺列表失败:', err);
					uni.showToast({
						title: '网络异常，请稍后再试',
						icon: 'none'
					});
				});
		},

	}
}
</script>

<style>
.container {
	padding: 20rpx;
	background-color: #f5f5f5;
	min-height: 100vh;
}

.store-list {
	height: 100vh;
}

.store-card {
	background: #fff;
	border-radius: 16rpx;
	margin-bottom: 20rpx;
	overflow: hidden;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.store-info {
	padding: 30rpx;
}

.store-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 20rpx;
}

.info-row {
	display: flex;
	margin-bottom: 10rpx;
	font-size: 28rpx;
}

.label {
	color: #666;
	width: 140rpx;
}

.value {
	color: #333;
	flex: 1;
}

.progress-timeline {
	padding: 30rpx;
	background: #f8f8f8;
}

.stage-item {
	position: relative;
	padding-left: 30rpx;
	margin-bottom: 40rpx;
}

.stage-item:last-child {
	margin-bottom: 0;
}

.stage-dot {
	position: absolute;
	left: 0;
	top: 8rpx;
	width: 16rpx;
	height: 16rpx;
	border-radius: 50%;
	background: #ddd;
}

.stage-item.completed .stage-dot {
	background: #52c41a;
}

.stage-item.active .stage-dot {
	background: #1890ff;
}

.stage-content {
	position: relative;
}

.stage-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8rpx;
}

.stage-name {
	font-size: 28rpx;
	color: #333;
	font-weight: 500;
}

.stage-date {
	font-size: 24rpx;
	color: #999;
}

.stage-status {
	font-size: 24rpx;
	color: #666;
}

.stage-detail {
	margin-top: 20rpx;
	padding: 20rpx;
	background: #fff;
	border-radius: 8rpx;
}

.detail-title {
	font-size: 28rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 16rpx;
}

.detail-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16rpx;
}

.detail-label {
	font-size: 28rpx;
	color: #333;
}

.detail-status {
	font-size: 24rpx;
	color: #1890ff;
	padding: 4rpx 12rpx;
	background: rgba(24, 144, 255, 0.1);
	border-radius: 4rpx;
}

.detail-info {
	font-size: 26rpx;
	color: #666;
}

.info-item {
	margin-bottom: 8rpx;
}

.site-photos {
	display: flex;
	margin-top: 16rpx;
	gap: 16rpx;
}

.site-photo {
	width: 160rpx;
	height: 160rpx;
	border-radius: 8rpx;
}

.page-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	padding: 30rpx;
	background: #fff;
	margin-bottom: 20rpx;
	border-radius: 16rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

/* 删除以下样式 */
.stage-item.disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

.stage-item.disabled .stage-dot {
	background: #999;
}

.stage-item.disabled .stage-name,
.stage-item.disabled .stage-date,
.stage-item.disabled .stage-status {
	color: #999;
}
</style>
