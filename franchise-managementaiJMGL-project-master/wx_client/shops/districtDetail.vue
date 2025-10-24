<template>
    <view class="container">
        <view class="item-list" v-if="dataList && dataList.length > 0">
            <view class="detail-item" v-for="(item, index) in dataList" :key="index">
                <view class="card-header">
                    <view class="chip">跟进人：{{ item.followerName || '暂无数据' }}</view>
                    <view class="time-text">{{ item.updatedAt || '暂无数据' }}</view>
                </view>

                <view class="detail-content">
                    <view class="content-text">{{ item.description || '暂无数据' }}</view>
                </view>

                <view class="detail-title">现场图片（{{ imageCount(item) }}）</view>
                <view class="detail-content">
                    <view class="media-grid" v-if="item.imageUrl && item.imageUrl.length > 0">
                        <view
                            class="media-card"
                            v-for="(img, imgIndex) in item.imageUrl"
                            :key="'img-' + imgIndex"
                            @tap="previewItemImages(index, imgIndex)"
                        >
                            <image :src="img" mode="aspectFill"></image>
                        </view>
                    </view>
                    <view class="no-data" v-else>暂无图片</view>
                </view>

                <view class="detail-title">现场视频（{{ videoCount(item) }}）</view>
                <view class="detail-content">
                    <view class="media-grid" v-if="item.videoList && item.videoList.length > 0">
                        <view
                            class="media-card video-card"
                            v-for="(video, vIndex) in item.videoList"
                            :key="'video-' + vIndex"
                            @tap="openItemVideo(index, vIndex)"
                        >
                            <view class="play-overlay"></view>
                        </view>
                    </view>
                    <view class="no-data" v-else>暂无视频</view>
                </view>
            </view>
        </view>

        <view class="empty-state" v-else>
            <view class="empty-text">暂无数据</view>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")

export default {
    data() {
        return {
            noteId: '',
            dataList: [],
            businessList: [],
            businessName: '',
            // 非微信端兜底播放
            currentVideo: '',
            showInlineVideo: false
        }
    },
    onLoad(options) {
        if (options.id) {
            this.noteId = options.id;
            this.fetchDataList();
        }
    },
    methods: {
        // 获取数据列表
        fetchDataList() {
            uni.showLoading({ title: '加载中...' });
            util.request("wx/api/allNoteList", { businessDistrictId: this.noteId }, "GET")
                .then(res => {
                    uni.hideLoading();
                    if (res.code === 200) {
                        this.dataList = res.data || [];
                        this.dataList.forEach(item => {
                            // 图片字符串转数组
                            if (typeof item.imageUrl === 'string') {
                                item.imageUrl = item.imageUrl
                                    ? item.imageUrl.split(',').filter(Boolean)
                                    : [];
                            }
                            // 视频字符串转数组（支持多个）
                            if (typeof item.videoUrl === 'string') {
                                item.videoList = item.videoUrl
                                    ? item.videoUrl.split(',').filter(Boolean)
                                    : [];
                            } else if (Array.isArray(item.videoUrl)) {
                                item.videoList = item.videoUrl;
                            } else {
                                item.videoList = [];
                            }
                        });
                    } else {
                        uni.showToast({ title: res.msg || '获取数据失败', icon: 'none' });
                    }
                })
                .catch(() => {
                    uni.hideLoading();
                    uni.showToast({ title: '网络异常，请稍后再试', icon: 'none' });
                });
        },

        // 图片预览（只传索引，避免 data-event-opts 报错）
        previewItemImages(itemIndex, imgIndex) {
            const urls = (this.dataList[itemIndex] && this.dataList[itemIndex].imageUrl) || [];
            const current = urls[imgIndex];
            if (!current) return;
            uni.previewImage({ current, urls });
        },

        // 计数与展示辅助（保留计数）
        imageCount(item) {
            return Array.isArray(item.imageUrl) ? item.imageUrl.length : 0;
        },
        // 删除：displayImages、extraImageCount、openItemImageMore
        videoCount(item) {
            return Array.isArray(item.videoList) ? item.videoList.length : 0;
        },
        displayImages(item) {
            return Array.isArray(item.imageUrl) ? item.imageUrl.slice(0, 9) : [];
        },
        extraImageCount(item) {
            const total = Array.isArray(item.imageUrl) ? item.imageUrl.length : 0;
            const shown = Array.isArray(item.imageUrl) ? item.imageUrl.slice(0, 9).length : 0;
            return total > shown ? total - shown : 0;
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

.item-list { margin-bottom: 30rpx; }

.detail-item {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 30rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 顶部跟进人 + 时间 */
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 30rpx 0 30rpx;
}
.chip {
    padding: 8rpx 16rpx;
    border-radius: 24rpx;
    background: #eaf3ff;
    color: #2b6def;
    font-size: 26rpx;
}
.time-text {
    color: #666;
    font-size: 26rpx;
}

.detail-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    padding: 30rpx 30rpx 20rpx 30rpx;

}

.detail-content { padding: 30rpx; }

.content-text {
    font-size: 28rpx;
    color: #555;
    line-height: 1.8;
    word-break: break-all;
}

.no-data {
    font-size: 28rpx;
    color: #999;
    text-align: center;
    padding: 40rpx 0;
}

/* 网格通用（图片/视频） */
.media-grid {
    display: flex;
    flex-wrap: wrap;
    margin: -10rpx;
}
.media-card {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    border-radius: 12rpx;
    overflow: hidden;
    margin: 10rpx;
    background: #f0f0f0;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
}
.media-card image {
    width: 100%;
    height: 100%;
}

/* +X 卡片样式 */
.more-card {
    background: #666;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
}
.more-text {
    font-size: 36rpx;
    font-weight: 600;
}

/* 视频卡片播放按钮 */
.video-card { background: #e9ecef; }
.play-overlay {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0.45);
}
.play-overlay::after {
    content: '';
    position: absolute;
    left: 32rpx;
    top: 26rpx;
    border-style: solid;
    border-width: 12rpx 0 12rpx 20rpx;
    border-color: transparent transparent transparent #fff;
}
/* 空数据状态 */
.empty-state {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 400rpx;
}

.empty-text {
    font-size: 32rpx;
    color: #999;
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


