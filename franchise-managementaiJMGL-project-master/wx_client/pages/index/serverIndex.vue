<template>
	<view class="container">
		<view class="overview-section">
			<view class="section-title">工作概况</view>
			<view class="metrics-container">
				<view class="metric-card" v-for="(metric, index) in metrics" :key="index">
					<view class="metric-title">{{ metric.title }}</view>
					<view class="metric-values">
						<view class="metric-value">{{ metric.value1 }}</view>
						<view class="metric-value">{{ metric.value2 }}</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 新增我的菜单部分 -->
		<view class="menu-section">
			<view class="section-title">我的菜单</view>
			<view class="menu-container">
				<view class="menu-item" v-for="(menu, index) in roleMenus" :key="index"
					@click="navigateToMenu(menu.path)">
					<view class="menu-icon">
						<text :class="['cuIcon-' + menu.icon]"></text>
					</view>
					<view class="menu-name">{{ menu.name }}</view>
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
	getUserType
} from '@/utils/auth';
export default {
	data() {
		return {
			userRole: 'shop', // 默认角色
			// 使用对象映射存储不同角色的指标数据
			roleMetrics: {
				// 店铺运营角色
				shop: [
					{
						title: '本月新增客户',
						value1: '128',
						value2: '+12%'
					},
					{
						title: '本月转化率',
						value1: '32%',
						value2: '+5%'
					},
					{
						title: '待跟进客户',
						value1: '45',
						value2: '-8%'
					}
				],
				// 招商角色
				attract: [
					{
						title: '本月新增铺位',
						value1: '15',
						value2: '+20%'
					},
					{
						title: '本月签约率',
						value1: '45%',
						value2: '+8%'
					},
					{
						title: '待处理铺位',
						value1: '23',
						value2: '-5%'
					}
				],
				// 设计角色
				design: [
					{
						title: '本月任务',
						value1: '10',
						value2: '+15%'
					}
				],
				manage: [
					{
						title: '本月监理项目',
						value1: '10',
						value2: '+15%'
					},
					{
						title: '本月验收通过率',
						value1: '11',
						value2: '+16%'
					},
					// {
					// 	title: '待处理问题',
					// 	value1: '2',
					// 	value2: '+17%'
					// },
				],
				// 管理角色
				admin: [
					{
						title: '本月新增店铺',
						value1: '8',
						value2: '同比+2'
					},
					{
						title: '本月业绩达标率',
						value1: '78%',
						value2: '同比+5%'
					},
					{
						title: '待审批事项',
						value1: '15',
						value2: '较上月+3'
					}
				]
			},
			// 可用角色列表
			availableRoles: ['shop', 'attract', 'design', 'manage', 'admin'],
			// 不同角色的菜单配置
			roleMenusConfig: {
				// 店铺运营角色菜单
				shop: [
					{
						name: '客户管理',
						icon: 'group',
						path: '/customer/index'
					},
					// {
					// 	name: '铺位采集',
					// 	icon: 'roundadd',
					// 	path: '/shops/collect'
					// },
					{
						name: '商圈笔记',
						icon: 'writefill',
						path: '/shops/noteList'
					}
				],
				// 招商角色菜单
				attract: [
					{
						name: '铺位管理',
						icon: 'location',
						path: '/shops/index'
					},
					{
						name: '签约记录',
						icon: 'file',
						path: '/shops/follow'
					},
					{
						name: '每日打卡',
						icon: 'countdown',
						path: '/clock/index'
					}
				],
				// 设计角色菜单
				design: [
					{
						name: '设计排期',
						icon: 'list',
						path: '/design/index'
					}

				],
				//监理人员
				manage: [
					{
						name: '监理任务',
						icon: 'repair',
						path: '/manage/index'
					}

				],
				// 管理角色菜单
				admin: [
					{
						name: '员工管理',
						icon: 'people',
						path: '/pages/admin/staff'
					},
					{
						name: '系统设置',
						icon: 'settings',
						path: '/pages/admin/settings'
					},
					{
						name: '数据分析',
						icon: 'rank',
						path: '/pages/admin/analytics'
					}
				]
			}
		}
	},
	computed: {
		metrics() {
			// 如果角色存在于预定义的角色中，返回对应的指标数据
			// 否则返回默认角色(shop)的指标数据

			return this.roleMetrics[this.userRole] || this.roleMetrics.shop;
		},
		roleMenus() {
			// 根据当前角色返回对应的菜单配置
			return this.roleMenusConfig[this.userRole] || this.roleMenusConfig.shop;
		}
	},
	onShow() {

	},
	onLoad() {
		const userType = getUserType()
		if (userType === "0") {
			uni.reLaunch({
				url: '/pages/index/index'
			})
			return // 重要：阻止后续代码执行
		}
		let that = this
		// 初始化获取用户角色和数据
		util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
			setUserInfo(wxGetInfoRes.user)
			wxGetInfoRes.user.roles.forEach(role => {

				that.userRole = role.roleKey

			})
			// 初始化获取指标数据
			that.fetchMetricsData();

		})
	},
	methods: {
		// 获取用户角色和相关数据


		// 获取指标数据
		fetchMetricsData() {
			let that = this
			// 根据角色获取不同的指标数据
			util.request("wx/api/getRoleMetrics", "", "GET").then(function (getRoleMetricsRes) {
				if (getRoleMetricsRes.code == 200) {
					let role = getRoleMetricsRes.data.role
					console.log(role)
					if (role == "shop") {
						let data = [
							{
								title: '本月新增客户',
								value1: getRoleMetricsRes.data.clientCurrentCount,
								value2: getRoleMetricsRes.data.clientGrowthRate
							},
							{
								title: '本月转化率',
								value1: getRoleMetricsRes.data.currentSignCount,
								value2: getRoleMetricsRes.data.signRate
							},
							{
								title: '待跟进客户',
								value1: getRoleMetricsRes.data.currentFollowCount,
								value2: getRoleMetricsRes.data.followRate
							}
						]
						that.roleMetrics["shop"] = data
					}else if(role == "attract"){
						let data = [
							{
								title: '本月新增铺位',
								value1: getRoleMetricsRes.data.currentShopCount,
								value2: getRoleMetricsRes.data.shopRate
							},
							{
								title: '本月签约率',
								value1: getRoleMetricsRes.data.currentShopSignCount,
								value2: getRoleMetricsRes.data.shopSignRate
							},
							{
								title: '待处理铺位',
								value1: getRoleMetricsRes.data.currentShopWaitCount,
								value2: getRoleMetricsRes.data.shopWaitRate
							}
						]
						that.roleMetrics["attract"] = data
					}else if(role == "design"){
						let data = [
							{
								title: '本月任务',
								value1: getRoleMetricsRes.data.currentDesignCount,
								value2: getRoleMetricsRes.data.designRate
							}
						]
						that.roleMetrics["design"] = data
					}else if(role == "manage"){
						let data = [
							{
								title: '本月监理项目',
								value1: getRoleMetricsRes.data.currentBuildCount,
								value2: getRoleMetricsRes.data.buildRate
							},
							{
								title: '本月验收通过率',
								value1: getRoleMetricsRes.data.currentBuildDownCount,
								value2: getRoleMetricsRes.data.buildDownRate
							}
						]
						that.roleMetrics["manage"] = data
					}

				}

			})
		},

		// 菜单导航方法
		navigateToMenu(path) {
			uni.navigateTo({
				url: path
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

.overview-section,
.menu-section {
	background: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.section-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 30rpx;
}

.metrics-container {
	display: flex;
	justify-content: space-between;
	flex-wrap: wrap;
}

.metric-card {
	width: 30%;
	background: #f8f8f8;
	border-radius: 12rpx;
	padding: 20rpx;
	margin-bottom: 20rpx;
}

.metric-title {
	font-size: 23rpx;
	color: #666;
	margin-bottom: 16rpx;
	text-align: center;
}

.metric-values {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.metric-value {
	font-size: 32rpx;
	color: #333;
	font-weight: bold;
	margin-bottom: 8rpx;
}

.metric-value:last-child {
	font-size: 24rpx;
	color: #1890ff;
}

/* 菜单样式 */
.menu-container {
	display: flex;
	justify-content: space-evenly;
}

.menu-item {
	width: 30%;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20rpx 0;
	border-radius: 12rpx;
	background: #f8f8f8;
}

.menu-icon {
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
	background: #1890ff;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-bottom: 16rpx;
}

.menu-icon text {
	color: #fff;
	font-size: 40rpx;
}

.menu-name {
	font-size: 28rpx;
	color: #333;
}
</style>
