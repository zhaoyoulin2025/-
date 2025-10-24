<template>
	<view class="container">
		<!-- 顶部筛选下拉选项 -->
		<!-- <view class="filter-section">
			<picker @change="handleFilterChange" :value="currentFilterIndex" :range="filterOptions" range-key="label">
				<view class="picker-box">
					<text>{{ filterOptions[currentFilterIndex].label }}</text>
					<text class="cuIcon-unfold"></text>
				</view>
			</picker>
		</view> -->


		<!-- 客户列表 -->
		<view class="customer-list">

			<view v-for="(customer, index) in customerList" :key="index" class="customer-card">
				<view class="customer-info">
					<!-- 左侧信息：客户名称和电话 -->
					<view class="info-left">
						<view class="customer-name">{{ customer.name }}</view>
						<view class="customer-phone">{{ customer.address }}</view>
						<!-- <view class="customer-phone">{{ customer.shopSize }}</view> -->
					</view>

					<!-- 右侧状态 -->
					<!-- <view class="info-row">
						<view v-if="customer.shopStatus === 0" class="status-pending">
							{{ getStatusText(customer.shopStatus) }}
						</view>
						<view v-else-if="customer.shopStatus === 1" class="status-available">
							{{ getStatusText(customer.shopStatus) }}
						</view>
						<view v-else-if="customer.shopStatus === 2" class="status-signed">
							{{ getStatusText(customer.shopStatus) }}
						</view>
						<view v-else-if="customer.shopStatus === 3" class="status-offline">
							{{ getStatusText(customer.shopStatus) }}
						</view>
					</view> -->
				</view>

				<!-- 底部按钮 -->
				<view class="action-buttons">
					<button class="btn-detail" @click="viewDetail(customer.id)">详情</button>
					<button class="btn-follow" @click="followUp(customer.id)">修改</button>
				</view>
			</view>


		</view>


	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo,
} from '@/utils/auth';

export default {
	data() {
		return {
			// 筛选选项 - 修改为使用label和value结构
			filterOptions: [
				{ label: '全部状态', value: 9 },
				{ label: '待审核', value: 0 },
				{ label: '可招商', value: 1 },
				{ label: '已签约', value: 2 },
				{ label: '已下架', value: 3 }
			],
			currentFilterIndex: 0,
			// 客户列表数据
			customerList: [

			]
		}
	},
	onLoad() {
		// 页面加载时获取客户列表
		this.fetchCustomerList();
	},
	methods: {
		// 处理筛选变化
		handleFilterChange(e) {
			this.currentFilterIndex = e.detail.value;
			// 根据筛选条件重新获取数据
			this.fetchCustomerList();
		},

		// 获取客户列表数据
		fetchCustomerList() {
			console.log('获取商铺列表数据');
			// 显示加载中
			uni.showLoading({
				title: '加载中...'
			});
			// 构建请求参数，根据筛选条件
			const params = {};
			
			// 使用选中选项的value值
			params.shopStatus = this.filterOptions[this.currentFilterIndex].value;
			

			util.request('wx/api/shopList',params, 'GET', 'application/json', false, true)
				.then(res => {
					uni.hideLoading()
					if (res.code === 200) {
						this.customerList = res.data;
					} else {
						uni.showToast({
							title: res.msg ,
							icon: 'none'
						})
					}
				})
				.catch(err => {
					uni.hideLoading()
					uni.showToast({
						title: '网络异常，请稍后再试',
						icon: 'none'
					})
					
				})
			uni.hideLoading();

			// 调用API获取客户列表
			// 实际项目中应该使用真实API
			// util.request('customer/list', params, 'GET').then(res => {
			// 	if (res.code === 200) {
			// 		this.customerList = res.data;
			// 	} else {
			// 		util.toast(res.msg);
			// 	}
			// 	uni.hideLoading();
			// }).catch(err => {
			// 	util.toast('获取数据失败');
			// 	uni.hideLoading();
			// });

			// 模拟数据，实际开发中请替换为真实API调用

		},

		// 根据状态获取对应的CSS类名
		getStatusClass(status) {
			const statusClasses = {
				0: 'status-potential', // 潜在客户 - 蓝色
				1: 'status-intention', // 意向客户 - 橙色
				2: 'status-deal',      // 成交客户 - 绿色
				3: 'status-lost'       // 流失客户 - 灰色
			};
			return statusClasses[status] || 'status-potential';
		},

		// 根据状态获取对应的文本
		getStatusText(status) {
			console.log('status',status);
			const statusTexts = {
				9: '全部状态',
				0: '待审核',
				1: '可招商',
				2: '已签约',
				3: '已下架'
			};
			return statusTexts[status];
		},

		// 查看客户详情
		viewDetail(customerId) {
			console.log("customerId",customerId)
			uni.navigateTo({
				url: `/shops/detail?id=${customerId}&mode=view`
			});
		},

		// 客户跟进（修改）
		followUp(customerId) {
			uni.navigateTo({
				url: `/shops/detail?id=${customerId}&mode=edit`
			});
		}
	}
}
</script>

<style>
.container {
	padding: 20rpx;
	background-color: #f5f5f5;
	min-height: 100vh;
}

/* 筛选区域样式 */
.filter-section {
	background: #fff;
	padding: 20rpx 30rpx;
	border-radius: 16rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.picker-box {
	display: flex;
	justify-content: space-between;
	align-items: center;
	height: 80rpx;
	font-size: 30rpx;
	color: #333;
}

/* 客户列表样式 */
.customer-list {
	height: calc(100vh - 240rpx);
	/* 调整为更合理的高度 */
}

.customer-card {
	background: #fff;
	border-radius: 16rpx;
	margin-bottom: 20rpx;
	overflow: hidden;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.customer-info {
	padding: 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1rpx solid #f0f0f0;
}

.info-left {
	flex: 1;
}

.customer-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 10rpx;
}

.customer-phone {
	font-size: 28rpx;
	color: #666;
}

.info-right {
	margin-left: 20rpx;
}

/* 状态标签样式 */
.status-tag {
	padding: 8rpx 16rpx;
	border-radius: 8rpx;
	font-size: 24rpx;
	color: #fff;
	text-align: center;
}

.status-badge {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 24rpx;
	color: #fff;
	text-align: center;
	min-width: 120rpx;
}

.status-text {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 24rpx;
	color: #333;
	background-color: #f0f0f0;
	text-align: center;
	min-width: 120rpx;
}

.status-pending {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 32rpx;
	color: #fff;
	background-color: #409EFF;
	text-align: center;
	min-width: 120rpx;
}

.status-available {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 32rpx;
	color: #fff;
	background-color: #E6A23C;
	text-align: center;
	min-width: 120rpx;
}

.status-signed {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 32rpx;
	color: #fff;
	background-color: #67C23A;
	text-align: center;
	min-width: 120rpx;
}

.status-offline {
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 32rpx;
	color: #fff;
	background-color: #909399;
	text-align: center;
	min-width: 120rpx;
}
.status-potential {
	background-color: #409EFF; /* 蓝色 - 待审核 */
}

.status-intention {
	background-color: #E6A23C; /* 橙色 - 可招商 */
}

.status-deal {
	background-color: #67C23A; /* 绿色 - 已签约 */
}

.status-lost {
	background-color: #909399; /* 灰色 - 已下架 */
}

/* 按钮区域样式 */
.action-buttons {
	display: flex;
	padding: 20rpx 30rpx;
}

.action-buttons button {
	flex: 1;
	height: 80rpx;
	line-height: 80rpx;
	font-size: 28rpx;
	margin: 0 10rpx;
	border-radius: 8rpx;
}

.btn-detail {
	background-color: #f5f5f5;
	color: #333;
}

.btn-follow {
	background-color: #1890ff;
	color: #fff;
}

/* 无数据提示 */
.empty-tip {
	text-align: center;
	padding: 100rpx 0;
	color: #999;
	font-size: 28rpx;
}
</style>