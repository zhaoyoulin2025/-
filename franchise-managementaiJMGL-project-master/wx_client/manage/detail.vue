<template>
    <view class="container">

        <!-- 报告列表 -->
        <view class="report-list">
            <!-- 空状态显示 -->
            <view v-if="reportListLength === 0" class="empty-state">
                <text class="empty-text">暂无监理报告数据</text>
            </view>

            <!-- 报告卡片列表 -->
            <block v-else>
                <view v-for="(report, index) in reportList" :key="index" class="report-card">
                    <!-- 基本信息（默认显示） -->
                    <view class="report-header" @click="toggleReport(index)">
                        <view class="report-date">{{ report.createDate || '未设置日期' }}</view>
                        <view class="report-basic">
                            <view class="report-item">
                                <text class="item-label">检查项目:</text>
                                <text class="item-value">{{ report.project || '未设置' }}</text>
                            </view>
                            <view class="report-item">
                                <text class="item-label">施工进度:</text>
                                <view class="progress-bar">
                                    <view class="progress-inner" :style="{ width: (report.progress || 0) + '%' }"></view>
                                    <text class="progress-text">{{ report.progress || 0 }}%</text>
                                </view>
                            </view>
                            <view class="report-item">
                                <text class="item-label">施工质量:</text>
                                <text :class="['item-value', 'quality-tag', getQualityClass(report.quality)]">{{
                                    report.quality || '未评定' }}</text>
                            </view>
                        </view>
                    </view>

                    <!-- 详细信息（点击展开） -->
                    <view v-if="report.expanded" class="report-detail">
                        <view class="detail-section">
                            <text class="section-title">发现的问题</text>
                            <text class="section-content">{{ report.question || '无' }}</text>
                        </view>
                        <view class="detail-section">
                            <text class="section-title">整改建议</text>
                            <text class="section-content">{{ report.suggestion || '无' }}</text>
                        </view>
                        <view class="detail-section" v-if="report.imgList && report.imgList.length > 0">
                            <text class="section-title">现场照片</text>
                            <view class="photo-grid">
                                <view v-for="(img, imgIndex) in report.imgList" :key="imgIndex" class="photo-item">
                                    <image :src="img" class="photo-image" @click="previewImages(report.imgList, imgIndex)"
                                        mode="aspectFill"></image>
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
            </block>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
import { getUserInfo } from '@/utils/auth'

export default {
    data() {
        return {
            shopId: '', // 店铺ID
            reportList: [], // 监理报告列表，初始化为空数组
        }
    },
    computed: {
        reportListLength() {
            return this.reportList ? this.reportList.length : 0;
        },
        reportListType() {
            return typeof this.reportList;
        }
    },
    onLoad(options) {
        // 获取传入的店铺ID
        if (options.id) {
            this.shopId = options.id;
            this.fetchReportList();
        } else {
            uni.showToast({
                title: '缺少店铺ID参数',
                icon: 'none'
            });
            setTimeout(() => {
                uni.navigateBack();
            }, 1500);
        }
    },
    // 下拉刷新
    onPullDownRefresh() {
        this.fetchReportList();
        uni.stopPullDownRefresh();
    },
    methods: {
        // 获取监理报告列表 - 参照 index.vue 的写法
        fetchReportList() {
            console.log('获取监理报告列表');
            // 显示加载中
            uni.showLoading({
                title: '加载中...'
            });
            
            // 构建请求参数
            const params = {
                shopId: this.shopId
            };
            
            // 调用接口获取数据
            util.request('wx/api/buildManageList', params, 'GET', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading();
                    console.log('接口返回数据:', res);
                    
                    if (res.code === 200) {
                        // 直接赋值，简化处理方式
                        if (Array.isArray(res.data)) {
                            // 处理数组数据
                            this.reportList = res.data.map(item => {
                                return {
                                    ...item,
                                    expanded: false,
                                    imgList: item.img ? item.img.split(',').filter(url => url.trim() !== '') : []
                                };
                            });
                        } else if (res.data && typeof res.data === 'object') {
                            // 处理单个对象
                            const item = res.data;
                            this.reportList = [{
                                ...item,
                                expanded: false,
                                imgList: item.img ? item.img.split(',').filter(url => url.trim() !== '') : []
                            }];
                        } else {
                            // 空数据
                            this.reportList = [];
                        }
                        
                        console.log('处理后的数据:', this.reportList);
                    } else {
                        this.reportList = [];
                        uni.showToast({
                            title: res.msg || '获取数据失败',
                            icon: 'none'
                        });
                    }
                })
                .catch(err => {
                    console.error('请求出错:', err);
                    this.reportList = [];
                    uni.hideLoading();
                    uni.showToast({
                        title: '网络异常，请稍后再试',
                        icon: 'none'
                    });
                });
        },
        
        // 切换报告展开/收起状态
        toggleReport(index) {
            if (this.reportList && this.reportList[index]) {
                this.$set(this.reportList[index], 'expanded', !this.reportList[index].expanded);
            }
        },
        
        // 根据质量等级获取对应的样式类名
        getQualityClass(quality) {
            const qualityMap = {
                '优秀': 'quality-excellent',
                '良好': 'quality-good',
                '合格': 'quality-pass',
                '不合格': 'quality-fail'
            };
            return qualityMap[quality] || '';
        },
        
        // 预览图片
        previewImages(imgList, current) {
            if (!imgList || imgList.length === 0) {
                uni.showToast({
                    title: '没有可预览的图片',
                    icon: 'none'
                });
                return;
            }
            
            uni.previewImage({
                urls: imgList,
                current: imgList[current]
            });
        }
    }
}
</script>

<style>
.container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 30rpx;
}

.header {
    background-color: #fff;
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #eee;
}

.title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.report-list {
    padding: 20rpx;
}

/* 调试信息样式 */
.debug-info {
    background-color: #f0f8ff;
    padding: 10rpx;
    margin-bottom: 10rpx;
    border: 1px solid #ccc;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100rpx 0;
}

.empty-text {
    font-size: 30rpx;
    color: #999;
    margin-top: 20rpx;
}

.report-card {
    background-color: #fff;
    border-radius: 12rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

.report-header {
    padding: 30rpx;
    position: relative;
}

.report-date {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 20rpx;
}

.report-basic {
    margin-bottom: 10rpx;
}

.report-item {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
}

.item-label {
    font-size: 28rpx;
    color: #666;
    width: 150rpx;
    flex-shrink: 0;
}

.item-value {
    font-size: 28rpx;
    color: #333;
    flex: 1;
}

/* 施工质量标签样式调整 */
.item-value.quality-tag {
    flex: none; /* 取消flex: 1，使其宽度自适应内容 */
    text-align: center;
}

.progress-bar {
    flex: 1;
    height: 36rpx; /* 增加高度 */
    background-color: #f0f0f0;
    border-radius: 18rpx; /* 保持圆角 */
    overflow: hidden;
    position: relative;
}

.progress-inner {
    height: 100%;
    background: linear-gradient(90deg, #007AFF, #0056CC);
    border-radius: 18rpx; /* 保持圆角 */
}

.progress-text {
    position: absolute;
    right: 15rpx;
    top: 50%;
    transform: translateY(-50%); /* 垂直居中 */
    font-size: 24rpx; /* 增大字体 */
    font-weight: bold; /* 加粗 */
    color: blue;

   
}

.quality-tag {
    display: inline-block;
    padding: 4rpx 16rpx;
    border-radius: 6rpx;
    font-size: 24rpx;
    color: #fff;
}

.quality-excellent {
    background-color: #4cd964;
}

.quality-good {
    background-color: #007aff;
}

.quality-pass {
    background-color: #ff9500;
}

.quality-fail {
    background-color: #ff3b30;
}

.toggle-icon {
    position: absolute;
    right: 30rpx;
    bottom: 30rpx;
    display: flex;
    align-items: center;
}

.icon-text {
    font-size: 24rpx;
    color: #007AFF;
    margin-right: 6rpx;
}

.arrow {
    font-size: 24rpx;
    color: #007AFF;
}

.expanded .arrow {
    transform: rotate(180deg);
}

.report-detail {
    padding: 0 30rpx 30rpx;
    border-top: 1rpx solid #f0f0f0;
    background-color: #fafafa;
}

.detail-section {
    margin-top: 20rpx;
}

.section-title {
    font-size: 28rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 10rpx;
    display: block;
}

.section-content {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
}

.photo-grid {
    display: flex;
    flex-wrap: wrap;
    margin: 0 -10rpx;
}

.photo-item {
    width: calc(33.33% - 20rpx);
    margin: 10rpx;
    aspect-ratio: 1;
    border-radius: 8rpx;
    overflow: hidden;
}

.photo-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>