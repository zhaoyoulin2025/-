<template>
	<view class="container">
		<!-- 页面标题 -->
		<view class="header">
			<text class="title">设计管理</text>
		</view>


		<!-- 店铺列表 -->
		<view class="store-list">
			<view v-for="(store, index) in storeList" :key="store.id" class="store-item" @click="toggleExpand(store.id)">
				<!-- 店铺基本信息 -->
				<view class="store-header">
					<view class="store-info">
						<text class="store-name">{{ store.shopName }}</text>
						<text class="customer-name">客户: {{ store.clientName || '暂无'}}</text>
					</view>
				</view>
				
				<view class="store-details">
					<view class="detail-item">
						<text class="detail-label">联系电话:</text>
						<text class="detail-value">{{ store.phone || '暂无' }}</text>
					</view>
					<view class="detail-item">
						<text class="detail-label">店铺地址:</text>
						<text class="detail-value">{{ store.shopAddress || '暂无' }}</text>
					</view>
				</view>

				<!-- 设计阶段详情 - 展开时显示 -->
				<view v-if="store.expanded" class="design-phases">
					<view class="phases-title">
						<text>设计阶段</text>
					</view>
					
					<view v-for="(phase, phaseIndex) in store.designPhases" :key="phaseIndex" class="phase-item">
						<view class="phase-header">
							<view class="phase-info">
								<text class="phase-name">{{ phase.name }}</text>
								<view class="phase-status" :class="phase.status">
									<text class="status-text">{{ getStatusText(phase.status) }}</text>
								</view>
							</view>
							<button class="modify-btn" @click.stop="modifyPhaseStatus(phase.id,phase.status)">
								修改
							</button>
						</view>
						
					</view>
				</view>
			</view>
		</view>

		<!-- 空数据提示 -->
		<view v-if="storeList.length === 0 && !loading" class="empty-state">
			<text class="empty-text">暂无店铺数据</text>
		</view>

		<!-- 加载更多 -->
		<view v-if="hasMore && storeList.length > 0" class="load-more" @click="loadMore">
			<text class="load-more-text">{{ loadingMore ? '加载中...' : '加载更多' }}</text>
		</view>

		<!-- 状态修改弹窗 -->
		<view v-if="showStatusModal" class="modal-overlay" @click="closeStatusModal">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">修改阶段状态</text>
				</view>
				
				<view class="modal-body">
					
					<view class="status-options">
						<view 
							v-for="(status, statusIndex) in statusOptions" 
							:key="statusIndex"
							class="status-option"
							:class="{ active: selectedStatus === status.value }"
							@click="selectStatus(status.value)"
						>
							<text>{{ status.label }}</text>
						</view>
					</view>
				</view>
				
				<view class="modal-footer">
					<button class="modal-btn cancel" @click="closeStatusModal">取消</button>
					<button class="modal-btn confirm" @click="confirmStatusChange">确认</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
import { getUserInfo } from '@/utils/auth'

export default {
	data() {
		return {
			storeList: [],
			total: 0,
			loading: false,
			loadingMore: false,
			hasMore: true,
			pageNum: 1,
			pageSize: 10,
			showStatusModal: false,
			currentStoreId: null,
			currentPhase: {},
			selectedStatus: '',
			statusOptions: [
				{ label: '设计中', value: 'designing' },
				{ label: '已完成', value: 'completed' }
			]
		}
	},
	onLoad() {
		console.log('页面加载');
		this.fetchStoreList();
	},
	onReachBottom() {
		// 触底加载更多
		if (this.hasMore && !this.loadingMore) {
			this.loadMore();
		}
	},
	onPullDownRefresh() {
		// 下拉刷新
		this.refreshData();
	},
	methods: {
		// 统一的数据获取方法
		getStoreListData(pageNum, pageSize, isLoadMore = false, isRefresh = false) {
			return new Promise((resolve, reject) => {
				util.request('wx/api/designList', {
					pageNum: pageNum,
					pageSize: pageSize
				}, 'GET', 'application/json', false, true)
					.then(res => {
						if (res.code === 200) {
						
							resolve({
								list: res.rows,
								total: res.total
							});
						} else {
							reject(new Error(res.msg || '获取数据失败'));
						}
					})
					.catch(err => {
						reject(err);
					});
			});
		},

		// 获取店铺列表
		fetchStoreList(isRefresh = false) {
			if (this.loading) return;
			
			this.loading = true;
			
			if (isRefresh) {
				this.pageNum = 1;
				this.storeList = [];
				this.hasMore = true;
			}

			this.getStoreListData(this.pageNum, this.pageSize, false, isRefresh)
				.then(result => {
					this.loading = false;
					
					if (isRefresh) {
						this.storeList = result.list;
					} else {
						this.storeList = [...this.storeList, ...result.list];
					}
					
					this.total = result.total;
					this.hasMore = this.storeList.length < result.total;
					
					console.log('处理后的数据:', this.storeList);
					
					if (isRefresh) {
						uni.stopPullDownRefresh();
					}
				})
				.catch(err => {
					this.loading = false;
					console.error('获取店铺列表失败:', err);
					uni.showToast({
						title: err.message || '网络请求失败',
						icon: 'none'
					});
					
					if (isRefresh) {
						uni.stopPullDownRefresh();
					}
				});
		},

		// 加载更多
		loadMore() {
			if (!this.hasMore || this.loadingMore) return;
			
			this.loadingMore = true;
			this.pageNum++;
			
			this.getStoreListData(this.pageNum, this.pageSize, true, false)
				.then(result => {
					this.loadingMore = false;
					
					this.storeList = [...this.storeList, ...result.list];
					this.total = result.total;
					this.hasMore = this.storeList.length < result.total;
				})
				.catch(err => {
					this.loadingMore = false;
					this.pageNum--; // 失败时回退页码
					console.error('加载更多失败:', err);
					uni.showToast({
						title: err.message || '网络请求失败',
						icon: 'none'
					});
				});
		},

		// 刷新数据
		refreshData() {
			this.fetchStoreList(true);
		},

		// 切换店铺展开/收起
		toggleExpand(storeId) {
			console.log('点击展开店铺:', storeId);
			const storeIndex = this.storeList.findIndex(s => s.id === storeId);
			if (storeIndex !== -1) {
				console.log('找到店铺，当前展开状态:', this.storeList[storeIndex].expanded);
				// 直接修改属性
				this.storeList[storeIndex].expanded = !this.storeList[storeIndex].expanded;
				console.log('更新后展开状态:', this.storeList[storeIndex].expanded);
				// 强制更新视图
				this.$forceUpdate();
			} else {
				console.log('未找到店铺');
			}
		},
		
		// 获取状态文本
		getStatusText(status) {
			return status === 'completed' ? '已完成' : '设计中';
		},
		
		// 修改阶段状态
		modifyPhaseStatus(storeId, status) {
			
			this.currentStoreId = storeId;
			this.selectedStatus = status;
			this.showStatusModal = true;
			
		},
		
		// 选择状态
		selectStatus(status) {
			this.selectedStatus = status;
		},
		
		// 确认状态修改
		confirmStatusChange() {
				console.log(this.currentStoreId)
				console.log(this.selectedStatus)


				// 调用接口更新状态
				util.request('wx/api/updateDesignStatus', {
					designId: this.currentStoreId,
					
					selectedStatus: this.selectedStatus
				}, 'POST', 'application/json', false, true)
					.then(res => {
						if (res.code === 200) {
							// 更新本地数据
							
							uni.showToast({
								title: '状态修改成功',
								icon: 'success'
							});
							
							this.closeStatusModal();
							this.refreshData()
						} else {
							uni.showToast({
								title: res.msg || '修改失败',
								icon: 'none'
							});
						}
					})
					.catch(err => {
						console.error('修改状态失败:', err);
						uni.showToast({
							title: '网络请求失败',
							icon: 'none'
						});
					});
			
		},
		
		// 关闭状态修改弹窗
		closeStatusModal() {
			this.showStatusModal = false;
			this.currentStoreId = null;
			this.selectedStatus = '';
		}
	}
}
</script>

<style scoped>
.container {
	padding: 20rpx;
	background-color: #f5f5f5;
	min-height: 100vh;
}

.header {
	background: #fff;
	padding: 30rpx 20rpx;
	margin-bottom: 20rpx;
	border-radius: 10rpx;
}

.title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
}

.stats-section {
	background: #fff;
	padding: 20rpx;
	margin-bottom: 20rpx;
	border-radius: 10rpx;
}

.stats-item {
	display: flex;
	align-items: center;
}

.stats-label {
	font-size: 28rpx;
	color: #666;
	margin-right: 10rpx;
}

.stats-value {
	font-size: 32rpx;
	font-weight: bold;
	color: #007aff;
}

.store-list {
	margin-bottom: 20rpx;
}

.store-item {
	background: #fff;
	margin-bottom: 20rpx;
	border-radius: 10rpx;
	padding: 20rpx;
	/* 移除点击样式 */
	-webkit-tap-highlight-color: transparent;
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	outline: none;
}

.store-item:active {
	background: #fff;
	transform: none;
}

.store-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15rpx;
}

.store-info {
	flex: 1;
}

.store-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 8rpx;
}

.customer-name {
	font-size: 26rpx;
	color: #666;
}

.store-details {
	margin-bottom: 20rpx;
}

.detail-item {
	display: flex;
	margin-bottom: 8rpx;
}

.detail-label {
	font-size: 26rpx;
	color: #666;
	width: 160rpx;
}

.detail-value {
	font-size: 26rpx;
	color: #333;
	flex: 1;
}

.design-phases {
	border-top: 1rpx solid #eee;
	padding-top: 20rpx;
}

.phases-title {
	margin-bottom: 15rpx;
}

.phases-title text {
	font-size: 28rpx;
	font-weight: bold;
	color: #333;
}

.phase-item {
	background: #f8f9fa;
	padding: 15rpx;
	margin-bottom: 10rpx;
	border-radius: 8rpx;
}

.phase-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15rpx;
}

.phase-info {
	flex: 1;
	display: flex;
	align-items: center;
}

.phase-name {
	font-size: 28rpx;
	font-weight: bold;
	color: #333;
	margin-right: 15rpx;
}

.phase-status {
	padding: 4rpx 12rpx;
	border-radius: 12rpx;
	font-size: 22rpx;
}

.phase-status.completed {
	background: #d4edda;
	color: #155724;
}

.phase-status.designing {
	background: #e6f7ff;
	color: #1890ff;
}

.modify-btn {
	background: #007aff;
	color: #fff;
	padding: 6rpx 16rpx;
	border-radius: 4rpx;
	font-size: 22rpx;
	border: none;
	min-width: 60rpx;
	text-align: center;
	/* 移除按钮点击样式 */
	-webkit-tap-highlight-color: transparent;
	outline: none;
}

.modify-btn:active {
	background: #0056b3;
}

.empty-state {
	background: #fff;
	padding: 80rpx 20rpx;
	text-align: center;
	border-radius: 10rpx;
}

.empty-text {
	font-size: 28rpx;
	color: #999;
}

.load-more {
	background: #fff;
	padding: 30rpx;
	text-align: center;
	border-radius: 10rpx;
	margin-bottom: 20rpx;
}

.load-more-text {
	font-size: 28rpx;
	color: #666;
}

/* 弹窗样式 */
.modal-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.5);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 1000;
}

.modal-content {
	background: #fff;
	width: 600rpx;
	border-radius: 12rpx;
	overflow: hidden;
}

.modal-header {
	padding: 30rpx 20rpx 20rpx;
	border-bottom: 1rpx solid #eee;
}

.modal-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	text-align: center;
}

.modal-body {
	padding: 30rpx 20rpx;
}

.current-phase {
	font-size: 28rpx;
	color: #333;
	margin-bottom: 20rpx;
	display: block;
}

.status-options {
	display: flex;
	gap: 20rpx;
}

.status-option {
	flex: 1;
	padding: 20rpx;
	border: 2rpx solid #eee;
	border-radius: 8rpx;
	text-align: center;
	/* 移除点击样式 */
	-webkit-tap-highlight-color: transparent;
	outline: none;
}

.status-option.active {
	border-color: #007aff;
	background: #e6f7ff;
	color: #007aff;
}

.modal-footer {
	display: flex;
	border-top: 1rpx solid #eee;
	padding: 15rpx;
	gap: 15rpx;
	justify-content: center;
}

.modal-btn {
	padding: 12rpx 30rpx;
	border: none;
	font-size: 24rpx;
	border-radius: 4rpx;
	min-width: 100rpx;
	/* 移除按钮点击样式 */
	-webkit-tap-highlight-color: transparent;
	outline: none;
}

.modal-btn.cancel {
	background: #f5f5f5;
	color: #666;
}

.modal-btn.confirm {
	background: #007aff;
	color: #fff;
}

.modal-btn:active {
	opacity: 0.8;
}
</style>