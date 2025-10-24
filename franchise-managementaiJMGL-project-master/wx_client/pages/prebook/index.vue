<template>
	<view class="container">
		<!-- Tab栏 -->
		<view class="tab-container">
			<view class="tab-item" :class="{ active: activeTab === 0 }" @click="switchTab(0)">
				<text class="tab-text">待确认</text>
				<view class="tab-badge" v-if="getPendingCount() > 0">{{ getPendingCount() }}</view>
			</view>
			<view class="tab-item" :class="{ active: activeTab === 1 }" @click="switchTab(1)">
				<text class="tab-text">已确认</text>
				<view class="tab-badge" v-if="getConfirmedCount() > 0">{{ getConfirmedCount() }}</view>
			</view>
		</view>

		<!-- 预约列表 -->
		<scroll-view class="list-container" scroll-y>
			<!-- 简化但完整的列表项 -->
			<view class="appointment-card" v-for="(item, index) in filteredList" :key="index" v-if="item">
				<!-- 状态标签 -->
				<view class="status-tag" :style="{ backgroundColor: item.inviteStatus == 0 ? '#ff9500' : '#34c759', color: '#fff', padding: '4px 8px', borderRadius: '4px', fontSize: '12px', marginBottom: '10px', display: 'inline-block' }">
					{{ item.inviteStatus == 0 ? '待确认' : '已确认' }}
				</view>

				<!-- 预约时间 -->
				<view style="margin-bottom: 10px;">
					<text style="font-size: 16px; font-weight: bold; color: #333;">{{ formatDate(item.invitationTime) }}  </text>
					<text style="font-size: 16px; font-weight: bold; color: #333;margin-left: 15px;">{{ item.timeMoment }}</text>
				</view>

				<!-- 预约地点 -->
				<view style="margin-bottom: 10px;">
					<text style="font-size: 14px; color: #666;">预约地点：</text>
					<text style="font-size: 14px; color: #333;">{{ item.invitePlace}}</text>
				</view>

				<!-- 专属经理 -->
				<view style="margin-bottom: 10px;">
					<text style="font-size: 14px; color: #666;">专属经理：</text>
					<text style="font-size: 14px; color: #333;">{{ item.guideName }} {{ item.phone }}</text>
				</view>

				<!-- 预约详情 -->
				<view style="margin-bottom: 15px;">
					<text style="font-size: 14px; color: #666;">预约描述：</text>
					<text style="font-size: 14px; color: #333; line-height: 1.4;">{{ item.invitationDesc }}</text>
				</view>
				<!-- 确认参加按钮（仅待确认状态显示） -->
				<view v-if="item.inviteStatus == 0" style="text-align: center; margin-top: 15px;">
					<button 
						class="confirm-btn"
						@click="confirmAppointment(item)">
						☑ 确认参加
					</button>
					<view style="margin-top: 10px;">
						<text 
							style="font-size: 12px; color: #000;">
							时间不便？请直接与专属顾问沟通
						</text>
					</view>
				</view>

				<!-- 已确认状态显示 -->
				<view v-if="item.inviteStatus == 1" style="text-align: center; margin-top: 15px; padding: 10px; background-color: #f0f9ff; border-radius: 6px;">
					<text style="font-size: 14px; color: #34c759; font-weight: bold;">✓ 已确认参加</text>
					<view style="margin-top: 5px;">
						<text style="font-size: 12px; color: #666;">请按时到达预约地点</text>
					</view>
				</view>
			</view>

			<!-- 空状态 -->
			<view v-if="filteredList.length === 0" style="text-align: center; padding: 50px 20px;">
				<text style="font-size: 16px; color: #999;">{{ activeTab === 0 ? '暂无待确认的预约' : '暂无已确认的预约' }}</text>
			</view>
		</scroll-view>

		<!-- 下拉刷新提示 -->
		<view class="refresh-tip" v-if="isRefreshing" style="text-align: center; padding: 10px;">
			<text>正在刷新...</text>
		</view>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo
} from '@/utils/auth';

export default {
	data() {
		return {
			activeTab: 0, // 0: 待确认, 1: 已确认
			appointmentList: [],
			isRefreshing: false,
			isLoading: false
		}
	},
	computed: {
		// 根据当前tab过滤列表
		filteredList() {
			console.log('filteredList计算 - appointmentList:', this.appointmentList)
			
			if (!Array.isArray(this.appointmentList)) {
				console.warn('appointmentList不是数组:', this.appointmentList)
				return []
			}
			
			// 先清理无效数据，再进行过滤
			const validItems = this.appointmentList.filter((item, index) => {
				if (!item || typeof item !== 'object') {
					console.warn(`发现无效的item at index ${index}:`, item)
					return false
				}
				if (!item.hasOwnProperty('inviteStatus')) {
					console.warn(`item缺少inviteStatus字段 at index ${index}:`, item)
					return false
				}
				return true
			})
			
			console.log('有效数据项:', validItems)
			
			// 根据activeTab过滤数据
			const filtered = validItems.filter(item => {
				const status = parseInt(item.inviteStatus)
				return status === this.activeTab
			})
			
			console.log(`当前tab: ${this.activeTab}, 过滤后数据:`, filtered)
			return filtered
		}
	},
	onLoad() {
		// 页面加载时获取预约列表
		const userInfo = getUserInfo()
		if (!userInfo) {
			uni.navigateTo({
				url: '/pages/login/login',
			})
			return
		}
		
		console.log("userInfo.customerType", userInfo.customerType)
		//customerType  没有提交问卷=0;  提交问卷没有预约=1;  预约了=2;  签约了的=3 不是客户 = 4
		if (userInfo.customerType == 0) {
			uni.navigateTo({
				url: '/pages/question/index',
			})
		} else {
			this.getAppointmentList()
		}
	},
	onShow() {
		// 页面显示时刷新数据
		this.refreshData()
	},
	
	// 下拉刷新
	onPullDownRefresh() {
		this.refreshData(true)
	},
	
	methods: {
		// 切换tab
		switchTab(index) {
			if (this.activeTab === index) return
			
			this.activeTab = index
			console.log(`切换到tab: ${index === 0 ? '待确认' : '已确认'}`)
			
			// 可以添加切换动画效果
			this.$nextTick(() => {
				console.log(`当前显示${this.filteredList.length}条数据`)
			})
		},

		// 获取待确认数量
		getPendingCount() {
			if (!Array.isArray(this.appointmentList)) return 0
			return this.appointmentList.filter(item => parseInt(item.inviteStatus) === 0).length
		},

		// 获取已确认数量
		getConfirmedCount() {
			if (!Array.isArray(this.appointmentList)) return 0
			return this.appointmentList.filter(item => parseInt(item.inviteStatus) === 1).length
		},

		// 获取状态样式类
		getStatusClass(status) {
			const statusNum = parseInt(status)
			return {
				'status-pending': statusNum === 0,
				'status-confirmed': statusNum === 1,
				'status-cancelled': statusNum === 2
			}
		},

		// 获取状态文本
		getStatusText(status) {
			const statusNum = parseInt(status)
			const statusMap = {
				0: '待确认',
				1: '已确认',
				2: '已取消'
			}
			return statusMap[statusNum] || '未知状态'
		},

		// 格式化日期
		formatDate(dateStr) {
			if (!dateStr) return '日期待定'
			
			try {
				const date = new Date(dateStr)
				const year = date.getFullYear()
				const month = String(date.getMonth() + 1).padStart(2, '0')
				const day = String(date.getDate()).padStart(2, '0')
				return `${year}年${month}月${day}日`
			} catch (e) {
				return dateStr
			}
		},

		// 刷新数据
		refreshData(isPullRefresh = false) {
			if (isPullRefresh) {
				this.isRefreshing = true
			}
			
			this.getAppointmentList().finally(() => {
				if (isPullRefresh) {
					this.isRefreshing = false
					uni.stopPullDownRefresh()
				}
			})
		},

		// 获取预约列表
		getAppointmentList() {
			if (this.isLoading) return Promise.resolve()
			
			this.isLoading = true
			uni.showLoading({
				title: '加载中...'
			})

			return util.request('wx/api/inviteList', {
				clientId: getUserInfo().clientId
			}, 'GET', 'application/json', false, true)
				.then(res => {
					uni.hideLoading()
					this.isLoading = false
					
					console.log('API响应:', res)
					
					if (res.code === 200) {
						// 确保数据是数组格式
						let data = res.data || []
						
						// 处理不同的数据结构
						if (!Array.isArray(data)) {
							if (data.list && Array.isArray(data.list)) {
								data = data.list
							} else if (data.items && Array.isArray(data.items)) {
								data = data.items
							} else {
								console.warn('数据格式异常，使用空数组')
								data = []
							}
						}
						
						console.log('API原始数据:', data)
						
						// 数据预处理 - 更严格的验证
						data = data.filter((item, index) => {
							if (!item || typeof item !== 'object') {
								console.warn(`过滤掉无效数据项 at index ${index}:`, item)
								return false
							}
							if (!item.hasOwnProperty('inviteStatus')) {
								console.warn(`过滤掉缺少inviteStatus的数据项 at index ${index}:`, item)
								return false
							}
							return true
						}).map((item, index) => {
							const processedItem = {
								...item,
								// 确保inviteStatus是数字类型
								inviteStatus: parseInt(item.inviteStatus) || 0,
								// 添加唯一ID（如果没有的话）
								id: item.id || `appointment_${Date.now()}_${index}_${Math.random()}`
							}
							console.log(`处理数据项 ${index}:`, processedItem)
							return processedItem
						})
						
						console.log('最终处理后的数据:', data)
						console.log('待确认数量:', data.filter(item => item.inviteStatus === 0).length)
						console.log('已确认数量:', data.filter(item => item.inviteStatus === 1).length)
						
						// 使用Vue.set确保响应式更新
						this.$set(this, 'appointmentList', data)
						// 或者使用 this.appointmentList = data
					} else {
						uni.showToast({
							title: res.message || '获取预约列表失败',
							icon: 'none'
						})
					}
				})
				.catch(err => {
					uni.hideLoading()
					this.isLoading = false
					
					uni.showToast({
						title: '网络异常，请稍后再试',
						icon: 'none'
					})
					console.error('获取预约列表失败', err)
				})
		},

		// 确认参加预约
		confirmAppointment(item) {
			uni.showModal({
				title: '确认参加',
				content: `确认参加${this.formatDate(item.invitationTime)} ${item.timeMoment || ''}的预约邀请吗？`,
				success: (res) => {
					if (res.confirm) {
						this.submitConfirmation(item)
					}
				}
			})
		},

		// 提交确认
		submitConfirmation(item) {
			uni.showLoading({ title: '确认中...' })

			util.request('wx/api/confirmInvite', {
				id: item.id
			}, 'POST', 'application/json', false, true)
				.then(res => {
					uni.hideLoading()
					
					if (res.code === 200) {
						uni.showToast({
							title: '确认成功',
							icon: 'success'
						})
						
						// 刷新列表
						this.getAppointmentList()
					} else {
						uni.showToast({
							title: res.message || '确认失败',
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
					console.error('确认预约失败', err)
				})
		},

		// 显示取消对话框
		showCancelDialog(item, index) {
			uni.showModal({
				title: '联系专属顾问',
				content: `如需调整预约时间，请联系专属顾问：${item.managerName || '王经理'} ${item.managerPhone || '138****8888'}`,
				confirmText: '拨打电话',
				cancelText: '取消',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: item.managerPhone || '13800000000'
						})
					}
				}
			})
		}
	}
}
</script>

<style scoped>
.container {
	min-height: 100vh;
	background-color: #f5f5f5;
}

/* Tab栏样式 */
.tab-container {
	display: flex;
	background-color: #fff;
	border-bottom: 1px solid #e5e5e5;
	position: sticky;
	top: 0;
	z-index: 100;
}

.tab-item {
	flex: 1;
	padding: 15px 0;
	text-align: center;
	position: relative;
	transition: all 0.3s ease;
}

.tab-item.active {
	color: #007aff;
	border-bottom: 2px solid #007aff;
}

.tab-text {
	font-size: 16px;
	font-weight: 500;
}

.tab-badge {
	position: absolute;
	top: 8px;
	right: 20px;
	background-color: #ff3b30;
	color: white;
	font-size: 12px;
	padding: 2px 6px;
	border-radius: 10px;
	min-width: 18px;
	height: 18px;
	line-height: 14px;
	text-align: center;
}

/* 列表容器 */
.list-container {
	flex: 1;
	padding: 10px;
}

/* 预约卡片 */
.appointment-card {
	background-color: #fff;
	border-radius: 8px;
	padding: 15px;
	margin-bottom: 10px;
	box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 刷新提示 */
.refresh-tip {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: rgba(0,0,0,0.7);
	color: white;
	padding: 10px 20px;
	border-radius: 4px;
	z-index: 1000;
}

/* 确认按钮样式 */
.confirm-btn {
	background-color: #28a745;
	color: white;
	border: none;
	padding: 10px 20px;
	border-radius: 6px;
	font-size: 16px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.confirm-btn:hover {
	background-color: #218838;
}
</style>

