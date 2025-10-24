<template>
	<view class="container">
		<!-- 搜索区域 -->
		<view class="search-bar">
			<input class="search-input" type="text" v-model="keyword" placeholder="请输入商铺名称或地址" @confirm="search" />
			<button class="search-btn" @click="search">搜索</button>
		</view>

		<!-- 商铺列表 -->
		<scroll-view class="shop-list-scroll" scroll-y="true" @scrolltolower="loadMore">
			<view class="shop-list">
				<view class="shop-item" v-for="item in shopList" :key="item.id" @click="goToDetail(item.id)">
					<image class="shop-image" :src="item.imageUrl"></image>
					<view class="shop-info">
						<view class="shop-name">{{ item.name }}</view>
						<view class="shop-address">{{ item.address }}</view>
						<view class="shop-area">面积：{{ item.shopSize }} ㎡</view>
					</view>
				</view>
			</view>
			<view v-if="status === 'loading'" class="loading-text">正在加载...</view>
			<view v-if="nomore" class="nomore-text">没有更多数据了</view>
		</scroll-view>
		<custom-tab-bar></custom-tab-bar>
	</view>
</template>

<script>
import util from '@/utils/util.js';
import {
	getUserInfo,
} from '@/utils/auth';
export default {
	data() {
		return {
			keyword: '',
			shopList: [],
			page: 1,
			limit: 10,
			status: 'more',
			nomore: false
		};
	},
	onLoad() {
		let user = getUserInfo()
		if (user.shopResource == 0) {
			uni.showToast({
				title: '请联系销售，开通查看铺位权限！',
				icon: 'none'
			})
		} else {
			this.getShopList();
		}

	},
	methods: {
		getShopList(loadMore = false) {
			if (this.status === 'loading' || this.nomore) {
				return;
			}
			this.status = 'loading';

			util.request('wx/api/allShopList', {
				pageNum: this.page,
				pageSize: this.limit,
				keyword: this.keyword
			}, "GET").then(res => {
				console.log(res)
				if (res.code === 200 && res.rows) {
					const processedData = res.rows.map(shop => {
						if (shop.imgUrl) {
							shop.imageUrl = shop.imgUrl.split(',')[0];
						} else {
							shop.imageUrl = '';
						}
						return shop;
					});

					if (loadMore) {
						this.shopList = [...this.shopList, ...processedData];
					} else {
						this.shopList = processedData;
					}

					if (res.rows.length < this.limit) {
						this.nomore = true;
						this.status = 'noMore';
					} else {
						this.status = 'more';
					}
				} else {
					this.status = 'more';
				}
			}).catch(() => {
				this.status = 'more';
			});
		},
		search() {
			this.page = 1;
			this.shopList = [];
			this.nomore = false;
			this.status = 'more';
			this.getShopList();
		},
		loadMore() {
			if (!this.nomore) {
				this.page++;
				this.getShopList(true);
			}
		},
		goToDetail(id) {
			uni.navigateTo({
				url: `/shops/detail?id=${id}`
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.container {
	display: flex;
	flex-direction: column;
	height: 100vh;
	background-color: #f5f5f5;
}

.search-bar {
	padding: 15rpx 20rpx;
	background-color: #ffffff;
	flex-shrink: 0;
	display: flex;
	align-items: center;
	border-radius: 10rpx;
	margin: 20rpx;

	.search-input {
		flex: 1;
		background-color: #f5f5f5;
		padding: 10rpx 20rpx;
		border-radius: 30rpx;
		font-size: 28rpx;
	}

	.search-btn {
		margin-left: 20rpx;
		background-color: #2979ff;
		color: #ffffff;
		border: none;
		padding: 0 30rpx;
		height: 60rpx;
		line-height: 60rpx;
		font-size: 28rpx;
		border-radius: 30rpx;
	}
}

.shop-list-scroll {
	flex: 1;
	overflow-y: auto;
	padding: 0 20rpx;
	padding-bottom: 120rpx;
	/* 添加底部边距，为底部导航栏留出空间 */
}

.shop-list {
	.shop-item {
		display: flex;
		background-color: #ffffff;
		padding: 20rpx;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

		.shop-image {
			width: 180rpx;
			height: 180rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
			flex-shrink: 0;
		}

		.shop-info {
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			flex: 1;

			.shop-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #303133;
			}

			.shop-address {
				font-size: 26rpx;
				color: #606266;
			}

			.shop-area {
				font-size: 24rpx;
				color: #909399;
			}
		}
	}
}

.loading-text,
.nomore-text {
	text-align: center;
	color: #999;
	padding: 20rpx 0;
}
</style>