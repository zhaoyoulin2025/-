<template>
	<view class="container">
		<!-- 顶部筛选：搜索 + 城市下拉 + 等级下拉 -->
		<view class="filter-section">
			<view class="search-box">
				<input class="search-input" v-model="keyword" placeholder="搜索姓名/电话" confirm-type="search"
					@confirm="fetchCustomerList" @input="onKeywordInput" />
			</view>
			<picker mode="selector" :value="selectedCityIndex" :range="cityOptions" @change="handleCityChange"
				@cancel="handleCityCancel">
				<view class="picker-box">
					<text>{{ selectedCityIndex === -1 ? '意向城市' : cityOptions[selectedCityIndex] }}</text>
					<text class="cuIcon-unfold"></text>
				</view>
			</picker>

			<picker mode="selector" :value="selectedLevelIndex" :range="levelOptions" @change="handleLevelChange"
				@cancel="handleLevelCancel">
				<view class="picker-box">
					<text>{{ selectedLevelIndex === -1 ? '客户等级' : levelOptions[selectedLevelIndex] }}</text>
					<text class="cuIcon-unfold"></text>
				</view>
			</picker>
		</view>


		<!-- 客户列表 -->
		<view class="customer-list">

			<view v-for="(customer, index) in customerList" :key="index" class="customer-card">
				<view class="customer-info"  @click="viewDetail(customer.id)">
					<!-- 左侧信息：客户名称和电话 -->
					<view class="info-left">
						<view class="customer-name">{{ customer.name || '' }}</view>
						<view class="customer-phone">电话：{{ customer.phone || '' }}</view>
						<view class="customer-phone">意向城市：{{ customer.expectedCity || '' }}</view>
					</view>

					<!-- 右侧状态 -->
					<view class="info-row">
						<view class="item-status" :class="{
							'status-potential': customer.clientLevel === '1',
							'status-intention': customer.clientLevel === '2',
							'status-deal': customer.clientLevel === '3',
							'status-lost': customer.clientLevel === '4',
						}">{{ getStatusText(customer.clientLevel) }}</view>

					</view>
				</view>

				<!-- 底部按钮 -->
				<!-- <view class="action-buttons">
					<button class="btn-detail" @click="viewDetail(customer.id)">详情</button>
					<button class="btn-follow" @click="followUp(customer.id)">跟进</button>
				</view> -->
			</view>


		</view>


	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo,
} from '@/utils/auth';
import { cityList as cityOptions } from '@/utils/city.js'

export default {
	data() {
		return {
			// 筛选选项 - 修改为使用label和value结构
			filterOptions: [
				{ label: '全部状态', value: 9 },
				{ label: '潜在', value: 0 },
				{ label: '已邀约', value: 2 },
				{ label: '已面谈', value: 3 },
				{ label: '已签约', value: 4 },
				{ label: '已流失', value: 10 },
			],
			currentFilterIndex: 0,
			// 客户列表数据
			customerList: [

			],
			keyword: '',
			cityOptions,
			selectedCityIndex: -1,
			selectedCity: '',
			levelOptions: ['A', 'B', 'C', 'D'],
			selectedLevelIndex: -1,
			selectedLevel: '',
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
		handleCityChange(e) {
			const idx = Number(e.detail.value)
			this.selectedCityIndex = idx
			this.selectedCity = this.cityOptions[idx] || ''
			this.fetchCustomerList()
		},
		handleCityCancel() {
			this.selectedCityIndex = -1
			this.selectedCity = ''
			this.fetchCustomerList()
		},
		handleLevelChange(e) {
			const idx = Number(e.detail.value)
			this.selectedLevelIndex = idx
			this.selectedLevel = this.levelOptions[idx] || ''
			this.fetchCustomerList()
		},
		handleLevelCancel() {
			this.selectedLevelIndex = -1
			this.selectedLevel = ''
			this.fetchCustomerList()
		},
		onKeywordInput(e) {
			// v-model 已更新 keyword，这里保证清空也能触发
			this.keyword = (e && e.detail && typeof e.detail.value === 'string') ? e.detail.value : ''
			this.fetchCustomerList()
		},
		// 获取客户列表数据（更新参数构建）
		fetchCustomerList() {
			console.log('获取客户列表数据');
			// 显示加载中
			uni.showLoading({
				title: '加载中...'
			});
			// 构建请求参数，根据筛选条件
			const params = {};


			if (this.keyword && this.keyword.trim()) {
				params.keyWord = this.keyword.trim()
			}
			if (this.selectedCity) {
				params.expectedCity = this.selectedCity
			}
			if (this.selectedLevel) {
				params.levelName = this.selectedLevel
			}
			getUserInfo().roles.forEach(role => {
				if (role.roleKey === 'shop') {
					params.clientStatus = 4;
				}
			});


			util.request('wx/api/clientList', params, 'GET', 'application/json', false, true)
				.then(res => {
					uni.hideLoading()
					if (res.code === 200) {
						this.customerList = res.data;
					} else {
						uni.showToast({
							title: res.msg,
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
				"1": 'status-potential', // 潜在客户 - 蓝色
				"2": 'status-intention', // 意向客户 - 橙色
				"3": 'status-deal',      // 成交客户 - 绿色
				"4": 'status-lost'       // 流失客户 - 灰色
			};
			return statusClasses[status] || 'status-potential';
		},

		// 根据状态获取对应的文本
		// 根据状态获取对应的文本
		getStatusText(status) {
			console.log('status', status);
			const statusTexts = {
				"1": 'A级',
				"2": 'B级',
				"3": 'C级',
				"4": 'D级',
			};
			return statusTexts[status] || 'A级';
		},

		// 查看客户详情
		viewDetail(customerId) {
			uni.navigateTo({
				url: `/customer/detail?id=${customerId}`
			});
		},

		// 客户跟进
		followUp(customerId) {
			uni.navigateTo({
				url: `/customer/follow?id=${customerId}`
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
	display: flex;
	align-items: center;
	gap: 20rpx;
}

.search-box {
	flex: 1;
	height: 72rpx;
	background: #f5f5f5;
	border-radius: 36rpx;
	padding: 0 24rpx;
	display: flex;
	align-items: center;
}

.search-input {
	width: 100%;
	font-size: 28rpx;
	color: #333;
}

.picker-box {
	min-width: 180rpx;
	height: 72rpx;
	padding: 0 24rpx;
	border-radius: 36rpx;
	background: #f5f5f5;
	display: flex;
	align-items: center;
	justify-content: space-between;
	font-size: 28rpx;
	color: #666;
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
	margin-bottom: 30rpx;
}

.customer-phone {
	font-size: 28rpx;
	color: #666;
	margin-bottom: 30rpx;
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

.status-potential {
	background: linear-gradient(135deg, #52c41a, #73d13d);

	color: #fff;
	font-size: 30rpx;
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	margin-top: -105rpx;

}

.status-intention {
	background: linear-gradient(135deg, #1890ff, #36a9ff);
	color: #fff;
	font-size: 30rpx;
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
}

.status-deal {

	background: linear-gradient(135deg, #fa1616, #fa1616);
	color: #fff;
	font-size: 30rpx;
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
}

.status-lost {
	background: linear-gradient(135deg, #8c8c8c, #bfbfbf);
	color: #fff;
	font-size: 30rpx;
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
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