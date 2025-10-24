<template>
    <view class="container">
        <!-- 商圈名 -->
        <view class="detail-item">
            <view class="detail-title">商圈名称</view>
            <view class="detail-content">
                <view class="content-text">{{ noteData.businessName || '暂无数据' }}</view>
            </view>
        </view>

        <!-- 描述 -->
        <view class="detail-item">
            <view class="detail-title">描述</view>
            <view class="detail-content">
                <view class="content-text">{{ noteData.description || '暂无数据' }}</view>
            </view>
        </view>

        <!-- 图片 -->
        <view class="detail-item">
            <view class="detail-title">图片</view>
            <view class="detail-content">
                <view class="photo-list" v-if="noteData.imageUrl && noteData.imageUrl.length > 0">
                    <view class="photo-preview" v-for="(img, index) in noteData.imageUrl" :key="index"
                        @tap="previewImage(img, index)">
                        <image :src="img" mode="aspectFill"></image>
                    </view>
                </view>
                <view class="no-data" v-else>暂无图片</view>
            </view>
        </view>

        <!-- 视频 -->
        <view class="detail-item">
            <view class="detail-title">视频</view>
            <view class="detail-content">
                <view class="video-preview" v-if="noteData.videoUrl">
                    <video :src="noteData.videoUrl" controls></video>
                </view>
                <view class="no-data" v-else>暂无视频</view>
            </view>
        </view>

        <!-- <view class="detail-item">
            <view class="detail-title">定位地点</view>
            <view class="detail-content">
                <view class="content-text">{{ formData.checkinLocation }}</view>
            </view>
        </view> -->
    </view>
</template>

<script>
const util = require("@/utils/util.js")

export default {
    data() {
        return {
            noteId: '',
            noteData: {
                businessDistrictId: '',
                description: '',
                images: [],
                videoUrl: ''
            },
            businessList: [], // 商圈列表
            businessName: '' // 商圈名称
        }
    },
    onLoad(options) {
        if (options.id) {
            this.noteId = options.id;
            // 先获取商圈列表，再获取笔记详情
            
            this.fetchNoteDetail();
         
        }
    },
    methods: {
        // 获取商圈列表
        getBusinessList() {
            return new Promise((resolve, reject) => {
                util.request("wx/api/districtList", {}, "GET")
                    .then(res => {
                        if (res.code === 200) {
                            this.businessList = res.data || [];
                            console.log('商圈列表：', this.businessList);
                            resolve();
                        } else {
                            console.error('获取商圈列表失败：', res.msg);
                            resolve(); // 即使失败也继续执行
                        }
                    })
                    .catch(err => {
                        console.error('获取商圈列表失败', err);
                        resolve(); // 即使失败也继续执行
                    });
            });
        },

        // 根据商圈ID获取商圈名称
        getBusinessNameById(businessId) {
            const business = this.businessList.find(item => item.id === businessId);
            return business ? business.name : '未知商圈';
        },

        // 获取笔记详情
        fetchNoteDetail() {
            uni.showLoading({
                title: '加载中...'
            });

            const params = {
                id: this.noteId
            };

            util.request('wx/api/noteDetail', params, 'GET', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading();
                    if (res.code === 200) {
                        this.noteData = res.data;

                        // 根据 businessId 获取商圈名称
                      

                        // 如果图片是字符串，转换为数组
                        if (typeof this.noteData.imageUrl === 'string') {
                            this.noteData.imageUrl = this.noteData.imageUrl ? this.noteData.imageUrl.split(',') : [];
                        }
                    } else {
                        uni.showToast({
                            title: res.msg || '获取数据失败',
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

        // 预览图片
        previewImage(current, index) {
            uni.previewImage({
                current: current,
                urls: this.noteData.imageUrl
            });
        }
    }
}
</script>

<style>
.container {
    padding: 30rpx;
    background: #f5f5f5;
    min-height: 100vh;
}

.detail-item {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.detail-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    padding: 30rpx 30rpx 20rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.detail-content {
    padding: 30rpx;
}

.content-text {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
    word-break: break-all;
}

.no-data {
    font-size: 28rpx;
    color: #999;
    text-align: center;
    padding: 40rpx 0;
}

/* 图片展示样式 */
.photo-list {
    display: flex;
    flex-wrap: wrap;
    margin: -10rpx;
}

.photo-preview {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    border-radius: 8rpx;
    overflow: hidden;
    margin: 10rpx;
}

.photo-preview image {
    width: 100%;
    height: 100%;
}

/* 视频展示样式 */
.video-preview {
    width: 100%;
    border-radius: 8rpx;
    overflow: hidden;
}

.video-preview video {
    width: 100%;
    height: 400rpx;
}
</style>